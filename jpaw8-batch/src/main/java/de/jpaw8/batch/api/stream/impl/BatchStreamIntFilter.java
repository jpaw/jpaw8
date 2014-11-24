package de.jpaw8.batch.api.stream.impl;

import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchStream;

public class BatchStreamIntFilter<E> extends BatchStream<E> {
    private final BatchStream<? extends E> producer;
    private final IntPredicate ordinalFilter;

    public BatchStreamIntFilter(BatchStream<? extends E> producer, IntPredicate ordinalFilter) {
        super(producer);
        this.producer = producer;
        this.ordinalFilter = ordinalFilter;
    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super E> whereToPut) throws Exception {
        producer.produceTo((data, i) -> { if (ordinalFilter.test(i)) whereToPut.accept(data, i); });
    }
}
