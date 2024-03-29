package Events;

import Main.Customer;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class StartRegisterCustomer extends Event {

    private SalonSimulation simulation;
    private Employee receptionist;
    private Customer customer;

    public StartRegisterCustomer(double timeToStartProcessing, SalonSimulation simulation, Customer customer, Employee receptionist) {
        super(timeToStartProcessing);
        this.simulation = simulation;
        this.receptionist = receptionist;
        this.customer = customer;
    }

    @Override
    public void execute() {

        this.receptionist.setOccupied();
        this.receptionist.setStart(this.time);

        customer.setStatus(CustomerStatus.ORDERING);
        customer.setTimeSpentInReceptionQueueAndStartOfRegistering(this.time);

        this.simulation.addTimeWithIncAverageTimeSpentInReceptionQueueForReplication(this.customer.getTimeSpentInReceptionQueue());

        this.simulation.getCustomersInSalon().add(customer);
        this.simulation.incIn();

        customer.setNum(this.simulation.getIn());

        this.simulation.addEventToCalendar(new EndRegisterCustomer(this.time + this.simulation.getTakeOrderTime(), this.simulation, customer, this.receptionist));
    }
}
