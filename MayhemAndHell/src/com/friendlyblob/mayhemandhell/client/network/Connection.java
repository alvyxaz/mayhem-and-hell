package com.friendlyblob.mayhemandhell.client.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;

import javolution.util.FastList;

import org.mmocore.network.NioNetStackList;
import org.mmocore.network.NioNetStringBuffer;

import com.friendlyblob.mayhemandhell.client.MyGame;
import com.friendlyblob.mayhemandhell.client.network.packets.ReceivablePacket;
import com.friendlyblob.mayhemandhell.client.network.packets.SendablePacket;

public class Connection extends Thread {
	
	// default BYTE_ORDER
	private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	
	private Socket serverSocket;
	
	private boolean shutdown;
	
	private final PacketHandler packetHandler;
	
	private final NioNetStackList<SendablePacket> sendQueue;
	
	// Main Buffers
	private final ByteBuffer DIRECT_WRITE_BUFFER;
	private final ByteBuffer WRITE_BUFFER;
	private final ByteBuffer READ_BUFFER;
	
	// String Buffer
	private final NioNetStringBuffer STRING_BUFFER;
	
	public int READ_BUFFER_SIZE = 64 * 1024;
	public int WRITE_BUFFER_SIZE = 64 * 1024;
	
	private static final int HEADER_SIZE = 2;
	
	private ByteBuffer readBuffer;
	private ByteBuffer primaryWriteBuffer;
	private ByteBuffer secondaryWriteBuffer;
	
	// ByteBuffers General Purpose Pool
	private final FastList<ByteBuffer> bufferPool;
	
	private final int MAX_SEND_PER_PASS = 12;
	private final int MAX_READ_PER_PASS = 12;
	
	private final GameCrypt crypt;
	
	private final int HELPER_BUFFER_COUNT = 20;
	private final int HELPER_BUFFER_SIZE =  64 * 1024;
	
	private volatile boolean pendingClose;
	
	private final OutputStream outputStream;
	private final InputStream inputStream;
	
	public Thread readerThread;
	
	public MyGame game;
	
	public Connection(final PacketHandler packetHandler, String host, int port) throws IOException{
		super.setName("PacketHandlerThread-" + super.getId());

		try {
			serverSocket = new Socket(host, port);
		} catch (UnknownHostException e) {
			System.err.println("Can't connect to host");
//            System.exit(1);
		} catch (IOException e) {
            e.printStackTrace();
//            System.exit(1);
        }
		
		outputStream = serverSocket.getOutputStream();
		inputStream = serverSocket.getInputStream();
		
		crypt = new GameCrypt();
		
		bufferPool = new FastList<ByteBuffer>();
		
		DIRECT_WRITE_BUFFER = ByteBuffer.allocateDirect(WRITE_BUFFER_SIZE).order(BYTE_ORDER);
		WRITE_BUFFER = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(BYTE_ORDER);
		READ_BUFFER = ByteBuffer.wrap(new byte[READ_BUFFER_SIZE]).order(BYTE_ORDER);
		STRING_BUFFER = new NioNetStringBuffer(64 * 1024);
		
		sendQueue = new NioNetStackList<SendablePacket>();
		
		this.packetHandler = packetHandler;
	}
	
	public final void sendPacket(final SendablePacket sp) {
		
		synchronized (getSendQueue()) {
			sendQueue.addLast(sp);
		}

	}
	
	final int write(final ByteBuffer buf) throws IOException {
		byte temp [] = new byte [buf.remaining()];

		buf.get(temp);
		outputStream.write(temp);
		
		return 0;
	}
	
	byte temp [] = new byte[1024]; // TODO optimize, improve, etc
	final int read(final ByteBuffer buf) throws IOException {
		int amountRead = inputStream.read(temp);
		buf.put(temp, 0, amountRead);
		return amountRead;
	}
	
