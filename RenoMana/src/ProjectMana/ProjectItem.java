package ProjectMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProjectItem {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerEmail;
    private SimpleStringProperty customerCell;
    private SimpleStringProperty company;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleStringProperty projectDesc;
    private SimpleStringProperty projectInq;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProjectItem(@JsonProperty("customerName") String customerName,
                       @JsonProperty("customerEmail") String customerEmail,
                       @JsonProperty("customerCell") String customerCell,
                       @JsonProperty("company") String company,
                       @JsonProperty("startDate") String startDate,
                       @JsonProperty("endDate") String endDate,
                       @JsonProperty("projectDesc") String projectDesc,
                       @JsonProperty("projectInq") String projectInq)
    {
        this.customerName =new SimpleStringProperty(customerName);
        this.customerEmail = new SimpleStringProperty(customerEmail);
        this.customerCell = new SimpleStringProperty(customerCell);
        this.company = new SimpleStringProperty(company);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.projectDesc = new SimpleStringProperty(projectDesc);
        this.projectInq = new SimpleStringProperty(projectInq);
    }

    public ProjectItem() {}
    public SimpleStringProperty Name() { return customerName; }

    public SimpleStringProperty Email() {
        return customerEmail;
    }

    public SimpleStringProperty Contact() {
        return customerCell;
    }

    public SimpleStringProperty Company() {
        return company;
    }

    public SimpleStringProperty Start_date() {return startDate;}

    public SimpleStringProperty End_date() {
        return endDate;
    }

    public SimpleStringProperty Description() {
        return projectDesc;
    }

    public SimpleStringProperty Inquiry() {
        return projectInq;
    }
}
