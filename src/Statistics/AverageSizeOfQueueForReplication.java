package Statistics;

public class AverageSizeOfQueueForReplication extends AverageStatistic {

    private double timeOfLastChangeCooling;
    private double timeOfLastChange;

    public void addCountCooling(double timeOfChange, double customersInQueue) {
        addCountCooling((timeOfChange - timeOfLastChangeCooling) * customersInQueue);;
        addTimeWithoutIncCooling((timeOfChange - timeOfLastChangeCooling));

        timeOfLastChangeCooling = timeOfChange;
    }

    public void addCount(double timeOfChange, double customersInQueue) {
        addCount((timeOfChange - timeOfLastChange) * customersInQueue);;
        addTimeWithoutInc((timeOfChange - timeOfLastChange));

        timeOfLastChange = timeOfChange;
    }
}
