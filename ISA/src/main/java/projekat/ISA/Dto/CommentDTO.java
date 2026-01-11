package projekat.ISA.Dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projekat.ISA.Domain.Post;
import projekat.ISA.Domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

	private Long id;
	
    private String authorName;
	
    private Post post;
	
	private String text;
	
	private LocalDateTime timeOfUpload;
}
