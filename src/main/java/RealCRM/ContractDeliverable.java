package RealCRM;

/**
 * Created by corey on 2/6/2015.
 */
public class ContractDeliverable {

    private String contractDeliverableId, contractId, clientId, deliverableName, description, responsible,
                    productCategory, startDate, endDate;

    private double quotedHours, quotedHourlyRate, quotedPrice, estimatedHours, actualHours, profitOrLoss, profitMargin, totalCost;

    private boolean isCompleted;

    public ContractDeliverable(String contractDeliverableId, String contractId, String clientId, String deliverableName,
                               String description, String responsible, String productCategory, String startDate,
                               String endDate, double quotedHours,double quotedHourlyRate, double profitOrLoss, double profitMargin,
                               boolean isCompleted) {
        this.contractDeliverableId = contractDeliverableId;
        this.contractId = contractId;
        this.clientId = clientId;
        this.deliverableName = deliverableName;
        this.description = description;
        this.responsible = responsible;
        this.startDate = startDate;
        this.endDate = endDate;
        this.productCategory = productCategory;
        this.quotedHours = quotedHours;
        this.quotedHourlyRate = quotedHourlyRate;
        this.profitOrLoss = profitOrLoss;
        this.profitMargin = profitMargin;
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return String.format(
                "Contract[contractDeliverableId='%s', contractId='%s', clientId='%s', deliverableName='%s', " +
                        "description='%s', responsible='%s', productCategory='%s', startDate='%s', endDate='%s', quotedHours='%.2f', " +
                        "quotedHourlyRate='%.2f', quotedPrice='%.2f', estimatedHours='%.2f', actualHours='%.2f', " +
                        "profitOrLoss='%.2f', profitMargin='%.2f', isCompleted='%b']",
                contractDeliverableId, contractId, clientId, deliverableName, description, responsible,
                productCategory, startDate, endDate, quotedHours, quotedHourlyRate, quotedPrice, estimatedHours, actualHours, profitOrLoss,
                profitMargin, isCompleted);
    }

    public void setActualHours(double actualHours) {
        this.actualHours = actualHours;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setContractDeliverableId(String contractDeliverableId) {
        this.contractDeliverableId = contractDeliverableId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public void setDeliverableName(String deliverableName) {
        this.deliverableName = deliverableName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public void setProfitOrLoss(double profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
    }

    public void setQuotedHourlyRate(double quotedHourlyRate) {
        this.quotedHourlyRate = quotedHourlyRate;
    }

    public void setQuotedHours(double quotedHours) {
        this.quotedHours = quotedHours;
    }

    public void setQuotedPrice(double quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public double getActualHours() {
        return this.actualHours;
    }

    public double getEstimatedHours() {
        return this.estimatedHours;
    }

    public double getProfitMargin() {
        return this.profitMargin;
    }

    public double getProfitOrLoss() {
        return this.profitOrLoss;
    }

    public double getQuotedHourlyRate() {
        return this.quotedHourlyRate;
    }

    public double getQuotedHours() {
        return this.quotedHours;
    }

    public double getQuotedPrice() {
        return this.quotedPrice;
    }

    public double getTotalCost() { return this.totalCost; }

    public String getClientId() {
        return this.clientId;
    }

    public String getContractDeliverableId() {
        return this.contractDeliverableId;
    }

    public String getContractId() {
        return this.contractId;
    }

    public String getDeliverableName() {
        return this.deliverableName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProductCategory() {
        return this.productCategory;
    }

    public String getResponsible() {
        return this.responsible;
    }

    public String getStartDate() { return this.startDate; }

    public String getEndDate() { return  this.endDate; }
}