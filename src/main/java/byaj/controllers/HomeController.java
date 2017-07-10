package byaj.controllers;

import byaj.models.Post;
import byaj.models.Search;
import byaj.models.User;
import byaj.repositories.*;
import byaj.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by student on 7/10/17.
 */
@Controller
public class HomeController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private byaj.services.UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private ProfileBuilderRepository profileBuilderRepository;

    @Autowired
    private PostBuilderRepository postBuilderRepository;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("search", new Search());
        return "base2";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("search", new Search());
        model.addAttribute("user", new User());
        return "register2";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("search", new Search());

        model.addAttribute("user", user);
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "register2";
        } else {
           /* if (user.getRoleSettings().toUpperCase().equals("ADMIN")) {
                //user.setRoles(Arrays.asList(adminRole));
                //userRepository.save(user);
                userService.saveAdmin(user);
            }
            if (user.getRoleSettings().toUpperCase().equals("EMPLOYER")) {
                //user.setRoles(Arrays.asList(employerRole));
                //userRepository.save(user);
                userService.saveEmployer(user);
            }
            if (user.getRoleSettings().toUpperCase().equals("USER")) {
                //user.setRoles(Arrays.asList(userRole));
                //userRepository.save(user);
                userService.saveUser(user);
                model.addAttribute("message", "User Account Successfully Created");
            }*/
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "redirect:/";
    }
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("search", new Search());
        return "login2";
    }

    @GetMapping("/post")
    public String newJob(Model model, Principal principal) {
        model.addAttribute("search", new Search());
        model.addAttribute("post", new Post());
        model.addAttribute("posts", postRepository.findAllByPostUserOrderByPostDateDesc(userRepository.findByUsername(principal.getName()).getId()));
        return "post2";
    }

    @PostMapping(path = "/post")
    public String processJob(@Valid Post post, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println("post");
            return "redirect:/job";
        }
        post.setPostUser(userRepository.findByUsername(principal.getName()).getId());
        post.setPostAuthor(userRepository.findByUsername(principal.getName()).getUsername());
        postRepository.save(post);
        return "redirect:/post";

    }
}
