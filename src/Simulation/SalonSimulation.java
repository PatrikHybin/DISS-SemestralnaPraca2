package Simulation;

import Events.Arrival;
import Events.Event;
import Events.SystemEvent;
import Generators.*;
import Main.*;
import Statistics.*;

import java.util.*;

public class SalonSimulation extends SimCore {

    private ArrayList<Employee> receptionists;
    private ArrayList<Employee> hairstylists;
    private ArrayList<Employee> cosmeticians;
    private ArrayList<Customer> customersInSalon;

    private PriorityQueue<Employee> unoccupiedReceptionists;
    private PriorityQueue<Employee> unoccupiedHairstylists;
    private PriorityQueue<Employee> unoccupiedCosmeticians;

    private Queue<Customer> receptionQueue;
    private Queue<Customer> payQueue;
    private Queue<Customer> hairstylingQueue;
    private Queue<Customer> makeupQueue;

    private GenExponential arrivalGen;
    private GenUniform takeOrderGen;
    private GenTriangular skinCleaningGen;
    private GenUniform payGen;
    private Random simpleHairStyleGen;
    private GenEmpirical complexHairStyleGen;
    private GenEmpirical weddingHairStyleGen;
    private Random simpleMakeUpGen;
    private Random complexMakeUpGen;
    private Random randomOrderGen;

    private AverageStatistic averageHairstylingTimeForReplications;
    private AverageStatistic averageHairstylingTimeForReplication;
    private AverageSizeOfQueueForReplication averageSizeOfQueueForReplication;
    private AverageStatistic averageSizeOfQueueForReplications;
    private AverageStatistic averageTimeInSystemForReplication;
    private AverageStatistic averageTimeInSystemForReplications;
    private AverageStatistic averageTimeSpentInReceptionQueueForReplication;
    private AverageStatistic averageTimeSpentInReceptionQueueForReplications;
    private AverageStatistic averageCoolingTimeForReplication;
    private AverageStatistic averageCoolingTimeForReplications;

    private ArrayList<AverageStatistic> averageStatistics;

    private int receptionistsNum;
    private int hairstylistsNum;
    private int cosmeticiansNum;

    private int in;
    private int out;
    private double endOfWork = 28800;
    private long timeToSleep;
    private double refresh;
    private int currentMode = 2;


    public SalonSimulation(int replications, String receptionists, String hairstylists, String cosmeticians) {
        this.replications = replications;
        this.receptionistsNum = (!receptionists.equals("")) ? Integer.parseInt(receptionists) : 4;
        this.hairstylistsNum = (!hairstylists.equals("")) ? Integer.parseInt(hairstylists) : 4;
        this.cosmeticiansNum = (!cosmeticians.equals("")) ? Integer.parseInt(cosmeticians) : 4;
    }

    @Override
    protected void beforeReplications() {

        receptionists = new ArrayList<>(receptionistsNum);
        for (int i = 0; i < receptionistsNum; i++) {
            receptionists.add(new Employee(EmployeeType.Receptionist));
        }
        hairstylists = new ArrayList<>(hairstylistsNum);
        for (int i = 0; i < hairstylistsNum; i++) {
            hairstylists.add(new Employee(EmployeeType.Hairstylist));
        }
        cosmeticians = new ArrayList<>(cosmeticiansNum);
        for (int i = 0; i < cosmeticiansNum; i++) {
            cosmeticians.add(new Employee(EmployeeType.Cosmetician));
        }

        customersInSalon = new ArrayList<>();

        unoccupiedReceptionists = new PriorityQueue<>(receptionists);
        unoccupiedHairstylists = new PriorityQueue<>(hairstylists);
        unoccupiedCosmeticians = new PriorityQueue<>(cosmeticians);

        receptionQueue = new LinkedList<>();
        payQueue = new LinkedList<>();
        hairstylingQueue = new LinkedList<>();
        makeupQueue = new LinkedList<>();

        simpleHairStyleGen = new Random(Seeder.getSeed());

        complexHairStyleGen = new GenEmpirical();
        complexHairStyleGen.addParams(new EmpParams(30, 60, 0.4));
        complexHairStyleGen.addParams(new EmpParams(61, 120, 0.6));

        weddingHairStyleGen = new GenEmpirical();
        weddingHairStyleGen.addParams(new EmpParams(50, 60, 0.2));
        weddingHairStyleGen.addParams(new EmpParams(61, 100, 0.3));
        weddingHairStyleGen.addParams(new EmpParams(101, 150, 0.5));

        simpleMakeUpGen = new Random(Seeder.getSeed());
        complexMakeUpGen = new Random(Seeder.getSeed());

        randomOrderGen = new Random(Seeder.getSeed());

        arrivalGen = new GenExponential(450);
        takeOrderGen = new GenUniform(-120, 120);
        //takeOrderGen = new GenUniform(80, 320);
        skinCleaningGen = new GenTriangular(360,900,540);
        payGen = new GenUniform(-50, 50);
        //payGen = new GenUniform(130, 230);

        currentReplication = 0;

        averageStatistics = new ArrayList<>();

        averageHairstylingTimeForReplications = new AverageStatistic("AverageHairstylingTime", true);
        averageSizeOfQueueForReplications = new AverageStatistic("AverageSizeOfQueue", false);
        averageTimeInSystemForReplications = new AverageStatistic("AverageTimeInSystem", true);
        averageTimeSpentInReceptionQueueForReplications = new AverageStatistic("AverageTimeSpentInReceptionQueue", true);
        averageCoolingTimeForReplications = new AverageStatistic("AverageCoolingTime", true);

        //averageStatistics.add(averageHairstylingTimeForReplications);
        averageStatistics.add(averageSizeOfQueueForReplications);
        averageStatistics.add(averageTimeInSystemForReplications);
        averageStatistics.add(averageTimeSpentInReceptionQueueForReplications);
        averageStatistics.add(averageCoolingTimeForReplications);
    }

