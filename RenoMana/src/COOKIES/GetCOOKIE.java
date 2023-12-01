package COOKIES;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCOOKIE {

    String status;
    String message;
    COOKIES cookies;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GetCOOKIE(@JsonProperty("status") String status, @JsonProperty("message") String message, @JsonProperty("cookie") COOKIES cookies){
        this.status = status;
        this.message = message;
        this.cookies = cookies;
    }

    public GetCOOKIE(){
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public COOKIES getCookies() {
        return cookies;
    }
}
