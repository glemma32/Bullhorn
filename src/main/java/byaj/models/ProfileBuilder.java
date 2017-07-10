package byaj.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by student on 6/27/17.
 */
@Entity
public class ProfileBuilder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@NotNull
    //@Min(1)
    private int profileBuilderID;

    private String profileBuilderValue;

    @Column(columnDefinition="integer default -1")
    private int profileBuilderRes;


    public int getProfileBuilderID() {
        return profileBuilderID;
    }

    /*public void setMateID(int profileBuilderID) {
        this.profileBuilderID = profileBuilderID;
    }*/
    public String getProfileBuilderValue() {
        return profileBuilderValue;
    }

    public void setProfileBuilderValue (String profileBuilderValue) {
        this.profileBuilderValue = profileBuilderValue;
    }

    public int getProfileBuilderRes() {
        return profileBuilderRes;
    }

    public void setProfileBuilderRes (int profileBuilderRes) {
        this.profileBuilderRes = profileBuilderRes;
    }
}
