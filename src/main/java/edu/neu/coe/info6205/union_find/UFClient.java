package edu.neu.coe.info6205.union_find;

import io.cucumber.java.bs.A;

import java.util.ArrayList;
import java.util.Random;
import edu.neu.coe.info6205.randomwalk.WalkPlotter;
import org.jfree.data.xy.XYSeries;

public class UFClient {
    public static void main(String[] args) {

        // Number of maximum initial components in the quick find problem
        int n = 10001;
        // Starting point for the number of components
        int start = 0;
        // Number of times we should repeat and average over
        int repeat = 200;

        // XYSeries objects used to graph the plots
        XYSeries rseries = new XYSeries("Objects(n)");
        XYSeries logseries = new XYSeries("0.53 * n * log(n)");

        // Running the count() program for values ranging from @start to @n, repeating @repeat times for
        // each number and averaging those values.
        for(int i = start; i < n; i = i + 200) {

            // Averaging values @repeat times
            int res = 0;
            for(int j = 0; j < repeat; j++) {
                if(i == 0){
                    res += count(1);
                } else {
                    res += count(i);
                }
            }
            double resd = res/repeat;

            // Calculating the value for my hypothesis
            double log = 0.53 * Math.log(i) * i;
            System.out.println("For n = " + i + " average number of pairs generated over 200 runs were = " + resd);
            // Calculating the slope
            double slope = (resd) / (i);
            System.out.println("Slope is " + slope);

            // Adding data to XYSeries for the plot
            rseries.add(i, resd);
            logseries.add(i, log);
        }

        // Plotter, used to graph the data.
        WalkPlotter plotter = new WalkPlotter(rseries,logseries,null,"Objects vs Pairs","Number of Objects","Number of Pairs");
        plotter.setVisible(true);
    }

    /**
     * Method to count number of pairs generated to get the number of components to 1 in a height
     * weighted quick find problem if the number of initial components in n.
     *
     * @param n Number of initial components
     * @return  Number of pairs generated
     */
    public static int count(int n) {
        // New quick find object
        UF_HWQUPC quickfind = new UF_HWQUPC(n, true);
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
}
