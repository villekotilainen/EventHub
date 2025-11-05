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

import eventhub.main.domain.EHUser;
import eventhub.main.domain.Event;
import eventhub.main.domain.EventType;
import eventhub.main.domain.UserRole;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.EventTypeRepository;
import eventhub.main.repositories.EHUserRepository;
import eventhub.main.repositories.UserRoleRepository;
import eventhub.main.service.EventService;
import eventhub.main.service.PasswordResetService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    @Autowired
    private EHUserRepository ehUserRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private PasswordResetService passwordResetService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication) {
        // Add admin status to all models globally
        boolean isAdmin = false;
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<EHUser> userOptional = ehUserRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                isAdmin = "ADMIN".equals(userOptional.get().getRole().getRoleName());
            }
        }
        model.addAttribute("isCurrentUserAdmin", isAdmin);
    }

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
    
    @GetMapping("/my-events")
    public String myEventsPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("DEBUG: Authenticated username: " + username);
            
            List<Event> events = eventRepository.findByEHUserUsername(username);
            System.out.println("DEBUG: Found " + events.size() + " events for user: " + username);
            
            // Also let's check all users in database for debugging
            List<EHUser> allUsers = ehUserRepository.findAll();
            System.out.println("DEBUG: All users in database:");
            for (EHUser user : allUsers) {
                System.out.println("  - Username: " + user.getUsername() + ", Role: " + user.getRole().getRoleName() + ", ID: " + user.getId());
            }
            
            model.addAttribute("events", events);
            model.addAttribute("username", username);
        } else {
            model.addAttribute("events", List.of());
        }
        return "my-events";
    }
    
    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        List<EventType> eventTypes = eventTypeRepository.findAll();
        model.addAttribute("eventTypes", eventTypes);
        return "create-event";
    }
    
    @GetMapping("/my-events/edit/{id}")
    public String editEventForm(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            
            // Check if the current user owns this event
            String username = authentication.getName();
            if (event.getEHUser() == null || !event.getEHUser().getUsername().equals(username)) {
                return "redirect:/my-events"; // Redirect if user doesn't own the event
            }
            
            model.addAttribute("event", event);
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            return "editevent";
        } else {
            return "redirect:/my-events";
        }
    }
    
    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute Event event, BindingResult result, 
                             Model model, RedirectAttributes redirectAttributes, Authentication authentication,
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
        
        // Associate event with the authenticated user
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<EHUser> userOptional = ehUserRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                event.setEHUser(userOptional.get());
            }
        }
        
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
    
    @PostMapping("/my-events/edit/{id}")
    public String updateEvent(@PathVariable Long id, @Valid @ModelAttribute Event event, BindingResult result,
                             Model model, RedirectAttributes redirectAttributes, Authentication authentication,
                             @RequestParam(required = false) String eventStartTime,
                             @RequestParam(required = false) String eventEndTime,
                             @RequestParam(required = false) String eventEndDate,
                             @RequestParam(required = false) List<Long> eventTypeIds) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        Optional<Event> existingEventOptional = eventRepository.findById(id);
        if (!existingEventOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event not found.");
            return "redirect:/my-events";
        }
        
        Event existingEvent = existingEventOptional.get();
        
        String username = authentication.getName();
        if (existingEvent.getEHUser() == null || !existingEvent.getEHUser().getUsername().equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only edit your own events.");
            return "redirect:/my-events";
        }
        
        existingEvent.setEventName(event.getEventName());
        existingEvent.setEventDate(event.getEventDate());
        existingEvent.setEventLocation(event.getEventLocation());
        existingEvent.setEventDescription(event.getEventDescription());
        existingEvent.setEventWebsite(event.getEventWebsite());
        
        if (event.getEventDate() != null && eventStartTime != null && !eventStartTime.isEmpty()) {
            try {
                LocalDateTime startDateTime = event.getEventDate().atTime(
                    Integer.parseInt(eventStartTime.split(":")[0]), 
                    Integer.parseInt(eventStartTime.split(":")[1])
                );
                existingEvent.setEventStart(startDateTime);
            } catch (Exception e) {
                result.rejectValue("eventStart", "error.event", "Invalid start time format");
            }
        }
        
        if (eventEndTime != null && !eventEndTime.isEmpty()) {
            try {
                LocalDate endDate = event.getEventDate(); 
                if (eventEndDate != null && !eventEndDate.isEmpty()) {
                    endDate = LocalDate.parse(eventEndDate);
                }
                
                LocalDateTime endDateTime = endDate.atTime(
                    Integer.parseInt(eventEndTime.split(":")[0]), 
                    Integer.parseInt(eventEndTime.split(":")[1])
                );
                existingEvent.setEventEnd(endDateTime);
            } catch (Exception e) {
                result.rejectValue("eventEnd", "error.event", "Invalid end time format");
            }
        }
        
        if (eventTypeIds != null && !eventTypeIds.isEmpty()) {
            Set<EventType> selectedEventTypes = new HashSet<>();
            for (Long typeId : eventTypeIds) {
                Optional<EventType> eventType = eventTypeRepository.findById(typeId);
                eventType.ifPresent(selectedEventTypes::add);
            }
            existingEvent.setEventTypes(selectedEventTypes);
        } else {
            result.rejectValue("eventTypes", "error.event", "At least one event type is required");
        }
        
        // Format website URL if provided
        if (existingEvent.getEventWebsite() != null && !existingEvent.getEventWebsite().trim().isEmpty()) {
            String website = existingEvent.getEventWebsite().trim();
            // If it doesn't start with http:// or https://, add https://
            if (!website.startsWith("http://") && !website.startsWith("https://")) {
                website = "https://" + website;
            }
            existingEvent.setEventWebsite(website);
        }
        
        if (result.hasErrors()) {
            // Re-populate event types for the form
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            model.addAttribute("event", existingEvent);
            return "editevent";
        }
        
        try {
            eventRepository.save(existingEvent);
            redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully!");
            return "redirect:/my-events";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update event. Please try again.");
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            model.addAttribute("event", existingEvent);
            return "editevent";
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
    
    @PostMapping("/my-events/delete/{id}")
    public String deleteEvent(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Find the event
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (!eventOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event not found.");
            return "redirect:/my-events";
        }
        
        Event event = eventOptional.get();
        String username = authentication.getName();
        
        // Verify ownership - user can only delete their own events
        if (event.getEHUser() == null || !event.getEHUser().getUsername().equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only delete your own events.");
            return "redirect:/my-events";
        }
        
        try {
            String eventName = event.getEventName();
            eventRepository.delete(event);
            redirectAttributes.addFlashAttribute("successMessage", "🎉 Event \"" + eventName + "\" has been successfully deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete event. Please try again.");
        }
        
        return "redirect:/my-events";
    }

    @GetMapping("/admin/users")
    public String adminUsersPage(Model model, Authentication authentication) {
        // Check if user is authenticated and has admin role
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Optional<EHUser> currentUserOptional = ehUserRepository.findByUsername(username);
        
        if (!currentUserOptional.isPresent() || 
            !currentUserOptional.get().getRole().getRoleName().equals("ADMIN")) {
            return "redirect:/"; // Redirect non-admin users
        }
        
        List<EHUser> allUsers = ehUserRepository.findAll();
        model.addAttribute("users", allUsers);
        model.addAttribute("currentUsername", username);
        return "userlist";
    }
    
    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        // Check if user is authenticated and has admin role
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Optional<EHUser> currentUserOptional = ehUserRepository.findByUsername(username);
        
        if (!currentUserOptional.isPresent() || 
            !currentUserOptional.get().getRole().getRoleName().equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        // Find the user to delete
        Optional<EHUser> userToDeleteOptional = ehUserRepository.findById(id);
        if (!userToDeleteOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/admin/users";
        }
        
        EHUser userToDelete = userToDeleteOptional.get();
        
        // Prevent admin from deleting themselves
        if (userToDelete.getUsername().equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete your own account.");
            return "redirect:/admin/users";
        }
        
        try {
            String deletedUsername = userToDelete.getUsername();
            String deletedName = userToDelete.getFirstname() + " " + userToDelete.getLastname();
            
            // Delete all events created by this user first (to maintain referential integrity)
            List<Event> userEvents = eventRepository.findByEHUserUsername(deletedUsername);
            eventRepository.deleteAll(userEvents);
            
            // Now delete the user
            ehUserRepository.delete(userToDelete);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "🗑️ User \"" + deletedName + "\" (" + deletedUsername + ") and their events have been successfully deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }
        
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/promote/{id}")
    public String promoteUserToAdmin(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        // Check if user is authenticated and has admin role
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Optional<EHUser> currentUserOptional = ehUserRepository.findByUsername(username);
        
        if (!currentUserOptional.isPresent() || 
            !currentUserOptional.get().getRole().getRoleName().equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        // Find the user to promote
        Optional<EHUser> userToPromoteOptional = ehUserRepository.findById(id);
        if (!userToPromoteOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/admin/users";
        }
        
        EHUser userToPromote = userToPromoteOptional.get();
        
        // Check if user is already an admin
        if (userToPromote.getRole().getRoleName().equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("errorMessage", "User is already an administrator.");
            return "redirect:/admin/users";
        }
        
        try {
            // Find the admin role by name
            List<UserRole> allRoles = userRoleRepository.findAll();
            UserRole adminRole = null;
            for (UserRole role : allRoles) {
                if ("ADMIN".equals(role.getRoleName())) {
                    adminRole = role;
                    break;
                }
            }
            
            if (adminRole != null) {
                String promotedName = userToPromote.getFirstname() + " " + userToPromote.getLastname();
                String promotedUsername = userToPromote.getUsername();
                
                // Set the user's role to admin
                userToPromote.setRole(adminRole);
                ehUserRepository.save(userToPromote);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "🎉 User \"" + promotedName + "\" (" + promotedUsername + ") has been promoted to Administrator!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Admin role not found in the system.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to promote user. Please try again.");
        }
        
        return "redirect:/admin/users";
    }

    // Admin event management endpoints
    @GetMapping("/admin/events/edit/{id}")
    public String adminEditEventForm(@PathVariable Long id, Model model, Authentication authentication) {
        // Check if user is authenticated and has admin role
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Optional<EHUser> currentUserOptional = ehUserRepository.findByUsername(username);
        
        if (!currentUserOptional.isPresent() || 
            !currentUserOptional.get().getRole().getRoleName().equals("ADMIN")) {
            return "redirect:/"; // Redirect non-admin users
        }
        
        // Use EventService (consistent with REST controller logic)
        Optional<Event> eventOptional = eventService.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            model.addAttribute("event", event);
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            model.addAttribute("isAdminEdit", true); // Flag to indicate this is admin editing
            return "editevent";
        } else {
            return "redirect:/";
        }
    }
    
    @PostMapping("/admin/events/edit/{id}")
    public String adminUpdateEvent(@PathVariable Long id, @Valid @ModelAttribute Event event, BindingResult result,
                                  Model model, RedirectAttributes redirectAttributes, Authentication authentication,
                                  @RequestParam(required = false) String eventStartTime,
                                  @RequestParam(required = false) String eventEndTime,
                                  @RequestParam(required = false) String eventEndDate,
                                  @RequestParam(required = false) List<Long> eventTypeIds) {
        
        // Check if user is authenticated and has admin role
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Optional<EHUser> currentUserOptional = ehUserRepository.findByUsername(username);
        
        if (!currentUserOptional.isPresent() || 
            !currentUserOptional.get().getRole().getRoleName().equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        // Validate event types first
        if (eventTypeIds == null || eventTypeIds.isEmpty()) {
            result.rejectValue("eventTypes", "error.event", "At least one event type is required");
        }
        
        if (result.hasErrors()) {
            // Get the existing event for form display
            Optional<Event> existingEventOptional = eventService.findById(id);
            if (existingEventOptional.isPresent()) {
                List<EventType> eventTypes = eventTypeRepository.findAll();
                model.addAttribute("eventTypes", eventTypes);
                model.addAttribute("event", existingEventOptional.get());
                model.addAttribute("isAdminEdit", true);
            }
            return "editevent";
        }
        
        // Use EventService to update (same logic as REST controller)
        Optional<Event> updatedEventOptional = eventService.updateEventFromForm(id, event, eventStartTime, eventEndTime, eventEndDate, eventTypeIds);
        
        if (updatedEventOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully by admin!");
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "Failed to update event. Event not found or update failed.");
            List<EventType> eventTypes = eventTypeRepository.findAll();
            model.addAttribute("eventTypes", eventTypes);
            model.addAttribute("event", event);
            model.addAttribute("isAdminEdit", true);
            return "editevent";
        }
    }
    
    @PostMapping("/admin/events/delete/{id}")
    public String adminDeleteEvent(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        // Check if user is authenticated and has admin role
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Optional<EHUser> currentUserOptional = ehUserRepository.findByUsername(username);
        
        if (!currentUserOptional.isPresent() || 
            !currentUserOptional.get().getRole().getRoleName().equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied. Admin privileges required.");
            return "redirect:/";
        }
        
        // Get event details before deletion using EventService
        Optional<Event> eventOptional = eventService.findById(id);
        if (!eventOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event not found.");
            return "redirect:/";
        }
        
        Event event = eventOptional.get();
        String eventName = event.getEventName();
        String eventOwner = event.getEHUser() != null ? event.getEHUser().getUsername() : "Unknown";
        
        try {
            // Use EventService (which uses the same logic as REST controller)
            boolean deleted = eventService.deleteEvent(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "🗑️ Event \"" + eventName + "\" (owned by " + eventOwner + ") has been successfully deleted by admin!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete event - event not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete event. Please try again.");
        }
        
        return "redirect:/";
    }
    
    // === PASSWORD RESET FUNCTIONALITY ===
    
    /**
     * Show forgot password form
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgotpassword";
    }
    
    /**
     * Handle forgot password form submission
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, 
                                       HttpServletRequest request,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        try {
            // Get base URL for reset link
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            
            // Create reset token and send email
            boolean success = passwordResetService.createPasswordResetTokenForUser(email, baseUrl);
            
            if (success) {
                model.addAttribute("successMessage", 
                    "If an account with that email exists, you will receive a password reset link shortly.");
                return "forgotpassword";
            } else {
                model.addAttribute("errorMessage", "There was an error processing your request. Please try again.");
                model.addAttribute("email", email);
                return "forgotpassword";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was an error processing your request. Please try again.");
            model.addAttribute("email", email);
            return "forgotpassword";
        }
    }
    
    /**
     * Show reset password form with token validation
     */
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Validate token
        Optional<EHUser> userOptional = passwordResetService.validatePasswordResetToken(token);
        
        if (!userOptional.isPresent()) {
            model.addAttribute("errorMessage", 
                "Invalid or expired password reset token. Please request a new password reset.");
            return "forgotpassword";
        }
        
        model.addAttribute("token", token);
        return "resetpassword";
    }
    
    /**
     * Handle reset password form submission
     */
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Validate passwords match
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match. Please try again.");
                model.addAttribute("token", token);
                return "resetpassword";
            }
            
            // Validate password strength
            if (!passwordResetService.isValidPassword(newPassword)) {
                model.addAttribute("errorMessage", 
                    "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character.");
                model.addAttribute("token", token);
                return "resetpassword";
            }
            
            // Reset password
            boolean success = passwordResetService.resetPassword(token, newPassword);
            
            if (success) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Your password has been successfully reset! You can now log in with your new password.");
                return "redirect:/login";
            } else {
                model.addAttribute("errorMessage", 
                    "Invalid or expired password reset token. Please request a new password reset.");
                return "forgotpassword";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was an error resetting your password. Please try again.");
            model.addAttribute("token", token);
            return "resetpassword";
        }
    }

}
