package Events;

import Main.Customer;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class StartProcessPayment extends Event {

    private SalonSimulation simulation;
    private Employee receptionist;
    private Customer customer;

    public StartProcessPayment(double time, SalonSimulation simulation, Customer customer, Employee receptionist) {
        super(time);
        this.simulation = simulation;
        this.receptionist = receptionist;
        this.customer = customer;
    }

    @Override
    public void execute() {
        this.receptionist.setOccupied();
        this.receptionist.setStart(this.time);

        customer.setStatus(CustomerStatus.PAYING);

        //System.out.println("StartProcessPayment " + customer.getNum() + " " +  this.time + " || "  + customer.getOrder());

        this.simulation.addEventToCalendar(new EndProcessPayment(this.time + this.simulation.getPayTime(), this.simulation, customer , this.receptionist));
    }
}
