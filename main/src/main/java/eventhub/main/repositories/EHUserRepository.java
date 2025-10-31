package eventhub.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.EHUser;

@Repository
public interface EHUserRepository extends JpaRepository<EHUser, Long> {
    
    Optional<EHUser> findByUsername(String username);
    
    Optional<EHUser> findByEmail(String email);
    
    @Query("SELECT u FROM EHUser u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<EHUser> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}
