package projekat.ISA.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekat.ISA.Domain.ViewCount;

public interface ViewCountRepository extends JpaRepository<ViewCount, Long> {
}