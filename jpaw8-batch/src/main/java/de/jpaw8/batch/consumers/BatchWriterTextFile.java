package de.jpaw8.batch.consumers;

import de.jpaw8.batch.api.BatchWriter;
import de.jpaw8.batch.consumers.base.BatchWriterTextFileAbstract;

public class BatchWriterTextFile extends BatchWriterTextFileAbstract implements BatchWriter<String> {

    public BatchWriterTextFile(String header, String footer) {
        super(header, footer);
    }
    public BatchWriterTextFile() {
        super();
    }

    @Override
    public void apply(int no, String response) throws Exception {
        super.write(response);
    }
}
