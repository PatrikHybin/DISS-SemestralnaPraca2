package Statistics;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class AverageStatistic {

    private double countCooling;
    private double timeCooling;
    private double averageCooling;

    private double count;
    private double time;
    private double average;

    private double averageCount;
    private double averageCountCooling;

    private double averagePowerOfTwo;

    private double averagePowerOfTwoCooling;

    protected boolean asTime;
    protected String statisticName = "NaN";

    public AverageStatistic(String statisticName, boolean asTime) {
        this.asTime = asTime;
        this.statisticName = statisticName;
    }

    public AverageStatistic() {

    }

    public double getCountCooling() {
        return countCooling;
    }

    public double getTimeCooling() {
        return timeCooling;
    }

    public double getAverageCooling() {
        return averageCooling;
    }

    public void addCountCooling(double count) {
        this.countCooling += count;
    }

    public void addTimeWithIncCooling(double time) {
        this.timeCooling += time;
        this.countCooling++;
    }

    public void addTimeWithoutIncCooling(double time) {
        this.timeCooling += time;
    }

    public void addAverageCooling(double average) {
        this.averageCooling += average;
        averageCountCooling++;
        averagePowerOfTwoCooling += Math.pow(average, 2);
    }

    public double getCount() {
        return count;
    }

    public double getTime() {
        return time;
    }

    public double getAverage() {
        return average;
    }

    public void addCount(double count) {
        this.count += count;
    }

    public void addTimeWithInc(double time) {
        this.time += time;
        this.count++;
    }

    public void addTimeWithoutInc(double time) {
        this.time += time;
    }

    public void addAverage(double average) {
        this.average += average;
        averageCount++;
        averagePowerOfTwo += Math.pow(average, 2);
    }

    public String getStatisticName() {
        return statisticName;
    }

    public boolean isTime() {
        return asTime;
    }

    public double getAverageCount() {
        return averageCount;
    }

    public double getAverageCountCooling() {
        return averageCountCooling;
    }

    public List<Double> calculateConfidenceInterval() {

        double firstPart = (1.0 / (this.averageCount - 1)) * this.averagePowerOfTwo;
        double secondPart = (1.0 / (this.averageCount - 1)) * (this.average);
        double standardDeviation = Math.sqrt(firstPart - Math.pow(secondPart, 2));

        double distribution = (standardDeviation * 1.645) / Math.sqrt(this.averageCount);
        double leftSide = (this.average / this.averageCount) - distribution;
        double rightSide = (this.average / this.averageCount) + distribution;

        return Arrays.asList(leftSide, rightSide);
    }

    public List<Double> calculateConfidenceIntervalCooling() {

        double firstPart = (1.0 / (this.averageCountCooling - 1)) * this.averagePowerOfTwoCooling;
        double secondPart = (1.0 / (this.averageCountCooling - 1)) * (this.averageCooling);
        double standardDeviation = Math.sqrt(firstPart - Math.pow(secondPart, 2));

        double distribution = (standardDeviation * 1.645) / Math.sqrt(this.averageCountCooling);
        double leftSide = (this.averageCooling / this.averageCountCooling) - distribution;
        double rightSide = (this.averageCooling / this.averageCountCooling) + distribution;

        return Arrays.asList(leftSide, rightSide);
    }
}
