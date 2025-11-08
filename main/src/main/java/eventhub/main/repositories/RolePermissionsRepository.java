package eventhub.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.RolePermissions;

@Repository
public interface RolePermissionsRepository extends JpaRepository<RolePermissions, Long> {
    
}