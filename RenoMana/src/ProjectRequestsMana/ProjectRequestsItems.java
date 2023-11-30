package ProjectRequestsMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProjectRequestsItems {

    private List<ProjectRequestsItem> projects;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProjectRequestsItems(@JsonProperty("data") List<ProjectRequestsItem> projects){
        this.projects = projects;
    }
    public ProjectRequestsItems(){
    }

    public List<ProjectRequestsItem> getprojects() {
        return projects;
    }
}
