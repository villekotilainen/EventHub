package eventhub.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
}
