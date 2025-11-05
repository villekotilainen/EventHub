package eventhub.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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

	@Value("${admin.username:admin}")
	private String adminUsername;
	
	@Value("${admin.password:SecureAdmin2024!}")
	private String adminPassword;
	
	@Value("${admin.email:admin@localhost}")
	private String adminEmail;

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
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
			
			// Create admin user with secure credentials from properties
			
			EHUser adminUser = new EHUser(adminUsername, passwordEncoder.encode(adminPassword), "System", "Administrator", adminEmail, adminRole);
			ehUserRepository.save(adminUser);
			
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



			EventType lectureEvent = new EventType("Lecture", "Academic or public lectures focused on specific topics");
			eventTypeRepository.save(lectureEvent);

			EventType courseEvent = new EventType("Course", "Educational courses or multi-session training programs");
			eventTypeRepository.save(courseEvent);

			EventType virtualEvent = new EventType("Virtual Event", "Online events such as webinars, live streams, or hybrid events");
			eventTypeRepository.save(virtualEvent);

			// Professional & Business - Additional Types
			EventType startupPitchEvent = new EventType("Startup Pitch", "Entrepreneurs presenting business ideas to investors");
			eventTypeRepository.save(startupPitchEvent);

			EventType investorMeetupEvent = new EventType("Investor Meetup", "Networking events for investors and entrepreneurs");
			eventTypeRepository.save(investorMeetupEvent);

			EventType productDemoEvent = new EventType("Product Demo", "Live demonstrations of products or software");
			eventTypeRepository.save(productDemoEvent);

			EventType corporateTrainingEvent = new EventType("Corporate Training", "Professional development and skills training");
			eventTypeRepository.save(corporateTrainingEvent);

			EventType recruitmentEvent = new EventType("Career Fair", "Recruitment events and career opportunities");
			eventTypeRepository.save(recruitmentEvent);

			// Education & Learning - Additional Types
			EventType seminarEvent = new EventType("Seminar", "Educational presentations and discussions");
			eventTypeRepository.save(seminarEvent);

			EventType certificationEvent = new EventType("Certification Program", "Professional certification courses and exams");
			eventTypeRepository.save(certificationEvent);

			EventType bootcampEvent = new EventType("Bootcamp", "Intensive training programs and skill-building sessions");
			eventTypeRepository.save(bootcampEvent);

			EventType panelEvent = new EventType("Panel Discussion", "Expert panels and group discussions");
			eventTypeRepository.save(panelEvent);

			EventType studyGroupEvent = new EventType("Study Group", "Collaborative learning and study sessions");
			eventTypeRepository.save(studyGroupEvent);

			// Entertainment & Culture - Additional Types
			EventType comedyEvent = new EventType("Comedy Show", "Stand-up comedy and humorous performances");
			eventTypeRepository.save(comedyEvent);

			EventType danceEvent = new EventType("Dance Performance", "Dance shows, recitals, and performances");
			eventTypeRepository.save(danceEvent);

			EventType filmWorkshopEvent = new EventType("Film Workshop", "Filmmaking workshops and cinema education");
			eventTypeRepository.save(filmWorkshopEvent);

			EventType culturalFestivalEvent = new EventType("Cultural Festival", "Cultural celebrations and heritage events");
			eventTypeRepository.save(culturalFestivalEvent);

			EventType literatureEvent = new EventType("Literature Reading", "Book readings, poetry events, and literary gatherings");
			eventTypeRepository.save(literatureEvent);

			// Community & Social - Additional Types
			EventType meetupEvent = new EventType("Meetup", "Casual social gatherings and interest-based meetings");
			eventTypeRepository.save(meetupEvent);

			EventType fundraiserEvent = new EventType("Fundraiser", "Fundraising events for various causes");
			eventTypeRepository.save(fundraiserEvent);

			EventType publicTalkEvent = new EventType("Public Talk", "Public speaking events and presentations");
			eventTypeRepository.save(publicTalkEvent);

			EventType townHallEvent = new EventType("Town Hall", "Community meetings and public forums");
			eventTypeRepository.save(townHallEvent);

			EventType awarenessEvent = new EventType("Awareness Campaign", "Events promoting social causes and awareness");
			eventTypeRepository.save(awarenessEvent);

			// Sports & Wellness - Additional Types
			EventType fitnessEvent = new EventType("Fitness Challenge", "Fitness competitions and exercise challenges");
			eventTypeRepository.save(fitnessEvent);

			EventType marathonEvent = new EventType("Marathon", "Running events, marathons, and races");
			eventTypeRepository.save(marathonEvent);

			EventType yogaEvent = new EventType("Yoga Retreat", "Yoga sessions, retreats, and mindfulness events");
			eventTypeRepository.save(yogaEvent);

			EventType outdoorEvent = new EventType("Outdoor Adventure", "Outdoor activities and adventure sports");
			eventTypeRepository.save(outdoorEvent);

			EventType esportsEvent = new EventType("E-sports Tournament", "Competitive gaming tournaments and events");
			eventTypeRepository.save(esportsEvent);

			// Lifestyle & Hobbies - New Category
			EventType fashionEvent = new EventType("Fashion & Beauty", "Fashion shows, beauty events, and style workshops");
			eventTypeRepository.save(fashionEvent);

			EventType cookingEvent = new EventType("Cooking", "Culinary workshops, cooking classes, and food preparation");
			eventTypeRepository.save(cookingEvent);

			EventType diyEvent = new EventType("DIY & Crafts", "Do-it-yourself projects, crafting workshops, and handmade activities");
			eventTypeRepository.save(diyEvent);

			EventType photographyEvent = new EventType("Photography", "Photography workshops, photo walks, and camera techniques");
			eventTypeRepository.save(photographyEvent);

			EventType gardeningEvent = new EventType("Gardening", "Gardening workshops, plant care, and horticultural events");
			eventTypeRepository.save(gardeningEvent);

			EventType travelEvent = new EventType("Travel", "Travel meetups, destination talks, and journey sharing");
			eventTypeRepository.save(travelEvent);

			EventType homeDesignEvent = new EventType("Home & Design", "Interior design, home improvement, and decor workshops");
			eventTypeRepository.save(homeDesignEvent);

			EventType bookClubEvent = new EventType("Book Club", "Book discussions, reading groups, and literary meetups");
			eventTypeRepository.save(bookClubEvent);

			EventType petEvent = new EventType("Pet Events", "Pet shows, animal care workshops, and pet-related gatherings");
			eventTypeRepository.save(petEvent);
			
			
			// Create events
			Event event1 = new Event();
			event1.setEventName("Spring Boot Conference 2025");
			event1.setEventDate(LocalDate.of(2025, 12, 15));
			event1.setEventStart(LocalDateTime.of(2025, 12, 15, 9, 0));
			event1.setEventEnd(LocalDateTime.of(2025, 12, 15, 17, 0));
			event1.setEventUpVote(25);
			event1.setEventDownVote(3);
			event1.setEventLocation("Helsinki Convention Center");
			event1.setEventDescription("Annual Spring Boot conference featuring latest developments and best practices");
			event1.setEventWebsite("https://springbootconf.com");
			Set<EventType> techTypes = new HashSet<>();
			techTypes.add(techEvent);
			techTypes.add(businessEvent); // Add multiple types for realistic demo
			event1.setEventTypes(techTypes);
			event1.setEHUser(adminUser);
			eventRepository.save(event1);
			
			Event event2 = new Event();
			event2.setEventName("Jazz Night at Savoy");
			event2.setEventDate(LocalDate.of(2025, 11, 20));
			event2.setEventStart(LocalDateTime.of(2025, 11, 20, 20, 0));
			event2.setEventEnd(LocalDateTime.of(2025, 11, 20, 23, 30));
			event2.setEventUpVote(15);
			event2.setEventDownVote(1);
			event2.setEventLocation("Savoy Theatre, Helsinki");
			event2.setEventDescription("An evening of smooth jazz with local and international artists");
			event2.setEventWebsite("https://savoyhelsinki.fi/events/jazz");
			Set<EventType> musicTypes = new HashSet<>();
			musicTypes.add(musicEvent);
			event2.setEventTypes(musicTypes);
			event2.setEHUser(adminUser);
			eventRepository.save(event2);
			
			Event event3 = new Event();
			event3.setEventName("Helsinki Marathon 2025");
			event3.setEventDate(LocalDate.of(2025, 8, 10));
			event3.setEventStart(LocalDateTime.of(2025, 8, 10, 8, 0));
			event3.setEventEnd(LocalDateTime.of(2025, 8, 10, 14, 0));
			event3.setEventUpVote(120);
			event3.setEventDownVote(5);
			event3.setEventLocation("Olympic Stadium Helsinki");
			event3.setEventDescription("Annual marathon event through the beautiful streets of Helsinki");
			event3.setEventWebsite("https://helsinkimarathon.fi");
			Set<EventType> sportsTypes = new HashSet<>();
			sportsTypes.add(sportsEvent);
			event3.setEventTypes(sportsTypes);
			event3.setEHUser(adminUser);
			eventRepository.save(event3);
			
			Event event4 = new Event();
			event4.setEventName("Startup Pitch Night");
			event4.setEventDate(LocalDate.of(2025, 11, 5));
			event4.setEventStart(LocalDateTime.of(2025, 11, 5, 18, 0));
			event4.setEventEnd(LocalDateTime.of(2025, 11, 5, 21, 0));
			event4.setEventUpVote(35);
			event4.setEventDownVote(2);
			event4.setEventLocation("Maria 01, Helsinki");
			event4.setEventDescription("Young entrepreneurs pitch their innovative ideas to investors and mentors");
			event4.setEventWebsite("https://maria.io");
			Set<EventType> businessTypes = new HashSet<>();
			businessTypes.add(businessEvent);
			businessTypes.add(networkingEvent);
			event4.setEventTypes(businessTypes);
			event4.setEHUser(adminUser);
			eventRepository.save(event4);
			
			Event event5 = new Event();
			event5.setEventName("React Developer Meetup");
			event5.setEventDate(LocalDate.of(2025, 11, 15));
			event5.setEventStart(LocalDateTime.of(2025, 11, 15, 18, 30));
			event5.setEventEnd(LocalDateTime.of(2025, 11, 15, 21, 0));
			event5.setEventUpVote(18);
			event5.setEventDownVote(0);
			event5.setEventLocation("Reaktor Office, Helsinki");
			event5.setEventDescription("Monthly meetup for React developers to share knowledge and network");
			event5.setEventWebsite("https://reacthelsinki.fi");
			Set<EventType> reactTypes = new HashSet<>();
			reactTypes.add(techEvent);
			event5.setEventTypes(reactTypes);
			event5.setEHUser(adminUser);
			eventRepository.save(event5);

			Event event6 = new Event();
			event6.setEventName("AI & Machine Learning Summit 2024");
			event6.setEventDate(LocalDate.of(2024, 9, 18));
			event6.setEventStart(LocalDateTime.of(2024, 9, 18, 9, 0));
			event6.setEventEnd(LocalDateTime.of(2024, 9, 18, 17, 30));
			event6.setEventUpVote(40);
			event6.setEventDownVote(5);
			event6.setEventLocation("Aalto University, Espoo");
			event6.setEventDescription("Cutting-edge discussions on AI applications and machine learning innovations");
			event6.setEventWebsite("https://aisummit.aalto.fi");
			Set<EventType> aiTypes = new HashSet<>();
			aiTypes.add(techEvent);
			aiTypes.add(educationEvent);
			event6.setEventTypes(aiTypes);
			event6.setEHUser(adminUser);
			eventRepository.save(event6);
			
			Event event7 = new Event();
			event7.setEventName("Christmas Charity Gala 2025");
			event7.setEventDate(LocalDate.of(2025, 12, 10));
			event7.setEventStart(LocalDateTime.of(2025, 12, 10, 19, 0));
			event7.setEventEnd(LocalDateTime.of(2025, 12, 10, 23, 0));
			event7.setEventUpVote(60);
			event7.setEventDownVote(10);
			event7.setEventLocation("Hotel Kämp, Helsinki");
			event7.setEventDescription("Elegant evening gala to raise funds for local children's charities");
			event7.setEventWebsite("https://kampgala.fi");
			Set<EventType> charityTypes = new HashSet<>();
			charityTypes.add(charityEvent);
			charityTypes.add(communityEvent);
			event7.setEventTypes(charityTypes);
			event7.setEHUser(adminUser);
			eventRepository.save(event7);



			Event event8 = new Event();
			event8.setEventName("Nordic Wellness Retreat 2026");
			event8.setEventDate(LocalDate.of(2026, 3, 22));
			event8.setEventStart(LocalDateTime.of(2026, 3, 22, 10, 0));
			event8.setEventEnd(LocalDateTime.of(2026, 3, 22, 17, 0));
			event8.setEventUpVote(30);
			event8.setEventDownVote(2);
			event8.setEventLocation("Naantali Spa, Naantali");
			event8.setEventDescription("Day-long wellness retreat focusing on mindfulness, nutrition, and self-care");
			event8.setEventWebsite("https://nordicwellness.fi");
			Set<EventType> wellnessTypes = new HashSet<>();
			wellnessTypes.add(wellnessEvent);
			event8.setEventTypes(wellnessTypes);
			event8.setEHUser(adminUser);
			eventRepository.save(event8);

			Event event9 = new Event();
			event9.setEventName("Helsinki Food Festival 2026");
			event9.setEventDate(LocalDate.of(2026, 5, 5));
			event9.setEventStart(LocalDateTime.of(2026, 5, 5, 11, 0));
			event9.setEventEnd(LocalDateTime.of(2026, 5, 5, 20, 0));
			event9.setEventUpVote(80);
			event9.setEventDownVote(15);
			event9.setEventLocation("Kauppatori, Helsinki");
			event9.setEventDescription("Culinary celebration of local and international cuisine with live music and tastings");
			event9.setEventWebsite("https://helsinkifoodfest.fi");
			Set<EventType> foodTypes = new HashSet<>();
			foodTypes.add(foodFestival);
			foodTypes.add(communityEvent);
			event9.setEventTypes(foodTypes);
			event9.setEHUser(adminUser);
			eventRepository.save(event9);

			Event event10 = new Event();
			event10.setEventName("Photography Workshop 2024");
			event10.setEventDate(LocalDate.of(2024, 4, 14));
			event10.setEventStart(LocalDateTime.of(2024, 4, 14, 10, 0));
			event10.setEventEnd(LocalDateTime.of(2024, 4, 14, 16, 0));
			event10.setEventUpVote(20);
			event10.setEventDownVote(2);
			event10.setEventLocation("Designmuseo, Helsinki");
			event10.setEventDescription("Hands-on workshop covering digital photography techniques and post-processing");
			event10.setEventWebsite("https://designmuseum.fi/workshops");
			Set<EventType> workshopTypes = new HashSet<>();
			workshopTypes.add(educationEvent);
			workshopTypes.add(workshopEvent);
			event10.setEventTypes(workshopTypes);
			event10.setEHUser(adminUser);
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
