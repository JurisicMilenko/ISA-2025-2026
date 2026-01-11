package projekat.ISA.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projekat.ISA.Domain.Comment;
import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.User;
import projekat.ISA.Dto.CommentDTO;
import projekat.ISA.Dto.CommentRequest;
import projekat.ISA.Repositories.CommentRepository;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    @Autowired
    private UserService userService;

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
    
    public Page<CommentDTO> getCommentsDTOForPost(Post post, Pageable pageable) {
    	Page<Comment> comments = getCommentsForPost(post, pageable);
    	List<CommentDTO> commentsDTO = new ArrayList<>();
    	for(Comment comment : comments) {
    		String username = comment.getAuthor().getUsername();
    		CommentDTO commentDTO = new CommentDTO(comment.getId(),username,post,comment.getText(),comment.getTimeOfUpload());;
    		commentsDTO.add(commentDTO);
    		System.out.println("Testing CommentDTO, username:"+username);	
    	}
    	Page<CommentDTO> page = new PageImpl<>(commentsDTO, pageable, commentsDTO.size());
        return page;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}

