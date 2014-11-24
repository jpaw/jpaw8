package de.jpaw8.batch.api.stream.impl;

import java.util.function.Consumer;

import de.jpaw8.batch.api.Batch;
import de.jpaw8.batch.api.BatchStream;

public class BatchStreamConsumer<E> extends Batch {
    private final BatchStream<? extends E> producer;
    private final Consumer<? super E> consumer;

    public BatchStreamConsumer(BatchStream<? extends E> producer, Consumer<? super E> consumer) {
        super(producer);
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public void run() throws Exception {
        producer.produceTo((data, i) -> consumer.accept(data));
    }
}
