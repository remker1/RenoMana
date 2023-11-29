package reviewMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ReviewItems {

    private List<ReviewItem> reviews;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReviewItems(@JsonProperty("reviews") List<ReviewItem> reviews){
        this.reviews = reviews;
    }
    public ReviewItems(){
    }

    public List<ReviewItem> getReviews() {
        return reviews;
    }
}