    @Override
    protected void beforeReplication() {
        averageHairstylingTimeForReplication = new AverageStatistic();
        averageSizeOfQueueForReplication = new AverageSizeOfQueueForReplication();
        averageTimeInSystemForReplication = new AverageStatistic();
        averageTimeSpentInReceptionQueueForReplication = new AverageStatistic();
        averageCoolingTimeForReplication = new AverageStatistic();

        this.simulationTime = 0;
        Arrival firstArrival = new Arrival(this.simulationTime + getArrivalTime(), this);
        SystemEvent systemEvent = new SystemEvent(this.simulationTime + 400,this);
        calendar.add(firstArrival);

        if (currentMode == 1) {
            calendar.add(systemEvent);
        }
    }

    @Override
    protected void doReplication() {
        Event currentEvent;
        while (calendar.size() > 0) {
            if (stopSimulation) {break;}
            while (pauseSimulation) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentEvent = calendar.poll();
            simulationTime = currentEvent.getTime();
            currentEvent.execute();
        }
    }

    @Override
    protected void afterReplication() {
        addCountAverageSizeOfQueueForReplication(this.simulationTime, getReceptionQueue().size());

        currentReplication++;

        if (currentMode == 2) {
            refreshGUI();
        }

        this.averageCoolingTimeForReplication.addTimeWithInc(this.simulationTime - this.endOfWork);

        //this.averageCoolingTimeForReplications.addTimeWithoutInc(this.averageCoolingTimeForReplication.getTime());
        //this.averageCoolingTimeForReplications.addCount(this.averageCoolingTimeForReplication.getCount());
        this.averageCoolingTimeForReplications.addAverage(this.averageCoolingTimeForReplication.getTime() / this.averageCoolingTimeForReplication.getCount());


        this.averageHairstylingTimeForReplications.addTimeWithoutIncCooling(this.averageHairstylingTimeForReplication.getTimeCooling());
        this.averageHairstylingTimeForReplications.addCountCooling(this.averageHairstylingTimeForReplication.getCountCooling());
        this.averageHairstylingTimeForReplications.addAverageCooling(this.averageHairstylingTimeForReplication.getTimeCooling() / this.averageHairstylingTimeForReplication.getCountCooling());

        //this.averageHairstylingTimeForReplications.addTimeWithoutInc(this.averageHairstylingTimeForReplication.getTime());
        //this.averageHairstylingTimeForReplications.addCount(this.averageHairstylingTimeForReplication.getCount());
        this.averageHairstylingTimeForReplications.addAverage(this.averageHairstylingTimeForReplication.getTime() / this.averageHairstylingTimeForReplication.getCount());


        //this.averageSizeOfQueueForReplications.addTimeWithoutIncCooling(this.averageSizeOfQueueForReplication.getTimeCooling());
        //this.averageSizeOfQueueForReplications.addCountCooling(this.averageSizeOfQueueForReplication.getCountCooling());
        this.averageSizeOfQueueForReplications.addAverageCooling(this.averageSizeOfQueueForReplication.getCountCooling() / this.averageSizeOfQueueForReplication.getTimeCooling());

        //this.averageSizeOfQueueForReplications.addTimeWithoutInc(this.averageSizeOfQueueForReplication.getTime());
        //this.averageSizeOfQueueForReplications.addCount(this.averageSizeOfQueueForReplication.getCount());
        this.averageSizeOfQueueForReplications.addAverage(this.averageSizeOfQueueForReplication.getCount() / this.averageSizeOfQueueForReplication.getTime());


        //this.averageTimeInSystemForReplications.addTimeWithoutIncCooling(this.averageTimeInSystemForReplication.getTimeCooling());
        //this.averageTimeInSystemForReplications.addCountCooling(this.averageTimeInSystemForReplication.getCountCooling());
        this.averageTimeInSystemForReplications.addAverageCooling(this.averageTimeInSystemForReplication.getTimeCooling() / this.averageTimeInSystemForReplication.getCountCooling());

        //this.averageTimeInSystemForReplications.addTimeWithoutInc(this.averageTimeInSystemForReplication.getTime());
        //this.averageTimeInSystemForReplications.addCount(this.averageTimeInSystemForReplication.getCount());
        this.averageTimeInSystemForReplications.addAverage(this.averageTimeInSystemForReplication.getTime() / this.averageTimeInSystemForReplication.getCount());


        //this.averageTimeSpentInReceptionQueueForReplications.addTimeWithoutIncCooling(this.averageTimeSpentInReceptionQueueForReplication.getTimeCooling());
        //this.averageTimeSpentInReceptionQueueForReplications.addCountCooling(this.averageTimeSpentInReceptionQueueForReplication.getCountCooling());
        this.averageTimeSpentInReceptionQueueForReplications.addAverageCooling(this.averageTimeSpentInReceptionQueueForReplication.getTimeCooling() / this.averageTimeSpentInReceptionQueueForReplication.getCountCooling());

        //this.averageTimeSpentInReceptionQueueForReplications.addTimeWithoutInc(this.averageTimeSpentInReceptionQueueForReplication.getTime());
        //this.averageTimeSpentInReceptionQueueForReplications.addCount(this.averageTimeSpentInReceptionQueueForReplication.getCount());
        this.averageTimeSpentInReceptionQueueForReplications.addAverage(this.averageTimeSpentInReceptionQueueForReplication.getTime() / this.averageTimeSpentInReceptionQueueForReplication.getCount());

        //this.replicationsHaircutTime.addTime(StartHairStyling.getHairStyileTime() / StartHairStyling.getDone());
    }

