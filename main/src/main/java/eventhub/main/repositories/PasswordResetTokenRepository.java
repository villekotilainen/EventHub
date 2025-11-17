package eventhub.main.repositories;

import eventhub.main.domain.PasswordResetToken;
import eventhub.main.domain.EHUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByEhUser(EHUser ehUser);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    @Modifying
    @Transactional
    void deleteByEhUser(EHUser ehUser);
    
    @Query("SELECT COUNT(p) > 0 FROM PasswordResetToken p WHERE p.ehUser = :user AND p.used = false AND p.expiryDate > :now")
    boolean hasValidTokenForUser(@Param("user") EHUser user, @Param("now") LocalDateTime now);
}