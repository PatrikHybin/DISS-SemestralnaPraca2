package Events;

import Main.*;
import Simulation.SalonSimulation;

public class EndRegisterCustomer extends Event {

    private SalonSimulation simulation;
    private Employee receptionist;
    private Customer customer;

    public EndRegisterCustomer(double timeToEndProcessing, SalonSimulation simulation, Customer customer, Employee receptionist) {
        super(timeToEndProcessing);
        this.simulation = simulation;
        this.receptionist = receptionist;
        this.customer = customer;
    }

    @Override
    public void execute() {
        //System.out.println("EndRegisterCustomer " + customer.getNum() + " " +  this.time);
        this.receptionist.setOccupied();
        this.receptionist.addTimeWorked(this.time - this.receptionist.getStart());
        this.simulation.getUnoccupiedReceptionists().add(this.receptionist);


        this.customer.setTimeEndOfRegistering(this.time);
        double customerOrder = this.simulation.getRandomValue();
        double deepCleaning;
        if (customerOrder < 0.2) {
            this.customer.setOrder(CustomerOrder.HAIR_STYLING);
            addCustomerToHairStylingQueue();

        } else if (customerOrder < 0.35) {
            deepCleaning = this.simulation.getRandomValue();
            if (deepCleaning < 0.35) {
                this.customer.setOrder(CustomerOrder.DEEP_CLEANING_AND_MAKE_UP);
            } else {
                this.customer.setOrder(CustomerOrder.MAKE_UP);
            }
            //TODO(1) maybe refactor
            this.customer.setStatus(CustomerStatus.IN_MAKE_UP_QUEUE);
            this.simulation.getMakeupQueue().add(this.customer);
            if (this.simulation.getMakeupQueue().size() > 0) {
                if (this.simulation.getUnoccupiedCosmeticians().size() > 0) {
                    this.customer = this.simulation.getMakeupQueue().poll();
                    if (this.customer.getOrder() == CustomerOrder.MAKE_UP) {
                        this.simulation.addEventToCalendar(new StartMakeUp(this.time, this.simulation, this.customer, this.simulation.getUnoccupiedCosmeticians().poll()));
                    } else {
                        this.simulation.addEventToCalendar(new StartDeepCleaning(this.time, this.simulation, this.customer, this.simulation.getUnoccupiedCosmeticians().poll()));
                    }
                }
            }
        } else {
            deepCleaning = this.simulation.getRandomValue();
            if (deepCleaning < 0.35) {
                this.customer.setOrder(CustomerOrder.HAIR_STYLING_AND_DEEP_CLEANING_AND_MAKE_UP);
            } else {
                this.customer.setOrder(CustomerOrder.HAIR_STYLING_AND_MAKE_UP);
            }
            addCustomerToHairStylingQueue();
        }

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

    private void addCustomerToHairStylingQueue() {
        this.simulation.getHairstylingQueue().add(this.customer);
        this.customer.setStatus(CustomerStatus.IN_HAIRSTYLING_QUEUE);
        if (this.simulation.getHairstylingQueue().size() > 0) {
            if (this.simulation.getUnoccupiedHairStylists().size() > 0) {
                Customer customerToCutHair = this.simulation.getHairstylingQueue().remove();
                this.simulation.addEventToCalendar(new StartHairStyling(this.time, this.simulation, customerToCutHair, this.simulation.getUnoccupiedHairStylists().poll()));
            }
        }
    }
}
