package de.jpaw.batch.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.jpaw.batch.impl.ContributorDelegator;

/** BatchStream<E> is a class which represents a Producer, creating objects of type E. */

public abstract class BatchStream<E> extends ContributorDelegator implements BatchReader<E> {
    
    protected BatchStream(Contributor priorStream) {
        super(priorStream);
    }
    
    /** Creates a batch from some Iterable. */
    static public <F> BatchStreamIterable<F> of(Iterable<F> iter) {
        return new BatchStreamIterable<F>(iter);
    }

    public BatchStream<E> filter(Predicate<E> filter) {
        return new BatchStreamFilter<E>(this, filter);
    }

    public <F> BatchStream<F> map(Function<E,F> function) {
        return new BatchStreamMap<E,F>(this, function);
    }

    public Batch forEach(Consumer<? super E> consumer) {
        return new BatchStreamConsumer<E>(this, consumer);
    }
}

