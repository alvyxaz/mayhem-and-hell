package com.friendlyblob.mayhemandhell.server.factories;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Takes care of generating a new id
 * @author Alvys
 *
 */
public class IdFactory {

	protected final Logger log = Logger.getLogger(getClass().getName());
	
	private static final int INITIAL_CAPACITY = 100000;
	
	private BitSet freeIds;
	private AtomicInteger freeIdCount;
	private AtomicInteger nextFreeId;
	
	public static final int FIRST_OID = 0x00000001;
	public static final int LAST_OID = 0x7FFFFFFF;
	public static final int FREE_OBJECT_ID_SIZE = LAST_OID - FIRST_OID;
	
	private static IdFactory instance;
	
	public int freeIdsSize;
	
	private static String[][] ID_COLUMNS = {
		{"player","object_id"},
		{"items", "object_id"}
	};
	
	public IdFactory() {
		prepare();
	}
	
	public static void initialize() {
		instance = new IdFactory();
	}
	
	public static IdFactory getInstance() {
		return instance;
	}
	
	public void prepare() {
		freeIdsSize = INITIAL_CAPACITY;
		freeIds = new BitSet(INITIAL_CAPACITY);
		freeIdCount = new AtomicInteger(FREE_OBJECT_ID_SIZE);
		
		for (int usedObjectId : getUsedObjectIds()) {
			
			int objectId = usedObjectId - FIRST_OID;
			
			if (objectId < 0) {
				log.warning(getClass().getSimpleName() + ": Object ID " + usedObjectId 
						+ " in DB is less than minimum ID of " + FIRST_OID);
				continue;
			}
			
			freeIds.set(usedObjectId - FIRST_OID);
			freeIdCount.decrementAndGet();
			nextFreeId = new AtomicInteger (freeIds.nextClearBit(0));
		}
		
		nextFreeId = new AtomicInteger(freeIds.nextClearBit(0));
	}
	
	public synchronized void releaseId(int objectID) {
		if ((objectID - FIRST_OID) > -1) {
			freeIds.clear(objectID - FIRST_OID);
			freeIdCount.incrementAndGet();
		} else {
			log.warning(getClass().getSimpleName() + ": Release objectID " + objectID + " failed (< " + FIRST_OID + ")");
		}
	}
	
	/**
	 * Grabs a new available id and sets the next available.
	 * @return
	 */
	public synchronized int getNextId() {
		int newID = nextFreeId.get();
		freeIds.set(newID);
		freeIdCount.decrementAndGet();
		
		int nextFree = freeIds.nextClearBit(newID);
		
		if (nextFree > freeIdsSize) {
			nextFree = freeIds.nextClearBit(0);
		}
		
		if (nextFree > freeIdsSize) {
			if (freeIds.size() < FREE_OBJECT_ID_SIZE) {
				increaseBitSetCapacity();
			} else {
				throw new NullPointerException("Ran out of valid Id's.");
			}
		}
		
		nextFreeId.set(nextFree);
		
		return newID + FIRST_OID;
	}
	
	public synchronized int size() {
		return freeIdCount.get();
	}

	protected synchronized int usedIdCount() {
		return (size() - FIRST_OID);
	}
	
	/**
	 * Increases BitSet capacity by 20%
	 */
	public synchronized void increaseBitSetCapacity() {
		freeIdsSize = (int)(freeIds.size()*1.2f);
		BitSet newBitSet = new BitSet(freeIdsSize);
		newBitSet.or(freeIds);
		freeIds = newBitSet;
	}
	
	/**
	 * 
	 * @return true if more than 90% of id's are used
	 */
	public synchronized boolean reachingBitSetCapacity() {
		return  (usedIdCount())*.9f > freeIds.size();
	}

	
	/**
	 * Thread, used to check whether IdFactory is about to run out of id's
	 * @author Alvys
	 *
	 */
	protected class BitSetCapacityCheck implements Runnable {

		@Override
		public void run() {
			synchronized (IdFactory.this) {
				if (reachingBitSetCapacity() ) {
					increaseBitSetCapacity();
				}
			}
		}
		
	}
	
	/**
	 * TODO implement
	 */
	public void cleanupDB() {
		
	}
	
	/**
	 * TODO implement
	 * @return
	 */
	public int[] getUsedObjectIds() {
		int[] usedIds = new int[1];
		usedIds[0] = 10;
		return usedIds;
	}
	
}
