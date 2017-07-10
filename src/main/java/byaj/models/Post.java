package byaj.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by student on 6/27/17.
 */
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@NotNull
    //@Min(1)
    private int postID;

    private String postName;

    private String postRating;
    @Column(columnDefinition="integer default -1")
    private int postUser;


    public int getPostID() {
        return postID;
    }

    /*public void setMateID(int postID) {
        this.postID = postID;
    }*/
    public String getPostName() {
        return postName;
    }

    public void setPostName (String postName) {
        this.postName = postName;
    }

    public int getPostUser() {
        return postUser;
    }

    public void setPostUser (int postUser) {
        this.postUser = postUser;
    }
}
