/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import java.util.ArrayList;
import java.util.Random;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.lang.model.type.ArrayType;

public class RandomWalk {

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        // Moving dx steps in the x direction
        x = x + dx;
        // Moving dy steps in the y direction
        y = y + dy;
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        for(int i=0; i<m; ++i){
            randomMove();
        }
    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        boolean ns = random.nextBoolean();
        int step = random.nextBoolean() ? 1 : -1;
        move(ns ? step : 0, ns ? 0 : step);
    }

    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        return Math.sqrt(x*x + y*y);
        //return x*x + y*y;
    }

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            totalDistance = totalDistance + walk.distance();
        }
        return totalDistance / n;
    }

    public static void main(String[] args) {
        if (args.length == 0)
            throw new RuntimeException("Syntax: RandomWalk steps [experiments]");

        // m represents the number of steps the drunkard is going to take
        int m = 1200;
        // n represents the number of experiments we're going to average over
        int n = 500;

        // XYSeries to store the results from random walk which is used to plot a graph
        XYSeries sr = new XYSeries("Mean Distance");
        // XYSeries to store the values of root(m) which again is used to plot a graph
        XYSeries srootx = new XYSeries("Root of Steps");
        // XYSeries to store the values of k*root(m) which again is used to plot a graph
        XYSeries ksrootx = new XYSeries("Root of Steps * Coefficient");

        // k variable to calculate the difference in ratio between the two values
        // srootx always comes out to be greater than sr.
        // hence srootx = k*sr, where k is a coefficient.
        // we use this k to calculate the approximate value of the coefficient.
        ArrayList<Double> k = new ArrayList<Double>();

        // for loop to execute the experiment from 1 to m steps over n experiments each time.

        for(int i = 0; i < m; i++) {
            // Double value to store the calculated mean distance.
            double meanDistance = randomWalkMulti(i+1, n);

            // Double value to store the square root of steps in.
            double rootx = Math.sqrt((i+1));

            // Storing the ratio between mean distance and the sq-root of steps
            // to calculate the coefficient.
            k.add(meanDistance/rootx);

            // Adding to XYSeries for plotting
            sr.add(i+1, meanDistance);

            //Adding to XYSeries for plotting
            srootx.add(i+1, rootx);

            System.out.println(i+1 + " steps, distance: " + meanDistance + " over " + n + " experiments");
        }

        Double coef = 0.0;
        for(Double er: k){
            coef += er;
        }
        coef = coef.doubleValue() / k.size();

        for(int i = 0; i < m; i++) {

            // Double value to store the square root * coefficient of steps in.
            double krootx = Math.sqrt((i+1))*coef;

            // Adding the value to a XYSeries for plotting
            ksrootx.add(i+1, krootx);
        }

        System.out.println("Coefficient is: " + coef);
        WalkPlotter plot = new WalkPlotter(ksrootx, sr, srootx,"","","");
        plot.setVisible(true);

    }

}
