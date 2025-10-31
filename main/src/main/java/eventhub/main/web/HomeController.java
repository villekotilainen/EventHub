package eventhub.main.web;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import eventhub.main.domain.Event;
import eventhub.main.repositories.EventRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private EventRepository eventRepository;

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
