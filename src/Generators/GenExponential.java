package Generators;

import java.util.Random;

public class GenExponential extends Generator {

    private final double lambda;
    private final Random genU;

    public GenExponential (double lambda) {
        this.lambda = lambda;
        this.genU = new Random(Seeder.getSeed());
    }

    @Override
    public double sample() {
        return -Math.log(1 - this.genU.nextDouble()) / (1 / this.lambda);
    }
}
