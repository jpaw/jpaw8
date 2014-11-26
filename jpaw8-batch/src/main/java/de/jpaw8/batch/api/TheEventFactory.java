package de.jpaw8.batch.api;

import com.lmax.disruptor.EventFactory;

public class TheEventFactory<T> implements EventFactory<DataWithOrdinal<T>> {

    @Override
    public DataWithOrdinal<T> newInstance() {
        return new DataWithOrdinal<T>(null, 0);
    }
}
