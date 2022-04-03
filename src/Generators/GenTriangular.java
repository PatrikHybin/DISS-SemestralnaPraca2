package Generators;

import java.util.Random;

public class GenTriangular extends Generator {

    private final double min;
    private final double max;
    private final double mode;
    private final Random genU;

    public GenTriangular(double min, double max, double mode) {
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.genU = new Random(Seeder.getSeed());
    }

    @Override
    public double sample() {

        double Fc = (this.mode - this.min) / (this.max - this.min);
        double rnd = genU.nextDouble();
        if (0 < rnd && rnd < Fc) {
            return this.min + Math.sqrt(rnd * (this.max - this.min) * (this.mode - this.min));
        } else {
            return this.max - Math.sqrt((1 - rnd) * (this.max - this.min) * (this.max - this.mode));
        }
        //if (Fc <= rnd && rnd < 1)
    }
}
