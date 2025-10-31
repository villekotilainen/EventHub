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
			
			// Hardcoded event types

			EventType techEvent = new EventType("Technology", "Technology related events, conferences, and meetups");
			eventTypeRepository.save(techEvent);

			EventType musicEvent = new EventType("Music", "General music-related events and performances");
			eventTypeRepository.save(musicEvent);

			EventType concertEvent = new EventType("Concert", "Live performances by artists or bands");
			eventTypeRepository.save(concertEvent);

			EventType musicFestivalEvent = new EventType("Music Festival", "Multi-day festivals featuring multiple artists and genres");
			eventTypeRepository.save(musicFestivalEvent);

			EventType sportsEvent = new EventType("Sports", "Sports events, tournaments, and competitions");
			eventTypeRepository.save(sportsEvent);

			EventType businessEvent = new EventType("Business", "Business conferences, networking events, and seminars");
			eventTypeRepository.save(businessEvent);

			EventType artExhibition = new EventType("Art Exhibition", "Visual art displays, galleries, and exhibitions");
			eventTypeRepository.save(artExhibition);

			EventType theaterEvent = new EventType("Theater", "Plays, musicals, and live drama performances");
			eventTypeRepository.save(theaterEvent);

			EventType communityEvent = new EventType("Community", "Local fairs, markets, and neighborhood gatherings");
			eventTypeRepository.save(communityEvent);

			EventType educationEvent = new EventType("Education", "Workshops, lectures, and training sessions");
			eventTypeRepository.save(educationEvent);

			EventType foodFestival = new EventType("Food Festival", "Culinary events celebrating food and drink");
			eventTypeRepository.save(foodFestival);

			EventType charityEvent = new EventType("Charity", "Fundraisers, donation drives, and benefit events");
			eventTypeRepository.save(charityEvent);

			EventType gamingEvent = new EventType("Gaming", "Esports tournaments, LAN parties, and gaming conventions");
			eventTypeRepository.save(gamingEvent);

			EventType filmFestival = new EventType("Film Festival", "Screenings, premieres, and film industry gatherings");
			eventTypeRepository.save(filmFestival);

			EventType wellnessEvent = new EventType("Wellness", "Health, fitness, and mindfulness related events");
			eventTypeRepository.save(wellnessEvent);

			EventType politicalEvent = new EventType("Political", "Debates, campaigns, and public discussions");
			eventTypeRepository.save(politicalEvent);
			
			EventType workshopEvent = new EventType("Workshop", "Hands-on training sessions or skill-based activities");
			eventTypeRepository.save(workshopEvent);

			EventType premiereEvent = new EventType("Movie Premiere", "Movie premieres or special film showings");
			eventTypeRepository.save(premiereEvent);

			EventType filmScreening = new EventType("Film Screening", "Public screenings of films or documentaries");
			eventTypeRepository.save(filmScreening);

			EventType productLaunch = new EventType("Product Launch", "Events introducing new products or services");
			eventTypeRepository.save(productLaunch);

			EventType networkingEvent = new EventType("Networking", "Professional or social networking gatherings");
			eventTypeRepository.save(networkingEvent);

			EventType charityFundraiser = new EventType("Charity Event", "Events organized to raise funds for good causes");
			eventTypeRepository.save(charityFundraiser);

			EventType lectureEvent = new EventType("Lecture", "Academic or public lectures focused on specific topics");
			eventTypeRepository.save(lectureEvent);

			EventType courseEvent = new EventType("Course", "Educational courses or multi-session training programs");
			eventTypeRepository.save(courseEvent);

			EventType virtualEvent = new EventType("Virtual Event", "Online events such as webinars, live streams, or hybrid events");
			eventTypeRepository.save(virtualEvent);
			
			
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
				"https://maria.io",
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

			Event event6 = new Event(
				"AI & Machine Learning Summit 2024",
				LocalDate.of(2024, 9, 18),
				LocalDateTime.of(2024, 9, 18, 9, 0),
				LocalDateTime.of(2024, 9, 18, 17, 30),
				40,
				5,
				"Messukeskus, Helsinki",
				"International summit exploring AI trends, ethical challenges, and future technologies",
				"https://aimlsummit.fi",
				techEvent,
				adminUser
			);
			eventRepository.save(event6);

			Event event7 = new Event(
				"Christmas Charity Gala 2025",
				LocalDate.of(2025, 12, 10),
				LocalDateTime.of(2025, 12, 10, 19, 0),
				LocalDateTime.of(2025, 12, 10, 23, 0),
				60,
				10,
				"Finlandia Hall, Helsinki",
				"A festive charity gala supporting children’s education initiatives",
				"https://charitygala.fi",
				charityEvent,
				adminUser
			);
			eventRepository.save(event7);

			Event event8 = new Event(
				"Nordic Wellness Retreat 2026",
				LocalDate.of(2026, 3, 22),
				LocalDateTime.of(2026, 3, 22, 10, 0),
				LocalDateTime.of(2026, 3, 22, 17, 0),
				30,
				2,
				"Naantali Spa, Naantali",
				"Day-long wellness retreat focusing on mindfulness, nutrition, and self-care",
				"https://nordicwellness.fi",
				wellnessEvent,
				regularUser2
			);
			eventRepository.save(event8);

			Event event9 = new Event(
				"Helsinki Food Festival 2026",
				LocalDate.of(2026, 5, 5),
				LocalDateTime.of(2026, 5, 5, 11, 0),
				LocalDateTime.of(2026, 5, 5, 20, 0),
				80,
				15,
				"Kauppatori, Helsinki",
				"Culinary celebration of local and international cuisine with live music and tastings",
				"https://helsinkifoodfest.fi",
				foodFestival,
				regularUser
			);
			eventRepository.save(event9);

			Event event10 = new Event(
				"Photography Workshop 2024",
				LocalDate.of(2024, 4, 14),
				LocalDateTime.of(2024, 4, 14, 10, 0),
				LocalDateTime.of(2024, 4, 14, 16, 0),
				20,
				2,
				"Design Museum, Helsinki",
				"Hands-on photography workshop focusing on composition, lighting, and creative techniques",
				"https://helsinkiphoto.fi",
				workshopEvent,
				regularUser
			);
			eventRepository.save(event10);
			
			System.out.println("Demo data created successfully!");
			System.out.println("Created " + rolePermissionsRepository.count() + " role permissions");
			System.out.println("Created " + userRoleRepository.count() + " user roles");
			System.out.println("Created " + ehUserRepository.count() + " users");
			System.out.println("Created " + eventTypeRepository.count() + " event types");
			System.out.println("Created " + eventRepository.count() + " events");
		};
	}
}
