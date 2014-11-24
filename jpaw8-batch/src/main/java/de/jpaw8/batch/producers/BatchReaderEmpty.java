package de.jpaw8.batch.producers;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchReader;
import de.jpaw8.batch.api.cmdline.impl.ContributorNoop;

/** Batch reader for testing. This one represents an empty source. */
public class BatchReaderEmpty<E> extends ContributorNoop implements BatchReader<E> {
    @Override
    public void produceTo(ObjIntConsumer<? super E> whereToPut) {
    }
}
