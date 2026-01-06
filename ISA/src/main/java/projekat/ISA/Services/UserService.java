package projekat.ISA.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import projekat.ISA.Domain.User;
import projekat.ISA.Dto.UserRequest;
import projekat.ISA.Repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public Optional<User> findById(Long id){
		return userRepository.findById(id);
	}
	
	public User save(UserRequest userRequest) {
		User u = new User();
		u.setUsername(userRequest.getUsername());
		
		// pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
		// treba voditi racuna da se koristi isti password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
		u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		
		u.setName(userRequest.getName());
		u.setSurname(userRequest.getSurname());
		u.setEmail(userRequest.getEmail());
		u.setAddress(userRequest.getAddress());
		
		return this.userRepository.save(u);
	}
	
	public void deleteById(Long Id) {
		userRepository.deleteById(Id);
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