    @Override
    protected void afterReplications() {
        if (currentMode == 3) {
            refreshGUI();
        }
    }

    public ArrayList<Employee> getReceptionists() {
        return receptionists;
    }

    public ArrayList<Employee> getHairstylists() {
        return hairstylists;
    }

    public ArrayList<Employee> getCosmeticians() {
        return cosmeticians;
    }

    public PriorityQueue<Employee> getUnoccupiedReceptionists() {
        return unoccupiedReceptionists;
    }

    public PriorityQueue<Employee> getUnoccupiedHairStylists() {
        return unoccupiedHairstylists;
    }

    public PriorityQueue<Employee> getUnoccupiedCosmeticians() {
        return unoccupiedCosmeticians;
    }

    public Queue<Customer> getReceptionQueue() {
        return receptionQueue;
    }

    public Queue<Customer> getPayQueue() {
        return payQueue;
    }

    public Queue<Customer> getHairstylingQueue() {
        return hairstylingQueue;
    }

    public Queue<Customer> getMakeupQueue() {
        return makeupQueue;
    }

    public GenExponential getArrivalGen() {
        return arrivalGen;
    }

    public GenUniform getTakeOrderGen() {
        return takeOrderGen;
    }

    public GenTriangular getSkinCleaningGen() {
        return skinCleaningGen;
    }

    public GenUniform getPayGen() {
        return payGen;
    }

    /**
     * randomOrderGen.nextDouble()
     * @return
     */
    public double getRandomValue() {
        return this.randomOrderGen.nextDouble();
    }

    public ArrayList<Customer> getCustomersInSalon() {
        return customersInSalon;
    }

    /**
     * GenExponential(450);
     * @return
     */
    public double getArrivalTime() {
        return arrivalGen.sample();
    }

    /**
     * //GenUniform(-120, 120)
     * GenUniform(80, 320)
     * @return
     */
    public double getTakeOrderTime() {
        return this.takeOrderGen.sample() + 200;
    }

    /**
     * GenTriangular(360,900,540);
     * @return
     */
    public double getDeepSkinCleaningTime() {
        return this.skinCleaningGen.sample();
    }

    /**
     * //GenUniform(-50, 50);
     * GenUniform(130, 230);
     * @return
     */
    public double getPayTime() {
        return this.payGen.sample() + 180;
    }

    /**
     * nextInt(10, 30) * 60
     * @return
     */
    public double getSimpleHairstyleTime() {
        return this.simpleHairStyleGen.nextInt(10, 30 + 1) * 60;
        //return (this.simpleHairStyleGen.nextInt(20 + 1) + 10) * 60;
    }

    /**
     * new EmpParams(30, 60, 0.4)
     * new EmpParams(61, 120, 0.6)
     * @return
     */
    public double getComplexHairstyleTime() {
        return this.complexHairStyleGen.sample() * 60;
    }

