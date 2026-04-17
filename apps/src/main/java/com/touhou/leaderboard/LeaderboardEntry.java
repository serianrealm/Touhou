package com.touhou.leaderboard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LeaderboardEntry(String playerName, int score, LocalDateTime playedAt) {
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formattedPlayedAt() {
        return playedAt.format(DISPLAY_FORMATTER);
    }
}
