package bjaj.controllers;

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
    private byaj.repositories.UserRepository userRepository;

    @Autowired
    private byaj.repositories.RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private byaj.repositories.SearchRepository searchRepository;

    @Autowired
    private ProfileBuilderRepository profileBuilderRepository;

    @Autowired
    private byaj.repositories.PostBuilderRepository postBuilderRepository;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("search", new byaj.models.Search());
        return "base2";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") byaj.models.User user, BindingResult result, Model model){
        model.addAttribute("search", new byaj.models.Search());

        model.addAttribute("user", user);
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "register2";
        } else {
            if (user.getRoleSettings().toUpperCase().equals("ADMIN")) {
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
            }

        }
        return "redirect:/";

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("search", new byaj.models.Search());
        return "login2";
    }
}
