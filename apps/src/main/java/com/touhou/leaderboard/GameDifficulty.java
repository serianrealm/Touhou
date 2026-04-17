package com.touhou.leaderboard;

import com.touhou.difficulty.DifficultyTemplate;
import com.touhou.difficulty.EasyDifficultyTemplate;
import com.touhou.difficulty.HardDifficultyTemplate;
import com.touhou.difficulty.NormalDifficultyTemplate;

public enum GameDifficulty {
    EASY("easy", "Easy"),
    NORMAL("normal", "Normal"),
    HARD("hard", "Hard");

    private final String fileKey;
    private final String displayName;

    GameDifficulty(String fileKey, String displayName) {
        this.fileKey = fileKey;
        this.displayName = displayName;
    }

    public String getFileKey() {
        return fileKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DifficultyTemplate createTemplate() {
        return switch (this) {
            case EASY -> new EasyDifficultyTemplate();
            case NORMAL -> new NormalDifficultyTemplate();
            case HARD -> new HardDifficultyTemplate();
        };
    }

    public static GameDifficulty fromArgs(String[] args) {
        if (args == null || args.length == 0) {
            return NORMAL;
        }

        for (GameDifficulty difficulty : values()) {
            if (difficulty.fileKey.equalsIgnoreCase(args[0]) || difficulty.name().equalsIgnoreCase(args[0])) {
                return difficulty;
            }
        }

        return NORMAL;
    }
}
