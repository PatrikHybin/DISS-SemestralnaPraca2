package Events;

import Simulation.SalonSimulation;

public class SystemEvent extends Event {

    private SalonSimulation simulation;
    private double timeToSleep;
    private boolean end;

    public SystemEvent(double time, SalonSimulation simulation) {
        super(time);
        this.simulation = simulation;
    }

    @Override
    public void execute() {

        try {
            Thread.sleep(this.simulation.getTimeToSleep());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.simulation.refreshGUI();
        if (this.simulation.getEventCalendar().size() > 0) {
            this.simulation.addEventToCalendar(new SystemEvent(this.time + 200, this.simulation));
        }
    }
}
