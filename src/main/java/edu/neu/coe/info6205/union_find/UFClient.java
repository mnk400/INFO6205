package edu.neu.coe.info6205.union_find;

import io.cucumber.java.bs.A;

import java.util.ArrayList;
import java.util.Random;
import edu.neu.coe.info6205.randomwalk.WalkPlotter;
import org.jfree.data.xy.XYSeries;

public class UFClient {
    public static void main(String[] args) {
        int n = 100;

        XYSeries rseries = new XYSeries("Number");
        for(int i = 0; i < 11; i++) {
            n *= 2;
            int res = 0;
            for(int j = 0; j < 50; j++) {
                res += count(n);
                System.out.println("For n = " + n + " value number = " + j);
            }
            double resd = res/20;
            System.out.println("Pairs generated were = " + resd);
            rseries.add(n, resd);
        }

        WalkPlotter plotter = new WalkPlotter(rseries,null,null,"","","");
        plotter.setVisible(true);

    }

    public static int count(int n) {
        UF_HWQUPC quickfind = new UF_HWQUPC(n, true);
        Random r = new Random();
        int ucount = 0;
        int pairs = 0;
        while(quickfind.components() != 1){
            int p = r.nextInt(n);
            int q = r.nextInt(n);
            pairs++;
            if(!quickfind.connected(p,q) || p == q) {
                quickfind.connect(p, q);
                ucount++;
            }
        }

        return pairs;
    }
}
