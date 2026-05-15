package com.touhou.difficulty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.touhou.components.BossEnemy;

class DifficultyTemplateTest {
    @Test
    void hardDifficultyIsMoreAggressiveThanEasy() {
        DifficultyTemplate easy = new EasyDifficultyTemplate();
        DifficultyTemplate hard = new HardDifficultyTemplate();

        assertTrue(hard.enemySpawnIntervalTicks() < easy.enemySpawnIntervalTicks());
        assertTrue(hard.maxNonBossEnemies() > easy.maxNonBossEnemies());
        assertTrue(hard.initialBossScoreThreshold() < easy.initialBossScoreThreshold());
        assertTrue(hard.enemyFactoryWeight(3) > easy.enemyFactoryWeight(3));
    }

    @Test
    void easyDifficultyDoesNotSpawnBoss() {
        DifficultyTemplate easy = new EasyDifficultyTemplate();

        assertFalse(easy.shouldSpawnBoss(false, 999, 10));
    }

    @Test
    void hardDifficultyIncreasesBossHealthBySpawnCount() {
        DifficultyTemplate hard = new HardDifficultyTemplate();
        BossEnemy firstBoss = new BossEnemy(300, 3);
        BossEnemy secondBoss = new BossEnemy(300, 3);

        hard.configureEnemy(firstBoss, 0, 1);
        hard.configureEnemy(secondBoss, 0, 2);

        assertTrue(secondBoss.getMaxHealth() > firstBoss.getMaxHealth());
    }

    @Test
    void normalDifficultyKeepsBossHealthFixed() {
        DifficultyTemplate normal = new NormalDifficultyTemplate();
        BossEnemy earlyBoss = new BossEnemy(300, 3);
        BossEnemy lateBoss = new BossEnemy(300, 3);

        normal.configureEnemy(earlyBoss, 0, 1);
        normal.configureEnemy(lateBoss, 1800, 3);

        assertEquals(earlyBoss.getMaxHealth(), lateBoss.getMaxHealth());
    }

    @Test
    void normalAndHardProgressOverTime() {
        DifficultyTemplate normal = new NormalDifficultyTemplate();
        DifficultyTemplate hard = new HardDifficultyTemplate();

        assertTrue(normal.enemySpawnIntervalTicks(1200) < normal.enemySpawnIntervalTicks(0));
        assertTrue(hard.enemySpawnIntervalTicks(1200) < hard.enemySpawnIntervalTicks(0));
    }
}