	@Override
	public void run() {
		// TODO Cleanup reading thread, no infinite loop
		readerThread = new Thread() {
			public void run() {
				while(!shutdown){
					readPacket();
				}
			}
		};
		readerThread.start();
		
		while(!shutdown){
			synchronized (getSendQueue()) {
				if(!sendQueue.isEmpty()){
					writePacket();
				}
			}
			
		}
	}
	
	private final void readPacket() {
		if (!pendingClose) {
			
			ByteBuffer buf;
			
			if ((buf = readBuffer) == null) {
				buf = READ_BUFFER;
			}
			
			// if we try to to do a read with no space in the buffer it will
			// read 0 bytes
			// going into infinite loop
			if (buf.position() == buf.limit()) {
				System.exit(0);
			}
			
			int result = -2;
			
			try {
				result = read(buf);
			} catch (IOException e) {
				// error handling goes bellow
			}
			
			if (result > 0) {
				buf.flip();
				
				for (int i = 0; i < MAX_READ_PER_PASS; i++)
				{
					if (!tryReadPacket( buf)) {
						return;
					}
				}
				
				// only reachable if MAX_READ_PER_PASS has been reached
				// check if there are some more bytes in buffer
				// and allocate/compact to prevent content lose.
				if (buf.remaining() > 0)
				{
					// did we use the READ_BUFFER ?
					if (buf == READ_BUFFER)
					{
						// move the pending byte to the connections READ_BUFFER
						allocateReadBuffer();
					}
					else
					{
						// move the first byte to the beginning :)
						buf.compact();
					}
				}
			}
			else
			{
				switch (result)
				{
					case 0:
					case -1:
						closeConnectionImpl();
						break;
					case -2:
						onForcedDisconnection();
						closeConnectionImpl();
						break;
				}
			}
		}
	}
	
	private final void allocateReadBuffer() {
		setReadBuffer(getPooledBuffer().put(READ_BUFFER));
		READ_BUFFER.clear();
	}
	
	private final boolean tryReadPacket(final ByteBuffer buf) {
		
		switch (buf.remaining())
		{
			case 0:
				// buffer is full
				// nothing to read
				return false;
			case 1:
				// we don`t have enough data for header so we need to read
//				key.interestOps(key.interestOps() | SelectionKey.OP_READ);
				
				// did we use the READ_BUFFER ?
				if (buf == READ_BUFFER)
				{
					// move the pending byte to the connections READ_BUFFER
					allocateReadBuffer();
				}
				else
				{
					// move the first byte to the beginning :)
					buf.compact();
				}
				return false;
			default:
				// data size excluding header size :>
				final int dataPending = (buf.getShort() & 0xFFFF) - HEADER_SIZE;
				
				// do we got enough bytes for the packet?
				if (dataPending <= buf.remaining()) {
					// avoid parsing dummy packets (packets without body)
					if (dataPending > 0) {
						final int pos = buf.position();
						parseClientPacket(pos, buf, dataPending);
						buf.position(pos + dataPending);
					}

					// if we are done with this buffer
					if (!buf.hasRemaining()) {
						if (buf != READ_BUFFER) {
							setReadBuffer(null);
							recycleBuffer(buf);
						} else {
							READ_BUFFER.clear();
						}
						return false;
					}
					
					return true;
				}
				
				// we don`t have enough bytes for the dataPacket so we need
				// to read
//				key.interestOps(key.interestOps() | SelectionKey.OP_READ);
				
				
				
				// did we use the READ_BUFFER ?
				if (buf == READ_BUFFER) {
					// move it`s position
					buf.position(buf.position() - HEADER_SIZE);
					// move the pending byte to the connections READ_BUFFER
					allocateReadBuffer();
				} else {
					buf.position(buf.position() - HEADER_SIZE);
					buf.compact();
				}
				return false;
		}
	}
	
