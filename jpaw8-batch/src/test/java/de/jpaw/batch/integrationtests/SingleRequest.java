package de.jpaw.batch.integrationtests;

import java.util.function.Consumer;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.jpaw.batch.api.Batch;
import de.jpaw.batch.endpoints.BatchRange;


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

}
