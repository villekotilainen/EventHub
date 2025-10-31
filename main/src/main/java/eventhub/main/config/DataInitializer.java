package eventhub.main.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import eventhub.main.domain.Event;
import eventhub.main.domain.EventType;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.EventTypeRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    @Autowired
    private EventRepository eventRepository;

    @Override
    public void run(String... args) throws Exception {
        // Event types are now initialized in MainApplication.java
        // This section has been disabled to prevent duplicates
        
        /*
        // Initialize event types if none exist
        if (eventTypeRepository.count() == 0) {
            EventType business = new EventType("Business", "Corporate events, conferences, networking");
            EventType technology = new EventType("Technology", "Tech meetups, hackathons, product launches");
            EventType music = new EventType("Music", "Concerts, festivals, live performances");
            EventType sports = new EventType("Sports", "Sports events, tournaments, fitness activities");
            EventType charity = new EventType("Charity", "Fundraising events, charity galas, community service");
            EventType education = new EventType("Education", "Workshops, seminars, training sessions");
            EventType entertainment = new EventType("Entertainment", "Shows, movies, comedy events");
            EventType food = new EventType("Food & Drink", "Food festivals, wine tastings, culinary events");
            EventType art = new EventType("Art & Culture", "Art exhibitions, cultural events, theater");
            EventType social = new EventType("Social", "Social gatherings, parties, community events");
            
            eventTypeRepository.save(business);
            eventTypeRepository.save(technology);
            eventTypeRepository.save(music);
            eventTypeRepository.save(sports);
            eventTypeRepository.save(charity);
            eventTypeRepository.save(education);
            eventTypeRepository.save(entertainment);
            eventTypeRepository.save(food);
            eventTypeRepository.save(art);
            eventTypeRepository.save(social);
            
            System.out.println("Event types initialized successfully!");
        }
        */
        
        // Add some sample events if none exist
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
}