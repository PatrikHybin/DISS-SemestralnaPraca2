package Generators;

import java.util.Random;

public class GenUniform extends Generator {

    private final double min;
    private final double max;
    private Random gen;

    public GenUniform(double min, double max) {
        this.min = min;
        this.max = max;
        this.gen = new Random(Seeder.getSeed());
    }

    @Override
    public double sample() {
        return (this.gen.nextDouble() * (this.max - this.min)) + this.min;
    }
}
