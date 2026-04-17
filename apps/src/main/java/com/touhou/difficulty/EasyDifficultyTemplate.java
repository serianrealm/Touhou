package com.touhou.difficulty;

public class EasyDifficultyTemplate extends DifficultyTemplate {
    @Override
    protected int enemySpawnIntervalDelta() {
        return 14;
    }

    @Override
    protected int maxNonBossEnemyDelta() {
        return -3;
    }

    @Override
    protected int initialBossScoreThresholdDelta() {
        return 15;
    }

    @Override
    protected int bossThresholdStepDelta() {
        return 15;
    }

    @Override
    protected int freezeDurationTicksDelta() {
        return 120;
    }

    @Override
    protected int weaponPowerUpDurationDelta() {
        return 2;
    }

    @Override
    protected double enemySpeedMultiplier() {
        return 0.85;
    }
}
