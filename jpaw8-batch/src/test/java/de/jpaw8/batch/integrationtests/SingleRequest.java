package de.jpaw8.batch.integrationtests;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.jpaw8.batch.api.Batch;
import de.jpaw8.batch.producers.BatchRange;


public class SingleRequest {
    static private class Adder implements Consumer<Long> {
        long sum = 0L;
        
        @Override
        public void accept(Long t) {
//            System.out.println("Got  " + t);
            sum += t.longValue();
        }
    }
    static private class Counter implements Consumer<Object> {
        int num = 0;
        
        @Override
        public void accept(Object t) {
            ++num;
        }
    }
    static private class ParallelCounter implements Consumer<Object> {
        AtomicInteger num = new AtomicInteger();
        
        @Override
        public void accept(Object t) {
            num.incrementAndGet();
        }
    }
    static private class Delay implements Predicate<Object> {
        
        @Override
        public boolean test(Object t) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted: " + e);
            }
            return true;
        }
    }
    
    @Test
    public void testCounterSimple() throws Exception {
        Counter a = new Counter();
        Batch sequence = new BatchRange(1L, 2000L).forEach(a);
        sequence.runAll();
        Assert.assertEquals(a.num, 2000);
    }

    @Test
    public void testCounterMap() throws Exception {
        Counter a = new Counter();
        Batch sequence = new BatchRange(1L, 2000L).map(l -> l * l).forEach(a);
        sequence.runAll();
        Assert.assertEquals(a.num, 2000);
    }

    @Test
    public void testCounterFilter() throws Exception {
        Counter a = new Counter();
        Batch sequence = new BatchRange(1L, 2000L).filter(l -> (l & 1) == 0L).forEach(a);
        sequence.runAll();
        Assert.assertEquals(a.num, 1000);
    }

    @Test
    public void testCounter() throws Exception {
        Counter a = new Counter();
        Batch sequence = new BatchRange(1L, 2000L).filter(l -> (l & 1) == 0L).map(l -> l * l).forEach(a);
        sequence.runAll();
        Assert.assertEquals(a.num, 1000);
    }
    
    @Test
    public void testAdder() throws Exception {
        Adder a = new Adder();
        Batch sequence = new BatchRange(1L, 100L).filter(l -> (l & 1) == 0L).map(l -> l * l).forEach(a);
//        Batch sequence = new BatchRange(1L, 100L).filter(l -> (l & 1) == 0L).map(l -> { System.out.println("Got " + l); return l * l; }).forEach(a);
        sequence.runAll();
        Assert.assertEquals(a.sum, 171700);
    }

    @Test
    public void testAdderLMAX() throws Exception {
        Adder a = new Adder();
        Batch sequence = new BatchRange(1L, 100L).newThread().filter(l -> (l & 1) == 0L).map(l -> l * l).forEach(a);
//        Batch sequence = new BatchRange(1L, 100L).filter(l -> (l & 1) == 0L).map(l -> { System.out.println("Got " + l); return l * l; }).forEach(a);
        sequence.runAll();
        Assert.assertEquals(a.sum, 171700);
    }

    @Test
    public void testCounterDelays() throws Exception {
        Counter a = new Counter();
        Delay d = new Delay();
        Date start = new Date();
        Batch sequence = new BatchRange(1L, 10L).filter(d).filter(d).forEach(a);
        sequence.runAll();
        Date end = new Date();
        Assert.assertEquals(a.num, 10);
        System.out.println("Took " + (end.getTime() - start.getTime()) + " ms");
    }

    @Test
    public void testCounterDelaysParallel() throws Exception {
        Counter a = new Counter();
        Delay d = new Delay();
        Date start = new Date();
        Batch sequence = new BatchRange(1L, 10L).filter(d).newThread().filter(d).forEach(a);
        sequence.runAll();
        Date end = new Date();
        Assert.assertEquals(a.num, 10);
        System.out.println("Took " + (end.getTime() - start.getTime()) + " ms");
    }
    
    @Test
    public void testCounterDelays4Parallel() throws Exception {
        ParallelCounter a = new ParallelCounter();
        Delay d = new Delay();
        Date start = new Date();
        Batch sequence = new BatchRange(1L, 12L).parallel(4).filter(d).forEach(a);
        sequence.runAll();
        Date end = new Date();
        Assert.assertEquals(a.num.get(), 12);
        System.out.println("Took " + (end.getTime() - start.getTime()) + " ms");
    }
}
