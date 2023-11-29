package ProjectMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProjectItems {

    private List<ProjectItem> projects;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProjectItems(@JsonProperty("data") List<ProjectItem> projects){
        this.projects = projects;
    }
    public ProjectItems(){
    }

    public List<ProjectItem> getprojects() {
        return projects;
    }
}
