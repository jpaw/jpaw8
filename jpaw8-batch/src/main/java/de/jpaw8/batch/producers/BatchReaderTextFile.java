package de.jpaw8.batch.producers;

import java.util.function.ObjIntConsumer;

import de.jpaw8.batch.api.BatchReader;
import de.jpaw8.batch.producers.base.BatchReaderTextFileAbstract;

public class BatchReaderTextFile extends BatchReaderTextFileAbstract implements BatchReader<String> {

    @Override
    public void produceTo(ObjIntConsumer<? super String> whereToPut) throws Exception {
        String line;
        int n = 0;
        while ((line = getNext()) != null) {
            whereToPut.accept(line, ++n);
        }
    }
}
