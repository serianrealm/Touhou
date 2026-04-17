package com.touhou.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.touhou.audio.AudioManager;
import com.touhou.components.Enemy;
import com.touhou.components.Hero;
import com.touhou.components.Item;
import com.touhou.components.Projectile;
import com.touhou.difficulty.NormalDifficultyTemplate;
import com.touhou.factories.BossEnemyFactory;
import com.touhou.leaderboard.LeaderboardDao;
import com.touhou.leaderboard.LeaderboardEntry;
import com.touhou.leaderboard.GameDifficulty;

class GameTest {
    @BeforeEach
    void setUp() {
        Hero.resetInstance();
    }

    @AfterEach
    void tearDown() {
        Hero.resetInstance();
    }

    @Test
    void tickIsSafeWithNoSpawnedEnemiesOrItems() {
        Game game = new Game(
                GameDifficulty.NORMAL,
                "Player",
                new InMemoryLeaderboardDao(),
                new AudioManager());

        assertEquals(0, game.getEnemies().size());
        assertEquals(0, game.getItems().size());
        assertDoesNotThrow(game::tick);
    }

    @Test
    void characterSystemSpawnsBossWhenScoreThresholdIsReached() {
        Hero hero = Hero.getInstance(400, 700);
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Projectile> projectiles = new ArrayList<>();
        ArrayList<Item> items = new ArrayList<>();
        CharacterSystem characterSystem = new CharacterSystem(
                new JPanel(),
                hero,
                enemies,
                projectiles,
                items,
                new NormalDifficultyTemplate(),
                new Random(0));

        characterSystem.onTick(1920, 1080, 30);

        assertTrue(enemies.stream().anyMatch(Enemy::isBoss));
    }

    @Test
    void heroFirepowerChangesProjectilePattern() {
        Hero hero = Hero.getInstance(300, 500);

        assertEquals(1, hero.tryFire().size());
        hero.applyFirepowerSupply(8);
        assertEquals(3, hero.tryFire().size());
        hero.applySuperFirepowerSupply(8);
        assertEquals(12, hero.tryFire().size());
    }

    @Test
    void bossFactoryCreatesBossEnemy() {
        Enemy enemy = new BossEnemyFactory().createEnemy(new Random(0), 1920);
        assertTrue(enemy.isBoss());
    }

    private static final class InMemoryLeaderboardDao implements LeaderboardDao {
        @Override
        public void save(GameDifficulty difficulty, LeaderboardEntry entry) {
        }

        @Override
        public List<LeaderboardEntry> findAll(GameDifficulty difficulty) {
            return List.of();
        }
    }
}
