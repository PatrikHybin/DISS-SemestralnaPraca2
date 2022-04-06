package Events;

import Main.Customer;
import Main.CustomerOrder;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class EndHairStyling extends Event {

    private SalonSimulation simulation;
    private Employee hairstylist;
    private Customer customer;

    public EndHairStyling(double time, SalonSimulation simulation, Customer customer, Employee hairstylist) {
        super(time);
        this.simulation = simulation;
        this.hairstylist = hairstylist;
        this.customer = customer;

    }

    @Override
    public void execute() {
        this.hairstylist.setOccupied();
        this.hairstylist.addTimeWorked(this.time - this.hairstylist.getStart());
        this.simulation.getUnoccupiedHairStylists().add(this.hairstylist);


        if (this.customer.getOrder() == CustomerOrder.HAIR_STYLING) {
            this.customer.setStatus(CustomerStatus.IN_PAY_QUEUE);

            this.simulation.getPayQueue().add(this.customer);
            if (this.simulation.getPayQueue().size() > 0) {
                if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                    Customer payingCustomer = this.simulation.getPayQueue().poll();
                    this.simulation.addEventToCalendar(new StartProcessPayment(this.time, this.simulation, payingCustomer, this.simulation.getUnoccupiedReceptionists().poll()));
                }
            }
        } else {
            if (this.customer.getOrder() == CustomerOrder.HAIR_STYLING_AND_MAKE_UP) {
                this.customer.setOrder(CustomerOrder.MAKE_UP);
            } else {
                this.customer.setOrder(CustomerOrder.DEEP_CLEANING_AND_MAKE_UP);
            }

            this.customer.setStatus(CustomerStatus.IN_MAKE_UP_QUEUE);

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

        if (this.simulation.getHairstylingQueue().size() > 0) {
            if (this.simulation.getUnoccupiedHairStylists().size() > 0) {
                Customer customerToCutHair = this.simulation.getHairstylingQueue().remove();
                this.simulation.addEventToCalendar(new StartHairStyling(this.time, this.simulation, customerToCutHair, this.simulation.getUnoccupiedHairStylists().poll()));
            }
        }

    }
}
