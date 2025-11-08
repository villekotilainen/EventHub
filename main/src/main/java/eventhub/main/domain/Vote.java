package eventhub.main.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "votes", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id"}))
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private EHUser user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @Column(nullable = false)
    private boolean isUpvote; // true for upvote, false for downvote
    
    // Constructors
    public Vote() {}
    
    public Vote(EHUser user, Event event, boolean isUpvote) {
        this.user = user;
        this.event = event;
        this.isUpvote = isUpvote;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public EHUser getUser() {
        return user;
    }
    
    public void setUser(EHUser user) {
        this.user = user;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public boolean isUpvote() {
        return isUpvote;
    }
    
    public void setUpvote(boolean upvote) {
        this.isUpvote = upvote;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote)) return false;
        Vote vote = (Vote) o;
        return user != null && user.equals(vote.user) && 
               event != null && event.equals(vote.event);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", event=" + (event != null ? event.getEventName() : "null") +
                ", isUpvote=" + isUpvote +
                '}';
    }
}