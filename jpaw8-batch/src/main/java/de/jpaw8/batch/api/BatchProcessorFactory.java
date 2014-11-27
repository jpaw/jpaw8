package de.jpaw8.batch.api;


/** Process input of type E to produce output of type F. */
public interface BatchProcessorFactory<E,F> extends BatchIO {
    BatchProcessor<E,F> getProcessor(int threadNo) throws Exception;
}
