package edu.neu.coe.info6205.union_find;

import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Random;
import java.util.function.Supplier;
import edu.neu.coe.info6205.plotter;
import org.jfree.data.xy.XYSeries;

public class UF_Alternative_Benchmarking {
    public static void main(String[] args) {
        // Define how many times the number of n should be doubled
        // running for values > 10 takes a while.
        int repeat = 8;
        // Starting position of n
        int n = 10000;
        // Temp store for n
        int n2 = n;

        // Bunch of benchmark timer objects to benchmark the four different cases
        Benchmark_Timer<WeightedUF> benchmarkWUFSize = new Benchmark_Timer<WeightedUF>("Benchmark for Weighted UF based on size",null, (a) -> count(a) ,null);
        Benchmark_Timer<UF_HWQUPC> benchmarkWUFHeight = new Benchmark_Timer<UF_HWQUPC>("Benchmark for Weighted UF based on height",null, (a) -> count(a) ,null);
        Benchmark_Timer<WeightedUF> benchmarkWUFPathCompression = new Benchmark_Timer<WeightedUF>("Benchmark for Weighted UF Path compression ",null, (a) -> count(a) ,null);
        Benchmark_Timer<WeightedUF> benchmarkWUFPathCompressionIntermediate = new Benchmark_Timer<WeightedUF>("Benchmark for Weighted UF Path compression Grandparent fix",null, (a) -> count(a) ,null);


        // XYSeries to plot the results
        XYSeries series1 = new XYSeries("UF Size");
        XYSeries series2 = new XYSeries("UF Height");
        XYSeries series3 = new XYSeries("UF with path compression");
        XYSeries series4 = new XYSeries("UF with path compression and grandparent fix");

        // For loop that runs repeat times and calculates the time(in ms) each time with the value of n doubling
        // each time.
        for(int i = 0; i<repeat;i++){
            // Final int to be used in suppliers
            final int el = n;

            // Supplier
            Supplier<WeightedUF> WUFsize = () -> new WeightedUF(el, false, false);
            // Storing benchmark time in a variable
            double random_time = benchmarkWUFSize.runFromSupplier(WUFsize, 20);
            System.out.println("Time taken: " + random_time + "ms");
            // Adding to the series
            series1.add(n,random_time);

            Supplier<UF_HWQUPC> WUFheight = () -> new UF_HWQUPC(el, false);
            random_time = benchmarkWUFHeight.runFromSupplier(WUFheight, 20);
            System.out.println("Time taken: " + random_time + "ms");
            series2.add(n, random_time);

            Supplier<WeightedUF> WUFPC2pass = () -> new WeightedUF(el, true, false);
            random_time = benchmarkWUFPathCompression.runFromSupplier(WUFPC2pass, 20);
            System.out.println("Time taken: " + random_time + "ms");
            series3.add(n, random_time);

            Supplier<WeightedUF> WUFPC1pass = () -> new WeightedUF(el, true, true);
            random_time = benchmarkWUFPathCompressionIntermediate.runFromSupplier(WUFPC1pass, 20);
            System.out.println("Time taken: " + random_time + "ms");
            series4.add(n, random_time);

            n *= 2;
        }

        // Reset the value of n
        n = n2;

        // Similar to the above forloop but instead of benchmarking we calculate the average depth.
        for(int i=0; i<repeat; i++){
            WeightedUF wufsize = new WeightedUF(n, false, false);
            UF_HWQUPC wufheight = new UF_HWQUPC(n, false);
            WeightedUF wufpc = new WeightedUF(n, true, false);
            WeightedUF wufpcgrandparent = new WeightedUF(n, true, true);

            count(wufsize);
            count(wufheight);
            count(wufpc);
            count(wufpcgrandparent);

            System.out.println("Depth for WUF with size and n = " + n + " is = " +wufsize.finalDepth());
            System.out.println("Depth for WUF with height and n = " + n + " is = " +wufheight.finalDepth());
            System.out.println("Depth for WUF and CP and n = " + n + " is = " +wufpc.finalDepth());
            System.out.println("Depth for WUF and CP with Grandparent Fix and n = " + n + " is = " +wufpcgrandparent.finalDepth());
            n = n*2;
        }

        // Code to plot the findings.
        plotter plot = new plotter("Benchmark Time vs Size of Quick Find problem","Time Taken(ms)","Elements in Quick Find problem");
        plot.addSeries(series1);
        plot.addSeries(series2);
        plot.addSeries(series3);
        plot.addSeries(series4);
        plot.initUI();
        plot.setVisible(true);
    }

    /**
     * Count method used as the fRun in becnhmark for UF_HWQUPC objects
     * @param quickfind UF_HWQUPC object
     * @return number of pairs generated
     */
    public static int count(UF_HWQUPC quickfind) {
        int n = quickfind.components();
        Random r = new Random();

        // Counter for number of pairs
        int pairs = 0;

        // Looping until number of components is 1
        while(quickfind.components() != 1){
            int p = r.nextInt(n);
            int q = r.nextInt(n);
            pairs++;
            if(!quickfind.connected(p,q)) {
                quickfind.union(p, q);
            }
        }

        return pairs;
    }

    /**
     * Count method used as the fRun in benchmark for WeightedUF objects
     * @param quickfind WeightedUF object
     * @return number of pairs generated
     */
    public static int count(WeightedUF quickfind) {
        int n = quickfind.count();
        Random r = new Random();

        // Counter for number of pairs
        int pairs = 0;

        // Looping until number of components is 1
        while(quickfind.count() != 1){
            int p = r.nextInt(n);
            int q = r.nextInt(n);
            pairs++;
            if(!quickfind.connected(p,q)) {
                quickfind.union(p, q);
            }
        }

        return pairs;
    }

}
