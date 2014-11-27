package de.jpaw8.batch.api;


public interface BatchReaderFactory<E> extends BatchIO {
    public void produceTo(BatchWriterFactory<? super E> whereToPut) throws Exception;

}
