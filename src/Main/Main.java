package Main;

import Generators.*;
import Simulation.SalonSimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {
        //testGenerators();

        GUI gui = new GUI();
        gui.setVisible(true);
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
