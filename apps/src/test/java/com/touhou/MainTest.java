package com.touhou;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.touhou.core.Game;

class MainTest {
    @Test
    void mainBootstrapsWithoutThrowingInTestMode() {
        String previousValue = System.getProperty("touhou.testMode");
        System.setProperty("touhou.testMode", "true");

        try {
            assertDoesNotThrow(() -> Main.main(new String[0]));
        } finally {
            if (previousValue == null) {
                System.clearProperty("touhou.testMode");
            } else {
                System.setProperty("touhou.testMode", previousValue);
            }
        }
    }

    @Test
    void createGameReturnsUsableGamePanel() {
        Game game = Main.createGame();

        assertNotNull(game.getHero());
        assertDoesNotThrow(game::tick);
    }
}
