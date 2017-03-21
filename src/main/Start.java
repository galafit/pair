package main;


import main.data.DataSeries;
import main.filters.*;
import main.functions.Function;
import main.functions.HarmonicPick;
import main.functions.HarmonicRect;
import main.functions.Sin;

import java.io.File;


/**
 * Created by gala on 11/02/17.
 */
public class Start {
    public static void main(String[] args) {
        //filePlayTest();
        //StdAudio.play(StdAudio.note(10, 2, 4));
        funcitonTest();

    }

    static void funcitonTest() {
        HarmonicRect harmonicRect = new HarmonicRect(1, 0);
       // HarmonicPick harmonic = new HarmonicPick(10, 1);
        Function freq = x -> {return 1 + x;};
        HarmonicPick harmonic = new HarmonicPick(freq, 1);

        Viewer viewer = new Viewer();
        viewer.addGraph(harmonic, 3 );

    }

    static void filePlayTest() {
        int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )


        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "devochka.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries eog_full = edfData.getChannelSeries(0);
            DataSeries accX = edfData.getChannelSeries(2);
            DataSeries accY = edfData.getChannelSeries(3);
            DataSeries accZ = edfData.getChannelSeries(4);

            DataSeries eog = new HiPassCollectingFilter(
                    new FrequencyDivider(eog_full, 5), eogCutOffPeriod);

            DataSeries acc = new AccelerometerMovement(
                    new FrequencyDivider(accX, 5),
                    new FrequencyDivider(accY, 5),
                    new FrequencyDivider(accZ, 5));

            Function test = new FrequencyDivider(accX, 10);



            DataSeries alfa = new FilterHiPass(new FilterBandPass_Alfa(eog), 2);
            DataSeries alfa_contur = new FilterAlfa(eog);

            Function harmonic = new HarmonicPick(10, 0.2);


            Function mix = (x) -> {
                return   alfa_contur.value(x) * harmonic.value(x);
            };
            /* Function mix1 = new Function() {
                @Override
                public double value(double x) {
                    return eog.value(x) * sin.value(x);
                }
            }; */
            viewer.addGraph(eog);
            viewer.addGraph(alfa_contur);
            viewer.addGraph(harmonic);
            viewer.addPreview(new FilterDerivativeRem(eog));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void filterTest() {
        int eogCutOffPeriod = 10; //sec. to remove steady component (cutoff_frequency = 1/cutoff_period )


        Viewer viewer = new Viewer();
        File recordsDir = new File(System.getProperty("user.dir"), "records");
        File fileToRead = new File(recordsDir, "devochka.bdf");
        try {
            EdfData edfData = new EdfData(fileToRead);
            DataSeries eog_full = edfData.getChannelSeries(0);
          /* DataSeries eog = new HiPassCollectingFilter(
                    eog_full, eogCutOffPeriod);*/

            viewer.addGraph(eog_full);
            viewer.addPreview(new FilterDerivativeRem(eog_full));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


