package eventhub.main.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eventhub.main.domain.EHUser;
import eventhub.main.repositories.EHUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EHUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Find user by username or email
        Optional<EHUser> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail);
        
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }
        
        EHUser user = userOptional.get();
        
        // Get user authorities based on role
        Collection<GrantedAuthority> authorities = getAuthorities(user);
        
        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Collection<GrantedAuthority> getAuthorities(EHUser user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add role-based authority
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().toUpperCase()));
        } else {
            // Default role if none assigned
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return authorities;
    }
}