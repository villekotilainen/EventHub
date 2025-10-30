package eventhub.main.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eventhub.main.domain.EHUser;
import eventhub.main.repositories.EHUserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class EHUserRestController {

    @Autowired
    private EHUserRepository ehUserRepository;
    
    // GET all users
    @GetMapping
    public List<EHUser> getAllUsers() {
        return ehUserRepository.findAll();
    }
    
    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<EHUser> getUserById(@PathVariable Long id) {
        Optional<EHUser> user = ehUserRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // POST - Create new user
    @PostMapping
    public ResponseEntity<EHUser> createUser(@Valid @RequestBody EHUser ehUser) {
        try {
            EHUser savedUser = ehUserRepository.save(ehUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT - Update existing user
    @PutMapping("/{id}")
    public ResponseEntity<EHUser> updateUser(@PathVariable Long id, @Valid @RequestBody EHUser ehUser) {
        Optional<EHUser> existingUser = ehUserRepository.findById(id);
        if (existingUser.isPresent()) {
            ehUser.setId(id); 
            EHUser updatedUser = ehUserRepository.save(ehUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<EHUser> user = ehUserRepository.findById(id);
        if (user.isPresent()) {
            ehUserRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
