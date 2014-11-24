package de.jpaw8.batch.consumers;

import de.jpaw8.batch.api.BatchWriter;
import de.jpaw8.batch.consumers.base.BatchWriterTextFileAbstract;

public class BatchWriterSimpleCSVResult extends BatchWriterTextFileAbstract implements BatchWriter<Boolean> {

    private String getResult(Boolean data) {
        if (data != null && data.booleanValue())
            return "OK";
        else
            return "ERROR";
    }
    
    @Override
    public void apply(int no, Boolean response) throws Exception {
        super.write(no + "," + getResult(response) + "\n");
    }
}
