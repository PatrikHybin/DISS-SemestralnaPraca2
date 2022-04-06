package Events;

import Main.Customer;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class StartDeepCleaning extends Event {

    private SalonSimulation simulation;
    private Employee cosmetician;
    private Customer customer;

    public StartDeepCleaning(double time, SalonSimulation simulation, Customer customer, Employee cosmetician) {
        super(time);
        this.simulation = simulation;
        this.cosmetician = cosmetician;
        this.customer = customer;

    }

    @Override
    public void execute() {
        this.cosmetician.setOccupied();
        this.cosmetician.setStart(this.time);

        this.customer.setStatus(CustomerStatus.DEEP_CLEANING);

        this.simulation.addEventToCalendar(new EndDeepCleaning(this.time + this.simulation.getDeepSkinCleaningTime(), this.simulation, this.customer, this.cosmetician));

    }
}
