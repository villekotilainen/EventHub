package eventhub.main.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Event {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Event name is required")
    private String eventName;

    @NotEmpty(message = "Event date is required")
    private LocalDate eventDate;

    @NotEmpty(message = "Event start time is required")
    private LocalDateTime eventStart;

    private LocalDateTime eventEnd;

    private Integer eventUpVote;

    private Integer eventDownVote;

    @NotEmpty(message = "Event location is required")
    private String eventLocation;
    
    @Size(max = 500, message = "Event description cannot exceed 500 characters")
    private String eventDescription;
    
    private String eventWebsite;

    @ManyToOne
    @NotEmpty(message = "Atleast one event type is required")
    private EventType eventType;

    @ManyToOne
    private EHUser EHUser;

    public Event() {}

    public Event(String eventName, LocalDate eventDate, LocalDateTime eventStart, LocalDateTime eventEnd, Integer eventUpVote,
            Integer eventDownVote, String eventLocation, String eventDescription, String eventWebsite,
            EventType eventType, EHUser eHUser) {
        super();
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventUpVote = eventUpVote;
        this.eventDownVote = eventDownVote;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventWebsite = eventWebsite;
        this.eventType = eventType;
        this.EHUser = eHUser;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getEventStart() {
        return eventStart;
    }

    public void setEventStart(LocalDateTime eventStart) {
        this.eventStart = eventStart;
    }

    public LocalDateTime getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(LocalDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }

    public Integer getEventUpVote() {
        return eventUpVote;
    }

    public void setEventUpVote(Integer eventUpVote) {
        this.eventUpVote = eventUpVote;
    }

    public Integer getEventDownVote() {
        return eventDownVote;
    }

    public void setEventDownVote(Integer eventDownVote) {
        this.eventDownVote = eventDownVote;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventWebsite() {
        return eventWebsite;
    }

    public void setEventWebsite(String eventWebsite) {
        this.eventWebsite = eventWebsite;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EHUser getEHUser() {
        return EHUser;
    }

    public void setEHUser(EHUser eHUser) {
        EHUser = eHUser;
    }

    // ToString

    @Override
    public String toString() {
        return "Event [id=" + id + ", eventName=" + eventName + ", eventDate=" + eventDate + ", eventStart="
                + eventStart + ", eventEnd=" + eventEnd + ", eventUpVote=" + eventUpVote + ", eventDownVote="
                + eventDownVote + ", eventLocation=" + eventLocation + ", eventDescription=" + eventDescription
                + ", eventWebsite=" + eventWebsite + ", eventType=" + eventType + ", EHUser=" + EHUser + "]";
    }
    
}
