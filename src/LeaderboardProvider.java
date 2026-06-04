import java.util.List;

/**
 * Interface: LeaderboardProvider
 * Purpose: Defines the contract for loading leaderboard data.
 * Implemented by: GamificationModule
 */

// for loading leaderboard data from storage, allowing for flexible implementations (for example, file-based, database, etc.)
public interface LeaderboardProvider {
    /**
     * Load leaderboard data from storage.
     * @return List of score entries sorted by rank
     * @throws GamificationDataException if data cannot be loaded or parsed
     */
    List<ScoreEntry> loadLeaderboard() throws GamificationDataException;
}
