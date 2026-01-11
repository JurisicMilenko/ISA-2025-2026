package projekat.ISA.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projekat.ISA.Domain.Comment;
import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.User;
import projekat.ISA.Dto.CommentDTO;
import projekat.ISA.Dto.CommentRequest;
import projekat.ISA.Services.CommentService;
import projekat.ISA.Services.PostService;
import projekat.ISA.Services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    public CommentController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping
    public Comment createComment(@RequestBody CommentRequest request, Principal principal) { 
    	return commentService.addComment(request, userService.findByUsername(principal.getName())); 
    }

    @GetMapping("/post/{postId}")
    public Page<Comment> getCommentsForPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Post post = postService.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Pageable pageable = PageRequest.of(page, size);
        return commentService.getCommentsForPost(post, pageable);
    }
    
    @GetMapping("/postDTO/{postId}")
    public Page<CommentDTO> getCommentsDTOForPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Post post = postService.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getCommentsDTOForPost(post, pageable);
    }
}
