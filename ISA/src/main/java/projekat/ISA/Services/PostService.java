package projekat.ISA.Services;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import projekat.ISA.Domain.Post;
import projekat.ISA.Repositories.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final Map<Long, byte[]> thumbnailCache = new ConcurrentHashMap<>();

    private final Path videoStorage = Paths.get("uploads/videos/");
    private final Path thumbnailStorage = Paths.get("uploads/thumbnails/");

    public PostService(PostRepository postRepository) throws IOException {
        this.postRepository = postRepository;

        // Ensure directories exist
        if (!Files.exists(videoStorage)) {
            Files.createDirectories(videoStorage);
        }
        if (!Files.exists(thumbnailStorage)) {
            Files.createDirectories(thumbnailStorage);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Post createPost(MultipartFile videoFile, MultipartFile thumbnailFile,
                           String title, String description, List<String> tags,
                           String geographicalLocation) throws IOException {

        // Generate unique file names to avoid collisions
        String videoFileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        String thumbnailFileName = UUID.randomUUID() + "_" + thumbnailFile.getOriginalFilename();

        // Save video and thumbnail to disk
        try {
            Files.copy(videoFile.getInputStream(), videoStorage.resolve(videoFileName),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(thumbnailFile.getInputStream(), thumbnailStorage.resolve(thumbnailFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Video or thumbnail upload failed", e);
        }

        // Create Post entity
        Post post = Post.builder()
                .title(title)
                .description(description)
                .tags(tags)
                .geographicalLocation(geographicalLocation)
                .videoPath(videoStorage.resolve(videoFileName).toString())
                .thumbnailPath(thumbnailStorage.resolve(thumbnailFileName).toString())
                .build();

        // Save to DB
        Post savedPost = postRepository.save(post);

        // Cache thumbnail in memory
        thumbnailCache.put(savedPost.getId(), thumbnailFile.getBytes());

        return savedPost;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
    
    public void deleteById(Long id) throws IOException {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();

            try {
                Files.deleteIfExists(Paths.get(post.getVideoPath()));
                Files.deleteIfExists(Paths.get(post.getThumbnailPath()));
            } catch (IOException e) {
                throw new IOException("Failed to delete video or thumbnail", e);
            }

            // Remove from cache
            thumbnailCache.remove(id);

            // Delete from database
            postRepository.deleteById(id);
        }
    }

    public byte[] getThumbnail(Long postId) throws IOException {
        byte[] cached = thumbnailCache.get(postId);
        if (cached != null) return cached;

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Path path = Paths.get(post.getThumbnailPath());
        byte[] bytes = Files.readAllBytes(path);
        thumbnailCache.put(postId, bytes);
        return bytes;
    }
    
    public List<Post> findByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Post> findByTag(String tag) {
        return postRepository.findByTags(tag);
    }
}
