package eventhub.main.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eventhub.main.domain.EventType;
import eventhub.main.repositories.EventTypeRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/eventtypes")
public class EventTypeRestController {

    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    // GET all event types
    @GetMapping
    public List<EventType> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }
    
    // GET event type by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventType> getEventTypeById(@PathVariable Long id) {
        Optional<EventType> eventType = eventTypeRepository.findById(id);
        if (eventType.isPresent()) {
            return ResponseEntity.ok(eventType.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // POST - Create new event type
    @PostMapping
    public ResponseEntity<EventType> createEventType(@Valid @RequestBody EventType eventType) {
        try {
            EventType savedEventType = eventTypeRepository.save(eventType);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEventType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT - Update existing event type
    @PutMapping("/{id}")
    public ResponseEntity<EventType> updateEventType(@PathVariable Long id, @Valid @RequestBody EventType eventType) {
        Optional<EventType> existingEventType = eventTypeRepository.findById(id);
        if (existingEventType.isPresent()) {
            eventType.setId(id);
            EventType updatedEventType = eventTypeRepository.save(eventType);
            return ResponseEntity.ok(updatedEventType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE event type by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        Optional<EventType> eventType = eventTypeRepository.findById(id);
        if (eventType.isPresent()) {
            eventTypeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
