package vnua.fita.tthieu.springboot.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import vnua.fita.tthieu.springboot.entity.User;
import vnua.fita.tthieu.springboot.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
    	return userRepository.findByEmail(email);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User updateUser(Long id, User newUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setFullname(newUser.getFullname());
        existing.setEmail(newUser.getEmail());
        existing.setPhone(newUser.getPhone());
        existing.setRole(newUser.getRole());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
