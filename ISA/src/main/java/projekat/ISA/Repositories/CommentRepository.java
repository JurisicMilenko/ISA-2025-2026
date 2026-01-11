package projekat.ISA.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekat.ISA.Domain.Comment;
import projekat.ISA.Domain.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    Page<Comment> findByPostOrderByTimeOfUploadDesc(Comment comment, Pageable pageable);
    
}
