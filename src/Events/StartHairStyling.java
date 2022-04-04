package Events;

import Main.Customer;
import Main.CustomerStatus;
import Main.Employee;
import Simulation.SalonSimulation;

public class StartHairStyling extends Event {

    private SalonSimulation simulation;
    private Employee hairstylist;
    private Customer customer;

    public StartHairStyling(double time, SalonSimulation simulation,  Employee hairstylist) {
        super(time);
        this.simulation = simulation;
        this.hairstylist = hairstylist;
        //this.customer = customer;
    }

    public StartHairStyling(double time, SalonSimulation simulation, Customer customer, Employee hairstylist) {
        super(time);
        this.simulation = simulation;
        this.hairstylist = hairstylist;
        this.customer = customer;
    }

    @Override
    public void execute() {
        this.hairstylist.setOccupied();
        this.hairstylist.setStart(this.time);
        double hairStyle = this.simulation.getRandomValue();
        double hairStyleTime;

        if (hairStyle < 0.4) {
            hairStyleTime = this.simulation.getSimpleHairstyleTime();
        } else if (hairStyle < 0.8) {
            hairStyleTime = this.simulation.getComplexHairstyleTime();
        } else {
            hairStyleTime = this.simulation.getWeddingHairstyleTime();
        }
        //Customer customer = this.simulation.getHairstylingQueue().poll();
        customer.setStatus(CustomerStatus.HAIRSTYLING);
        //this.simulation.getAverageHairstylingTimeForReplication().addTimeWithIncCooling(hairStyleTime);
        this.simulation.addTimeWithIncAverageHairstylingTimeForReplication(hairStyleTime);
        //System.out.println("StartHairStyling " + customer.getNum() + " " +  this.time);

        double hairCutTime = this.time + hairStyleTime;

        this.simulation.addEventToCalendar(new EndHairStyling(hairCutTime, this.simulation, customer, this.hairstylist));

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

                            this.simulation.addCountAverageSizeOfQueueForReplication(this.time, this.simulation.getReceptionQueue().size());
                            Customer customerToRegister = this.simulation.getReceptionQueue().poll();
                            this.simulation.addEventToCalendar(new StartRegisterCustomer(this.time, this.simulation, customerToRegister, this.simulation.getUnoccupiedReceptionists().poll()));
                        }
                    }
                }
            }
        }*/
    }
}
