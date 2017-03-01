package main;


import main.data.DataSeries;
import main.data.Scaling;
import main.data.ScalingImpl;
import main.graph.GraphViewer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {

        Function f1 = sin(5);
        Function f2 = harmonic(2, 0.3);

        Function f= multi(sin(0.5), harmonic(10, 0.4));


        show(f, 2);
        play(f, 5,  4);


       //File file = new File(System.getProperty("user.dir"), "cat_purr.wav");
       // StdAudio.playFile(file, 1);
       // StdAudio.playFile(file, 3);
    }

    static Function multi(Function f1, Function f2) {
        return new Function() {
            @Override
            public double value(double x) {
                return  f1.value(x) * f2.value(x);
            }
        };
    }

    static Function sin(double freq) {
        return  new Function() {
            @Override
            public double value(double x) {
                return Math.sin(2 * Math.PI * x * freq);
            }
        };
    }

    static Function harmonic(double freq, double pct) {
        return new Function() {
            @Override
            public double value(double x) {
                x = x * 2 * freq;
                int x_int = (int) x;
                double x_double = x - x_int;

                double t = x_int % 2 - 1 + x_double ;
                return base(t);
            }

            private double base(double x) {
                double y = 0;
                if (x > 0) {
                    y = (Math.min(Math.min((x / pct), 1), ((1 - x) / pct)));
                }
                if (x < 0) {
                    y = -(Math.min(Math.min(((-x) / pct), 1), ((1 + x) / pct)));
                }

                return y;
            }
        };

    }


    static void play(Function f, double volume, double duration) {
        int sampleRate = StdAudio.SAMPLE_RATE;
        int n = (int) (sampleRate * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++) {
            a[i] = volume * f.value((double)i/sampleRate);

        }
        StdAudio.play(a);
    }

    static void show(Function f,  double duration) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;
            int numberOfPoints = 1000;
            int sampleRate = (int)(numberOfPoints/duration);
            @Override
            public int size() {
                return  (int) (duration * sampleRate);
            }

            @Override
            public int get(int index) {
                double x = (double)(index) / sampleRate;

                return (int) (f.value(x) * intScaling);
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setDataGain(1.0/intScaling);
                scaling.setSamplingInterval(1.0/sampleRate);
                return scaling;
            }
        };
        show(dataSeries);
    }

    static void show(double[] data, int sampleRate) {
        DataSeries dataSeries = new DataSeries() {
            int intScaling = 300;
            @Override
            public int size() {
                return data.length;
            }

            @Override
            public int get(int index) {
                return (int) (data[index]* intScaling);
            }

            @Override
            public Scaling getScaling() {
                ScalingImpl scaling = new ScalingImpl();
                scaling.setDataGain(1.0/intScaling);
                scaling.setSamplingInterval(1.0/sampleRate);
                return scaling;
            }
        };
        show(dataSeries);
    }

    static void show(DataSeries dataSeries) {
        JFrame frame = new JFrame("Graphic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphViewer graphViewer = new GraphViewer(false, false);
        graphViewer.addGraphPanel(1, true);
        frame.setPreferredSize(new Dimension(1100, 600));
        frame.add(graphViewer, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        graphViewer.addGraph(dataSeries);
    }
}


