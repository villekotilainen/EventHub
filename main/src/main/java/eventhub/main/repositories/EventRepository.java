package eventhub.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Optional<Event> findByEventLocation(String eventLocation);

}
