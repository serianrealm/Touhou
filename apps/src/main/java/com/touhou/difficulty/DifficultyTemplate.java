package com.touhou.difficulty;

import com.touhou.components.Enemy;

public abstract class DifficultyTemplate {
    public final int enemySpawnIntervalTicks() {
        return Math.max(12, baseEnemySpawnIntervalTicks() + enemySpawnIntervalDelta());
    }

    public final int maxNonBossEnemies() {
        return Math.max(3, baseMaxNonBossEnemies() + maxNonBossEnemyDelta());
    }

    public final int initialBossScoreThreshold() {
        return Math.max(10, baseInitialBossScoreThreshold() + initialBossScoreThresholdDelta());
    }

    public final int bossThresholdStep() {
        return Math.max(15, baseBossThresholdStep() + bossThresholdStepDelta());
    }

    public final int freezeDurationTicks() {
        return Math.max(60, baseFreezeDurationTicks() + freezeDurationTicksDelta());
    }

    public final int weaponPowerUpDurationSeconds() {
        return Math.max(4, baseWeaponPowerUpDurationSeconds() + weaponPowerUpDurationDelta());
    }

    public final void configureEnemy(Enemy enemy) {
        int adjustedVelocityX = scaleVelocity(enemy.getVelocityX(), enemySpeedMultiplier());
        int adjustedVelocityY = Math.max(1, scaleVelocity(enemy.getVelocityY(), enemySpeedMultiplier()));
        enemy.setVelocity(adjustedVelocityX, adjustedVelocityY);
    }

    protected int baseEnemySpawnIntervalTicks() {
        return 40;
    }

    protected int baseMaxNonBossEnemies() {
        return 10;
    }

    protected int baseInitialBossScoreThreshold() {
        return 30;
    }

    protected int baseBossThresholdStep() {
        return 45;
    }

    protected int baseFreezeDurationTicks() {
        return 180;
    }

    protected int baseWeaponPowerUpDurationSeconds() {
        return 8;
    }

    protected int enemySpawnIntervalDelta() {
        return 0;
    }

    protected int maxNonBossEnemyDelta() {
        return 0;
    }

    protected int initialBossScoreThresholdDelta() {
        return 0;
    }

    protected int bossThresholdStepDelta() {
        return 0;
    }

    protected int freezeDurationTicksDelta() {
        return 0;
    }

    protected int weaponPowerUpDurationDelta() {
        return 0;
    }

    protected double enemySpeedMultiplier() {
        return 1.0;
    }

    private int scaleVelocity(int velocity, double multiplier) {
        return (int) Math.round(velocity * multiplier);
    }
}
