package com.touhou;

import java.awt.GraphicsEnvironment;

import javax.swing.SwingUtilities;

import com.touhou.core.Game;
import com.touhou.leaderboard.GameDifficulty;
import com.touhou.ui.AppController;
import com.touhou.ui.Layout;

public final class Main {
    private Main() {
    }

    public static Game createGame() {
        return new Game();
    }

    public static Game createGame(GameDifficulty difficulty, String playerName) {
        return new Game(difficulty, playerName);
    }

    public static void main(String[] args) {
        GameDifficulty difficulty = GameDifficulty.fromArgs(args);
        String playerName = args != null && args.length > 1 ? args[1] : System.getProperty("user.name", "Player");

        if (Boolean.getBoolean("touhou.testMode") || GraphicsEnvironment.isHeadless()) {
            createGame(difficulty, playerName);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Layout layout = new Layout();
            AppController appController = new AppController(layout);
            appController.showMenu();
        });
    }
}
