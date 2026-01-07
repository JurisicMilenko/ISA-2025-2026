package projekat.ISA.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    // Path to thumbnail image on the server
    private String thumbnailPath;

    // Path to video file on the server
    private String videoPath;

    private LocalDateTime timeOfUpload;

    private String geographicalLocation;

    @PrePersist
    protected void onCreate() {
        timeOfUpload = LocalDateTime.now();
    }
}
