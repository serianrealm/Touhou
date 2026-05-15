package com.touhou.components;

import java.awt.Color;
import java.util.List;

import com.touhou.strategy.FireStrategy;

public abstract class Enemy extends Aircraft {
    private final int scoreValue;
    private final double dropChance;
    private final List<ItemType> droppableItems;

    private int frozenTicks;
    private int slowTicks;
    private int savedVelocityX;
    private int savedVelocityY;
    private boolean dropsSuppressed;

    protected Enemy(
            int x,
            int y,
            int width,
            int height,
            int velocityX,
            int velocityY,
            int maxHealth,
            Color fallbackColor,
            int projectileDamage,
            int fireIntervalTicks,
            FireStrategy fireStrategy,
            int scoreValue,
            double dropChance,
            List<ItemType> droppableItems) {
        super(
                x,
                y,
                width,
                height,
                velocityX,
                velocityY,
                maxHealth,
                fallbackColor,
                Projectile.Owner.ENEMY,
                projectileDamage,
                fireIntervalTicks,
                fireStrategy);
        this.scoreValue = scoreValue;
        this.dropChance = dropChance;
        this.droppableItems = List.copyOf(droppableItems);
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public double getDropChance() {
        return dropChance;
    }

    public List<ItemType> getDroppableItems() {
        return droppableItems;
    }

    public int getDropCount() {
        return 1;
    }

    public boolean isBoss() {
        return false;
    }

    public void destroyWithoutDrop() {
        dropsSuppressed = true;
        vanish();
    }

    public boolean canDropItems() {
        return !dropsSuppressed;
    }

    public final void advance(int playfieldWidth, int playfieldHeight) {
        if (frozenTicks > 0) {
            frozenTicks--;
            return;
        }
        if (slowTicks > 0) {
            slowTicks--;
            if (slowTicks == 0) {
                setVelocity(savedVelocityX, savedVelocityY);
            }
        }
        advanceNormally(playfieldWidth, playfieldHeight);
    }

    public void freeze(int ticks) {
        frozenTicks = Math.max(frozenTicks, ticks);
    }

    public boolean isFrozen() {
        return frozenTicks > 0;
    }

    public void slow(int ticks, double multiplier) {
        if (ticks <= 0 || multiplier <= 0.0 || multiplier >= 1.0) {
            return;
        }
        if (slowTicks <= 0) {
            savedVelocityX = getVelocityX();
            savedVelocityY = getVelocityY();
        }
        setVelocity((int) Math.round(savedVelocityX * multiplier), Math.max(1, (int) Math.round(savedVelocityY * multiplier)));
        slowTicks = Math.max(slowTicks, ticks);
    }

    public boolean isSlowed() {
        return slowTicks > 0;
    }

    public boolean isMob() {
        return false;
    }

    public boolean isElite() {
        return false;
    }

    public boolean isElitePlus() {
        return false;
    }

    public boolean isAce() {
        return false;
    }

    protected void bounceHorizontally(int playfieldWidth) {
        int halfWidth = getWidth() / 2;
        if (getX() <= halfWidth || getX() >= playfieldWidth - halfWidth) {
            setVelocity(-getVelocityX(), getVelocityY());
        }
    }

    protected void advanceNormally(int playfieldWidth, int playfieldHeight) {
        tick();
    }
}
