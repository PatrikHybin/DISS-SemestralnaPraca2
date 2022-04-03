package Statistics;

public class AverageStatistic {

    private double countCooling;
    private double timeCooling;
    private double averageCooling;

    private double count;
    private double time;
    private double average;

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
    }
}
