package byaj.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by student on 6/27/17.
 */
@Entity
public class PostBuilder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@NotNull
    //@Min(1)
    private int postBuilderID;

    private String postBuilderValue;

    @Column(columnDefinition="integer default -1")
    private int postBuilderRes;


    public int getPostBuilderID() {
        return postBuilderID;
    }

    /*public void setMateID(int postBuilderID) {
        this.postBuilderID = postBuilderID;
    }*/
    public String getPostBuilderValue() {
        return postBuilderValue;
    }

    public void setPostBuilderValue (String postBuilderValue) {
        this.postBuilderValue = postBuilderValue;
    }

    public int getPostBuilderRes() {
        return postBuilderRes;
    }

    public void setPostBuilderRes (int postBuilderRes) {
        this.postBuilderRes = postBuilderRes;
    }
}
