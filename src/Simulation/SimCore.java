package Simulation;

import Events.Event;
import Main.ISimDelegate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class SimCore {

    protected boolean stopSimulation;
    protected boolean pauseSimulation;
    protected int replications;
    protected int currentReplication;

    protected double simulationTime;

    private ArrayList<ISimDelegate> delegates;
    protected PriorityQueue<Event> calendar;

    public SimCore() {
        this.delegates = new ArrayList<>();
        this.calendar = new PriorityQueue<>();
    }

    abstract protected void beforeReplications();
    abstract protected void beforeReplication();
    abstract protected void doReplication();
    abstract protected void afterReplication();
    abstract protected void afterReplications();

    public void simulate() {
        beforeReplications();
        for (int i = 0; i < replications; i++) {
            beforeReplication();
            doReplication();
            afterReplication();
        }
        afterReplications();
    }

    public void addEventToCalendar(Event event) {
        this.calendar.add(event);
    }

    public void stopSimulation() {
        this.stopSimulation = true;
    }

    public void pauseSimulation() {
        this.pauseSimulation = !this.pauseSimulation;
    }

    public void registerDelegate(ISimDelegate delegate) {
        this.delegates.add(delegate);
    }

    public void refreshGUI() {
        for (ISimDelegate delegate: this.delegates) {
            delegate.refresh(this);
        }
    }

    public double getSimulationTime() {
        return this.simulationTime;
    }
}
