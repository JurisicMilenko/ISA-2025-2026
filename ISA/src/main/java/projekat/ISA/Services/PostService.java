package projekat.ISA.Services;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.User;
import projekat.ISA.Dto.PostRequest;
import projekat.ISA.Repositories.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final Path videoStorage = Paths.get("uploads/videos/");
    private final Path thumbnailStorage = Paths.get("uploads/thumbnails/");

    public PostService(PostRepository postRepository) throws IOException {
        this.postRepository = postRepository;

        if (!Files.exists(videoStorage)) {
            Files.createDirectories(videoStorage);
        }
        if (!Files.exists(thumbnailStorage)) {
            Files.createDirectories(thumbnailStorage);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Post createPost(PostRequest postRequest, User currentUser) throws IOException {

        String videoFileName = UUID.randomUUID() + postRequest.getVideo().getOriginalFilename();
        String thumbnailFileName = UUID.randomUUID() + postRequest.getThumbnail().getOriginalFilename();

        try {
            Files.copy(postRequest.getVideo().getInputStream(), videoStorage.resolve(videoFileName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(postRequest.getThumbnail().getInputStream(), thumbnailStorage.resolve(thumbnailFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Video or thumbnail upload failed", e);
        }

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .author(currentUser)
                .description(postRequest.getDescription())
                .tags(postRequest.getTags())
                .likes(0)
                .videoPath(videoStorage.resolve(videoFileName).toString())
                .thumbnailPath(thumbnailStorage.resolve(thumbnailFileName).toString())
                .geographicalLocation(postRequest.getGeographicalLocation())
                .build();

        return postRepository.save(post);
    }


    public List<Post> findAll() {
        return postRepository.findAllByOrderByTimeOfUploadDesc();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
    
    public Optional<Post> likePost(Long id) {
    	Optional<Post> post = postRepository.findById(id);
    	post.get().setLikes(post.get().getLikes()+1);
    	postRepository.save(post.get());
    	return post;
    }

    @Cacheable(value = "postThumbnails", key = "#postId")
    public byte[] getThumbnail(Long postId) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Path path = Paths.get(post.getThumbnailPath());
        byte[] bytes = Files.readAllBytes(path);
        return bytes;
    }
    
    public List<Post> findByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public boolean exists(Long postId) {
        return postRepository.existsById(postId);
    }
}
