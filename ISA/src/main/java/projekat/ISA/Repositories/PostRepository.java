package projekat.ISA.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import projekat.ISA.Domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingIgnoreCase(String title);

    List<Post> findByTags(String tag);
    
    List<Post> findAllByOrderByTimeOfUploadDesc();
}

