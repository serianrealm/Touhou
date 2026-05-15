package com.touhou.difficulty;

public class EasyDifficultyTemplate extends DifficultyTemplate {
    @Override
    public boolean canSpawnBoss() {
        return false;
    }

    @Override
    protected int enemySpawnIntervalDelta(int tickCount) {
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
    protected double enemySpeedMultiplier(int tickCount) {
        return 0.85;
    }

    @Override
    protected int heroFireIntervalTicks(int baseFireIntervalTicks, int tickCount) {
        return Math.max(5, baseFireIntervalTicks - 2);
    }

    @Override
    protected int enemyFireIntervalTicks(int baseFireIntervalTicks, int tickCount) {
        return baseFireIntervalTicks + 12;
    }

    @Override
    protected int[] enemyFactoryWeights() {
        return new int[] {7, 2, 1, 0};
    }
}
