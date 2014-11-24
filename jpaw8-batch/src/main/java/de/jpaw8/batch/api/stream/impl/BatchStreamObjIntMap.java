package de.jpaw8.batch.api.stream.impl;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchStream;
import de.jpaw8.function.ObjIntFunction;

public class BatchStreamObjIntMap<E,R> extends BatchStream<R> {
    private final BatchStream<? extends E> producer;
    private final ObjIntFunction<E,R> function;

    public BatchStreamObjIntMap(BatchStream<? extends E> producer, ObjIntFunction<E,R> function) {
        super(producer);
        this.producer = producer;
        this.function = function;
    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super R> whereToPut) throws Exception {
        producer.produceTo((data, i) -> whereToPut.accept(function.apply(data, i), i));
    }
}
