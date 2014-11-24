package de.jpaw8.batch.api;

import java.util.function.ObjIntConsumer;


/** Defines the methods a bonaparte batch processor must implement.
 * The implementation typically also hosts the main() method, and invokes the batch processor
 * with a reference of an instance to itself.
 *
 * This interface loosely corresponds to the Java 8 streams
 */

public interface BatchReader<E> extends Contributor {
    public void produceTo(ObjIntConsumer<? super E> whereToPut) throws Exception;
}
