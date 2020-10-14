/*
 * Java file used to create the graphs in assignment 0
 * Only contains code used to generate the charts
 * contains some code directly from:
 * http://zetcode.com/java/jfreechart/
 */

package edu.neu.coe.info6205;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import edu.neu.coe.info6205.Iteration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class plotter extends JFrame {

    private String graph_title = "";
    private String x_label = "";
    private String y_label = "";
    private ArrayList<XYSeries> seriesList = new ArrayList<XYSeries>();

    /**
     * Constructor which intakes XYSeries to plot
     */
    public plotter(String title, String x, String y) {
        this.graph_title = title;
        this.x_label = x;
        this.y_label = y;
        ;

    }

    public void addSeries(XYSeries series){
        seriesList.add(series);
    }

    /**
     * Method to initiate the UI for the graphs
     */
    public void initUI() {

        XYSeriesCollection dataset = new XYSeriesCollection();
        Iterator<XYSeries> it = seriesList.iterator();

        while(it.hasNext()){
            dataset.addSeries(it.next());
        }


        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle(graph_title);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                graph_title,
                x_label,
                y_label,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        Color colorg = new Color(140, 207, 64, 150);
//        Color colorr = new Color(207, 64, 78, 180);
//        Color colorb = new Color(79, 129, 189, 160);
//
//        renderer.setSeriesPaint(0, colorb);
//        renderer.setSeriesPaint(1, colorr);
        renderer.setSeriesPaint(3, Color.DARK_GRAY);
//
//        renderer.setSeriesStroke(0, new BasicStroke(2f));
//        renderer.setSeriesShapesVisible(0 ,true);
//        renderer.setSeriesStroke(1, new BasicStroke(2f));
//        renderer.setSeriesShapesVisible(1, true);
//        renderer.setSeriesStroke(2, new BasicStroke(2f));
//        renderer.setSeriesShapesVisible(2, true);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

//        NumberAxis domain = (NumberAxis) plot.getRangeAxis();
        //domain.setRange(0.00, 35.00);
//        domain.setTickUnit(new NumberTickUnit(5));
//        domain.setVerticalTickLabels(true);


        chart.setTitle(new TextTitle(graph_title,
                        new Font("Helvetica", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }



}

