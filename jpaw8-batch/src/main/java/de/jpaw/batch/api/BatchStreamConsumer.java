package de.jpaw.batch.api;

import java.util.function.Consumer;

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
        producer.produceTo(data -> consumer.accept(data));
    }
}
