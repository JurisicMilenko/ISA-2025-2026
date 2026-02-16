package projekat.ISA.Domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ETL {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDateTime runDate;
	
	@OneToMany(mappedBy="etl", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ETLPair> scores = new ArrayList<>();
	
}
