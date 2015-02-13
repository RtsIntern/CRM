package RealCRM;

public class Contract {

    private String contractId, name, description, clientId, scopeOfWork, contractNumber, contractFilename, startDate,
            endDate;
    private double hourlyRate, totalCost, profit, margin;
    public Contract(String contractId, String name, String description, String clientId, String scopeOfWork,
                  String contractNumber, String contractFilename, String startDate, String endDate,
                  double hourlyRate, double totalCost, double profit, double margin) {
        this.contractId = contractId;
        this.name = name;
        this.description = description;
        this.clientId = clientId;
        this.scopeOfWork = scopeOfWork;
        this.contractNumber = contractNumber;
        this.contractFilename = contractFilename;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hourlyRate = hourlyRate;
        this.totalCost = totalCost;
        this.profit = profit;
        this.margin = margin;
    }

    @Override
    public String toString() {
        return String.format(
                "Contract[contractId='%s', name='%s', description='%s', clientId='%s', scopeOfWork='%s', " +
                        "contractNumber='%s', contractFilename='%s', startDate='%s', endDate='%s', hourlyRate='%.2f', " +
                        "totalCost='%.2f', profit='%.2f', margin='%.2f']",
                contractId, name, description, clientId, scopeOfWork, contractNumber, contractFilename, startDate,
                endDate, hourlyRate, totalCost, profit, margin);
    }

    public String getContractId() {

        return this.contractId;
    }

    public String getName() {

        return this.name;
    }

    public  String getDescription() {

        return this.description;
    }

    public String getClientId() {

        return this.clientId;
    }

    public String getScopeOfWork() {

        return this.scopeOfWork;
    }

    public String getContractNumber() {

        return this.contractNumber;
    }

    public String getContractFilename() {

        return this.contractFilename;
    }

    public String getStartDate() {

        return this.startDate;
    }

    public String getEndDate() {

        return this.endDate;
    }

    public double getHourlyRate() {

        return this.hourlyRate;
    }

    public double getTotalCost() {

        return this.totalCost;
    }

    public double getProfit() {

        return this.profit;
    }

    public double getMargin() {

        return this.margin;
    }

    public void setContractId(String contractId1) {

        this.contractId = contractId1;
    }

    public void setName(String name1) {

        this.name = name1;
    }

    public  void setDescription(String description1) {

        this.description = description1;
    }

    public void setClientId(String clientId1) {

        this.clientId = clientId1;
    }

    public void setScopeOfWork(String scopeOfWork1) {

        this.scopeOfWork = scopeOfWork1;
    }

    public void setContractNumber(String contractNumber1) {

        this.contractNumber = contractNumber1;
    }

    public void setContractFilename(String contractFilename1) {

        this.contractFilename = contractFilename1;
    }

    public void setStartDate(String startDate1) {

        this.startDate = startDate1;
    }

    public void setEndDate(String endDate1) {

        this.endDate = endDate1;
    }

    public void setHourlyRate(double hourlyRate1) {

        this.hourlyRate = hourlyRate1;
    }

    public void setTotalCost(double totalCost1) {

        this.totalCost = totalCost1;
    }

    public void setProfit(double profit1) {

        this.profit = profit1;
    }

    public void setMargin(double margin1) {

        this.margin = margin1;
    }
}

