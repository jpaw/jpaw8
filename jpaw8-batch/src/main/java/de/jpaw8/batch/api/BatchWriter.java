package de.jpaw8.batch.api;

/** Defines the methods a jpaw batch output writer must implement.
 * The accept() method is called in ordered or unordered sequence for every processed record.
 * If processing resulted in an exception, the data component of response will be null.
 * A single thread (or the main thread) will be allocated to writing.
 * 
 * This interface loosely corresponds to the Java 8 ObjIntConsumer<F> interface, but allows exceptions.
 * Linking is done via the BatchWriterLinked class.
 */

public interface BatchWriter<F> extends BatchIO {
    void store(F response, int no);
}
