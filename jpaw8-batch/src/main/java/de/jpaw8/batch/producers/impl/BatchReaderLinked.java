package de.jpaw8.batch.producers.impl;

import de.jpaw8.batch.api.BatchIO;

/** A stream (reader) which receives input from a nested reader.
 * Purpose of this class is to invoke the open() and close() respectively.
 *
 * @param <E>
 */
public abstract class BatchReaderLinked<E> extends BatchReaderAbstract<E> {
    private final BatchIO myProducer;
    protected BatchReaderLinked(BatchIO producer) {
        myProducer = producer;
    }
    
    @Override
    public void open() throws Exception {
        myProducer.open();
    }
    @Override
    public void close() throws Exception {
        myProducer.close();
    }

}
