package projekat.ISA.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {
	
    private Long postId;
    private String text;
    
}

