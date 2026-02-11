package projekat.ISA.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.io.Serializable;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column
    private String description;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;
    
    private long views;
    
    @Column(name = "likes")
    private long likes;

    private String thumbnailPath;

    private String videoPath;

    private LocalDateTime timeOfUpload;
    
    private LocalDateTime premiereTime;

    private String geographicalLocation;

    @PrePersist
    protected void onCreate() {
        timeOfUpload = LocalDateTime.now();
        views = 0;
    }
}
