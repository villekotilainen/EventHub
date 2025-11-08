package eventhub.main.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eventhub.main.domain.EHUser;
import eventhub.main.domain.Event;
import eventhub.main.domain.Vote;
import eventhub.main.repositories.EventRepository;
import eventhub.main.repositories.VoteRepository;

@Service
@Transactional
public class VoteService {
    
    @Autowired
    private VoteRepository voteRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    /**
     * Vote for an event (upvote or downvote)
     * If user has already voted, change their vote or remove it if same vote type
     */
    public void voteForEvent(EHUser user, Event event, boolean isUpvote) {
        Optional<Vote> existingVote = voteRepository.findByUserAndEvent(user, event);
        
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            
            if (vote.isUpvote() == isUpvote) {
                // Same vote type - remove the vote
                removeVote(user, event);
            } else {
                // Different vote type - change the vote
                changeVote(user, event, isUpvote);
            }
        } else {
            // New vote
            addVote(user, event, isUpvote);
        }
    }
    
    /**
     * Add a new vote
     */
    private void addVote(EHUser user, Event event, boolean isUpvote) {
        Vote vote = new Vote(user, event, isUpvote);
        voteRepository.save(vote);
        
        // Update event counters
        if (isUpvote) {
            event.setEventUpVote((event.getEventUpVote() != null ? event.getEventUpVote() : 0) + 1);
        } else {
            event.setEventDownVote((event.getEventDownVote() != null ? event.getEventDownVote() : 0) + 1);
        }
        eventRepository.save(event);
    }
    
    /**
     * Change an existing vote
     */
    private void changeVote(EHUser user, Event event, boolean newVoteType) {
        Optional<Vote> existingVote = voteRepository.findByUserAndEvent(user, event);
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            boolean oldVoteType = vote.isUpvote();
            
            // Update vote
            vote.setUpvote(newVoteType);
            voteRepository.save(vote);
            
            // Update event counters
            if (oldVoteType) {
                // Was upvote, now downvote
                event.setEventUpVote(Math.max(0, (event.getEventUpVote() != null ? event.getEventUpVote() : 0) - 1));
                event.setEventDownVote((event.getEventDownVote() != null ? event.getEventDownVote() : 0) + 1);
            } else {
                // Was downvote, now upvote
                event.setEventDownVote(Math.max(0, (event.getEventDownVote() != null ? event.getEventDownVote() : 0) - 1));
                event.setEventUpVote((event.getEventUpVote() != null ? event.getEventUpVote() : 0) + 1);
            }
            eventRepository.save(event);
        }
    }
    
    /**
     * Remove a vote
     */
    private void removeVote(EHUser user, Event event) {
        Optional<Vote> existingVote = voteRepository.findByUserAndEvent(user, event);
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            boolean wasUpvote = vote.isUpvote();
            
            // Remove vote
            voteRepository.delete(vote);
            
            // Update event counters
            if (wasUpvote) {
                event.setEventUpVote(Math.max(0, (event.getEventUpVote() != null ? event.getEventUpVote() : 0) - 1));
            } else {
                event.setEventDownVote(Math.max(0, (event.getEventDownVote() != null ? event.getEventDownVote() : 0) - 1));
            }
            eventRepository.save(event);
        }
    }
    
    /**
     * Get user's vote for an event (null if no vote)
     */
    public Vote getUserVoteForEvent(EHUser user, Event event) {
        Optional<Vote> vote = voteRepository.findByUserAndEvent(user, event);
        return vote.orElse(null);
    }
    
    /**
     * Check if user has voted for an event
     */
    public boolean hasUserVotedForEvent(EHUser user, Event event) {
        return voteRepository.existsByUserAndEvent(user, event);
    }
    
    /**
     * Recalculate and update vote counts for an event
     * (useful for data consistency)
     */
    public void recalculateVoteCounts(Event event) {
        long upvotes = voteRepository.countByEventAndIsUpvoteTrue(event);
        long downvotes = voteRepository.countByEventAndIsUpvoteFalse(event);
        
        event.setEventUpVote((int) upvotes);
        event.setEventDownVote((int) downvotes);
        eventRepository.save(event);
    }
}