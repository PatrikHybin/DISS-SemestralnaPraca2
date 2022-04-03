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

        //System.out.println("StartDeepCleaning " + customer.getNum() + " " +  this.time);

        this.simulation.addEventToCalendar(new EndDeepCleaning(this.time + this.simulation.getDeepSkinCleaningTime(), this.simulation, this.customer, this.cosmetician));

        /*if (this.simulation.getPayQueue().size() > 0) {
            if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                Customer payingCustomer = this.simulation.getPayQueue().poll();
                this.simulation.addEventToCalendar(new StartProcessPayment(this.time, this.simulation, payingCustomer, this.simulation.getUnoccupiedReceptionists().poll()));
            }
        } else {
            if (this.time < 28800.0) {
                if (this.simulation.getHairstylingQueue().size() + this.simulation.getMakeupQueue().size() <= 10) {
                    if (this.simulation.getReceptionQueue().size() > 0) {
                        if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                            this.simulation.getAverageSizeOfQueueForReplication().addCustomersInQueue(this.time, this.simulation.getReceptionQueue().size());
                            Customer customerToRegister = this.simulation.getReceptionQueue().poll();
                            this.simulation.addEventToCalendar(new StartRegisterCustomer(this.time, this.simulation, customerToRegister, this.simulation.getUnoccupiedReceptionists().poll()));
                        }
                    }
                }

            }
        }*/
    }
}
