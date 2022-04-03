package Events;

import Main.Customer;
import Main.Employee;
import Simulation.SalonSimulation;

public class EndProcessPayment extends Event {

    private SalonSimulation simulation;
    private Customer customer;
    private Employee receptionist;

    public EndProcessPayment(double time, SalonSimulation simulation, Customer customer, Employee receptionist) {
        super(time);
        this.simulation = simulation;
        this.customer = customer;
        this.receptionist = receptionist;
    }

    @Override
    public void execute() {
        this.receptionist.setOccupied();
        this.receptionist.addTimeWorked(this.time - this.receptionist.getStart());
        this.simulation.getUnoccupiedReceptionists().add(this.receptionist);

        this.customer.setTimeSpentInSalon(this.time);
        //this.simulation.getAverageTimeInSystemForReplication().addTimeWithIncCooling(this.customer.getTimeInSystem());
        this.simulation.addTimeWithIncAverageTimeInSystemForReplication(this.customer.getTimeInSystem());
        //System.out.println("EndProcessPayment " + customer.getNum() + " " + customer.getArrivalTime() + "  " + this.time);

        this.simulation.incOut();
        this.simulation.getCustomersInSalon().remove(this.customer);


        if (this.simulation.getPayQueue().size() > 0) {
            if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                Customer payingCustomer = this.simulation.getPayQueue().poll();
                this.simulation.addEventToCalendar(new StartProcessPayment(this.time, this.simulation, payingCustomer, this.simulation.getUnoccupiedReceptionists().poll()));
            }
        } else {
            if (this.simulation.getPayQueue().size() == 0) {
                if (this.simulation.getHairstylingQueue().size() + this.simulation.getMakeupQueue().size() < 11) {
                    if (this.simulation.getReceptionQueue().size() > 0) {
                        if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                            Customer customerToRegister = this.simulation.getReceptionQueue().poll();
                            this.simulation.addEventToCalendar(new StartRegisterCustomer(this.time, this.simulation, customerToRegister, this.simulation.getUnoccupiedReceptionists().poll()));
                        }
                    }

                }
            }
        }
    }
}
