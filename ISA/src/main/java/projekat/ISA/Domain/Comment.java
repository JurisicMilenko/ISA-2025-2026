package projekat.ISA.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
	
	@Column(length = 3000)
	private String text;
	
	private LocalDateTime timeOfUpload;
	
	@PrePersist
    protected void onCreate() {
        timeOfUpload = LocalDateTime.now();
    }
	
}
