package com.touhou.ui;

import java.nio.file.Path;

import com.touhou.audio.AudioManager;
import com.touhou.core.Game;
import com.touhou.leaderboard.FileLeaderboardDao;
import com.touhou.leaderboard.GameDifficulty;
import com.touhou.leaderboard.LeaderboardDao;

public class AppController {
    private final Layout layout;
    private final LeaderboardDao leaderboardDao;
    private final AudioManager audioManager;

    public AppController(Layout layout) {
        this.layout = layout;
        this.leaderboardDao = new FileLeaderboardDao(Path.of(System.getProperty("touhou.leaderboardDir", "leaderboards")));
        this.audioManager = new AudioManager();
    }

    public void showMenu() {
        layout.showPanel(new MenuPanel(this, audioManager));
    }

    public void showLeaderboard(GameDifficulty selectedDifficulty) {
        layout.showPanel(new LeaderboardPanel(this, leaderboardDao, selectedDifficulty));
    }

    public void startGame(GameDifficulty difficulty, String playerName) {
        Game game = new Game(difficulty, playerName, leaderboardDao, audioManager, this::showLeaderboard);
        layout.showPanel(game);
        game.start();
    }
}
