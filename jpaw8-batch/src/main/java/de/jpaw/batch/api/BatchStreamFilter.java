package de.jpaw.batch.api;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class BatchStreamFilter<E> extends BatchStream<E> {
    private final BatchStream<? extends E> producer;
    private final Predicate<E> filter;

    public BatchStreamFilter(BatchStream<? extends E> producer, Predicate<E> filter) {
        super(producer);
        this.producer = producer;
        this.filter = filter;
    }
    
    @Override
    public void produceTo(final Consumer<? super E> whereToPut) throws Exception {
        producer.produceTo(data -> { if (filter.test(data)) whereToPut.accept(data); });
        // xtend: producer.produceTo[ if (filter.test(it)) whereToPut.accept(it) ]
    }
}
