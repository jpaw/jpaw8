package de.jpaw.batch.endpoints;

import java.util.function.Consumer;

import de.jpaw.batch.api.BatchReader;
import de.jpaw.batch.impl.ContributorNoop;

/** Batch reader for testing. This one represents an empty source. */
public class BatchReaderEmpty<E> extends ContributorNoop implements BatchReader<E> {
    @Override
    public void produceTo(Consumer<? super E> whereToPut) {
    }
}