	private final void parseClientPacket(final int pos, final ByteBuffer buf, final int dataSize) {
		
		
		final boolean ret = decrypt(buf, dataSize);
		
		if (ret && buf.hasRemaining())
		{
			// apply limit
			final int limit = buf.limit();
			buf.limit(pos + dataSize);
			final ReceivablePacket cp = packetHandler.handlePacket(buf);
			cp.setConnection(this);
			
			if (cp != null) {
				cp.buf = buf;
				cp.stringBuffer = STRING_BUFFER;
				
				if (cp.read()) {
					execute(cp);
				}
				
				cp.buf = null;
				cp.stringBuffer = null;
			}
			buf.limit(limit);
		}
	}
	
	public void execute(ReceivablePacket packet) {
		packet.run();
	}
	
	public boolean decrypt(ByteBuffer buf, int size) {
		crypt.decrypt(buf.array(), buf.position(), size);
		return true;
	}
	
	protected final void writePacket() {
		if (!prepareWriteBuffer()){
			return;
		}
		
		DIRECT_WRITE_BUFFER.flip();
		
		final int size = DIRECT_WRITE_BUFFER.remaining();
		
		int result = -1;
		
		try {
			result = write(DIRECT_WRITE_BUFFER);
		} catch (IOException e) {
			// error handling goes on the if bellow
		}
		
		// check if no error happened
		if (result >= 0) {
			// check if we written everything
			if (result == size) {
				// complete write
				synchronized (getSendQueue()) {
					if (getSendQueue().isEmpty() && !hasPendingWriteBuffer()) {
					}
				}
			} else {
				// incomplete write
				createWriteBuffer(DIRECT_WRITE_BUFFER);
			}
		} else {
			onForcedDisconnection();
			closeConnectionImpl();
		}
	}
	
	
	
	protected void onForcedDisconnection() {
		// TODO add more appropriate methods of notification and etc
		MyGame.connection = null;
		System.out.println("Connection with server lost");
		shutdown = true;
	}
	
	private final boolean prepareWriteBuffer() {
		boolean hasPending = false;
		DIRECT_WRITE_BUFFER.clear();
		
		// if there is pending content add it
		if (hasPendingWriteBuffer())
		{
			movePendingWriteBufferTo(DIRECT_WRITE_BUFFER);
			hasPending = true;
		}
		
		if ((DIRECT_WRITE_BUFFER.remaining() > 1) && !hasPendingWriteBuffer()) {
			final NioNetStackList<SendablePacket> sendQueue = getSendQueue();
			SendablePacket sp;
			
			for (int i = 0; i < MAX_SEND_PER_PASS; i++) {
				synchronized (getSendQueue()) {
					if (sendQueue.isEmpty()) {
						sp = null;
					} else {
						sp = sendQueue.removeFirst();
					}
				}
				
				if (sp == null) {
					break;
				}
				
				hasPending = true;
				
				// put into WriteBuffer
				putPacketIntoWriteBuffer(sp);
				
				WRITE_BUFFER.flip();
				
				if (DIRECT_WRITE_BUFFER.remaining() >= WRITE_BUFFER.limit()) {
					DIRECT_WRITE_BUFFER.put(WRITE_BUFFER);
				} else {
					createWriteBuffer(WRITE_BUFFER);
					break;
				}
			}
		}
		return hasPending;
	}
	
	private final void putPacketIntoWriteBuffer(final SendablePacket sp)
	{
		WRITE_BUFFER.clear();
		
		// reserve space for the size
		final int headerPos = WRITE_BUFFER.position();
		final int dataPos = headerPos + HEADER_SIZE;
		WRITE_BUFFER.position(dataPos);
		
		// set the write buffer
		sp.buf = WRITE_BUFFER;
		// write content to buffer
		sp.write();
		// delete the write buffer
		sp.buf = null;
		
		// size (inclusive header)
		int dataSize = WRITE_BUFFER.position() - dataPos;
		WRITE_BUFFER.position(dataPos);
		encrypt(WRITE_BUFFER, dataSize);
		
		// recalculate size after encryption
		dataSize = WRITE_BUFFER.position() - dataPos;
		
		WRITE_BUFFER.position(headerPos);
		// write header
		WRITE_BUFFER.putShort((short) (dataSize + HEADER_SIZE));
		WRITE_BUFFER.position(dataPos + dataSize);
	}
	
