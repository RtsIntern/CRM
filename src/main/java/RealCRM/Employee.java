package RealCRM;

/**
 * Created by corey on 2/11/2015.
 */
public class Employee {
    private String employeeId, name;
    private double hourlyRate;

    public Employee (String employeeId, String name, double hourlyRate) {
        this.employeeId = employeeId;
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return String.format("Employee[employeeId='%s', name='%s', hourlyRate='%.2f']", employeeId, name, hourlyRate);
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getName() {
        return this.name;
    }

    public double getHourlyRate() {
        return this.hourlyRate;
    }

    public void setName(String name1) {
        this.name = name1;
    }

    public void setHourlyRate(double hourlyRate1) {
        this.hourlyRate = hourlyRate1;
    }
}
