package eventhub.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eventhub.main.domain.Event;
import eventhub.main.domain.EventType;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.EventTypeRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    public Optional<Event> updateEvent(Long id, Event event) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            event.setId(id);
            Event updatedEvent = eventRepository.save(event);
            return Optional.of(updatedEvent);
        }
        return Optional.empty();
    }
    
    public Optional<Event> updateEventFromForm(Long id, Event updatedEvent, String eventStartTime, 
                                               String eventEndTime, String eventEndDate, List<Long> eventTypeIds) {
        Optional<Event> existingEventOptional = eventRepository.findById(id);
        if (!existingEventOptional.isPresent()) {
            return Optional.empty();
        }
        
        Event existingEvent = existingEventOptional.get();
        
        // Update basic fields
        existingEvent.setEventName(updatedEvent.getEventName());
        existingEvent.setEventDate(updatedEvent.getEventDate());
        existingEvent.setEventLocation(updatedEvent.getEventLocation());
        existingEvent.setEventDescription(updatedEvent.getEventDescription());
        existingEvent.setEventWebsite(updatedEvent.getEventWebsite());
        
        // Handle start time
        if (updatedEvent.getEventDate() != null && eventStartTime != null && !eventStartTime.isEmpty()) {
            try {
                LocalDateTime startDateTime = updatedEvent.getEventDate().atTime(
                    Integer.parseInt(eventStartTime.split(":")[0]), 
                    Integer.parseInt(eventStartTime.split(":")[1])
                );
                existingEvent.setEventStart(startDateTime);
            } catch (Exception e) {
                // Handle error 
            }
        }
        
        // Handle end time
        if (eventEndTime != null && !eventEndTime.isEmpty()) {
            try {
                LocalDate endDate = updatedEvent.getEventDate(); 
                if (eventEndDate != null && !eventEndDate.isEmpty()) {
                    endDate = LocalDate.parse(eventEndDate);
                }
                
                LocalDateTime endDateTime = endDate.atTime(
                    Integer.parseInt(eventEndTime.split(":")[0]), 
                    Integer.parseInt(eventEndTime.split(":")[1])
                );
                existingEvent.setEventEnd(endDateTime);
            } catch (Exception e) {
                // Handle error
            }
        }
        
        // Handle event types
        if (eventTypeIds != null && !eventTypeIds.isEmpty()) {
            Set<EventType> selectedEventTypes = new HashSet<>();
            for (Long typeId : eventTypeIds) {
                Optional<EventType> eventType = eventTypeRepository.findById(typeId);
                eventType.ifPresent(selectedEventTypes::add);
            }
            existingEvent.setEventTypes(selectedEventTypes);
        }
        
        // Format website URL if provided
        if (existingEvent.getEventWebsite() != null && !existingEvent.getEventWebsite().trim().isEmpty()) {
            String website = existingEvent.getEventWebsite().trim();
            if (!website.startsWith("http://") && !website.startsWith("https://")) {
                website = "https://" + website;
            }
            existingEvent.setEventWebsite(website);
        }
        
        try {
            Event savedEvent = eventRepository.save(existingEvent);
            return Optional.of(savedEvent);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public boolean deleteEvent(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }
}