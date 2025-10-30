package eventhub.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
}