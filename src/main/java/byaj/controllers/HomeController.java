package byaj.controllers;

import byaj.models.*;
import byaj.repositories.*;
import byaj.validators.UserValidator;
import byaj.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.utils.ObjectUtils;

import byaj.models.Photo;
import byaj.configs.CloudinaryConfig;

import javax.validation.Valid;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by student on 7/10/17.
 */
@Controller
public class HomeController {
	
	@Autowired
    private CloudinaryConfig cloudc;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private byaj.services.UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private FollowRepository followRepository;
    
    @Autowired
    private PhotoRepository photoRepository;

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
        model.addAttribute("post", new Post());
        model.addAttribute("follow", new Follow());
        model.addAttribute("posts", postRepository.findAllByOrderByPostDateDesc());
        return "postresults2";
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
    
    public UserValidator getUserValidator() {
        return userValidator;
    }

    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }


    @GetMapping("/post")
    public String newPost(Model model, Principal principal) {
        model.addAttribute("search", new Search());
        model.addAttribute("post", new Post());
        model.addAttribute("posts", postRepository.findAllByPostUserOrderByPostDateDesc(userRepository.findByUsername(principal.getName()).getId()));
        model.addAttribute("follow", new Follow());
        return "post2";
    }

    @PostMapping(path = "/post")
    public String processPost(@Valid Post post, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("post");
            return "redirect:/post";
        }
        
       
        post.setPostUser(userRepository.findByUsername(principal.getName()).getId());
        post.setPostAuthor(userRepository.findByUsername(principal.getName()).getUsername());
        postRepository.save(post);
        return "redirect:/post";
        

    }
    @PostMapping("/profile/picture")
    public String singleImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,Principal p, Model model){
        model.addAttribute("search", new Search());
        String url = "";
        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message","Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

            model.addAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
            
            String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
            
            User user = userRepository.findByEmail(p.getName());
            
            Photo photo = new Photo();
            photo.setImage(cloudc.createUrl(filename, 150, 150,"crop", "fit"));
            photo.setFileName(filename);
            photo.setCreatedAt(new Date());
            photo.setUser(user);
            photoRepository.save(photo);
            
            user.setprofilePic(photo);
            userRepository.save(user);
            
            model.addAttribute("imageurl", cloudc.createautoUrl(filename, 150, 150));
            model.addAttribute("user", user);
            model.addAttribute("imagename", filename);
            
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("message", "Sorry I can't upload that!");
        }
        return "tweet";
    }

    @PostMapping("/search")
    public String searchForResumes(@Valid Search search, BindingResult bindingResult, Principal principal, Model model){
       
    	if (bindingResult.hasErrors()) {
            System.out.println("search");
            return "redirect:/";
        }
        model.addAttribute("search", new Search());
        model.addAttribute("profileBuilder", new ProfileBuilder());
        model.addAttribute("postBuilder", new PostBuilder());
        search.setSearchUser(userRepository.findByUsername(principal.getName()).getId());
        searchRepository.save(search);
        if(search.getSearchType().toLowerCase().equals("username")){
            model.addAttribute("posts", postRepository.findAllByPostAuthorOrderByPostDateDesc(search.getSearchValue()));
            model.addAttribute("follow", new Follow());
            return "postresults2";
        }
       
        return "redirect:/";
    }
    
    @GetMapping("/upload")
    public String uploadForm(Model model){
        model.addAttribute("search", new Search());
        return "upload";
    }
    @PostMapping("/upload")
    public String singleImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,String sizedimageurl, String imagename, Model model, Principal principal){
        model.addAttribute("search", new Search());
        if (file.isEmpty()){
            model.addAttribute("message","Please select a file to upload");
            return "upload";
        }
        try {
            
        
            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

            String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
            
            model.addAttribute("imageurl", uploadResult.get("url"));
            model.addAttribute("imagename", filename);
                    
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("message", "Sorry I can't upload that!");
        }
        

        return "upload";
    }
    
    @RequestMapping("/filter")
    public String filter( String imagename, Principal principal, Model model, int width, int height, String action,String filter){
    	 model.addAttribute("search", new Search());
    	if(width==0 ){
    		width=250;
    	}
    	if(height==0){
    		height=250;
    	}
    	
    	
    	String sizedimageurl=cloudc.createUrl(imagename, width, height, action, filter);
    	Photo photo = new Photo();
    	photo.setImage(sizedimageurl);
        photo.setTitle(imagename);
        photo.setCreatedAt(new Date());
        User user = userRepository.findByEmail(principal.getName());
        photo.setUser(user);
        photoRepository.save(photo);
        
        model.addAttribute("sizedimageurl", sizedimageurl);
    	model.addAttribute("imagename", imagename);
        System.out.println(sizedimageurl);
    	return "upload";
    }
    
   	/*@GetMapping("/uploadProfile")
    public String uploadProfileForm(){
        return "uploadProfile";
    }

    @PostMapping("/uploadProfile")
    public String profileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,Principal p, Model model){

        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message","Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

            model.addAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
            
            String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
            
            User user = userRepository.findByEmail(p.getName());
            
            Photo photo = new Photo();
            photo.setImage(cloudc.createUrl(filename,40,40, "fit"));
            photo.setFileName(filename);
            photo.setCreatedAt(new Date());
            photo.setUser(user);
            photoRepository.save(photo);
            
            user.setProfilePicture(photo);
            userRepository.save(user);
            
            model.addAttribute("imageUrl", cloudc.createUrl(filename,40,40, "fit"));
            model.addAttribute("user", user);
            model.addAttribute("imagename", filename);
            
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("message", "Sorry I can't upload that!");
        }
        return "postResults";
    }
    
    /*@GetMapping("/upload")
    public String uploadForm(Model model){
        model.addAttribute("search", new Search());
        model.addAttribute("photo", new Photo());
        return "upload";
    }
    public String singleImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,  @ModelAttribute Photo photo, Model model){

        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message","Please select a file to upload");
            return "redirect:uploadStatus";
        }
            try {
                Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));

                model.addAttribute("message",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");
                model.addAttribute("imageurl", uploadResult.get("url"));
                String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
                model.addAttribute("sizedimageurl", cloudc.createUrl(filename,100,150, "fit"));
                model.addAttribute("cropped4040imageurl", cloudc.createCroppedSepia(filename,40,40, "crop", "sepia"));
                
                //model.addAttribute("imageurl", uploadResult.get("url"));
	            //model.addAttribute("imagename", filename);
	            //photoRepository.save(photo);
            } catch (IOException e){
                e.printStackTrace();
                model.addAttribute("message", "Sorry I can't upload that!");
            }
            return "upload";
        }*/
    //Follow Post Mapping*/
    @PostMapping("/follow")
    public String changeFollowStatus(@Valid Follow follow, BindingResult bindingResult, Principal principal, Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/";
        }
        if(follow.getFollowType().toLowerCase().equals("follow")){
            userService.followUser(userRepository.findByUsername(follow.getFollowValue()), userRepository.findByUsername(principal.getName()));
        }
        if(follow.getFollowType().toLowerCase().equals("unfollow")){
            userService.unfollowUser(userRepository.findByUsername(follow.getFollowValue()), userRepository.findByUsername(principal.getName()));
        }
        follow.setFollowUser(userRepository.findByUsername(principal.getName()).getId());
        follow.setFollowAuthor(userRepository.findByUsername(principal.getName()).getUsername());
        followRepository.save(follow);
        System.out.println(userRepository.findByUsername(principal.getName()).getUsername());
        System.out.println(userRepository.findByUsername(follow.getFollowValue()).getUsername());
        return "redirect:/followres";
    }

    @GetMapping("/following")
    public String viewFollowing(Model model, Principal principal){
        model.addAttribute("search", new Search());
        model.addAttribute("profileBuilder", new ProfileBuilder());
        model.addAttribute("userPrincipal", userRepository.findByUsername(principal.getName()));
        model.addAttribute("users", userRepository.findByUsername(principal.getName()).getFollowing());
        model.addAttribute("follow", new Follow());
        return "userresults2";
    }
    @GetMapping("/followers")
    public String viewFollowers(Model model, Principal principal){
        model.addAttribute("search", new Search());
        model.addAttribute("profileBuilder", new ProfileBuilder());
        model.addAttribute("userPrincipal", userRepository.findByUsername(principal.getName()));
        model.addAttribute("users", userRepository.findByUsername(principal.getName()).getFollowed());
        model.addAttribute("follow", new Follow());
        return "userresults2";
    }

   /* @PostMapping("/like")
    public String changeLikeStatus(@Valid Like like, BindingResult bindingResult, Principal principal, Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/";
        }
        if(like.getLikeType().toLowerCase().equals("like")){
            userService.likePost(postRepository.findByPostID(Integer.parseInt(like.getLikeValue())), userRepository.findByUsername(principal.getName()));
        }
        if(like.getLikeType().toLowerCase().equals("unlike")){
            userService.unlikePost(postRepository.findByPostID(Integer.parseInt(like.getLikeValue())), userRepository.findByUsername(principal.getName()));
        }
        like.setLikeUser(userRepository.findByUsername(principal.getName()).getId());
        like.setLikeAuthor(userRepository.findByUsername(principal.getName()).getUsername());
        likeRepository.save(like);
        return "redirect:/";
    }*/
    @GetMapping("/users")
    public String viewUsers(Model model, Principal principal){
        model.addAttribute("search", new Search());
        model.addAttribute("profileBuilder", new ProfileBuilder());
        model.addAttribute("users", userRepository.findAllByOrderByUserDateDesc());
        model.addAttribute("follow", new Follow());
        model.addAttribute("userPrincipal", userRepository.findByUsername(principal.getName()));
        return "userresults2";
    }
    @PostMapping("/generate/posts")
    public String generatePosts(@Valid ProfileBuilder profileBuilder, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "redirect:/";
        }
        model.addAttribute("search", new Search());
        model.addAttribute("posts", postRepository.findAllByPostAuthorOrderByPostDateDesc(profileBuilder.getProfileBuilderValue()));
        model.addAttribute("follow", new Follow());
        return "postresults2";
    }

}
