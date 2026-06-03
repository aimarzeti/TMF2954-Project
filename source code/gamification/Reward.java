import java.util.List;

/**
 * Interface: Reward
 * Creator: Zeti Nur Aimar binti Ali
 * Tester: G04/SE Group 14
 * Purpose: Defines the reward contract used by the gamification module.
 */
public interface Reward {
    int calculatePoints();
    int calculateStars();
    List<String> getUnlockedBadges();
    String getRewardSummary();
}
