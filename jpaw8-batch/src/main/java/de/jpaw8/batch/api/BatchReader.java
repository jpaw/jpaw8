package de.jpaw8.batch.api;

import java.util.function.ObjIntConsumer;


/** Defines the methods a jpaw batch processor record source must implement, typically via subclassing BatchStream.
 * Pipelining of processing steps is possible by linking functional interfaces, using subclasses of the BatchLinkedStream class.
 *
 * Implementing classes work similar to the Java 8 streams, but using a push method instead of the Java 8 streams / Iterable pulling via getNext().
 */

public interface BatchReader<E> extends BatchIO {
    public void produceTo(ObjIntConsumer<? super E> whereToPut) throws Exception;
}
