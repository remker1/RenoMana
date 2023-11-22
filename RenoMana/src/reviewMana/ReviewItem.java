package reviewMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReviewItem {

    private SimpleStringProperty title;

    private SimpleStringProperty description;

    private SimpleStringProperty rating;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReviewItem(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("rating") int rating){
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.rating = new SimpleStringProperty("★".repeat(rating) + "☆".repeat(5 - rating));
    }

    public ReviewItem(){}

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleStringProperty ratingProperty() {
        return rating;
    }
}

