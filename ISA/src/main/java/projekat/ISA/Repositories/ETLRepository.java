package projekat.ISA.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import projekat.ISA.Domain.ETL;

public interface ETLRepository  extends JpaRepository<ETL, Long>{
	Optional<ETL> findFirstByOrderByRunDateDesc();
}
