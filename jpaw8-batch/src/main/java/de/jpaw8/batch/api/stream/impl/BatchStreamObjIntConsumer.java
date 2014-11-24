package de.jpaw8.batch.api.stream.impl;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.Batch;
import de.jpaw8.batch.api.BatchStream;

public class BatchStreamObjIntConsumer<E> extends Batch {
    private final BatchStream<? extends E> producer;
    private final ObjIntConsumer<? super E> consumer;

    public BatchStreamObjIntConsumer(BatchStream<? extends E> producer, ObjIntConsumer<? super E> consumer) {
        super(producer);
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public void run() throws Exception {
        producer.produceTo((data, i) -> consumer.accept(data, i));
    }
}
