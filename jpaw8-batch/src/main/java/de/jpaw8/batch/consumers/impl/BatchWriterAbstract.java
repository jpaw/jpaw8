package de.jpaw8.batch.consumers.impl;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import de.jpaw8.batch.api.BatchWriter;
import de.jpaw8.function.ObjIntFunction;
import de.jpaw8.function.ObjIntPredicate;

/** AbstractBatchWriter<E> is a class which represents a Consumer of items with their ordinal, creating objects of type E.
 * The only addon provided in this abstract class, comparsed to the underlying interface, is the definition of the
 * mapping / linking methods (which should work well as extension methods in xtend). */

public abstract class BatchWriterAbstract<E> implements BatchWriter<E> {
    
    /** Converts a OrdinalConsumer to a Consumer */
    public static <X> ObjIntConsumer<X> asObjIntConsumer(final Consumer<X> c) {
        return (X t, int i) -> c.accept(t); 
    }
    
    public static <X> BatchWriter<X> of(Consumer<X> consumer) {
        return new BatchWriterConsumer<X>(consumer);
    }
    
    
    // filter

    public BatchWriterAbstract<E> filteredFrom(Predicate<? super E> filter) {
        return new BatchWriterFilter<E>(this, filter);
    }
    public BatchWriterAbstract<E> intfilteredFrom(IntPredicate ordinalFilter) {       // different name required because it's ambiguous otherwise
        return new BatchWriterFilterInt<E>(this, ordinalFilter);
    }
    public BatchWriterAbstract<E> filteredFrom(ObjIntPredicate<? super E> biFilter) {
        return new BatchWriterFilterObjInt<E>(this, biFilter);
    }
    
    

    // map
    
    public <F> BatchWriterAbstract<F> mappedFrom(Function<F,E> function) {
        return new BatchWriterMap<F,E>(this, function);
    }
    public <F> BatchWriterAbstract<F> mappedFrom(ObjIntFunction<F,E> function) {
        return new BatchWriterMapObjInt<F,E>(this, function);
    }
    
    
    
    // thread splitter
    
    public <F> BatchWriterAbstract<E> newThread() {
        return new BatchWriterNewThread<E>(this, 1024);
    }
    public <F> BatchWriterAbstract<E> parallel(int numThreads) {
        return new BatchWriterNewThread<E>(this, 1024, numThreads);
    }
}

