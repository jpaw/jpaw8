package de.jpaw8.batch.api.stream.impl;

import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import de.jpaw8.batch.api.BatchStream;
import de.jpaw8.batch.api.DataWithOrdinal;

public class BatchStreamNewThread<E> extends BatchStream<E> {
    private final BatchStream<? extends E> producer;
    private final Disruptor<DataWithOrdinal<E>> disruptor;
    
    private static class MyEventFactory<T> implements EventFactory<DataWithOrdinal<T>> {

        @Override
        public DataWithOrdinal<T> newInstance() {
            return new DataWithOrdinal<T>(null, 0);
        }
    }
    private final EventFactory<DataWithOrdinal<E>> factory = new MyEventFactory<E>();

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
                
            } finally {
                rb.publish(sequence);
            }
        }
    }
    
    public BatchStreamNewThread(BatchStream<? extends E> producer, int bufferSize) {
        super(producer);
        this.producer = producer;
        
        // Construct the Disruptor which interfaces the decoder to DB storage
        disruptor = new Disruptor<DataWithOrdinal<E>>(factory, bufferSize, Executors.newSingleThreadExecutor());

    }
    
    @Override
    public void produceTo(final ObjIntConsumer<? super E> whereToPut) throws Exception {
        // Connect the handler
        EventHandler<DataWithOrdinal<E>> handler = (data, sequence, isLast) -> whereToPut.accept(data.data, data.recordno);
        disruptor.handleEventsWith(handler);

        // Start the Disruptor, starts all threads running
        // and get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<DataWithOrdinal<E>> rb = disruptor.start();

        // kick off the sender
        producer.produceTo(new MyConsumer<E>(rb));
    }
}
