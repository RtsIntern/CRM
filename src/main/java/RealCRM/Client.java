package RealCRM;

public class Client {
    private String clientId, name, address, phone, contactName, contactPhone;
    private int numContracts;
    public Client(String clientId, String name, String address, String phone, String contactName, String contactPhone) {
        this.clientId = clientId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    @Override
    public String toString() {
        return String.format(
                "Client[clientId='%s', name='%s', address='%s', phone='%s', contactName='%s', contactPhone='%s']",
                clientId, name, address, phone, contactName, contactPhone);
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getName() {
        return this.name;
    }

    public  String getAddress() {
        return this.address;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public int getNumContracts() { return this.numContracts; }

    public void setClientId(String clientId1) {
        this.clientId = clientId1;
    }

    public void setName(String name1) {
        this.name = name1;
    }

    public void setAddress(String address1) {
        this.address = address1;
    }

    public void setPhone(String phone1) {
        this.phone = phone1;
    }

    public void setContactName(String contactName1) {
        this.contactName = contactName1;
    }

    public void setContactPhone(String contactPhone1) {
        this.contactPhone = contactPhone1;
    }

    public void setNumContracts(int numContracts1) { this.numContracts = numContracts1; }

}

