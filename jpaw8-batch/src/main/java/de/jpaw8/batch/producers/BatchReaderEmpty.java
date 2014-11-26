package de.jpaw8.batch.producers;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.producers.impl.BatchReaderAbstract;

/** Batch reader for testing. This one represents an empty source. */
public class BatchReaderEmpty<E> extends BatchReaderAbstract<E> {
    @Override
    public void produceTo(ObjIntConsumer<? super E> whereToPut) {
    }
}
