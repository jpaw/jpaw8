package de.jpaw.batch.api;

import java.util.function.Consumer;

public class BatchStreamIterable<E> extends BatchStream<E> {
    private final Iterable<E> iter;

    public BatchStreamIterable(Iterable<E> iter) {
        super(null);
        this.iter = iter;
    }

    @Override
    public void produceTo(Consumer<? super E> whereToPut) throws Exception {
        for (E e : iter)
            whereToPut.accept(e);
    }
}
