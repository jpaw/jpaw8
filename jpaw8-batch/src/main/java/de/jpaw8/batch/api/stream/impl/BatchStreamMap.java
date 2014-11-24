package de.jpaw8.batch.api.stream.impl;

import java.util.function.Function;
import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchStream;

public class BatchStreamMap<E,R> extends BatchStream<R> {
    private final BatchStream<? extends E> producer;
    private final Function<E,R> function;

    public BatchStreamMap(BatchStream<? extends E> producer, Function<E,R> function) {
        super(producer);
        this.producer = producer;
        this.function = function;
    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super R> whereToPut) throws Exception {
        producer.produceTo((data, i) -> whereToPut.accept(function.apply(data), i));
    }
}
