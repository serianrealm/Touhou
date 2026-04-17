package com.touhou.leaderboard;

import java.util.List;

public final class LeaderboardPresenter {
    private LeaderboardPresenter() {
    }

    public static String format(GameDifficulty difficulty, List<LeaderboardEntry> entries, int limit) {
        StringBuilder builder = new StringBuilder();
        builder.append("Leaderboard [").append(difficulty.getDisplayName()).append(']').append(System.lineSeparator());

        if (entries.isEmpty()) {
            builder.append("No scores yet.");
            return builder.toString();
        }

        int count = Math.min(limit, entries.size());
        for (int index = 0; index < count; index++) {
            LeaderboardEntry entry = entries.get(index);
            builder.append(index + 1)
                    .append(". ")
                    .append(entry.playerName())
                    .append(" | ")
                    .append(entry.score())
                    .append(" | ")
                    .append(entry.formattedPlayedAt());

            if (index + 1 < count) {
                builder.append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
