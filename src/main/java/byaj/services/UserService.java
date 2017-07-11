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

    public void followUser(User user){
        Collection<User> following=user.getFollowing();
        following.add(userRepository.findOneByUsername(principal.getName()));
        user.setFollowing(following);
        Collection<User> followed=userRepository.findByUsername(principal.getName()).getFollowed();
        followed.add(user);
        userRepository.findByUsername(principal.getName()).setFollowed(followed);
    }
    public void unfollowUser(User user){
        Collection<User> unfollowing;

            if( userRepository.findByUsername(principal.getName()).getFollowing().contains(user)){
                unfollowing=userRepository.findByUsername(principal.getName()).getFollowing();
                unfollowing.remove(user);
                userRepository.findByUsername(principal.getName()).setFollowing(unfollowing);
            }
        Collection<User> unfollowed;
            if (user.getFollowed().contains(userRepository.findByUsername(principal.getName()))){
                unfollowed=user.getFollowed();
                unfollowed.remove(userRepository.findByUsername(principal.getName()));
                user.setFollowed(unfollowed);
            }

    }
}
