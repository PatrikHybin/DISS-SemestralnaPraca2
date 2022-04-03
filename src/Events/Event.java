package Events;

import Simulation.SimCore;

public abstract class Event implements Comparable<Event> {

    protected double time;

    public Event(double time) {
        this.time = time;
    }

    abstract public void execute();

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event event) {
        if (this.time < event.getTime()) {
            return -1;
        } else if (this.time == event.getTime()) {
            return  0;
        } else {
            return 1;
        }
    }
}
