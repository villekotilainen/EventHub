package eventhub.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.EventType;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    
}