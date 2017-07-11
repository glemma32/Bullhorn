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
        Collection<User> followed=userRepository.findOneByUsername(principal.getName()).getFollowed();
        followed.add(user);
        userRepository.findOneByUsername(principal.getName()).setFollowed(followed);
    }
    public void unfollowUser(User user){
        Collection<User> unfollowing;

            if( userRepository.findOneByUsername(principal.getName()).getFollowing().contains(user)){
                unfollowing=userRepository.findOneByUsername(principal.getName()).getFollowing();
                unfollowing.remove(user);
                userRepository.findOneByUsername(principal.getName()).setFollowing(unfollowing);
            }
        Collection<User> unfollowed;
            if (user.getFollowed().contains(userRepository.findOneByUsername(principal.getName()))){
                unfollowed=user.getFollowed();
                unfollowed.remove(userRepository.findOneByUsername(principal.getName()));
                user.setFollowed(unfollowed);
            }

    }
}
