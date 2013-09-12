package com.friendlyblob.mayhemandhell.client.network.packets;

import java.nio.ByteBuffer;

import org.mmocore.network.NioNetStringBuffer;

import com.friendlyblob.mayhemandhell.client.entities.Player;
import com.friendlyblob.mayhemandhell.client.network.Connection;

public abstract class ReceivablePacket extends AbstractPacket implements Runnable{
	public NioNetStringBuffer stringBuffer;
	
	protected Connection connection;
	protected Player player;
	
	protected ReceivablePacket() {
		
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setConnection(Connection connection){
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public abstract boolean read();
	
	@Override
	public abstract void run();
	
	/**
	 * Reads <B>byte[]</B> from the buffer. <BR>
	 * Reads as many bytes as the length of the array.
	 * @param dst : the byte array which will be filled with the data.
	 */
	protected final void readB(final byte[] dst)
	{
		buf.get(dst);
	}
	
	/**
	 * Reads <B>byte[]</B> from the buffer. <BR>
	 * Reads as many bytes as the given length (len). Starts to fill the
	 * byte array from the given offset to <B>offset</B> + <B>len</B>.
	 * @param dst : the byte array which will be filled with the data.
	 * @param offset : starts to fill the byte array from the given offset.
	 * @param len : the given length of bytes to be read.
	 */
	protected final void readB(final byte[] dst, final int offset, final int len)
	{
		buf.get(dst, offset, len);
	}
	
	/**
	 * Reads <B>byte</B> from the buffer. <BR>
	 * 8bit integer (00)
	 * @return
	 */
	protected final int readC()
	{
		return buf.get() & 0xFF;
	}
	
	/**
	 * Reads <B>short</B> from the buffer. <BR>
	 * 16bit integer (00 00)
	 * @return
	 */
	protected final int readH()
	{
		return buf.getShort() & 0xFFFF;
	}
	
	/**
	 * Reads <B>int</B> from the buffer. <BR>
	 * 32bit integer (00 00 00 00)
	 * @return
	 */
	protected final int readD()
	{
		return buf.getInt();
	}
	
	/**
	 * Reads <B>long</B> from the buffer. <BR>
	 * 64bit integer (00 00 00 00 00 00 00 00)
	 * @return
	 */
	protected final long readQ()
	{
		return buf.getLong();
	}
	
	/**
	 * Reads <B>double</B> from the buffer. <BR>
	 * 64bit double precision float (00 00 00 00 00 00 00 00)
	 * @return
	 */
	protected final double readF()
	{
		return buf.getDouble();
	}
	
	/**
	 * Reads <B>String</B> from the buffer.
	 * @return
	 */
	protected final String readS()
	{
		stringBuffer.clear();
		
		char ch;
		while ((ch = buf.getChar()) != 0)
		{
			stringBuffer.append(ch);
		}
		
		return stringBuffer.toString();
	}
	
	/**
	 * packet forge purpose
	 * @param data
	 * @param sBuffer
	 */
	public void setBuffers(ByteBuffer data, NioNetStringBuffer sBuffer)
	{
		buf = data;
		stringBuffer = sBuffer;
	}
}
