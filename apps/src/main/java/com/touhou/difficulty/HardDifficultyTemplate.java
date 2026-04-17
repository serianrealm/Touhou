package com.touhou.difficulty;

public class HardDifficultyTemplate extends DifficultyTemplate {
    @Override
    protected int enemySpawnIntervalDelta() {
        return -10;
    }

    @Override
    protected int maxNonBossEnemyDelta() {
        return 4;
    }

    @Override
    protected int initialBossScoreThresholdDelta() {
        return -8;
    }

    @Override
    protected int bossThresholdStepDelta() {
        return -10;
    }

    @Override
    protected int freezeDurationTicksDelta() {
        return -60;
    }

    @Override
    protected int weaponPowerUpDurationDelta() {
        return -2;
    }

    @Override
    protected double enemySpeedMultiplier() {
        return 1.2;
    }
}
