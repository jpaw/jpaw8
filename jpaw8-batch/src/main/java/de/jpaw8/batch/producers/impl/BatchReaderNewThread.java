package de.jpaw8.batch.producers.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.ObjIntConsumer;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import de.jpaw8.batch.api.BatchReader;
import de.jpaw8.batch.api.DataWithOrdinal;
import de.jpaw8.batch.api.TheEventFactory;

public class BatchReaderNewThread<E> extends BatchReaderLinked<E> {
    private final BatchReader<? extends E> producer;
    private final int bufferSize;
    private final int numThreads;
    
    private final EventFactory<DataWithOrdinal<E>> factory = new TheEventFactory<E>();

//    private static class MyEventHandler<F> implements EventHandler<DataWithOrdinal<F>> {
//        private final ObjIntConsumer<? super F> localWriter;
//
//        private MyEventHandler(ObjIntConsumer<? super F> localWriter) {
//            this.localWriter = localWriter;
//        }
//        
//        @Override
//        public void onEvent(DataWithOrdinal<F> data, long sequence, boolean isLast) throws Exception {
//            // and write the output to the writer
//            localWriter.accept(data.data, data.recordno);
//        }
//        
//    }
    
    private static class MyConsumer<F> implements ObjIntConsumer<F> {
        private final RingBuffer<DataWithOrdinal<F>> rb;
        private MyConsumer(RingBuffer<DataWithOrdinal<F>> rb) {
            this.rb = rb;
        }
        
        // BatchMainCallback. Calls to this procedure feed the input disruptor
        @Override
        public void accept(F record, int n) {
            
            long sequence = rb.next();  // Grab the next sequence
            try {
                DataWithOrdinal<F> event = rb.get(sequence);    // Get the entry in the Disruptor for the sequence
                event.data = record;                            // fill data
                event.recordno = n;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                rb.publish(sequence);
            }
        }
    }
    
    public BatchReaderNewThread(BatchReader<? extends E> producer, int bufferSize) {
        super(producer);
        this.producer = producer;
        this.bufferSize = bufferSize;
        numThreads = 1;
    }
    
    public BatchReaderNewThread(BatchReader<? extends E> producer, int bufferSize, int numThreads) {
        super(producer);
        this.producer = producer;
        this.bufferSize = bufferSize;
        this.numThreads = numThreads;
    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super E> whereToPut) throws Exception {
        // create an executorService
        ExecutorService threads = (numThreads <= 1) ? Executors.newSingleThreadExecutor() : Executors.newFixedThreadPool(numThreads) ;
        
        // Construct the Disruptor which interfaces the decoder to DB storage
        final Disruptor<DataWithOrdinal<E>> disruptor = new Disruptor<DataWithOrdinal<E>>(factory, bufferSize, threads);
        
        // Connect the handler - ST
        EventHandler<DataWithOrdinal<E>> handler = (data, sequence, isLast) -> whereToPut.accept(data.data, data.recordno);
        disruptor.handleEventsWith(handler);
        
        // Connect the handler - MT - notice the difference between EventHandler (all get the same record) and WorkHandler (only one of all gets the recod)
//        WorkHandler<DataWithOrdinal<E>> [] handlerTab = new WorkHandler [numThreads];
//        for (int i = 0; i < numThreads; ++i)
//            handlerTab[i] = (data) -> whereToPut.accept(data.data, data.recordno);
//        disruptor.handleEventsWithWorkerPool(handlerTab);

        
        // Start the Disruptor, starts all threads running
        // and get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DataWithOrdinal<E>> rb = disruptor.start();

        // kick off the sender
        producer.produceTo(new MyConsumer<E>(rb));
        
        // tell them it's done
        disruptor.shutdown();
        
        // shutdown the Executor
        threads.shutdown();
    }
}
