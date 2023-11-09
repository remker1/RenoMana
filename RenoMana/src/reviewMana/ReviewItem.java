package reviewMana;

import javafx.beans.property.SimpleStringProperty;

public class ReviewItem {
    private SimpleStringProperty title;
    private SimpleStringProperty description;
    private SimpleStringProperty rating;

    public ReviewItem(SimpleStringProperty title, SimpleStringProperty description,
                  SimpleStringProperty rating){
        this.title = title;
        this.description = description;
        this.rating = rating;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getRating() {
        return rating.get();
    }

    public SimpleStringProperty ratingProperty() {
        return rating;
    }
}
