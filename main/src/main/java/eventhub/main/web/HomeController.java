package eventhub.main.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eventhub.main.domain.Event;
import eventhub.main.domain.EventType;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.EventTypeRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventTypeRepository eventTypeRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "index";
    }
    
    @GetMapping("/events")
    public String eventsPage(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "index";
    }

    @RequestMapping("/index")
    public String home(Model model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        List<EventType> eventTypes = eventTypeRepository.findAll();
        model.addAttribute("eventTypes", eventTypes);
        return "create-event";
    }
    
    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute Event event, BindingResult result, 
                             Model model, RedirectAttributes redirectAttributes,
                             @RequestParam(required = false) String eventStartTime,
                             @RequestParam(required = false) String eventEndTime,
                             @RequestParam(required = false) String eventEndDate,
                             @RequestParam(required = false) List<Long> eventTypeIds) {
        
        // Combine date and time into LocalDateTime for event start
        if (event.getEventDate() != null && eventStartTime != null && !eventStartTime.isEmpty()) {
            try {
                LocalDateTime startDateTime = event.getEventDate().atTime(
                    Integer.parseInt(eventStartTime.split(":")[0]), 
                    Integer.parseInt(eventStartTime.split(":")[1])
                );
                event.setEventStart(startDateTime);
            } catch (Exception e) {
                result.rejectValue("eventStart", "error.event", "Invalid start time format");
            }
        }
        
        // Handle end date/time - use end date if provided (multi-day event), otherwise use start date
        if (eventEndTime != null && !eventEndTime.isEmpty()) {
            try {
                LocalDate endDate = event.getEventDate(); // Default to start date
                if (eventEndDate != null && !eventEndDate.isEmpty()) {
                    endDate = LocalDate.parse(eventEndDate); // Use end date if provided
                }
                
                LocalDateTime endDateTime = endDate.atTime(
                    Integer.parseInt(eventEndTime.split(":")[0]), 
                    Integer.parseInt(eventEndTime.split(":")[1])
                );
                event.setEventEnd(endDateTime);
            } catch (Exception e) {
                result.rejectValue("eventEnd", "error.event", "Invalid end time format");
            }
        }
        
        // Handle multiple event types
        if (eventTypeIds != null && !eventTypeIds.isEmpty()) {
            Set<EventType> selectedEventTypes = new HashSet<>();
            for (Long typeId : eventTypeIds) {
                Optional<EventType> eventType = eventTypeRepository.findById(typeId);
                eventType.ifPresent(selectedEventTypes::add);
            }
            event.setEventTypes(selectedEventTypes);
        } else {
            result.rejectValue("eventTypes", "error.event", "At least one event type is required");
        }
        
        // Format website URL if provided
        if (event.getEventWebsite() != null && !event.getEventWebsite().trim().isEmpty()) {
            String website = event.getEventWebsite().trim();
            // If it doesn't start with http:// or https://, add https://
            if (!website.startsWith("http://") && !website.startsWith("https://")) {
                website = "https://" + website;
            }
            event.setEventWebsite(website);
        }
        
        if (result.hasErrors()) {
            // Re-populate event types for the form
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            return "create-event";
        }
        
        // Initialize vote counts to 0
        event.setEventUpVote(0);
        event.setEventDownVote(0);
        
        try {
            eventRepository.save(event);
            redirectAttributes.addFlashAttribute("successMessage", "Event created successfully!");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create event. Please try again.");
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            return "create-event";
        }
    }

    @PostMapping("/vote/{eventId}/upvote")
    public ResponseEntity<String> upvoteEvent(@PathVariable Long eventId, HttpSession session) {
        // Check if user has already voted for this event
        @SuppressWarnings("unchecked")
        Set<Long> votedEvents = (Set<Long>) session.getAttribute("votedEvents");
        if (votedEvents == null) {
            votedEvents = new HashSet<>();
        }
        
        if (votedEvents.contains(eventId)) {
            return ResponseEntity.badRequest().body("You have already voted for this event");
        }
        
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            Integer currentUpvotes = event.getEventUpVote();
            event.setEventUpVote(currentUpvotes != null ? currentUpvotes + 1 : 1);
            eventRepository.save(event);
            
            // Mark this event as voted by this session
            votedEvents.add(eventId);
            session.setAttribute("votedEvents", votedEvents);
            
            return ResponseEntity.ok("Upvote successful");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/vote/{eventId}/downvote")
    public ResponseEntity<String> downvoteEvent(@PathVariable Long eventId, HttpSession session) {
        // Check if user has already voted for this event
        @SuppressWarnings("unchecked")
        Set<Long> votedEvents = (Set<Long>) session.getAttribute("votedEvents");
        if (votedEvents == null) {
            votedEvents = new HashSet<>();
        }
        
        if (votedEvents.contains(eventId)) {
            return ResponseEntity.badRequest().body("You have already voted for this event");
        }
        
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            Integer currentDownvotes = event.getEventDownVote();
            event.setEventDownVote(currentDownvotes != null ? currentDownvotes + 1 : 1);
            eventRepository.save(event);
            
            // Mark this event as voted by this session
            votedEvents.add(eventId);
            session.setAttribute("votedEvents", votedEvents);
            
            return ResponseEntity.ok("Downvote successful");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
