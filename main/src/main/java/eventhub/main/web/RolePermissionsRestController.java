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

import eventhub.main.domain.RolePermissions;
import eventhub.main.repositories.RolePermissionsRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rolepermissions")
public class RolePermissionsRestController {

    @Autowired
    private RolePermissionsRepository rolePermissionsRepository;
    
    // GET all role permissions
    @GetMapping
    public List<RolePermissions> getAllRolePermissions() {
        return rolePermissionsRepository.findAll();
    }
    
    // GET role permissions by ID
    @GetMapping("/{id}")
    public ResponseEntity<RolePermissions> getRolePermissionsById(@PathVariable Long id) {
        Optional<RolePermissions> rolePermissions = rolePermissionsRepository.findById(id);
        if (rolePermissions.isPresent()) {
            return ResponseEntity.ok(rolePermissions.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // POST - Create new role permissions
    @PostMapping
    public ResponseEntity<RolePermissions> createRolePermissions(@Valid @RequestBody RolePermissions rolePermissions) {
        try {
            RolePermissions savedRolePermissions = rolePermissionsRepository.save(rolePermissions);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRolePermissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT - Update existing role permissions
    @PutMapping("/{id}")
    public ResponseEntity<RolePermissions> updateRolePermissions(@PathVariable Long id, @Valid @RequestBody RolePermissions rolePermissions) {
        Optional<RolePermissions> existingRolePermissions = rolePermissionsRepository.findById(id);
        if (existingRolePermissions.isPresent()) {
            rolePermissions.setId(id);
            RolePermissions updatedRolePermissions = rolePermissionsRepository.save(rolePermissions);
            return ResponseEntity.ok(updatedRolePermissions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE role permissions by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRolePermissions(@PathVariable Long id) {
        Optional<RolePermissions> rolePermissions = rolePermissionsRepository.findById(id);
        if (rolePermissions.isPresent()) {
            rolePermissionsRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}

