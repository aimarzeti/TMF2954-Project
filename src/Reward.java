import java.util.List;

/**
 * Interface: Reward
 * Creator: Zeti Nur Aimar binti Ali
 * Tester: G04/SE Group 14
 * Purpose: Defines the reward contract used by the gamification module.
 * Implemented by: GamificationModule
 */
public interface Reward {
    /**
     * Calculate total points earned.
     * @return Total points
     */
    int calculatePoints();

    /**
     * Calculate total stars earned.
     * @return Total stars
     */
    int calculateStars();

    /**
     * Get list of unlocked badge names.
     * @return List of unlocked badge names
     */
    List<String> getUnlockedBadges();

    /**
     * Get a summary of all rewards.
     * @return Reward summary string
     */
    String getRewardSummary();
}
