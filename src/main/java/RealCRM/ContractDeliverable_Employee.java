package RealCRM;

/**
 * Created by corey on 2/11/2015.
 */
public class ContractDeliverable_Employee {
    private String contractDeliverableId, employeeId, name;
    private double estimatedHours, actualHours, rate, cost;

    public ContractDeliverable_Employee (String contractDeliverableId, String employeeId, String name, double estimatedHours,
                                         double actualHours, double rate) {
        this.contractDeliverableId = contractDeliverableId;
        this.employeeId = employeeId;
        this.name = name;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return String.format("ContractDeliverable_Employee[contractDeliverableId='%s\', employeeId='%s'," +
                " name='%s', estimatedHours='%.2f', actualHours='%.2f', rate='%.2f', cost='%.2f']",
                contractDeliverableId, employeeId, name, estimatedHours, actualHours, rate, cost);
    }

    public String getContractDeliverableId() {
        return this.contractDeliverableId;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getName() { return this.name; }

    public double getEstimatedHours () {
        return this.estimatedHours;
    }

    public double getActualHours () {
        return  this.actualHours;
    }

    public double getRate () { return this.rate; }

    public double getCost () { return this.cost; }

    public void setName(String name1) { this.name = name1; }

    public void setHours (double estimatedHours1) {
        this.estimatedHours = estimatedHours1;
    }

    public void setRate (double rate1) { this.rate = rate1; }

    public void setCost (double cost1) { this.cost = cost1; }
}
