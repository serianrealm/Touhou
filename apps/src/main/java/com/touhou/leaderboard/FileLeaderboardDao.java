package com.touhou.leaderboard;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileLeaderboardDao implements LeaderboardDao {
    private static final String SEPARATOR = "\t";

    private final Path baseDirectory;

    public FileLeaderboardDao(Path baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void save(GameDifficulty difficulty, LeaderboardEntry entry) {
        Path storagePath = resolveStoragePath(difficulty);
        String line = sanitize(entry.playerName()) + SEPARATOR + entry.score() + SEPARATOR + entry.playedAt();

        try {
            Files.createDirectories(baseDirectory);
            Files.writeString(
                    storagePath,
                    line + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    Files.exists(storagePath)
                            ? java.nio.file.StandardOpenOption.APPEND
                            : java.nio.file.StandardOpenOption.CREATE);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to save leaderboard entry", exception);
        }
    }

    @Override
    public List<LeaderboardEntry> findAll(GameDifficulty difficulty) {
        Path storagePath = resolveStoragePath(difficulty);
        if (!Files.exists(storagePath)) {
            return List.of();
        }

        try {
            List<LeaderboardEntry> entries = new ArrayList<>();
            for (String line : Files.readAllLines(storagePath, StandardCharsets.UTF_8)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(SEPARATOR, 3);
                if (columns.length != 3) {
                    continue;
                }
                entries.add(new LeaderboardEntry(
                        columns[0],
                        Integer.parseInt(columns[1]),
                        LocalDateTime.parse(columns[2])));
            }
            entries.sort(Comparator
                    .comparingInt(LeaderboardEntry::score)
                    .reversed()
                    .thenComparing(LeaderboardEntry::playedAt, Comparator.reverseOrder()));
            return entries;
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read leaderboard entries", exception);
        }
    }

    private Path resolveStoragePath(GameDifficulty difficulty) {
        return baseDirectory.resolve("leaderboard-" + difficulty.getFileKey() + ".tsv");
    }

    private String sanitize(String playerName) {
        return playerName.replace('\t', ' ').trim();
    }
}
