package projekat.ISA.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "view_count")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewCount {

    @Id
    private Long postId;

    private Long count;
}
