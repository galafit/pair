package main.chart;

import main.chart.axis.Axis;
import main.chart.axis.LinearAxis;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {

        setTitle("Test chart");


        Chart chart = new Chart();

        Axis yAxis2 = new LinearAxis();
        Axis xAxis2 = new LinearAxis();


        yAxis2.getGridSettings().setGridLineWidth(0);
        yAxis2.getGridSettings().setMinorGridLineWidth(0);
        yAxis2.setOpposite(true);

       // yAxis2.setVisible(false);

        xAxis2.getGridSettings().setGridLineWidth(0);
        xAxis2.getGridSettings().setMinorGridLineWidth(0);
        xAxis2.setOpposite(true);


        chart.addYAxis(yAxis2);
        chart.addXAxis(xAxis2);

        XYList xyList = new XYList();
        Random rand = new Random();
        // xyList.addItem(-2.5,-8);
        for (int i = 0; i <15 ; i++) {
            xyList.addItem(i,rand.nextInt(200) + 1000);
        }


        XYList xyList2 = new XYList();
        for (int i = 0; i <6 ; i++) {
            //xyList2.addItem(i,rand.nextInt(100));
            xyList2.addItem(4057.0789,i);
        }

        Graph graph1 = new LineGraph(xyList);
        Graph graph2 = new LineGraph(xyList2);

        chart.addGraph(graph1);
        chart.addGraph(graph2,1,1);


        chart.setPreferredSize(new Dimension(500, 500));
        chart.setBackground(Color.BLACK);
        chart.setPreferredSize(new Dimension(500, 500));
        add(chart,BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
        new MainFrame();
    }

    }
