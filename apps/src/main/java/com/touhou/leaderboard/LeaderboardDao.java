package com.touhou.leaderboard;

import java.util.List;

public interface LeaderboardDao {
    void save(GameDifficulty difficulty, LeaderboardEntry entry);

    List<LeaderboardEntry> findAll(GameDifficulty difficulty);
}
