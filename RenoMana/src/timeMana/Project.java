package timeMana;

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
    private final SimpleListProperty<String> members;

    public Project(SimpleStringProperty name, SimpleStringProperty timeline, SimpleStringProperty details, SimpleListProperty<String> members) {
        this.name = name;
        this.timeline = timeline;
        this.details = details;
        this.members = members;
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

    public ObservableList<String> getMembers() {
        return members.get();
    }

    public SimpleListProperty<String> membersProperty() {
        return members;
    }

    public void setMembers(ObservableList<String> members) {
        this.members.set(members);
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
