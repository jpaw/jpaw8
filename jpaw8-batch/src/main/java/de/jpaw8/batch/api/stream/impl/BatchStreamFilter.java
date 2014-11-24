package de.jpaw8.batch.api.stream.impl;

import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import de.jpaw8.batch.api.BatchStream;

public class BatchStreamFilter<E> extends BatchStream<E> {
    private final BatchStream<? extends E> producer;
    private final Predicate<? super E> filter;

    public BatchStreamFilter(BatchStream<? extends E> producer, Predicate<? super E> filter) {
        super(producer);
        this.producer = producer;
        this.filter = filter;
    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super E> whereToPut) throws Exception {
        producer.produceTo((data, i) -> { if (filter.test(data)) whereToPut.accept(data, i); });
    }
}
