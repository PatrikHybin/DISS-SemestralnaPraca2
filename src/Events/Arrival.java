package Events;

import Main.Customer;
import Main.CustomerStatus;
import Simulation.SalonSimulation;

public class Arrival extends Event {

    private SalonSimulation simulation;

    public Arrival(double timeToArrive, SalonSimulation simulation) {
        super(timeToArrive);
        this.simulation = simulation;

    }

    @Override
    public void execute() {
        double timeOfArrival = this.time + this.simulation.getArrivalTime();
        if (this.simulation.getSimulationTime() < 28800.0 && timeOfArrival < 28800.0) {
            this.simulation.addEventToCalendar(new Arrival(timeOfArrival, this.simulation));
        } else {
            this.simulation.addCountAverageSizeOfQueueForReplication(this.time, this.simulation.getReceptionQueue().size());
            for (Customer customer : this.simulation.getReceptionQueue()) {
                customer.setTimeSpentInReceptionQueueAndStartOfRegistering(this.time);
                this.simulation.addTimeWithIncAverageTimeSpentInReceptionQueueForReplication(customer.getTimeSpentInReceptionQueue());
            }
            this.simulation.getReceptionQueue().clear();
        }

        Customer customer = new Customer(this.time);

        this.simulation.addCountAverageSizeOfQueueForReplication(this.time, this.simulation.getReceptionQueue().size());

        this.simulation.getReceptionQueue().add(customer);
        customer.setStatus(CustomerStatus.IN_RECEPTION_QUEUE);

        if (this.simulation.getPayQueue().size() == 0) {
            if (this.simulation.getHairstylingQueue().size() + this.simulation.getMakeupQueue().size() <= 10) {
                if (this.simulation.getReceptionQueue().size() > 0) {
                    if (this.simulation.getUnoccupiedReceptionists().size() > 0) {

                        this.simulation.addCountAverageSizeOfQueueForReplication(this.time, this.simulation.getReceptionQueue().size());
                        Customer customerToRegister = this.simulation.getReceptionQueue().poll();
                        this.simulation.addEventToCalendar(new StartRegisterCustomer(this.time, this.simulation, customerToRegister, this.simulation.getUnoccupiedReceptionists().poll()));
                    }
                }
            }
        } else {
            if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                Customer payingCustomer = this.simulation.getPayQueue().poll();
                this.simulation.addEventToCalendar(new StartProcessPayment(this.time, this.simulation, payingCustomer, this.simulation.getUnoccupiedReceptionists().poll()));
            }
        }
    }
}
