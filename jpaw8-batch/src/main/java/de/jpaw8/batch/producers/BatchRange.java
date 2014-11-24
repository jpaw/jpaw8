package de.jpaw8.batch.producers;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchStream;

public class BatchRange extends BatchStream<Long> {
    final long from, to;
    
    public BatchRange(long from, long to) {
        super(null);
        this.from = from;
        this.to = to;
    }

    @Override
    public void produceTo(ObjIntConsumer<? super Long> whereToPut) throws Exception {
        int n = 0;
        for (long l = from; l <= to; ++l)
            whereToPut.accept(Long.valueOf(l), ++n);
    }
}
