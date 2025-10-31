package eventhub.main.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eventhub.main.domain.EHUser;
import eventhub.main.domain.UserRole;
import eventhub.main.repositories.EHUserRepository;
import eventhub.main.repositories.UserRoleRepository;

@Service
public class UserService {
    
    @Autowired
    private EHUserRepository userRepository;
    
    @Autowired
    private UserRoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public EHUser registerUser(EHUser user) {
        // Encode the password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // Assign default role (USER) if no role is set
        if (user.getRole() == null) {
            Optional<UserRole> defaultRole = roleRepository.findByRoleName("USER");
            if (defaultRole.isPresent()) {
                user.setRole(defaultRole.get());
            }
        }
        
        return userRepository.save(user);
    }
    
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    public Optional<EHUser> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }
}