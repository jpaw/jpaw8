package de.jpaw8.batch.api.cmdline.impl;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;

import de.jpaw8.batch.api.Contributor;

public class ContributorDelegator implements Contributor {
    protected final Contributor priorStream;
    
    protected ContributorDelegator(Contributor priorStream) {
        this.priorStream = priorStream;
    }
    
    // delegator methods to linked stream
    @Override
    public void addCommandlineParameters(JSAP params) throws Exception {
        if (priorStream != null)
            priorStream.addCommandlineParameters(params);
    }

    @Override
    public void evalCommandlineParameters(JSAPResult params) throws Exception {
        if (priorStream != null)
            priorStream.evalCommandlineParameters(params);
    }

    @Override
    public void close() throws Exception {
        if (priorStream != null)
            priorStream.close();
    }
}
