package de.jpaw.batch.endpoints;

import java.util.function.Consumer;

import de.jpaw.batch.api.BatchStream;

public class BatchRange extends BatchStream<Long> {
    final long from, to;
    
    public BatchRange(long from, long to) {
        super(null);
        this.from = from;
        this.to = to;
    }

    @Override
    public void produceTo(Consumer<? super Long> whereToPut) throws Exception {
        for (long l = from; l <= to; ++l)
            whereToPut.accept(Long.valueOf(l));
    }
}
