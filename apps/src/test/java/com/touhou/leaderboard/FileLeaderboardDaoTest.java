package com.touhou.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileLeaderboardDaoTest {
    @TempDir
    Path tempDir;

    @Test
    void storesEachDifficultyInItsOwnFile() {
        FileLeaderboardDao dao = new FileLeaderboardDao(tempDir);

        dao.save(GameDifficulty.EASY, new LeaderboardEntry("Reimu", 15, LocalDateTime.of(2026, 4, 17, 10, 0)));
        dao.save(GameDifficulty.HARD, new LeaderboardEntry("Marisa", 30, LocalDateTime.of(2026, 4, 17, 10, 5)));

        List<LeaderboardEntry> easyEntries = dao.findAll(GameDifficulty.EASY);
        List<LeaderboardEntry> hardEntries = dao.findAll(GameDifficulty.HARD);

        assertEquals(1, easyEntries.size());
        assertEquals("Reimu", easyEntries.get(0).playerName());
        assertEquals(1, hardEntries.size());
        assertEquals("Marisa", hardEntries.get(0).playerName());
    }

    @Test
    void returnsEntriesOrderedByScoreThenTimeDescending() {
        FileLeaderboardDao dao = new FileLeaderboardDao(tempDir);

        dao.save(GameDifficulty.NORMAL, new LeaderboardEntry("Alice", 20, LocalDateTime.of(2026, 4, 17, 10, 0)));
        dao.save(GameDifficulty.NORMAL, new LeaderboardEntry("Sakuya", 40, LocalDateTime.of(2026, 4, 17, 9, 0)));
        dao.save(GameDifficulty.NORMAL, new LeaderboardEntry("Youmu", 40, LocalDateTime.of(2026, 4, 17, 11, 0)));

        List<LeaderboardEntry> entries = dao.findAll(GameDifficulty.NORMAL);

        assertEquals(List.of("Youmu", "Sakuya", "Alice"), entries.stream().map(LeaderboardEntry::playerName).toList());
    }

    @Test
    void deletesSelectedEntryForOneDifficulty() {
        FileLeaderboardDao dao = new FileLeaderboardDao(tempDir);
        LeaderboardEntry easyEntry = new LeaderboardEntry("Reimu", 15, LocalDateTime.of(2026, 4, 17, 10, 0));
        LeaderboardEntry normalEntry = new LeaderboardEntry("Marisa", 30, LocalDateTime.of(2026, 4, 17, 10, 5));

        dao.save(GameDifficulty.EASY, easyEntry);
        dao.save(GameDifficulty.NORMAL, normalEntry);
        dao.delete(GameDifficulty.EASY, easyEntry);

        assertEquals(List.of(), dao.findAll(GameDifficulty.EASY));
        assertEquals(List.of(normalEntry), dao.findAll(GameDifficulty.NORMAL));
    }
}
