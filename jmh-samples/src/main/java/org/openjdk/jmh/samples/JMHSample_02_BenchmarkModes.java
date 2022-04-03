/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openjdk.jmh.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class JMHSample_02_BenchmarkModes {

    /*
     * JMH有多种模式用于衡量基准方法。用户可通过特定注解或者通过运行时参数选择。
     *
     * JMH generates lots of synthetic code for the benchmarks for you during
     * the benchmark compilation. JMH can measure the benchmark methods in lots
     * of modes. Users may select the default benchmark mode with a special
     * annotation, or select/override the mode via the runtime options.
     *
     * 如果基准方法抛出异常则JMH会退出。
     * With this scenario, we start to measure something useful. Note that our
     * payload code potentially throws exceptions, and we can just declare them
     * to be thrown. If the code throws the actual exception, the benchmark
     * execution will stop with an error.
     *
     * 如果对某些行为感到困惑，可尝试查看生成的代码。良好的实验总是伴随着实验设置、交叉检查生成的代码。
     * When you are puzzled with some particular behavior, it usually helps to
     * look into the generated code. You might see the code is doing not
     * something you intend it to do. Good experiments always follow up on the
     * experimental setup, and cross-checking the generated code is an important
     * part of that follow up.
     *
     * The generated code for this particular sample is somewhere at
     * target/generated-sources/annotations/.../JMHSample_02_BenchmarkModes.java
     */

    /*
     * 吞吐量。通过在限定时间内持续调用基准方法并计数获取
     * Mode.Throughput, as stated in its Javadoc, measures the raw throughput by
     * continuously calling the benchmark method in a time-bound iteration, and
     * counting how many times we executed the method.
     *
     * 这里指定了衡量的单位，你也可以用默认值。
     * We are using the special annotation to select the units to measure in,
     * although you can use the default.
     */

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /*
     * 平均时间测量平均执行时间，行为与吞吐量模式类似。
     * Mode.AverageTime measures the average execution time, and it does it
     * in the way similar to Mode.Throughput.
     *
     * 有人说这是吞吐量的倒数，哎，他就是。不过有些工作它做起来就是更方便
     * Some might say it is the reciprocal throughput, and it really is.
     * There are workloads where measuring times is more convenient though.
     */

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAvgTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /**
     * 采样时间模式采集执行时间。也在限定时间执行，不过不测量总时间，而是测量一些基准方法调用时间
     * Mode.SampleTime samples the execution time. With this mode, we are
     * still running the method in a time-bound iteration, but instead of
     * measuring the total time, we measure the time spent in *some* of
     * the benchmark method calls.
     *
     * 可以推断分布、百分数等。
     * This allows us to infer the distributions, percentiles, etc.
     *
     * JMH也尝试自动适配采样频率：如果方法足够长，你可以捕获到所有样本。
     * JMH also tries to auto-adjust sampling frequency: if the method
     * is long enough, you will end up capturing all the samples.
     */
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureSamples() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /**
     * 单发时间记录单个方法执行时间。
     * Mode.SingleShotTime measures the single method invocation time. As the Javadoc
     * suggests, we do only the single benchmark method invocation. The iteration
     * time is meaningless in this mode: as soon as benchmark method stops, the
     * iteration is over.
     *
     * 对于冷启动测试很有效，当你不想持续调用基准方法。
     * This mode is useful to do cold startup tests, when you specifically
     * do not want to call the benchmark method continuously.
     */
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureSingleShot() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /**
     * 也可以一次选择多种模式。
     * We can also ask for multiple benchmark modes at once. All the tests
     * above can be replaced with just a single test like this:
     */
    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureMultiple() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /*
     * Or even...
     */

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureAll() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * You are expected to see the different run modes for the same benchmark.
     * Note the units are different, scores are consistent with each other.
     *
     * You can run this test:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -jar target/benchmarks.jar JMHSample_02 -f 1
     *    (we requested a single fork; there are also other options, see -h)
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_02_BenchmarkModes.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
