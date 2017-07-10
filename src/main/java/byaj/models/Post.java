package byaj.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private String postAuthor;
    @Column(columnDefinition="integer default -1")
    private int postUser;

    private Date postDate=new Date();


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
    
    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor (String postAuthor) {
        this.postAuthor = postAuthor;
    }
    
    public int getPostUser() {
        return postUser;
    }

    public void setPostUser (int postUser) {
        this.postUser = postUser;
    }

    public Date getPostDate() {
        return postDate;
    }


    public String getFormatDate(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE MMMMM dd, yyyy hh:mm a zzzz", Locale.US);
        return format.format(postDate);
    }
}
