package projekat.ISA.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projekat.ISA.Domain.Comment;
import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.User;
import projekat.ISA.Dto.CommentRequest;
import projekat.ISA.Repositories.CommentRepository;

import java.util.*;
import java.util.concurrent.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public Comment addComment(CommentRequest commentRequest, User currentUser) {
        Post post = postService.findById(commentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + commentRequest.getPostId()));

        Comment comment = Comment.builder()
                .post(post)
                .author(currentUser)
                .text(commentRequest.getText())
                .build();

        return commentRepository.save(comment);
    }

    public Page<Comment> getCommentsForPost(Post post, Pageable pageable) {
        return commentRepository.findByPostOrderByTimeOfUploadDesc(post, pageable);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}

