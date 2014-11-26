package de.jpaw8.benchmarks.lambda;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import de.jpaw8.batch.api.Batch;
import de.jpaw8.batch.producers.BatchReaderRange;

// Benchmarks to investigate how much performance the new lambda take

//java -jar target/jpaw8-benchmarks.jar -i 3 -f 3 -wf 1 -wi 3 ".*LmaxTest.*"
//# Run complete. Total time: 00:02:31

//Benchmark                           Mode  Samples        Score        Error  Units
//d.j.b.l.LmaxTest.jpawStreamMap      avgt        9    69354.892 ±   1621.926  ns/op
//d.j.b.l.LmaxTest.jpawStreamSetup    avgt        9       81.025 ±      0.613  ns/op
//d.j.b.l.LmaxTest.lmaxMap            avgt        9  1643093.088 ± 156223.160  ns/op
//d.j.b.l.LmaxTest.lmaxSetup          avgt        9   151717.831 ±  14879.137  ns/op




@State(value = Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class LmaxTest {
    public static final long NUM = 10000;
    

//    
//  Benchmarks to measure the overhead of the disruptor
//    
    
    @Benchmark
    public void lmaxMap(Blackhole bh) throws Exception {
        Batch sequence = new BatchReaderRange(1, NUM).newThread().forEach(l -> bh.consume(l));
        sequence.run();
    }
    @Benchmark
    public void lmaxSetup(Blackhole bh) throws Exception {
        Batch sequence = new BatchReaderRange(1,  10).newThread().forEach(l -> bh.consume(l));
        sequence.run();
    }

    @Benchmark
    public void jpawStreamMap(Blackhole bh) throws Exception {
        Batch sequence = new BatchReaderRange(1, NUM).forEach(l -> bh.consume(l));
        sequence.run();
    }

    @Benchmark
    public void jpawStreamSetup(Blackhole bh) throws Exception {
        Batch sequence = new BatchReaderRange(1,  10).forEach(l -> bh.consume(l));
        sequence.run();
    }

}