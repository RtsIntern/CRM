package RealCRM;

/**
 * Created by corey on 2/5/2015.
 */

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Controller
public class RealCRMController {

    //private ClientManager clientManager;

    /*Autowired
    public RealCRMController(ClientManager clientManager) {
        this.clientManager = clientManager;
    }*/

    // Return a JdbcTemplate that is hooked up to the RealCRM database
    public JdbcTemplate getJdbcTemplate() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(SQLServerDriver.class);
        dataSource.setUsername("realcrmdbuser");
        dataSource.setUrl("jdbc:sqlserver://development.sks.com;databaseName=RealCRM;integratedSecurity=false;");
        dataSource.setPassword("rtssks2011Q!");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        return jdbcTemplate;
    }

    public void deleteDeliverable(String id) {

        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update("DELETE FROM ContractDeliverable WHERE ContractDeliverableId = ?", id);
    }

    public void deleteContract(String id) {

        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update("DELETE FROM Contract WHERE ContractId = ?", id);
        jdbcTemplate.update("DELETE FROM ContractDeliverable WHERE ContractId = ?", id);
    }

    public void deleteClient(String id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update("DELETE FROM Client WHERE ClientId = ?", id);
        jdbcTemplate.update("DELETE FROM Contract WHERE ClientId = ?", id);
        jdbcTemplate.update("DELETE FROM ContractDeliverable WHERE ClientId = ?", new Object[] { id });
    }

    public void deleteEmployee(String id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update("DELETE FROM Employee WHERE EmployeeId = ?", id);
        jdbcTemplate.update("DELETE FROM ContractDeliverable_Employee WHERE EmployeeId = ?", id);
    }

    // Deleting a ContractDeliverable_Employee needs a different method, because we need two id parameters
    // to identify the item we want to delete
    @RequestMapping(value = "/deleteContractDeliverable_Employee")
    public @ResponseBody void deleteContractDeliverable_Employee(@RequestParam(value="contractDeliverableId")
                                                                 String contractDeliverableId, @RequestParam(value="employeeId")
                                                                 String employeeId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update("DELETE FROM ContractDeliverable_Employee WHERE ContractDeliverableId = ? AND" +
                " EmployeeId = ?", contractDeliverableId, employeeId);

    }

    // "/delete" handles the deletion of everything except for ContractDeliverable_Employee relations.
    @RequestMapping(value = "/delete")
    public @ResponseBody void delete(@RequestParam(value = "id") String id, @RequestParam(value = "type") String type) {

        if (type.equals("client")) {
            deleteClient(id);
        }

        if (type.equals("contract")) {
            deleteContract(id);
        }

        if (type.equals("deliverable")) {
            deleteDeliverable(id);
        }

        if (type.equals("employee")) {
            deleteEmployee(id);
        }
    }



    @RequestMapping(value= "/default" )
    public String defaultPage() {
        return "default";
    }

    @RequestMapping(value = "/clients")
    public @ResponseBody List getClients() {

        JdbcTemplate jdbcTemplate = getJdbcTemplate();


        // Get every row from the Clients table
        System.out.println("Querying all clients");
        List<Client> clients = jdbcTemplate.query(
                "select * from Client",
                new RowMapper<Client>() {
                    @Override
                    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Client(rs.getString("ClientId"), rs.getString("Name"),
                                rs.getString("Address"), rs.getString("Phone"),
                                rs.getString("ContactName"), rs.getString("ContactPhone"));
                    }
                });

        for(Client client : clients) {
            String clientId = client.getClientId();

            // This is rather slow. I'm sure there's a better way to do it, but I don't have time to look for it
            // Query the Contract database with each ClientId. Count how many Contracts are returned in the list,
            // and set that integer to the Client's numContracts/
            List<Contract> contractsForClient = jdbcTemplate.query(
                    "select * from Contract WHERE ClientId=?", new Object[] {clientId},
                    new RowMapper<Contract>() {
                        @Override
                        public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new Contract(rs.getString("ContractId"), "",
                                    "", "", "", "", "", "", "", 0, 0, 0, 0);
                        }
                    });
            client.setNumContracts(contractsForClient.size());
        }



        /*for (Client client : results) {
            System.out.println(client);
        }*/
        return clients;
    }

    @RequestMapping(value = "/addClient" )
    public @ResponseBody void addClient(@RequestParam(value = "name") String name) {
        System.out.println("Adding new client");

        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update(
                "INSERT INTO Client(Name) values(?)",name);
    }



    @RequestMapping(value = "/contracts" )
    public String contractsPage(@RequestParam(value = "clientId") String clientId) {
        return "contracts";
    }

    @RequestMapping(value = "/getContracts" )
    public @ResponseBody List getContracts(@RequestParam(value = "clientId") String clientId) {

        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println("Querying contracts: " + clientId);
        List<Contract> contracts = jdbcTemplate.query(
                "select * from Contract where ClientId=?", new Object[] {clientId},
                new RowMapper<Contract>() {
                    @Override
                    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Contract(rs.getString("ContractId"), rs.getString("Name"),
                                rs.getString("Description"), rs.getString("ClientId"),
                                rs.getString("ScopeOfWork"), rs.getString("ContractNumber"),
                                rs.getString("ContractFilename"), rs.getString("StartDate"),
                                rs.getString("EndDate"), rs.getDouble("HourlyRate"), rs.getDouble("TotalCost"),
                                0, 0);
                    }
                });

        for (Contract contract : contracts){
            String startDate = contract.getStartDate();
            String endDate = contract.getEndDate();

            if (startDate == null)
            {
                startDate = "";
            }

            if (endDate == null) {
                endDate = "";
            }

            if (!startDate.isEmpty()){
                startDate = startDate.replace("-", "/");
            }

            if(!endDate.isEmpty()) {
                endDate = endDate.replace("-", "/");
            }


            // MySql replaces null dates with 1900/01/01. Also annoying. Replace that with an empty string.
            if (startDate.equals("1900/01/01"))
            {
                startDate = "";
            }

            if (endDate.equals("1900/01/01"))
            {
                endDate = "";
            }

            contract.setStartDate(startDate);
            contract.setEndDate(endDate);

        }

        return contracts;
    }

    @RequestMapping(value = "/addContract" )
    public @ResponseBody void addContract(@RequestParam(value = "name") String name, @RequestParam(value = "desc") String desc,
                                          @RequestParam(value = "clientId") String clientId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update(
                "INSERT INTO Contract(Name,Description,ClientId) values(?,?,?)", name, desc, clientId);

        System.out.println("Added contract");
    }

    @RequestMapping(value = "/getParentClient" )
    public @ResponseBody Client getParentClient (@RequestParam(value = "clientId") String clientId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println("Getting parent client");
        List<Client> clients = jdbcTemplate.query(
                "select * from Client where ClientId = ?", new Object[] {clientId},
                new RowMapper<Client>() {
                    @Override
                    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Client(rs.getString("ClientId"), rs.getString("Name"),
                                rs.getString("Address"), rs.getString("Phone"),
                                rs.getString("ContactName"), rs.getString("ContactPhone"));
                    }
                });

        Client parentClient = clients.get(0);
        return parentClient;
    }



    @RequestMapping(value = "/contract-details")
    public String contractDetailsPage() {
        return "contract-details";
    }

    @RequestMapping(value = "/getContractDeliverables")
    public @ResponseBody List getContractDeliverables(@RequestParam(value = "contractId") String contractId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();


        System.out.println("Querying ContractDeliverables: " + contractId);
        List<ContractDeliverable> contractDeliverables = jdbcTemplate.query(
                "select * from ContractDeliverable where ContractId=?", new Object[] {contractId},
                new RowMapper<ContractDeliverable>() {
                    @Override
                    public ContractDeliverable mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new ContractDeliverable(rs.getString("ContractDeliverableId"), rs.getString("ContractId"),
                                rs.getString("ClientId"), rs.getString("DeliverableName"),
                                rs.getString("Description"), rs.getString("Responsible"),
                                rs.getString("ProductCategory"), rs.getString("startDate"),
                                rs.getString("endDate"), rs.getDouble("QuotedHours"),
                                rs.getDouble("QuotedHourlyRate"),
                                rs.getDouble("ProfitOrLoss"), rs.getDouble("ProfitMargin"),
                                rs.getBoolean("IsCompleted"));
                    }
                });
        System.out.println("queried");
        System.out.println(contractDeliverables);

        for (ContractDeliverable contractDeliverable : contractDeliverables) {
            List<ContractDeliverable_Employee> assignedEmployeesForContractDeliverable = assignedEmployees(contractDeliverable.getContractDeliverableId());
            double totalActualHours = 0;
            double totalEstimatedHours = 0;
            double totalCost = 0;
            double profitOrLoss = 0;
            double quotedPrice = 0;

            for (ContractDeliverable_Employee assignedEmployee : assignedEmployeesForContractDeliverable) {
                totalActualHours += assignedEmployee.getActualHours();
                totalEstimatedHours += assignedEmployee.getEstimatedHours();
                totalCost += assignedEmployee.getCost();
                quotedPrice = contractDeliverable.getQuotedHourlyRate()*contractDeliverable.getQuotedHours();
            }

            contractDeliverable.setActualHours(totalActualHours);
            contractDeliverable.setEstimatedHours(totalEstimatedHours);
            contractDeliverable.setTotalCost(totalCost);
            contractDeliverable.setQuotedPrice(quotedPrice);

            profitOrLoss = contractDeliverable.getQuotedPrice() - totalCost;
            contractDeliverable.setProfitOrLoss(profitOrLoss);

            double profitMargin = ((profitOrLoss/contractDeliverable.getQuotedPrice()) * 100);
            profitMargin = Math.round(profitMargin);
            contractDeliverable.setProfitMargin(profitMargin);

            System.out.println(contractDeliverable);
        }

        return contractDeliverables;
    }

    @RequestMapping(value = "/getParentContract" )
    public @ResponseBody Contract getParentContract (@RequestParam(value = "contractId") String contractId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println("Getting parent contract");
        List<Contract> contracts = jdbcTemplate.query(
                "select * from Contract where ContractId = ?", new Object[] {contractId},
                new RowMapper<Contract>() {
                    @Override
                    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Contract(rs.getString("ContractId"), rs.getString("Name"),
                                rs.getString("Description"), rs.getString("ClientId"),
                                rs.getString("ScopeOfWork"), rs.getString("ContractNumber"),
                                rs.getString("ContractFilename"), rs.getString("StartDate"),
                                rs.getString("EndDate"), rs.getDouble("HourlyRate"), rs.getDouble("TotalCost"),
                                0, 0);
                    }
                });

        Contract parentContract = contracts.get(0);

        // MySql replaces slashes in dates with dashes, and then can't read them back in with dashes. Odd.
        // Revert dates back to their original format.

        String startDate = parentContract.getStartDate();
        String endDate = parentContract.getEndDate();

        if (startDate == null) {
            startDate = "";
            System.out.println("heloooooooooooooo");
        }

        if (endDate == null) {
            endDate = "";
        }

        startDate = startDate.replace("-", "/");
        endDate = endDate.replace("-", "/");

        // MySql replaces null dates with 1900/01/01. Also annoying. Replace that with null.
        if (startDate.equals("1900/01/01"))
        {
            startDate = "";
        }

        if (endDate.equals("1900/01/01"))
        {
            endDate = "";
        }


        parentContract.setStartDate(startDate);
        parentContract.setEndDate(endDate);

        String parentContractNumber = parentContract.getContractNumber();
        if (parentContractNumber == null) {
            parentContract.setContractNumber("");
        }

        System.out.println(parentContract);
        return parentContract;
    }

    @RequestMapping(value = "/updateContract" )
    public @ResponseBody void updateContract (@RequestParam(value = "contractId") String contractId,
                                              @RequestParam(value = "contractTitle") String contractTitle,
                                              @RequestParam(value = "contractNumber") String contractNumber,
                                              @RequestParam(value = "contractStartDate") String contractStartDate,
                                              @RequestParam(value = "contractEndDate") String contractEndDate,
                                              @RequestParam(value = "contractHourlyRate") String contractHourlyRateString,
                                              @RequestParam(value = "clientId") String clientId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println("Updating contract " + contractTitle + "string: " + contractHourlyRateString);

        double contractHourlyRate;
        System.out.println(contractHourlyRateString);
        if (contractHourlyRateString.isEmpty())
        {
            contractHourlyRate = 0;
        }
        else
        {
            contractHourlyRate = Double.parseDouble(contractHourlyRateString);
        }



        jdbcTemplate.update("UPDATE Contract SET Name=?, ContractNumber=?, StartDate=?, EndDate=?, HourlyRate=?, " +
                "ClientId=? WHERE ContractId=?", contractTitle, contractNumber, contractStartDate, contractEndDate,
                contractHourlyRate, clientId, contractId);

        jdbcTemplate.update("UPDATE ContractDeliverable SET QuotedHourlyRate=? WHERE ContractId=?", contractHourlyRate,
                contractId);
    }

    // TODO: Finish implementing file upload. There is an example here:
    // https://spring.io/guides/gs/uploading-files/
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody void handleFileUpload(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
            }
        }
    }


    @RequestMapping(value = "/contract-deliverables" )
    public String contractDeliverablesPage () { return "contract-deliverables"; }

    @RequestMapping(value = "/addDeliverable" )
    public @ResponseBody void addDeliverable(@RequestParam(value = "name") String name,
                                             @RequestParam(value = "clientId") String clientId,
                                             @RequestParam(value = "contractId") String contractId,
                                             @RequestParam(value = "contractHourlyRate") String contractHourlyRateString) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        if (contractHourlyRateString == null) {
            contractHourlyRateString = "";
        }

        double contractHourlyRate = Double.parseDouble(contractHourlyRateString);

        jdbcTemplate.update(
                "INSERT INTO ContractDeliverable(DeliverableName,ClientId,ContractId,QuotedHourlyRate) values(?,?,?,?)",
                name, clientId, contractId, contractHourlyRate);

        System.out.println("Added contract deliverable");
    }

    @RequestMapping(value = "/editDeliverable" )
    public @ResponseBody void editDeliverable(@RequestParam(value = "id") String id,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "description") String description,
                                             @RequestParam(value = "startDate") String startDate,
                                             @RequestParam(value = "endDate") String endDate,
                                             @RequestParam(value = "contractHours") String deliverableHoursString) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println("Updating deliverable " + name);

        if (deliverableHoursString.isEmpty())
        {
            deliverableHoursString = "0";
        }

        double deliverableHours;
        deliverableHours = Double.parseDouble(deliverableHoursString);

        if (description.isEmpty())
        {
            description = "";
        }

        if (startDate.isEmpty())
        {
            startDate = "";
        }

        if (endDate.isEmpty())
        {
            endDate = "";
        }

        jdbcTemplate.update("UPDATE ContractDeliverable SET DeliverableName=?, Description=?, StartDate=?, EndDate=?, QuotedHours=? " +
                        "WHERE ContractDeliverableId=?", name, description, startDate, endDate, deliverableHours, id);
    }

    @RequestMapping(value = "/getParentDeliverable")
    public @ResponseBody ContractDeliverable getParentDeliverable (@RequestParam(value = "contractDeliverableId") String id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println("Getting parent deliverable");
        List<ContractDeliverable> contractDeliverable = jdbcTemplate.query(
                "select * from ContractDeliverable where ContractDeliverableId = ?", new Object[] {id},
                new RowMapper<ContractDeliverable>() {
                    @Override
                    public ContractDeliverable mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new ContractDeliverable(rs.getString("ContractDeliverableId"), rs.getString("ContractId"),
                                rs.getString("ClientId"), rs.getString("DeliverableName"),
                                rs.getString("Description"), rs.getString("Responsible"),
                                rs.getString("ProductCategory"), rs.getString("StartDate"),
                                rs.getString("EndDate"), rs.getDouble("QuotedHours"), rs.getDouble("QuotedHourlyRate"),
                                0, 0, rs.getBoolean("IsCompleted"));
                    }
                });

        ContractDeliverable parentContractDeliverable = contractDeliverable.get(0);

        // MySql replaces slashes in dates with dashes, and then can't read them back in with dashes. Odd.
        // Revert dates back to their original format.

        String startDate = parentContractDeliverable.getStartDate();
        String endDate = parentContractDeliverable.getEndDate();

        if (startDate == null) {
            startDate = "";
        }

        if (endDate == null) {
            endDate = "";
        }

        startDate = startDate.replace("-", "/");
        endDate = endDate.replace("-", "/");

        // MySql replaces null dates with 1900/01/01. Also annoying. Replace that with null.
        if (startDate.equals("1900/01/01"))
        {
            startDate = "";
        }

        if (endDate.equals("1900/01/01"))
        {
            endDate = "";
        }

        if (parentContractDeliverable.getDescription() == null) {
            parentContractDeliverable.setDescription("");
        }

        parentContractDeliverable.setStartDate(startDate);
        parentContractDeliverable.setEndDate(endDate);

        System.out.println(parentContractDeliverable);
        return parentContractDeliverable;
    }

    @RequestMapping(value = "/employees" )
    public String employeesPage () {
        return "employees"; }

    @RequestMapping(value = "/getEmployees" )
    public @ResponseBody List getEmployees() {

        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        List<Employee> employees = jdbcTemplate.query(
                "select * from Employee",
                new RowMapper<Employee>() {
                    @Override
                    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Employee(rs.getString("EmployeeId"), rs.getString("Name"),
                                rs.getDouble("HourlyRate"));
                    }
                });
        return employees;
    }

    @RequestMapping(value = "/addEmployee" )
    public @ResponseBody void addEmployee (@RequestParam(value = "name") String name,
                                           @RequestParam(value = "rate") double rate) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        jdbcTemplate.update(
                "INSERT INTO Employee(Name,HourlyRate) values(?,?)", name, rate);

        System.out.println("Added Employee");
    }

    @RequestMapping(value = "/editEmployee" )
    public @ResponseBody void editEmployee (@RequestParam(value = "name") String name,
                                            @RequestParam(value = "rate") String rateString,
                                            @RequestParam(value = "employeeId") String employeeId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if (rateString.isEmpty())
        {
            rateString = "0";
        }
        double rate = Double.parseDouble(rateString);
        jdbcTemplate.update("UPDATE Employee SET Name=?, HourlyRate=? WHERE EmployeeId=?", name, rate, employeeId);
    }

    @RequestMapping(value = "/assignEmployee")
    public @ResponseBody void assignEmployee(@RequestParam(value = "employeeId") String employeeId,
                                             @RequestParam(value = "contractDeliverableId") String contractDeliverableId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        System.out.println(contractDeliverableId);
        List<ContractDeliverable_Employee> assignedEmployees = jdbcTemplate.query(
                "select * from ContractDeliverable_Employee WHERE ContractDeliverableId=? AND EmployeeId=?",
                new Object [] {contractDeliverableId, employeeId},
                new RowMapper<ContractDeliverable_Employee>() {
                    @Override
                    public ContractDeliverable_Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new ContractDeliverable_Employee(rs.getString("ContractDeliverableId"), rs.getString("EmployeeId"),
                                "", 0, 0, 0);
                    }
                });

        if (assignedEmployees.size() == 0) {
            jdbcTemplate.update(
                    "INSERT INTO ContractDeliverable_Employee(EmployeeId,ContractDeliverableId, ActualHours," +
                            " EstimatedHours) values(?,?,?,?)",
                    employeeId, contractDeliverableId, 0, 0);

            System.out.println("Assigned Employee");
        }
    }

    @RequestMapping(value ="/assignedEmployees")
    public @ResponseBody List assignedEmployees(@RequestParam(value = "contractDeliverableId") String contractDeliverableId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        List<ContractDeliverable_Employee> assignedEmployees = jdbcTemplate.query(
                "select * from ContractDeliverable_Employee WHERE ContractDeliverableId=?", new Object[] {contractDeliverableId},
                new RowMapper<ContractDeliverable_Employee>() {
                    @Override
                    public ContractDeliverable_Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new ContractDeliverable_Employee(rs.getString("ContractDeliverableId"), rs.getString("EmployeeId"),
                                "", rs.getDouble("EstimatedHours"), rs.getDouble("ActualHours"), 0);
                    }
                });

        for (ContractDeliverable_Employee assignedEmployee : assignedEmployees){
            List<Employee> singleEmployee = jdbcTemplate.query(
                    "select * from Employee where EmployeeId = ?", new Object[] {assignedEmployee.getEmployeeId()},
                    new RowMapper<Employee>() {
                        @Override
                        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return new Employee("", rs.getString("Name"), rs.getDouble("HourlyRate"));
                        }
                    });
            Employee employee = singleEmployee.get(0);
            assignedEmployee.setName(employee.getName());
            assignedEmployee.setRate(employee.getHourlyRate());
            assignedEmployee.setCost(assignedEmployee.getActualHours()*assignedEmployee.getRate());
        }

        System.out.println(assignedEmployees);
        return assignedEmployees;
    }

    @RequestMapping(value ="/updateHours")
    public @ResponseBody void updateHours(@RequestParam(value = "updatedActualHours") String updatedActualHoursString,
                                          @RequestParam(value = "updatedEstimatedHours") String updatedEstimatedHoursString,
                                          @RequestParam(value = "employeeId")
                                          String employeeId, @RequestParam(value = "contractDeliverableId") String contractDeliverableId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if (updatedActualHoursString.isEmpty()) {
            updatedActualHoursString = "0";
        }
        double actualHours = Double.parseDouble(updatedActualHoursString);

        if (updatedEstimatedHoursString.isEmpty()) {
            updatedEstimatedHoursString = "0";
        }
        double estimatedHours = Double.parseDouble(updatedEstimatedHoursString);

        jdbcTemplate.update("UPDATE ContractDeliverable_Employee SET ActualHours=?, EstimatedHours=? WHERE EmployeeId=? " +
                "AND ContractDeliverableId=?", actualHours, estimatedHours, employeeId, contractDeliverableId);

    }
}

