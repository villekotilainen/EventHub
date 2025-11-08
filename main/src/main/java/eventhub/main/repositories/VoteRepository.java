package eventhub.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eventhub.main.domain.EHUser;
import eventhub.main.domain.Event;
import eventhub.main.domain.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    /**
     * Find a vote by user and event
     */
    Optional<Vote> findByUserAndEvent(EHUser user, Event event);
    
    /**
     * Check if a user has voted for an event
     */
    boolean existsByUserAndEvent(EHUser user, Event event);
    
    /**
     * Delete a vote by user and event
     */
    void deleteByUserAndEvent(EHUser user, Event event);
    
    /**
     * Count total upvotes for an event
     */
    long countByEventAndIsUpvoteTrue(Event event);
    
    /**
     * Count total downvotes for an event
     */
    long countByEventAndIsUpvoteFalse(Event event);
}