/**
 * Class/Interface: LeaderboardProvider
 * Creator: Zeti Nur Aimar binti Ali
 * Tester: G04/SE Group 14
 * Description: Defines the contract for loading leaderboard data.
 * Implemented by: GamificationModule
 */

import java.util.List;

public interface LeaderboardProvider {
    /**
     * Load leaderboard data from storage.
     * @return List of score entries sorted by rank
     * @throws GamificationDataException if data cannot be loaded or parsed
     */
    List<ScoreEntry> loadLeaderboard() throws GamificationDataException;
}
