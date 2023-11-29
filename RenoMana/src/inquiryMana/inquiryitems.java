package inquiryMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class inquiryitems {

    private List<inquiryitem> inquiries;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public inquiryitems(@JsonProperty("inq") List<inquiryitem> inquiries){
        this.inquiries = inquiries;
    }
    public inquiryitems(){
    }

    public List<inquiryitem> getinquiries() {
        return inquiries;
    }
}
