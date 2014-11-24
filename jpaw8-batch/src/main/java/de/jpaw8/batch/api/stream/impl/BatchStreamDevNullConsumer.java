package de.jpaw8.batch.api.stream.impl;

import de.jpaw8.batch.api.Batch;
import de.jpaw8.batch.api.BatchStream;

public class BatchStreamDevNullConsumer<E> extends Batch {
    private final BatchStream<? extends E> producer;

    public BatchStreamDevNullConsumer(BatchStream<? extends E> producer) {
        super(producer);
        this.producer = producer;
    }

    @Override
    public void run() throws Exception {
        producer.produceTo((data, i) -> {});
    }
}
