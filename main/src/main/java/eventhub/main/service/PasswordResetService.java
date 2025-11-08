package eventhub.main.service;

import eventhub.main.domain.EHUser;
import eventhub.main.domain.PasswordResetToken;
import eventhub.main.repositories.EHUserRepository;
import eventhub.main.repositories.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {
    
    @Autowired
    private EHUserRepository ehUserRepository;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean createPasswordResetTokenForUser(String email, String baseUrl) {
        try {
            // Find user by email
            Optional<EHUser> userOptional = ehUserRepository.findByEmail(email);
            if (!userOptional.isPresent()) {
                // Don't reveal if email exists or not for security reasons
                return true;
            }
            
            EHUser user = userOptional.get();
            
            passwordResetTokenRepository.deleteByEhUser(user);
            
            // Generate new token
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(passwordResetToken);
            
            // Send email
            sendPasswordResetEmail(user, token, baseUrl);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Optional<EHUser> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
        
        if (!tokenOptional.isPresent()) {
            return Optional.empty();
        }
        
        PasswordResetToken resetToken = tokenOptional.get();
        
        if (!resetToken.isValid()) {
            return Optional.empty();
        }
        
        return Optional.of(resetToken.getEhUser());
    }
    
    public boolean resetPassword(String token, String newPassword) {
        try {
            Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
            
            if (!tokenOptional.isPresent()) {
                return false;
            }
            
            PasswordResetToken resetToken = tokenOptional.get();
            
            if (!resetToken.isValid()) {
                return false;
            }
            
            // Update user password
            EHUser user = resetToken.getEhUser();
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            ehUserRepository.save(user);
            
            // Mark token as used
            resetToken.setUsed(true);
            passwordResetTokenRepository.save(resetToken);
            
            // Delete all tokens for this user
            passwordResetTokenRepository.deleteByEhUser(user);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void sendPasswordResetEmail(EHUser user, String token, String baseUrl) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(user.getEmail());
            email.setSubject("EventHub - Password Reset Request");
            email.setFrom("eventhubdemoemail@gmail.com");
            
            String resetUrl = baseUrl + "/reset-password?token=" + token;
            
            String message = "Hello " + user.getUsername() + ",\n\n" +
                    "You have requested to reset your password for your EventHub account.\n\n" +
                    "Please click on the link below to reset your password:\n" +
                    resetUrl + "\n\n" +
                    "This link will expire in 1 hour for security reasons.\n\n" +
                    "If you did not request this password reset, please ignore this email and your password will remain unchanged.\n\n" +
                    "Best regards,\n" +
                    "The EventHub Team";
            
            email.setText(message);
            mailSender.send(email);
            
            System.out.println("Password reset email sent successfully to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    public void cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
    
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
}