package timeMana;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Project {
    private final SimpleStringProperty name;
    private final SimpleStringProperty timeline;
    private final SimpleStringProperty details;
    private final SimpleStringProperty members;
    private final SimpleStringProperty _id;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Project(@JsonProperty("pName") String name,
                   @JsonProperty("pTime") String timeline,
                   @JsonProperty("pDetails") String details,
                   @JsonProperty("pMember") String members,
                   @JsonProperty("_id") String _id) {
        this.name = new SimpleStringProperty(name);
        this.timeline = new SimpleStringProperty(timeline);
        this.details = new SimpleStringProperty(details);
        this.members = new SimpleStringProperty(members);
        this._id = new SimpleStringProperty(_id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTimeline() {
        return timeline.get();
    }

    public SimpleStringProperty timelineProperty() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline.set(timeline);
    }

    public String getDetails() {
        return details.get();
    }

    public SimpleStringProperty detailsProperty() {
        return details;
    }

    public void setDetails(String details) {
        this.details.set(details);
    }

    public String getMembers() {
        return members.get();
    }

    public SimpleStringProperty membersProperty() {
        return members;
    }

    public void setMembers(String members) {this.members.set(members);}

    public String get_id() {
        return _id.get();
    }

    public SimpleStringProperty _idProperty() {
        return _id;
    }

    public void set_id(String _id) {
        this._id.set(_id);
    }

    public String getTimelineStatus() {
        try {
            // parse timeline string into LocalDate object
            LocalDate dueDate = LocalDate.parse(timeline.get(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // current date to compare due date with
            LocalDate currentDate = LocalDate.now();

            // calculate the number of days until the project is due
            long daysUntilDue = ChronoUnit.DAYS.between(currentDate, dueDate);

            // determine status based on days until due date
            if (daysUntilDue < 0) {
                return "overdue"; // when project is past its due date
            } else if (daysUntilDue < 7) {
                return "red"; // due in less than a week
            } else if (daysUntilDue <= 30) {
                return "yellow"; // due in more than a week but less than a month
            } else {
                return "green"; // due in more than a month
            }
        } catch (Exception e) {
            // parsing exceptions
            return "unknown"; // could not determine the status
        }
    }
}