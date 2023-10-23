package timeMana;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Project {
    private SimpleStringProperty name;
    private SimpleStringProperty timeline;
    private SimpleStringProperty details;
    private SimpleListProperty<String> members;

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
}
