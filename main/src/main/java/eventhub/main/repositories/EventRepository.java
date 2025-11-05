package eventhub.main.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.Event;
import eventhub.main.domain.EHUser;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Optional<Event> findByEventLocation(String eventLocation);
    
    List<Event> findByEHUser(EHUser user);
    
    List<Event> findByEHUserUsername(String username);

}
