package de.jpaw8.batch.api.stream.impl;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchStream;
import de.jpaw8.function.ObjIntPredicate;

public class BatchStreamObjIntFilter<E> extends BatchStream<E> {
    private final BatchStream<? extends E> producer;
    private final ObjIntPredicate<? super E> biFilter;

    public BatchStreamObjIntFilter(BatchStream<? extends E> producer, ObjIntPredicate<? super E> biFilter) {
        super(producer);
        this.producer = producer;
        this.biFilter = biFilter;
    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super E> whereToPut) throws Exception {
        producer.produceTo((data, i) -> { if (biFilter.test(data, i)) whereToPut.accept(data, i); });
    }
}
