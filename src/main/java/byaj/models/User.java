package byaj.models;


import byaj.models.Role;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * Created by student on 6/28/17.
 */
@Entity
@Table(name = "userData")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;
    
    private Photo photos;

    @Column(name = "role_settings")
    private String roleSettings;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "user_date")
    private Date userDate=new Date();

   
   /* @Basic
    //@Column(name="picture_url", columnDefinition="blob default http://res.cloudinary.com/andrewjonesdev/image/upload/c_scale,h_100/v1499894133/profilepic_kos4l4.jpg")
    private String profilePicUrl;*/
    
    @Column(name = "picture_date")
    private Date picDate=new Date();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="likes",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Collection<Post> likes;

    @ManyToMany//(fetch = FetchType.EAGER)
    @JoinTable(name="follow_status", joinColumns = @JoinColumn(name = "following_id"),inverseJoinColumns = @JoinColumn(name = "followed_id"))
    private Collection<User> following;

    @ManyToMany//(fetch = FetchType.EAGER)
    @JoinTable(name="follow_status", joinColumns = @JoinColumn(name = "followed_id"),inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Collection<User> followed;

    public User(String email, String password, String firstName, String lastName, boolean enabled, String username, String picUrl, String picOriginUrl, String picDefaultUrl) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.username = username;
        fullName = firstName + " " + lastName;
        //this.profilePicUrl=picUrl;
        
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", enabled=" + enabled +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        fullName = firstName + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        fullName = firstName + " " + lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public Photo getPhotos() {
		return photos;
	}

	public void setPhotos(Photo photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<User> getFollowing() {
        return following;
    }

    public void setFollowing(Collection<User> following) {
        this.following = following;
    }

    public Collection<User> getFollowed() {
        return followed;
    }

    public void setFollowed(Collection<User> followed) {
        this.followed = followed;
    }

    public String getRoleSettings() {
        return roleSettings;
    }

    public void setRoleSettings(String roleSettings) {
        this.roleSettings = roleSettings;
    }

    public Date getUserDate() {
        return userDate;
    }


    public String getFormatDate(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE MMMMM dd, yyyy hh:mm a zzzz", Locale.US);
        return format.format(userDate);
    }

    public Date getPicDate() {
        return picDate;
    }

    public void setPicDate (){
        this.picDate = new Date();
    }

    public String getFormatPicDate(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE MMMMM dd, yyyy hh:mm a zzzz", Locale.US);
        return format.format(picDate);
    }

    public boolean followedContains(User user){
        if(followed.contains(user)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean followingContains(User user){
        if(following.contains(user)){
            return true;
        }
        else{
            return false;
        }
    }

	/*public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}*/
}
   