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

import eventhub.main.domain.UserRole;
import eventhub.main.repositories.UserRoleRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/userroles")
public class UserRoleRestController {

    @Autowired
    private UserRoleRepository userRoleRepository;
    
    // GET all user roles
    @GetMapping
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }
    
    // GET user role by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserRole> getUserRoleById(@PathVariable Long id) {
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        if (userRole.isPresent()) {
            return ResponseEntity.ok(userRole.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // POST - Create new user role
    @PostMapping
    public ResponseEntity<UserRole> createUserRole(@Valid @RequestBody UserRole userRole) {
        try {
            UserRole savedUserRole = userRoleRepository.save(userRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUserRole);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT - Update existing user role
    @PutMapping("/{id}")
    public ResponseEntity<UserRole> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRole userRole) {
        Optional<UserRole> existingUserRole = userRoleRepository.findById(id);
        if (existingUserRole.isPresent()) {
            userRole.setId(id); 
            UserRole updatedUserRole = userRoleRepository.save(userRole);
            return ResponseEntity.ok(updatedUserRole);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE user role by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        Optional<UserRole> userRole = userRoleRepository.findById(id);
        if (userRole.isPresent()) {
            userRoleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
