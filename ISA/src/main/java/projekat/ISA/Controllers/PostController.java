package projekat.ISA.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import projekat.ISA.Domain.Post;
import projekat.ISA.Dto.PostRequest;
import projekat.ISA.Services.PostService;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> findAll() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Post> findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws IOException {
        postService.deleteById(id);
    }

    // Upload new post
    @PostMapping("/upload")
    public Post uploadPost(@ModelAttribute PostRequest postRequest) throws IOException {

        return postService.createPost(
                postRequest.getVideo(),
                postRequest.getThumbnail(),
                postRequest.getTitle(),
                postRequest.getDescription(),
                postRequest.getTags(),
                postRequest.getGeographicalLocation()
        );
    }

    // Get thumbnail for a post
    @GetMapping("/{id}/thumbnail")
    public byte[] getThumbnail(@PathVariable Long id) throws IOException {
        return postService.getThumbnail(id);
    }
}

