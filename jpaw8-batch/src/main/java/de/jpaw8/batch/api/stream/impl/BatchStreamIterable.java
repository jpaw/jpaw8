package de.jpaw8.batch.api.stream.impl;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchStream;

public class BatchStreamIterable<E> extends BatchStream<E> {
    private final Iterable<E> iter;

    public BatchStreamIterable(Iterable<E> iter) {
        super(null);
        this.iter = iter;
    }

    @Override
    public void produceTo(ObjIntConsumer<? super E> whereToPut) throws Exception {
        int n = 0;
        for (E e : iter)
            whereToPut.accept(e, ++n);
    }
}
