package COOKIES;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class COOKIES {

    private String username;
    private String title;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public COOKIES(@JsonProperty("username") String username, @JsonProperty("title")String title){
        this.username = username;
        this.title = title;
    }

    public COOKIES(){
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }
}
