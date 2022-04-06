package Events;

import Main.Customer;
import Main.CustomerOrder;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class EndMakeUp extends Event {

    private SalonSimulation simulation;
    private Employee cosmetician;
    private Customer customer;

    public EndMakeUp(double time, SalonSimulation simulation, Customer customer, Employee cosmetician) {
        super(time);
        this.simulation = simulation;
        this.cosmetician = cosmetician;
        this.customer = customer;
    }

    @Override
    public void execute() {
        this.cosmetician.setOccupied();
        this.cosmetician.addTimeWorked(this.time - this.cosmetician.getStart());
        this.simulation.getUnoccupiedCosmeticians().add(this.cosmetician);

        this.customer.setStatus(CustomerStatus.IN_PAY_QUEUE);
        this.simulation.getPayQueue().add(this.customer);

        if (this.simulation.getPayQueue().size() > 0) {
            if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                Customer payingCustomer = this.simulation.getPayQueue().poll();
                this.simulation.addEventToCalendar(new StartProcessPayment(this.time, this.simulation, payingCustomer, this.simulation.getUnoccupiedReceptionists().poll()));
            }
        }

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
