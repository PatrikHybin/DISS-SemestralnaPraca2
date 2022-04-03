package Main;

public class Customer {

    private double arrivalTime;
    private double startOfRegistering;
    private double timeSpentInReceptionQueue;
    private double timeSpentRegisteringOrder;
    private double timeSpentInSalon;
    private CustomerOrder order;
    private CustomerStatus status;
    private int num;
    private double startInSalon;

    public Customer(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setTimeSpentInReceptionQueueAndStartOfRegistering(double startOfRegistering) {
        this.timeSpentInReceptionQueue = startOfRegistering - this.arrivalTime;
        this.startOfRegistering = startOfRegistering;
    }

    public void setOrder(CustomerOrder hairStyling) {
        this.order = hairStyling;
    }

    public void setTimeEndOfRegistering(double timeSpentRegisteringOrder) {
        this.timeSpentRegisteringOrder = timeSpentRegisteringOrder - startOfRegistering;
    }

    public CustomerOrder getOrder() {
        return this.order;
    }

    public void setTimeSpentInSalon(double timeOfPayment) {
        this.timeSpentInSalon = timeOfPayment - this.arrivalTime;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public double getTimeSpentInReceptionQueue() {
        return this.timeSpentInReceptionQueue;
    }

    public double getTimeInSystem() {
        return this.timeSpentInSalon;
    }
}
