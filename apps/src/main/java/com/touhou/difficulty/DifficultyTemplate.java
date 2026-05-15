package com.touhou.difficulty;

import com.touhou.components.Enemy;
import com.touhou.components.Hero;

public abstract class DifficultyTemplate {
    private static final int PROGRESSION_INTERVAL_TICKS = 600;

    public final int action(GameLoopContext context) {
        beforeCharactersAdvance(context);
        context.characterSystem().onTick(
                context.playfieldWidth(),
                context.playfieldHeight(),
                context.score(),
                context.tickCount());
        int scoreGained = context.collisionSystem().resolve(
                context.game().getHero(),
                context.game().mutableEnemies(),
                context.game().mutableProjectiles(),
                context.game().mutableItems());
        afterCollisions(context, scoreGained);
        return scoreGained;
    }

    public final int enemySpawnIntervalTicks() {
        return enemySpawnIntervalTicks(0);
    }

    public final int enemySpawnIntervalTicks(int tickCount) {
        return Math.max(12, baseEnemySpawnIntervalTicks() + enemySpawnIntervalDelta(tickCount));
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
        configureEnemy(enemy, 0, 1);
    }

    public final void configureEnemy(Enemy enemy, int tickCount, int bossSpawnCount) {
        int adjustedVelocityX = scaleVelocity(enemy.getVelocityX(), enemySpeedMultiplier(tickCount));
        int adjustedVelocityY = Math.max(1, scaleVelocity(enemy.getVelocityY(), enemySpeedMultiplier(tickCount)));
        enemy.setVelocity(adjustedVelocityX, adjustedVelocityY);
        if (enemy.isBoss()) {
            enemy.increaseMaxHealth(bossHealthIncrease(bossSpawnCount));
            enemy.setProjectileDamage(bossProjectileDamage(enemy.getBaseProjectileDamage(), tickCount, bossSpawnCount));
        } else {
            enemy.increaseMaxHealth(enemyHealthIncrease(tickCount));
            enemy.setFireIntervalTicks(enemyFireIntervalTicks(enemy.getFireIntervalTicks(), tickCount));
            enemy.setProjectileDamage(enemyProjectileDamage(enemy.getBaseProjectileDamage(), tickCount));
        }
    }

    public final boolean shouldSpawnBoss(boolean bossPresent, int score, int nextBossScoreThreshold) {
        return canSpawnBoss() && !bossPresent && score >= nextBossScoreThreshold;
    }

    public final int enemyFactoryWeight(int factoryIndex) {
        int[] weights = enemyFactoryWeights();
        if (factoryIndex < 0 || factoryIndex >= weights.length) {
            return 0;
        }
        return Math.max(0, weights[factoryIndex]);
    }

    public final int progressionLevel(int tickCount) {
        if (!hasProgression()) {
            return 0;
        }
        return Math.max(0, tickCount / PROGRESSION_INTERVAL_TICKS);
    }

    public boolean canSpawnBoss() {
        return true;
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

    protected int enemySpawnIntervalDelta(int tickCount) {
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

    protected double enemySpeedMultiplier(int tickCount) {
        return 1.0;
    }

    protected int enemyHealthIncrease(int tickCount) {
        return 0;
    }

    protected int bossHealthIncrease(int bossSpawnCount) {
        return 0;
    }

    protected int heroFireIntervalTicks(int baseFireIntervalTicks, int tickCount) {
        return baseFireIntervalTicks;
    }

    protected int enemyFireIntervalTicks(int baseFireIntervalTicks, int tickCount) {
        return baseFireIntervalTicks;
    }

    protected int enemyProjectileDamage(int baseProjectileDamage, int tickCount) {
        return baseProjectileDamage;
    }

    protected int bossProjectileDamage(int baseProjectileDamage, int tickCount, int bossSpawnCount) {
        return enemyProjectileDamage(baseProjectileDamage, tickCount);
    }

    protected boolean hasProgression() {
        return false;
    }

    protected int[] enemyFactoryWeights() {
        return new int[] {4, 3, 2, 1};
    }

    protected void afterCollisions(GameLoopContext context, int scoreGained) {
    }

    private void beforeCharactersAdvance(GameLoopContext context) {
        Hero hero = context.game().getHero();
        hero.setFireIntervalTicks(heroFireIntervalTicks(hero.getBaseFireIntervalTicks(), context.tickCount()));
        int level = progressionLevel(context.tickCount());
        if (level > context.game().getReportedDifficultyLevel()) {
            context.game().setReportedDifficultyLevel(level);
            System.out.println(context.game().getDifficulty().getDisplayName() + " difficulty increased to level " + level);
        }
    }

    private int scaleVelocity(int velocity, double multiplier) {
        return (int) Math.round(velocity * multiplier);
    }
}
