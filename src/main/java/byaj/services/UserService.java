package byaj.services;

import byaj.models.User;
import byaj.repositories.RoleRepository;
import byaj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public int countByEmail(String email) {
        return userRepository.countByEmail(email);
    }
    Principal principal;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveEmployer(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setRoles(Arrays.asList(roleRepository.findByRole("EMPLOYER")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveAdmin(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setRoles(Arrays.asList(roleRepository.findByRole("EMPLOYER")));
        user.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void followUser(User otherUser, User thisUser){
        //this user is following other user
        Collection<User> following=thisUser.getFollowing();
        if(!following.contains(otherUser)) {
            following.add(otherUser);
            thisUser.setFollowing(following);
        }
        //other user is being followed by this user
        Collection<User> followed=otherUser.getFollowed();
        if(followed.contains(thisUser)) {
            followed.add(thisUser);
            otherUser.setFollowed(followed);
        }
        userRepository.save(otherUser);
        userRepository.save(thisUser);
    }
    public void unfollowUser(User otherUser, User thisUser){
        Collection<User> unfollowing;

            if( thisUser.getFollowing().contains(otherUser)){
                unfollowing=thisUser.getFollowing();
                unfollowing.remove(otherUser);
                thisUser.setFollowing(unfollowing);
            }
        Collection<User> unfollowed;
            if (otherUser.getFollowed().contains(thisUser)){
                unfollowed=otherUser.getFollowed();
                unfollowed.remove(thisUser);
                otherUser.setFollowed(unfollowed);
            }
        userRepository.save(otherUser);
        userRepository.save(thisUser);
    }
}
