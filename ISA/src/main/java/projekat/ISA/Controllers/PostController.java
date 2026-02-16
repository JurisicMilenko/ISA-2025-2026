package projekat.ISA.Controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.View;
import projekat.ISA.Dto.PostRequest;
import projekat.ISA.Services.PostService;
import projekat.ISA.Services.UploadEventService;
import projekat.ISA.Services.UserService;
import projekat.ISA.Services.ViewCountService;
import projekat.ISA.Services.ViewService;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewCountService viewCountService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private UploadEventService uploadEventService;

    @GetMapping
    public List<Post> findAll() {
        return postService.findAll();
    }
    
    @GetMapping("/popular")
    public List<Post> findTop3() {
        return viewService.getTop3();
    }

    @GetMapping("/exists/{id}")
    public boolean exists(@PathVariable Long id) {
        return postService.exists(id);
    }
    
    @GetMapping("/{id}")
    public Optional<Post> findById(@PathVariable Long id) {
        return postService.findById(id);
    }
    
    @GetMapping("like/{id}")
    public Optional<Post> likePost(@PathVariable Long id) {
        return postService.likePost(id);
    }
    
    @GetMapping("view/{id}")
    public ResponseEntity<Void> viewPost(@PathVariable Long id) {
        if(!viewCountService.registerView(id))
        	return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("viewSingular/{id}")
    public View viewPostSingular(@PathVariable Long id) {
    	return viewService.save(id);
    }
    
    @GetMapping("viewsFrom/{id}")
    public long getViewsById(@PathVariable Long id) {
    	return viewCountService.getTotalViews(id);
    }

    @PostMapping("/upload")
    public Post uploadPost(@ModelAttribute PostRequest postRequest, Principal principal) throws Exception {
    	Post post = postService.createPost(postRequest, userService.findByUsername(principal.getName()));
    	uploadEventService.sendProtobuf(post.getTitle(), post.getAuthor().getUsername(), post.getDescription(), post.getTimeOfUpload());
        return post;
    }

    @GetMapping("/{id}/thumbnail")
    public byte[] getThumbnail(@PathVariable Long id) throws IOException {
        return postService.getThumbnail(id);
    }
}

