package eventhub.main;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import eventhub.main.domain.EHUser;
import eventhub.main.domain.Event;
import eventhub.main.domain.EventType;
import eventhub.main.domain.RolePermissions;
import eventhub.main.domain.UserRole;
import eventhub.main.repositories.EHUserRepository;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.EventTypeRepository;
import eventhub.main.repositories.RolePermissionsRepository;
import eventhub.main.repositories.UserRoleRepository;

@SpringBootApplication
public class MainApplication {

	@Autowired
	private RolePermissionsRepository rolePermissionsRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private EHUserRepository ehUserRepository;
	
	@Autowired
	private EventTypeRepository eventTypeRepository;
	
	@Autowired
	private EventRepository eventRepository;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			System.out.println("Creating demo data...");
			
			// Create role permissions
			RolePermissions adminPermissions = new RolePermissions();
			adminPermissions.setRolePermissionsDescription("Full access to all features");
			adminPermissions.setCanCreateEvent(true);
			adminPermissions.setCanEditEvent(true);
			adminPermissions.setCanDeleteEvent(true);
			adminPermissions.setCanManageUsers(true);
			rolePermissionsRepository.save(adminPermissions);
			
			RolePermissions userPermissions = new RolePermissions();
			userPermissions.setRolePermissionsDescription("Basic user permissions");
			userPermissions.setCanCreateEvent(true);
			userPermissions.setCanEditEvent(false);
			userPermissions.setCanDeleteEvent(false);
			userPermissions.setCanManageUsers(false);
			rolePermissionsRepository.save(userPermissions);
			
			// Create user roles
			UserRole adminRole = new UserRole("ADMIN", adminPermissions);
			userRoleRepository.save(adminRole);
			
			UserRole userRole = new UserRole("USER", userPermissions);
			userRoleRepository.save(userRole);
			
			// Create users
			EHUser adminUser = new EHUser("admin", "password123", "John", "Admin", "admin@eventhub.com", adminRole);
			ehUserRepository.save(adminUser);
			
			EHUser regularUser = new EHUser("user1", "password123", "Jane", "Doe", "jane@example.com", userRole);
			ehUserRepository.save(regularUser);
			
			EHUser regularUser2 = new EHUser("user2", "password123", "Bob", "Smith", "bob@example.com", userRole);
			ehUserRepository.save(regularUser2);
			
			// Create event types
			EventType techEvent = new EventType("Technology", "Technology related events, conferences, and meetups");
			eventTypeRepository.save(techEvent);
			
			EventType musicEvent = new EventType("Music", "Concerts, festivals, and music performances");
			eventTypeRepository.save(musicEvent);
			
			EventType sportsEvent = new EventType("Sports", "Sports events, tournaments, and competitions");
			eventTypeRepository.save(sportsEvent);
			
			EventType businessEvent = new EventType("Business", "Business conferences, networking events, and seminars");
			eventTypeRepository.save(businessEvent);
			
			// Create events
			Event event1 = new Event(
				"Spring Boot Conference 2025",
				LocalDate.of(2025, 12, 15),
				LocalDateTime.of(2025, 12, 15, 9, 0),
				LocalDateTime.of(2025, 12, 15, 17, 0),
				25,
				3,
				"Helsinki Convention Center",
				"Annual Spring Boot conference featuring latest developments and best practices",
				"https://springbootconf.com",
				techEvent,
				adminUser
			);
			eventRepository.save(event1);
			
			Event event2 = new Event(
				"Jazz Night at Savoy",
				LocalDate.of(2025, 11, 20),
				LocalDateTime.of(2025, 11, 20, 20, 0),
				LocalDateTime.of(2025, 11, 20, 23, 30),
				15,
				1,
				"Savoy Theatre, Helsinki",
				"An evening of smooth jazz featuring local and international artists",
				"https://savoyhelsinki.fi",
				musicEvent,
				regularUser
			);
			eventRepository.save(event2);
			
			Event event3 = new Event(
				"Helsinki Marathon 2025",
				LocalDate.of(2025, 8, 10),
				LocalDateTime.of(2025, 8, 10, 8, 0),
				LocalDateTime.of(2025, 8, 10, 14, 0),
				120,
				5,
				"Olympic Stadium, Helsinki",
				"Annual Helsinki Marathon with routes through the beautiful city center",
				"https://helsinkimarathon.fi",
				sportsEvent,
				regularUser2
			);
			eventRepository.save(event3);
			
			Event event4 = new Event(
				"Startup Pitch Night",
				LocalDate.of(2025, 11, 5),
				LocalDateTime.of(2025, 11, 5, 18, 0),
				LocalDateTime.of(2025, 11, 5, 21, 0),
				35,
				2,
				"Maria 01, Helsinki",
				"Young entrepreneurs pitch their innovative ideas to investors and mentors",
				"https://maria01.io",
				businessEvent,
				adminUser
			);
			eventRepository.save(event4);
			
			Event event5 = new Event(
				"React Developer Meetup",
				LocalDate.of(2025, 11, 15),
				LocalDateTime.of(2025, 11, 15, 18, 30),
				LocalDateTime.of(2025, 11, 15, 21, 0),
				18,
				0,
				"Reaktor Office, Helsinki",
				"Monthly meetup for React developers to share knowledge and network",
				"https://reacthelsinki.fi",
				techEvent,
				regularUser
			);
			eventRepository.save(event5);
			
			System.out.println("Demo data created successfully!");
			System.out.println("Created " + rolePermissionsRepository.count() + " role permissions");
			System.out.println("Created " + userRoleRepository.count() + " user roles");
			System.out.println("Created " + ehUserRepository.count() + " users");
			System.out.println("Created " + eventTypeRepository.count() + " event types");
			System.out.println("Created " + eventRepository.count() + " events");
		};
	}
}
