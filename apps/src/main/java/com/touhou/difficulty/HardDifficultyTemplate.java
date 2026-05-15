package com.touhou.difficulty;

public class HardDifficultyTemplate extends DifficultyTemplate {
    @Override
    protected int enemySpawnIntervalDelta(int tickCount) {
        return -10 - progressionLevel(tickCount) * 3;
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
    protected double enemySpeedMultiplier(int tickCount) {
        return 1.2 + progressionLevel(tickCount) * 0.08;
    }

    @Override
    protected int enemyHealthIncrease(int tickCount) {
        return progressionLevel(tickCount);
    }

    @Override
    protected int bossHealthIncrease(int bossSpawnCount) {
        return Math.max(0, bossSpawnCount - 1) * 8;
    }

    @Override
    protected int heroFireIntervalTicks(int baseFireIntervalTicks, int tickCount) {
        return Math.max(5, baseFireIntervalTicks + 2 - progressionLevel(tickCount));
    }

    @Override
    protected int enemyFireIntervalTicks(int baseFireIntervalTicks, int tickCount) {
        return Math.max(25, baseFireIntervalTicks - 8 - progressionLevel(tickCount) * 4);
    }

    @Override
    protected boolean hasProgression() {
        return true;
    }

    @Override
    protected int[] enemyFactoryWeights() {
        return new int[] {2, 3, 3, 2};
    }
}
