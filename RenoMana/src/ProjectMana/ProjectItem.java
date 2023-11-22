package ProjectMana;

import javafx.beans.property.SimpleStringProperty;

public class ProjectItem {
    private final SimpleStringProperty Name;
    private final SimpleStringProperty Email;
    private final SimpleStringProperty Contact;
    private final SimpleStringProperty Company;
    private final SimpleStringProperty Start_date;
    private final SimpleStringProperty End_date;
    private final SimpleStringProperty Description;
    private final SimpleStringProperty Inquiry;



    public ProjectItem(SimpleStringProperty Name,
                       SimpleStringProperty Email,
                       SimpleStringProperty Contact,
                       SimpleStringProperty Company,
                       SimpleStringProperty Start_date,
                       SimpleStringProperty End_date,
                       SimpleStringProperty Description,
                       SimpleStringProperty Inquiry) {
                        this.Name = Name;
                        this.Email = Email;
                        this.Contact = Contact;
                        this.Company = Company;
                        this.Start_date = Start_date;
                        this.End_date = End_date;
                        this.Description = Description;
                        this.Inquiry = Inquiry;
    }

    public String getName() { return Name.get(); }
    public SimpleStringProperty Name() {
        return Name;
    }

    public String getEmail() { return Email.get(); }
    public SimpleStringProperty Email() {
        return Email;
    }

    public String getContact() { return Contact.get(); }
    public SimpleStringProperty Contact() {
        return Contact;
    }

    public String getCompany() { return Company.get(); }
    public SimpleStringProperty Company() {
        return Company;
    }

    public String getStart_date() { return Start_date.get(); }
    public SimpleStringProperty Start_date() {
        return Start_date;
    }

    public String getEnd_date() { return End_date.get(); }
    public SimpleStringProperty End_date() {
        return End_date;
    }

    public String getDescription() { return Description.get(); }
    public SimpleStringProperty Description() {
        return Description;
    }

    public String getInquiry() { return Inquiry.get(); }
    public SimpleStringProperty Inquiry() {
        return Inquiry;
    }
}
