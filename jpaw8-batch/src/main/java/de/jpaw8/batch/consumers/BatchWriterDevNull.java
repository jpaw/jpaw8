package de.jpaw8.batch.consumers;

import de.jpaw8.batch.api.BatchWriter;
import de.jpaw8.batch.api.cmdline.impl.ContributorNoop;

public class BatchWriterDevNull<E> extends ContributorNoop implements BatchWriter<E> {

    @Override
    public void apply(int no, E response) {
    }
}
