package eventhub.main.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component; - DISABLED

import eventhub.main.domain.EHUser;
import eventhub.main.domain.Event;
import eventhub.main.domain.EventType;
import eventhub.main.domain.UserRole;
import eventhub.main.repositories.EHUserRepository;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.EventTypeRepository;
import eventhub.main.repositories.UserRoleRepository;

// @Component - DISABLED to prevent duplicate user creation (MainApplication.java handles initialization)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRoleRepository roleRepository;
    
    @Autowired
    private EHUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeUserRoles();
        
    
        initializeSampleUsers();
        
        if (eventRepository.count() == 0) {
            EventType business = eventTypeRepository.findAll().stream()
                    .filter(type -> "Business".equals(type.getEventTypeName()))
                    .findFirst().orElse(null);
            EventType technology = eventTypeRepository.findAll().stream()
                    .filter(type -> "Technology".equals(type.getEventTypeName()))
                    .findFirst().orElse(null);
            EventType music = eventTypeRepository.findAll().stream()
                    .filter(type -> "Music".equals(type.getEventTypeName()))
                    .findFirst().orElse(null);
            EventType charity = eventTypeRepository.findAll().stream()
                    .filter(type -> "Charity".equals(type.getEventTypeName()))
                    .findFirst().orElse(null);
            
            if (business != null) {
                Event startupPitch = new Event();
                startupPitch.setEventName("Startup Pitch Night");
                startupPitch.setEventDescription("Young entrepreneurs pitch their innovative ideas to investors and mentors");
                startupPitch.setEventDate(LocalDate.of(2025, 11, 5));
                startupPitch.setEventStart(LocalDateTime.of(2025, 11, 5, 18, 0));
                startupPitch.setEventEnd(LocalDateTime.of(2025, 11, 5, 21, 0));
                startupPitch.setEventLocation("Maria 01, Helsinki");
                Set<EventType> businessTypes = new HashSet<>();
                businessTypes.add(business);
                startupPitch.setEventTypes(businessTypes);
                startupPitch.setEventUpVote(35);
                startupPitch.setEventDownVote(2);
                eventRepository.save(startupPitch);
            }
            
            if (technology != null) {
                Event reactMeetup = new Event();
                reactMeetup.setEventName("React Developer Meetup");
                reactMeetup.setEventDescription("Monthly meetup for React developers to share knowledge and network");
                reactMeetup.setEventDate(LocalDate.of(2025, 11, 15));
                reactMeetup.setEventStart(LocalDateTime.of(2025, 11, 15, 18, 30));
                reactMeetup.setEventEnd(LocalDateTime.of(2025, 11, 15, 21, 0));
                reactMeetup.setEventLocation("Reaktor Office, Helsinki");
                Set<EventType> techTypes = new HashSet<>();
                techTypes.add(technology);
                reactMeetup.setEventTypes(techTypes);
                reactMeetup.setEventUpVote(18);
                reactMeetup.setEventDownVote(0);
                eventRepository.save(reactMeetup);
            }
            
            if (music != null) {
                Event jazzNight = new Event();
                jazzNight.setEventName("Jazz Night at Savoy");
                jazzNight.setEventDescription("An evening of smooth jazz featuring local and international artists");
                jazzNight.setEventDate(LocalDate.of(2025, 11, 20));
                jazzNight.setEventStart(LocalDateTime.of(2025, 11, 20, 20, 0));
                jazzNight.setEventEnd(LocalDateTime.of(2025, 11, 20, 23, 30));
                jazzNight.setEventLocation("Savoy Theatre, Helsinki");
                jazzNight.setEventWebsite("https://savoy.fi");
                Set<EventType> musicTypes = new HashSet<>();
                musicTypes.add(music);
                jazzNight.setEventTypes(musicTypes);
                jazzNight.setEventUpVote(15);
                jazzNight.setEventDownVote(1);
                eventRepository.save(jazzNight);
            }
            
            if (charity != null) {
                Event charityGala = new Event();
                charityGala.setEventName("Christmas Charity Gala 2025");
                charityGala.setEventDescription("A festive charity gala supporting children's education initiatives");
                charityGala.setEventDate(LocalDate.of(2025, 12, 10));
                charityGala.setEventStart(LocalDateTime.of(2025, 12, 10, 19, 0));
                charityGala.setEventEnd(LocalDateTime.of(2025, 12, 10, 23, 0));
                charityGala.setEventLocation("Finlandia Hall, Helsinki");
                Set<EventType> charityTypes = new HashSet<>();
                charityTypes.add(charity);
                charityGala.setEventTypes(charityTypes);
                charityGala.setEventUpVote(60);
                charityGala.setEventDownVote(10);
                eventRepository.save(charityGala);
            }
            
            System.out.println("Sample events created successfully!");
        }
    }
    
    private void initializeUserRoles() {
        if (roleRepository.count() == 0) {
            UserRole adminRole = new UserRole();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);
            
            UserRole userRole = new UserRole();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
            
            System.out.println("User roles initialized successfully!");
        }
    }
    
    private void initializeSampleUsers() {
        if (userRepository.count() == 0) {
            UserRole adminRole = roleRepository.findByRoleName("ADMIN").orElse(null);
            
            if (adminRole != null) {
                String adminUsername = System.getenv("ADMIN_USERNAME") != null ? 
                    System.getenv("ADMIN_USERNAME") : "admin";
                String adminPassword = System.getenv("ADMIN_PASSWORD") != null ? 
                    System.getenv("ADMIN_PASSWORD") : "SecureAdmin2024!";
                String adminEmail = System.getenv("ADMIN_EMAIL") != null ? 
                    System.getenv("ADMIN_EMAIL") : "admin@localhost";
                
                EHUser admin = new EHUser();
                admin.setUsername(adminUsername);
                admin.setPasswordHash(passwordEncoder.encode(adminPassword));
                admin.setFirstname("System");
                admin.setLastname("Administrator");
                admin.setEmail(adminEmail);
                admin.setRole(adminRole);
                userRepository.save(admin);
                
                System.out.println("Admin user created successfully!");
                // Only show this in development - remove in production
                if (System.getenv("SPRING_PROFILES_ACTIVE") != null && 
                    System.getenv("SPRING_PROFILES_ACTIVE").contains("dev")) {
                    System.out.println("Development mode - Admin credentials: " + adminUsername + " / " + adminPassword);
                }
            }
        }
    }
}