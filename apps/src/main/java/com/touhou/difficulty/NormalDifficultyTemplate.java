package com.touhou.difficulty;

public class NormalDifficultyTemplate extends DifficultyTemplate {
    @Override
    protected int enemySpawnIntervalDelta(int tickCount) {
        return -progressionLevel(tickCount) * 2;
    }

    @Override
    protected double enemySpeedMultiplier(int tickCount) {
        return 1.0 + progressionLevel(tickCount) * 0.05;
    }

    @Override
    protected int enemyHealthIncrease(int tickCount) {
        return progressionLevel(tickCount) / 2;
    }

    @Override
    protected boolean hasProgression() {
        return true;
    }
}
