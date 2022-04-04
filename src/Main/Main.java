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

        int replications = 100000;
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

        /*Random simpleHairStyleGen = new Random(Seeder.getSeed());

        GenEmpirical complexHairStyleGen = new GenEmpirical();
        complexHairStyleGen.addParams(new EmpParams(30, 60, 0.4));
        complexHairStyleGen.addParams(new EmpParams(61, 120, 0.6));

        GenEmpirical weddingHairStyleGen = new GenEmpirical();
        weddingHairStyleGen.addParams(new EmpParams(50, 60, 0.2));
        weddingHairStyleGen.addParams(new EmpParams(61, 100, 0.3));
        weddingHairStyleGen.addParams(new EmpParams(101, 150, 0.5));

        int count = 10000000;
        Random gen = new Random(Seeder.getSeed());
        double ran = 0;
        double sum = 0;
        for (int i = 0; i < count; i++) {
            ran = gen.nextDouble();
            if (ran < 0.4) {
                sum += (simpleHairStyleGen.nextInt(20 + 1) + 10) * 60;
            } else if (ran < 0.8) {
                sum += complexHairStyleGen.sample() * 60;
            } else {
                sum += weddingHairStyleGen.sample() * 60;
            }
        }
        System.out.println(sum/count);*/
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
