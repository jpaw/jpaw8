package de.jpaw8.batch.api;

public interface BatchWriterFactory<E> extends BatchIO {
    BatchWriter<E> get(int threadno);
}
