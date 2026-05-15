package com.touhou.difficulty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.touhou.components.BossEnemy;
import com.touhou.components.EliteEnemy;

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

    @Test
    void hardDifficultyImprovesEnemyProjectileDamage() {
        DifficultyTemplate hard = new HardDifficultyTemplate();
        EliteEnemy enemy = new EliteEnemy(300, 0, 3);

        hard.configureEnemy(enemy, 0, 1);

        assertTrue(enemy.getProjectileDamage() > enemy.getBaseProjectileDamage());
    }

    @Test
    void normalDifficultyProjectileDamageScalesLater() {
        DifficultyTemplate normal = new NormalDifficultyTemplate();
        EliteEnemy earlyEnemy = new EliteEnemy(300, 0, 3);
        EliteEnemy lateEnemy = new EliteEnemy(300, 0, 3);

        normal.configureEnemy(earlyEnemy, 0, 1);
        normal.configureEnemy(lateEnemy, 1800, 1);

        assertTrue(lateEnemy.getProjectileDamage() > earlyEnemy.getProjectileDamage());
    }

    @Test
    void hardBossProjectileDamageImprovesAcrossSpawns() {
        DifficultyTemplate hard = new HardDifficultyTemplate();
        BossEnemy firstBoss = new BossEnemy(300, 3);
        BossEnemy laterBoss = new BossEnemy(300, 3);

        hard.configureEnemy(firstBoss, 0, 1);
        hard.configureEnemy(laterBoss, 0, 3);

        assertTrue(laterBoss.getProjectileDamage() > firstBoss.getProjectileDamage());
    }
}