    /**
     * new EmpParams(50, 60, 0.2)
     * new EmpParams(61, 100, 0.3)
     * new EmpParams(101, 150, 0.5)
     * @return
     */
    public double getWeddingHairstyleTime() {
        return this.weddingHairStyleGen.sample() * 60;
    }

    /**
     * nextInt(10,25) * 60
     * @return
     */
    public double getSimpleMakeUp() {
        return this.simpleMakeUpGen.nextInt(10,25 + 1) * 60;
        //return (this.simpleMakeUpGen.nextInt(15 + 1) + 10) * 60;
    }

    /**
     * nextInt(20,100) * 60
     * @return
     */
    public double getComplexMakeUp() {
        return this.complexMakeUpGen.nextInt(20,100 + 1) * 60;
        //return (this.complexMakeUpGen.nextInt(80 + 1) + 20) * 60;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public void incIn() {
        this.in++;
    }

    public void incOut() {
        this.out++;
    }

    public long getTimeToSleep() {
        return this.timeToSleep;
    }

    public void setTimeToSleep(int speed) {
        this.timeToSleep = (100 - speed) * 2L;
    }

    public double getRefreshTime() {
        return this.refresh;
    }

    public void setRefreshTime(double refreshTime) {
        this.refresh = refreshTime;
    }

    public PriorityQueue<Event> getEventCalendar() {
        return this.calendar;
    }

    /*public void setPause() {
        this.pauseSimulation = !pauseSimulation;
    }

    public void setStop() {
        this.stopSimulation = true;
    }*/

    public AverageStatistic getAverageHairstylingTimeForReplication() {
        return this.averageHairstylingTimeForReplication;
    }

    public AverageStatistic getAverageHairstylingTimeForReplications() {
        return this.averageHairstylingTimeForReplications;
    }

    public AverageStatistic getAverageSizeOfQueueForReplication() {
        return averageSizeOfQueueForReplication;
    }

    public AverageStatistic getAverageSizeOfQueueForReplications() {
        return averageSizeOfQueueForReplications;
    }

    public AverageStatistic getAverageTimeInSystemForReplication() {
        return averageTimeInSystemForReplication;
    }

    public AverageStatistic getAverageTimeInSystemForReplications() {
        return averageTimeInSystemForReplications;
    }

    public AverageStatistic getAverageTimeSpentInReceptionQueueForReplication() {
        return averageTimeSpentInReceptionQueueForReplication;
    }

    public AverageStatistic getAverageTimeSpentInReceptionQueueForReplications() {
        return averageTimeSpentInReceptionQueueForReplications;
    }

    public AverageStatistic getAverageCoolingTimeForReplication() {
        return averageCoolingTimeForReplication;
    }

    public AverageStatistic getAverageCoolingTimeForReplications() {
        return averageCoolingTimeForReplications;
    }

    public void addCountAverageSizeOfQueueForReplication(double timeOfChange, int customersInQueue) {
        averageSizeOfQueueForReplication.addCountCooling(timeOfChange, customersInQueue);
        if (this.simulationTime < this.endOfWork) {
            averageSizeOfQueueForReplication.addCount(timeOfChange, customersInQueue);
        }
    }

    public void addTimeWithIncAverageHairstylingTimeForReplication(double hairStyleTime) {
        averageHairstylingTimeForReplication.addTimeWithIncCooling(hairStyleTime);
        if (this.simulationTime < this.endOfWork) {
            averageHairstylingTimeForReplication.addTimeWithInc(hairStyleTime);
        }
    }

    public void addTimeWithIncAverageTimeSpentInReceptionQueueForReplication(double timeSpentInReceptionQueue) {
        averageTimeSpentInReceptionQueueForReplication.addTimeWithIncCooling(timeSpentInReceptionQueue);
        if (this.simulationTime < this.endOfWork) {
            averageTimeSpentInReceptionQueueForReplication.addTimeWithInc(timeSpentInReceptionQueue);
        }
    }

    public void addTimeWithIncAverageTimeInSystemForReplication(double timeInSystem) {
        averageTimeInSystemForReplication.addTimeWithIncCooling(timeInSystem);
        if (this.simulationTime < this.endOfWork) {
            averageTimeInSystemForReplication.addTimeWithInc(timeInSystem);
        }
    }

    public ArrayList<AverageStatistic> getAverageStatistics() {
        return averageStatistics;
    }

    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    public int getCurrentMode() {
        return this.currentMode;
    }

    public int getNumberOfHairstylists() {
        return this.hairstylistsNum;
    }

    public double getReplications() {
        return this.replications;
    }

    public int getCurrentReplicationCount() {
        return this.currentReplication;
    }
}
