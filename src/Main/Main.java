package Main;

import Generators.*;
import Simulation.SalonSimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        //testGenerators();

        /*GUI gui = new GUI();
        gui.setVisible(true);*/
        //2 6 6
        /*int replications = 100000;
        SalonSimulation simulation = new SalonSimulation(replications, "2", "6", "6");

        try {
            simulation.simulate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println((double) simulation.getIn()/replications);
        System.out.println((double) simulation.getOut()/replications);
        System.out.println();
        System.out.println(simulation.getAverageHairstylingTimeForReplications().getCountCooling() / replications);
        System.out.println(simulation.getAverageHairstylingTimeForReplications().getAverageCooling() / replications);
        System.out.println("\nAVERAGE TIME OF HAIRSTYLING\n");
        System.out.println(simulation.getAverageHairstylingTimeForReplications().getTimeCooling() / simulation.getAverageHairstylingTimeForReplications().getCountCooling());
        System.out.println("\nAVERAGE SIZE OF RECEPTION QUEUE\n");
        System.out.println(simulation.getAverageSizeOfQueueForReplications().getCountCooling() / simulation.getAverageSizeOfQueueForReplications().getTimeCooling());
        System.out.println(simulation.getAverageSizeOfQueueForReplications().getAverageCooling() / replications);
        System.out.println(simulation.getAverageSizeOfQueueForReplications().getAverage() / replications);
        System.out.println("\nAVERAGE TIME IN SYSTEM\n");
        System.out.println(simulation.getAverageTimeInSystemForReplications().getTimeCooling() / simulation.getAverageTimeInSystemForReplications().getCountCooling());
        System.out.println(LocalTime.MIN.plusSeconds((int)(simulation.getAverageTimeInSystemForReplications().getTimeCooling() / simulation.getAverageTimeInSystemForReplications().getCountCooling())));
        System.out.println(simulation.getAverageTimeInSystemForReplications().getAverageCooling() / replications);
        System.out.println(LocalTime.MIN.plusSeconds((int)(simulation.getAverageTimeInSystemForReplications().getAverageCooling() / replications)));
        System.out.println("\nAVERAGE TIME IN QUEUE\n");
        System.out.println(simulation.getAverageTimeSpentInReceptionQueueForReplications().getTimeCooling() / simulation.getAverageTimeSpentInReceptionQueueForReplications().getCountCooling());
        System.out.println(LocalTime.MIN.plusSeconds((int)(simulation.getAverageTimeSpentInReceptionQueueForReplications().getTimeCooling() / simulation.getAverageTimeSpentInReceptionQueueForReplications().getCountCooling())));
        System.out.println(simulation.getAverageTimeSpentInReceptionQueueForReplications().getAverageCooling() / replications);
        System.out.println(LocalTime.MIN.plusSeconds((int)(simulation.getAverageTimeSpentInReceptionQueueForReplications().getAverageCooling() / replications)));
        System.out.println("\nAVERAGE TIME OF COOLING\n");
        System.out.println(simulation.getAverageCoolingTimeForReplications().getTime() / simulation.getAverageCoolingTimeForReplications().getCount());
        System.out.println(LocalTime.MIN.plusSeconds((int)(simulation.getAverageCoolingTimeForReplications().getTime() / simulation.getAverageCoolingTimeForReplications().getCount())));
        System.out.println(simulation.getAverageCoolingTimeForReplications().getAverage() / replications);
        System.out.println(LocalTime.MIN.plusSeconds((int)(simulation.getAverageCoolingTimeForReplications().getAverage() / replications)));

        System.out.println("\nCI\n");
        System.out.println(simulation.getAverageSizeOfQueueForReplications().calculateConfidenceInterval().get(0));
        System.out.println(simulation.getAverageSizeOfQueueForReplications().calculateConfidenceInterval().get(1));

        System.out.println("\nCI - AVERAGE TIME IN SYSTEM\n");
        System.out.println("< " + LocalTime.MIN.plusSeconds((simulation.getAverageTimeInSystemForReplications().calculateConfidenceIntervalCooling().get(0).longValue())) + " , " + LocalTime.MIN.plusSeconds((simulation.getAverageTimeInSystemForReplications().calculateConfidenceIntervalCooling().get(1).longValue())) + " >");
        System.out.println("\nCI - AVERAGE TIME IN QUEUE\n");
        System.out.println("< " + LocalTime.MIN.plusSeconds((simulation.getAverageTimeSpentInReceptionQueueForReplications().calculateConfidenceIntervalCooling().get(0).longValue())) + " , " + LocalTime.MIN.plusSeconds((simulation.getAverageTimeSpentInReceptionQueueForReplications().calculateConfidenceIntervalCooling().get(1).longValue())) + " >");
        */

    }

    private static void testGenerators() {
        testExponential();
        testEmpirical();
        testTriangular();
    }

    private static void testTriangular() {
        try {
            String base = "rozdelenia/";
            FileWriter myWriter = new FileWriter(base + "testTriangular.dst");
            GenTriangular triangular = new GenTriangular(360, 900, 540);
            for (int i = 0; i < 1000000; i++) {
                double generated = triangular.sample();
                myWriter.write(generated + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testEmpirical() {
        try {
            String base = "rozdelenia/";
            FileWriter myWriter = new FileWriter(base + "testEmpirical.dst");
            GenEmpirical empirical = new GenEmpirical();
            empirical.addParams(new EmpParams(50, 60, 0.2));
            empirical.addParams(new EmpParams(61, 100, 0.3));
            empirical.addParams(new EmpParams(101, 150, 0.5));
            for (int i = 0; i < 100000; i++) {
                double generated = empirical.sample();
                myWriter.write(generated + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testExponential() {
        try {
            String base = "rozdelenia/";
            FileWriter myWriter = new FileWriter(base + "testExponential.dst");
            GenExponential exponential = new GenExponential(450);
            for (int i = 0; i < 100000; i++) {
                double generated = exponential.sample();
                myWriter.write(generated + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
