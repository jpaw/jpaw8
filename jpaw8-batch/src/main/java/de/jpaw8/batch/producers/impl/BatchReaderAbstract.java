package de.jpaw8.batch.producers.impl;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

import de.jpaw8.batch.api.Batch;
import de.jpaw8.batch.api.BatchReader;
import de.jpaw8.batch.api.BatchWriter;
import de.jpaw8.batch.consumers.BatchWriterDevNull;
import de.jpaw8.batch.consumers.impl.BatchWriterConsumer;
import de.jpaw8.batch.consumers.impl.BatchWriterConsumerObjInt;
import de.jpaw8.function.ObjIntFunction;
import de.jpaw8.function.ObjIntPredicate;

/** AbstractBatchReader<E> is a class which represents a Producer of items with their ordinal, creating objects of type E.
 * The only addon provided in this abstract class, comparsed to the underlying interface, is the definition of the
 * mapping / linking methods (which should work well as extension methods in xtend). */

public abstract class BatchReaderAbstract<E> implements BatchReader<E> {
   
    /** Creates a batch from some Iterable. */
    static public <F> BatchReaderIterable<F> of(Iterable<F> iter) {
        return new BatchReaderIterable<F>(iter);
    }
    
    
    
    // filter

    public BatchReaderAbstract<E> filter(Predicate<? super E> filter) {
        return new BatchReaderFilter<E>(this, filter);
    }
    public BatchReaderAbstract<E> intfilter(IntPredicate ordinalFilter) {       // different name required because it's ambiguous otherwise
        return new BatchReaderFilterInt<E>(this, ordinalFilter);
    }
    public BatchReaderAbstract<E> filter(ObjIntPredicate<? super E> biFilter) {
        return new BatchReaderFilterObjInt<E>(this, biFilter);
    }
    
    

    // map
    
    public <F> BatchReaderAbstract<F> map(Function<E,F> function) {
        return new BatchReaderMap<E,F>(this, function);
    }
    public <F> BatchReaderAbstract<F> map(ObjIntFunction<E,F> function) {
        return new BatchReaderMapObjInt<E,F>(this, function);
    }
    
    
    
    // forEach

    public Batch<E> forEach(Consumer<? super E> consumer) {
        return new Batch<E> (this, new BatchWriterConsumer<E>(consumer));
    }
    public Batch<E> forEach(ObjIntConsumer<? super E> consumer) {
        return new Batch<E> (this, new BatchWriterConsumerObjInt<E>(consumer));
    }
    public Batch<E> forEach(BatchWriter<? super E> consumer) {
        return new Batch<E> (this, consumer);
    }

    // discard
    public Batch<E> discard() {
        return new Batch<E> (this, new BatchWriterDevNull<E>());
    }
    // add a discard and run it right away.
    public void run(String ... args) throws Exception {
        discard().run(args);       // just a synonym
    }
    
    
    
    // thread splitter
    
    public <F> BatchReaderAbstract<E> newThread() {
        return new BatchReaderNewThread<E>(this, 1024);
    }
    public <F> BatchReaderAbstract<E> parallel(int numThreads) {
        return new BatchReaderNewThread<E>(this, 1024, numThreads);
    }
}

