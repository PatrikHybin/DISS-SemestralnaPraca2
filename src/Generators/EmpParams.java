package Generators;

public class EmpParams {

    private final double min;
    private final double max;
    private final double probability;

    public EmpParams(double min, double max, double probability) {
        this.min = min;
        this.max = max;
        this.probability = probability;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getProbability() {
        return probability;
    }
}
