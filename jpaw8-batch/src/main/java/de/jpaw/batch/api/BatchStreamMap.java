package de.jpaw.batch.api;

import java.util.function.Consumer;
import java.util.function.Function;

public class BatchStreamMap<E,R> extends BatchStream<R> {
    private final BatchStream<? extends E> producer;
    private final Function<E,R> function;

    public BatchStreamMap(BatchStream<? extends E> producer, Function<E,R> function) {
        super(producer);
        this.producer = producer;
        this.function = function;
    }
    
    @Override
    public void produceTo(final Consumer<? super R> whereToPut) throws Exception {
        producer.produceTo(data -> whereToPut.accept(function.apply(data)));
    }
}