	final void createWriteBuffer(final ByteBuffer buf)
	{
		if (primaryWriteBuffer == null) {
			primaryWriteBuffer = getPooledBuffer();
			primaryWriteBuffer.put(buf);
		} else {
			final ByteBuffer temp = getPooledBuffer();
			temp.put(buf);
			
			final int remaining = temp.remaining();
			primaryWriteBuffer.flip();
			final int limit = primaryWriteBuffer.limit();
			
			if (remaining >= primaryWriteBuffer.remaining()) {
				temp.put(primaryWriteBuffer);
				recycleBuffer(primaryWriteBuffer);
				primaryWriteBuffer = temp;
			} else {
				primaryWriteBuffer.limit(remaining);
				temp.put(primaryWriteBuffer);
				primaryWriteBuffer.limit(limit);
				primaryWriteBuffer.compact();
				secondaryWriteBuffer = primaryWriteBuffer;
				primaryWriteBuffer = temp;
			}
		}
	}
	
	final ByteBuffer getPooledBuffer() {
		if (bufferPool.isEmpty()) {
			return ByteBuffer.wrap(new byte[HELPER_BUFFER_SIZE]).order(BYTE_ORDER);
		}
		
		return bufferPool.removeFirst();
	}
	
	private final void closeConnectionImpl() {
		try {
			// notify connection
			onDisconnection();
		} finally {
			close();
			releaseBuffers();
		}
	}
	
	final void movePendingWriteBufferTo(final ByteBuffer dest) {
		primaryWriteBuffer.flip();
		dest.put(primaryWriteBuffer);
		recycleBuffer(primaryWriteBuffer);
		primaryWriteBuffer = secondaryWriteBuffer;
		secondaryWriteBuffer = null;
	}
	
	final NioNetStackList<SendablePacket> getSendQueue() {
		return sendQueue;
	}
	
	final boolean hasPendingWriteBuffer() {
		return primaryWriteBuffer != null;
	}
	
	public boolean encrypt(final ByteBuffer buf, final int size) {
		crypt.encrypt(buf.array(), buf.position(), size);
		buf.position(buf.position() + size);
		return true;
	}
	
	final void recycleBuffer(final ByteBuffer buf) {
		if (bufferPool.size() < HELPER_BUFFER_COUNT) {
			buf.clear();
			bufferPool.addLast(buf);
		}
	}
	
	public final void close(final SendablePacket sp) {
		
		close(new SendablePacket[]
		{
			sp
		});
	}
	
	public final void close(final SendablePacket[] closeList) {
		if (pendingClose) {
			return;
		}
		
		synchronized (getSendQueue()) {
			if (!pendingClose) {
				pendingClose = true;
				sendQueue.clear();
				for (SendablePacket sp : closeList) {
					sendQueue.addLast(sp);
				}
			}
		}
		
		// _closePacket = sp;
		closeConnection();
	}
	
	final void closeConnection() {
		close();
	}
	
	final void close(){
		try {
			serverSocket.close();
		} catch (Exception e){
			
		}
	}
	
	final void releaseBuffers()
	{
		if (primaryWriteBuffer != null) {
			recycleBuffer(primaryWriteBuffer);
			primaryWriteBuffer = null;
			
			if (secondaryWriteBuffer != null) {
				recycleBuffer(secondaryWriteBuffer);
				secondaryWriteBuffer = null;
			}
		}
		
		if (readBuffer != null) {
			recycleBuffer(readBuffer);
			readBuffer = null;
		}
	}
	
	
	protected void onDisconnection() {
	}
	
	final void setReadBuffer(final ByteBuffer buf) {
		readBuffer = buf;
	}
	
	public final void shutdown(){
		shutdown = true;
	}
	
	public GameCrypt getCrypt(){
		return crypt;
	}
	
}
