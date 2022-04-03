package Main;

public class Employee implements Comparable<Employee> {

    protected double timeWorked;
    protected boolean occupied;
    private double start;
    private EmployeeType type;
    private int id;
    private static int idCounter = 1;

    public Employee(EmployeeType type) {
        this.type = type;
        this.id = idCounter;
        idCounter++;
    }

    public void setOccupied() {
        this.occupied = !this.occupied;
    }

    public void addTimeWorked(double timeWorked) {
        this.timeWorked += timeWorked;
    }

    public double getTimeWorked() {
        return timeWorked;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    @Override
    public int compareTo(Employee employee) {
        if (this.timeWorked < employee.getTimeWorked()) {
            return -1;
        } else if (this.timeWorked == employee.getTimeWorked()) {
            return  0;
        } else {
            return 1;
        }
    }

    public EmployeeType getType() {
        return type;
    }

    public boolean getOccupied() {
        return this.occupied;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "timeWorked=" + timeWorked +
                ", occupied=" + occupied +
                ", type=" + type +
                ", id=" + id +
                '}';
    }
}
