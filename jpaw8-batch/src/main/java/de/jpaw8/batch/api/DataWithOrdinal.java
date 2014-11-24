package de.jpaw8.batch.api;

/** Represents a generic data record with an integral ordinal attached to it. */
public class DataWithOrdinal<E> {
    public E data;                                      // the actual payload
    public int recordno;                                // just a counter 1...n
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;   // avoid false sharing
    
    public DataWithOrdinal(E data, int n) {
        this.data = data;
        this.recordno = n;
    }
}
