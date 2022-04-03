package Events;

import Main.Customer;
import Main.CustomerOrder;
import Main.Employee;
import Simulation.SalonSimulation;

public class EndDeepCleaning extends Event {

    private SalonSimulation simulation;
    private Employee cosmetician;
    private Customer customer;

    public EndDeepCleaning(double time, SalonSimulation simulation, Customer customer, Employee cosmetician) {
        super(time);
        this.simulation = simulation;
        this.customer = customer;
        this.cosmetician = cosmetician;
    }

    @Override
    public void execute() {
        this.customer.setOrder(CustomerOrder.MAKE_UP);
        this.cosmetician.setOccupied();
        this.cosmetician.addTimeWorked(this.time - this.cosmetician.getStart());
        this.simulation.getUnoccupiedCosmeticians().add(this.cosmetician);

        //System.out.println("EndDeepCleaning " + customer.getNum() + " " +  this.time);

        //TODO(1) maybe refactor
        this.simulation.getMakeupQueue().add(this.customer);
        if (this.simulation.getMakeupQueue().size() > 0) {
            if (this.simulation.getUnoccupiedCosmeticians().size() > 0) {
                this.customer = this.simulation.getMakeupQueue().poll();
                if (this.customer.getOrder() == CustomerOrder.MAKE_UP) {
                    this.simulation.addEventToCalendar(new StartMakeUp(this.time, this.simulation ,this.customer, this.simulation.getUnoccupiedCosmeticians().poll()));
                } else {
                    this.simulation.addEventToCalendar(new StartDeepCleaning(this.time, this.simulation ,this.customer, this.simulation.getUnoccupiedCosmeticians().poll()));
                }
            }
        }
    }
}
