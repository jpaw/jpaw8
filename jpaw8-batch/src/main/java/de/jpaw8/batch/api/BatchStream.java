package de.jpaw8.batch.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import de.jpaw8.batch.api.cmdline.impl.ContributorDelegator;
import de.jpaw8.batch.api.stream.impl.BatchStreamConsumer;
import de.jpaw8.batch.api.stream.impl.BatchStreamFilter;
import de.jpaw8.batch.api.stream.impl.BatchStreamIntFilter;
import de.jpaw8.batch.api.stream.impl.BatchStreamIterable;
import de.jpaw8.batch.api.stream.impl.BatchStreamMap;
import de.jpaw8.batch.api.stream.impl.BatchStreamNewThread;
import de.jpaw8.batch.api.stream.impl.BatchStreamObjIntFilter;
import de.jpaw8.batch.api.stream.impl.BatchStreamObjIntMap;
import de.jpaw8.function.ObjIntFunction;
import de.jpaw8.function.ObjIntPredicate;

/** BatchStream<E> is a class which represents a Producer of items with their ordinal, creating objects of type E. */

public abstract class BatchStream<E> extends ContributorDelegator implements BatchReader<E> {
    
    protected BatchStream(Contributor priorStream) {
        super(priorStream);
    }
    
    /** Creates a batch from some Iterable. */
    static public <F> BatchStreamIterable<F> of(Iterable<F> iter) {
        return new BatchStreamIterable<F>(iter);
    }
    
    /** private class for the Consumer wrapper. */
//    private static class Wrapper<Y> implements OrdinalConsumer<Y> {
//        private final Consumer<Y> c;
//        private Wrapper(Consumer<Y> c) {
//            this.c = c;
//        }
//        @Override
//        public void accept(int i, Y data) {
//            
//        }
//    }
    
    /** Converts a OrdinalConsumer to a Consumer */
    public static <X> ObjIntConsumer<X> asObjIntConsumer(final Consumer<X> c) {
        return (X t, int i) -> c.accept(t); 
    }
    
    // filter

    public BatchStream<E> filter(Predicate<? super E> filter) {
        return new BatchStreamFilter<E>(this, filter);
    }
    public BatchStream<E> intfilter(IntPredicate ordinalFilter) {       // different name required because it's ambiguous otherwise
        return new BatchStreamIntFilter<E>(this, ordinalFilter);
    }
    public BatchStream<E> filter(ObjIntPredicate<? super E> biFilter) {
        return new BatchStreamObjIntFilter<E>(this, biFilter);
    }

    // map
    
    public <F> BatchStream<F> map(Function<E,F> function) {
        return new BatchStreamMap<E,F>(this, function);
    }
    public <F> BatchStream<F> map(ObjIntFunction<E,F> function) {
        return new BatchStreamObjIntMap<E,F>(this, function);
    }
    
    // forEach

    public Batch forEach(Consumer<? super E> consumer) {
        return new BatchStreamConsumer<E>(this, consumer);
    }
    
    // thread splitter
    
    public <F> BatchStream<E> newThread() {
        return new BatchStreamNewThread<E>(this, 1024);
    }
}

