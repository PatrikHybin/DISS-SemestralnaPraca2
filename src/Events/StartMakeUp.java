package Events;

import Main.Customer;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class StartMakeUp extends Event {

    private SalonSimulation simulation;
    private Employee cosmetician;
    private Customer customer;

    public StartMakeUp(double time, SalonSimulation simulation, Customer customer, Employee cosmetician) {
        super(time);
        this.simulation = simulation;
        this.cosmetician = cosmetician;
        this.customer = customer;
    }

    @Override
    public void execute() {
        this.cosmetician.setOccupied();
        this.cosmetician.setStart(this.time);
        double makeUpTime;
        if (this.simulation.getRandomValue() < 0.3) {
            makeUpTime = this.simulation.getSimpleMakeUp();
        } else  {
            makeUpTime = this.simulation.getComplexMakeUp();
        }

        this.customer.setStatus(CustomerStatus.MAKEUP);

        //System.out.println("StartMakeUp " + customer.getNum() + " " +  this.time);

        this.simulation.addEventToCalendar(new EndMakeUp(this.time + makeUpTime, this.simulation, this.customer, this.cosmetician));

        if (this.simulation.getPayQueue().size() > 0) {
            if (this.simulation.getUnoccupiedReceptionists().size() > 0) {
                Customer payingCustomer = this.simulation.getPayQueue().poll();
                this.simulation.addEventToCalendar(new StartProcessPayment(this.time, this.simulation, payingCustomer, this.simulation.getUnoccupiedReceptionists().poll()));
            }
        } else {
            if (this.time < 28800.0) {
                if (this.simulation.getHairstylingQueue().size() + this.simulation.getMakeupQueue().size() <= 10) {
                    if (this.simulation.getReceptionQueue().size() > 0) {
                        if (this.simulation.getUnoccupiedReceptionists().size() > 0) {

                            this.simulation.addCountAverageSizeOfQueueForReplication(this.time, this.simulation.getReceptionQueue().size());
                            Customer customerToRegister = this.simulation.getReceptionQueue().poll();
                            this.simulation.addEventToCalendar(new StartRegisterCustomer(this.time, this.simulation, customerToRegister, this.simulation.getUnoccupiedReceptionists().poll()));
                        }
                    }
                }

            }
        }
    }
}
