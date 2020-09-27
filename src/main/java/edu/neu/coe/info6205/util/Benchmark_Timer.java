/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import edu.neu.coe.info6205.randomwalk.WalkPlotter;
import edu.neu.coe.info6205.sort.simple.InsertionSort;
import org.jfree.data.xy.XYSeries;

import static edu.neu.coe.info6205.util.Utilities.formatWhole;

/**
 * This class implements a simple Benchmark utility for measuring the running time of algorithms.
 * It is part of the repository for the INFO6205 class, taught by Prof. Robin Hillyard
 * <p>
 * It requires Java 8 as it uses function types, in particular, UnaryOperator&lt;T&gt; (a function of T => T),
 * Consumer&lt;T&gt; (essentially a function of T => Void) and Supplier&lt;T&gt; (essentially a function of Void => T).
 * <p>
 * In general, the benchmark class handles three phases of a "run:"
 * <ol>
 *     <li>The pre-function which prepares the input to the study function (field fPre) (may be null);</li>
 *     <li>The study function itself (field fRun) -- assumed to be a mutating function since it does not return a result;</li>
 *     <li>The post-function which cleans up and/or checks the results of the study function (field fPost) (may be null).</li>
 * </ol>
 * <p>
 * Note that the clock does not run during invocations of the pre-function and the post-function (if any).
 *
 * @param <T> The generic type T is that of the input to the function f which you will pass in to the constructor.
 */
public class Benchmark_Timer<T> implements Benchmark<T> {

    /**
     * The main method, contains calls to the insertion sort to measure it's time
     * using TimeLogger, and plot a graph for doubling number of elements vs time taken.
     * @param args  Commandline arguments
     */
    public static void main(String[] args) {
        // Object for InsertionSort
        InsertionSort<Integer> sorter = new InsertionSort<Integer>();

        // An object for benchmark_timer to set fpre, frun and fpost.
        Benchmark_Timer<Integer[]> benchmark = new Benchmark_Timer<Integer[]>("Benchmark for Insertion Sort",null, (a) -> sorter.sort(a,0,a.length) ,null);

        // XYSeries to store data in to plot
        XYSeries random_series = new XYSeries("Random Array");
        XYSeries partial_series = new XYSeries("Sorted Array");
        XYSeries reverse_series = new XYSeries("Reverse Sorted Array");

        // Starting the sort with 400 elements.
        int elements = 10;

        // For loop that increasingly doubles the number of elements and sorts them.
        for(int i=0; i<1; i++) {

            // Creating a final integer with number of elements.
            final int el = elements;

            // Creating a supplier with Integer[] to supply the random array
            Supplier<Integer[]> randomsup = () -> benchmark.getArray(el, "random");
            Supplier<Integer[]> partialsup = () -> benchmark.getArray(el, "partial");
            Supplier<Integer[]> reversesup = () -> benchmark.getArray(el, "reverse");
            Supplier<Integer[]> ordersup = () -> benchmark.getArray(el, "sorted");

            // Storing the mean time calculated from running runFromSupplier on insertion sort
            double random_time = benchmark.runFromSupplier(randomsup, 5);
            logger.info("Ran for Random with " + el + " elements." + " Time taken was: " + random_time);
            double partial_time = benchmark.runFromSupplier(partialsup, 5);
            logger.info("Ran for Partially with " + el + " elements." + " Time taken was: " + partial_time);
            double reverse_time = benchmark.runFromSupplier(reversesup, 5);
            logger.info("Ran for Reversed with " + el + " elements." + " Time taken was: " + reverse_time);
            double order_time = benchmark.runFromSupplier(ordersup, 5);
            logger.info("Ran for Ordered with " + el + " elements." + " Time taken was: " + order_time);

            // Adding to the logs of time and elements to XYSeries
//            if(random_time>=0)
//                random_series.add(Math.log(el), Math.log(random_time));
//            if(partial_time>=0)
//                partial_series.add(Math.log(el), Math.log(partial_time));
//            if(reverse_time>=0)
//                reverse_series.add(Math.log(el), Math.log(reverse_time));

            // Adding to XYSeries
            random_series.add((el), (random_time));
            partial_series.add((el), (partial_time));
            reverse_series.add((el), (reverse_time));

            // Doubling the number of elements
            elements *= 2;
        }

        // WalkPlotter object to plot graph
        WalkPlotter plot = new WalkPlotter(random_series, partial_series, reverse_series," Elements vs Time","Elements","Time in Milliseconds");
        plot.setVisible(true);
    }

    /**
     * Method to create a sorted, partially sorted, random, or reverse sorted array
     * of n elements.
     * @param n     Size of the output array
     * @param type  Select the type of array to be generated. ex: Sorted, Random, Partially Sorted, Reverse
     * @return Integer array of size n
     */
    public Integer[] getArray(int n, String type) {
        // Temporary arraylist to return elements from
        ArrayList<Integer> lt = new ArrayList<Integer>();
        Random random = new Random();

        // Checking if we need partially sorted elements
        // To produce a partially sorted list, we get a fully sorted list
        // and generate a random number of indexes, 20% of the size of the list
        // in this case. And then randomly generate new values at those indexes.
        if(type == "partial"){
            Integer[] temp_sorted = getArray(n,"sorted");

            for(int i=0; i<n/5; i++){
                temp_sorted[random.nextInt(n)] = random.nextInt(1000);
            }

            return temp_sorted;
        }

        // In case of other options
        for (int i = 0; i < n; i++) {

            switch (type){
                case "random" :  lt.add(random.nextInt(1000));
                                break;
                case "sorted" : lt.add(i);
                                break;
                case "reverse": lt.add(n-i);
                                break;
            }

        }

        return lt.toArray(new Integer[lt.size()]);
    }


    /**
     * Calculate the appropriate number of warmup runs.
     *
     * @param m the number of runs.
     * @return at least 2 and at most m/10.
     */
    static int getWarmupRuns(int m) {
        return Integer.max(2, Integer.min(10, m / 10));
    }

    /**
     * Run function f m times and return the average time in milliseconds.
     *
     * @param supplier a Supplier of a T
     * @param m        the number of times the function f will be called.
     * @return the average number of milliseconds taken for each run of function f.
     */
    @Override
    public double runFromSupplier(Supplier<T> supplier, int m) {
        logger.info("Begin run: " + description + " with " + formatWhole(m) + " runs");
        // Warmup phase
        final Function<T, T> function = t -> {
            fRun.accept(t);
            return t;
        };
        new Timer().repeat(getWarmupRuns(m), supplier, function, fPre, null);

        // Timed phase
        return new Timer().repeat(m, supplier, function, fPre, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun, Consumer<T> fPost) {
        this.description = description;
        this.fPre = fPre;
        this.fRun = fRun;
        this.fPost = fPost;
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun) {
        this(description, fPre, fRun, null);
    }

    /**
     * Constructor for a Benchmark_Timer with only fRun and fPost Consumer parameters.
     *
     * @param description the description of the benchmark.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, Consumer<T> fRun, Consumer<T> fPost) {
        this(description, null, fRun, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer where only the (timed) run function is specified.
     *
     * @param description the description of the benchmark.
     * @param f           a Consumer function (i.e. a function of T => Void).
     *                    Function f is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, Consumer<T> f) {
        this(description, null, f, null);
    }

    private final String description;
    private final UnaryOperator<T> fPre;
    private final Consumer<T> fRun;
    private final Consumer<T> fPost;

    final static LazyLogger logger = new LazyLogger(Benchmark_Timer.class);

}
