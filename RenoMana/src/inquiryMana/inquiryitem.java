package inquiryMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.lang.model.util.SimpleElementVisitor6;

public class inquiryitem {

    private SimpleStringProperty projectInq;
    private SimpleStringProperty Name;
    private SimpleStringProperty Contact;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public inquiryitem(@JsonProperty("Name") String Name,
                       @JsonProperty("Contact") String Contact,
                       @JsonProperty("projectInq") String projectInq)

    {
        this.Name = new SimpleStringProperty(Name);
        this.Contact = new SimpleStringProperty(Contact);
        this.projectInq = new SimpleStringProperty(projectInq);
    }

    public SimpleStringProperty Name() {
        return Name;
    }
    public SimpleStringProperty Contact() {
        return Contact;
    }
    public SimpleStringProperty Inquiry() {
        return projectInq;
    }

}
