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
import projekat.ISA.Dto.PostRequest;
import projekat.ISA.Services.PostService;
import projekat.ISA.Services.UserService;
import projekat.ISA.Services.ViewCountService;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ViewCountService viewCountService;

    @GetMapping
    public List<Post> findAll() {
        return postService.findAll();
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
    
    @GetMapping("viewsFrom/{id}")
    public long getViewsById(@PathVariable Long id) {
    	return viewCountService.getTotalViews(id);
    }

    @PostMapping("/upload")
    public Post uploadPost(@ModelAttribute PostRequest postRequest, Principal principal) throws IOException {
        return postService.createPost(postRequest, userService.findByUsername(principal.getName()));
    }

    @GetMapping("/{id}/thumbnail")
    public byte[] getThumbnail(@PathVariable Long id) throws IOException {
        return postService.getThumbnail(id);
    }
}

