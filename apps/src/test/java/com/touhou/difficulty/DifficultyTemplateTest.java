package com.touhou.difficulty;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DifficultyTemplateTest {
    @Test
    void hardDifficultyIsMoreAggressiveThanEasy() {
        DifficultyTemplate easy = new EasyDifficultyTemplate();
        DifficultyTemplate hard = new HardDifficultyTemplate();

        assertTrue(hard.enemySpawnIntervalTicks() < easy.enemySpawnIntervalTicks());
        assertTrue(hard.maxNonBossEnemies() > easy.maxNonBossEnemies());
        assertTrue(hard.initialBossScoreThreshold() < easy.initialBossScoreThreshold());
    }
}
