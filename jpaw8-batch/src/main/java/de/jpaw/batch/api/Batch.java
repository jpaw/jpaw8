package de.jpaw.batch.api;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Parameter;
import com.martiansoftware.jsap.SimpleJSAP;

import de.jpaw.batch.impl.ContributorDelegator;

public abstract class Batch extends ContributorDelegator {
    private static final Logger LOG = LoggerFactory.getLogger(Batch.class);
    
    // some statistics data
    protected Date programStart;
    protected Date programEnd;
    protected Date parsingStart;
    protected Date parsingEnd;
    
    protected Batch(Contributor priorStream) {
        super(priorStream);
    }
    
    abstract public void run() throws Exception;
    
    public final void runAll(String ... args) throws Exception {
        programStart = new Date();
        // add the main command line parameters
        SimpleJSAP commandLineOptions = null;
        try {
            commandLineOptions = new SimpleJSAP("Bonaparte batch processor", "Runs batched tasks with multithreading", new Parameter[] {});
        } catch (JSAPException e) {
            LOG.error("Cannot create command line parameters: {}", e);
            System.exit(1);
        }
        // add input / output related options
        addCommandlineParameters(commandLineOptions);
        
        JSAPResult params = commandLineOptions.parse(args);
        if (commandLineOptions.messagePrinted()) {
            System.err.println("(use option --help for usage)");
            System.exit(1);
        }
        
        evalCommandlineParameters(params);
        
        parsingStart = new Date();
        LOG.info("{}, Bonaparte batch: Starting to parse", parsingStart);
        
        run();
        
        parsingEnd = new Date();
        long timediffInMillis = parsingEnd.getTime() - parsingStart.getTime();
        
        close();
        
        programEnd = new Date();
        timediffInMillis = programEnd.getTime() - programStart.getTime();
        LOG.info("{}, Bonaparte batch: total time = {} ms", programEnd, timediffInMillis);
    }
}
