package projekat.ISA.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projekat.ISA.Domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmail(String email);
	User findByUsername(String username);
}
