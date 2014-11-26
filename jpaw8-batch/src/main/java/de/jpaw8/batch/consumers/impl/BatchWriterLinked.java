package de.jpaw8.batch.consumers.impl;

import de.jpaw8.batch.api.BatchIO;

/** A stream (reader) which receives input from a nested reader.
 * Purpose of this class is to invoke the open() and close() respectively.
 *
 * @param <E>
 */
public abstract class BatchWriterLinked<E> extends BatchWriterAbstract<E> {
    private final BatchIO myConsumer;
    protected BatchWriterLinked(BatchIO producer) {
        myConsumer = producer;
    }
    
    @Override
    public void open() throws Exception {
        myConsumer.open();
    }
    @Override
    public void close() throws Exception {
        myConsumer.close();
    }

}
