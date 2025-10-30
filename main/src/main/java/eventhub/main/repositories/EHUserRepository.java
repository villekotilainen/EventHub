package eventhub.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.EHUser;

@Repository
public interface EHUserRepository extends JpaRepository<EHUser, Long> {
    
}
