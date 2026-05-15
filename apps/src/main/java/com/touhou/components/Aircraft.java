package com.touhou.components;

import java.awt.Color;
import java.util.List;

import com.touhou.strategy.FireStrategy;

public abstract class Aircraft extends GameCharacter {
    private FireStrategy fireStrategy;
    private final Projectile.Owner projectileOwner;
    private final int baseProjectileDamage;
    private int projectileDamage;
    private final int baseFireIntervalTicks;
    private int fireIntervalTicks;
    private int fireCooldown;

    protected Aircraft(
            int x,
            int y,
            int width,
            int height,
            int velocityX,
            int velocityY,
            int maxHealth,
            Color fallbackColor,
            Projectile.Owner projectileOwner,
            int projectileDamage,
            int fireIntervalTicks,
            FireStrategy fireStrategy) {
        super(x, y, width, height, velocityX, velocityY, maxHealth, fallbackColor);
        this.fireStrategy = fireStrategy;
        this.projectileOwner = projectileOwner;
        this.baseProjectileDamage = projectileDamage;
        this.projectileDamage = projectileDamage;
        this.baseFireIntervalTicks = fireIntervalTicks;
        this.fireIntervalTicks = fireIntervalTicks;
        this.fireCooldown = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (fireCooldown > 0) {
            fireCooldown--;
        }
    }

    public List<Projectile> tryFire() {
        if (!isAlive() || fireStrategy == null || fireCooldown > 0) {
            return List.of();
        }

        fireCooldown = fireIntervalTicks;
        return fireStrategy.fire(this);
    }

    public void setFireStrategy(FireStrategy fireStrategy) {
        this.fireStrategy = fireStrategy;
        this.fireCooldown = 0;
    }

    public FireStrategy getFireStrategy() {
        return fireStrategy;
    }

    public Projectile.Owner getProjectileOwner() {
        return projectileOwner;
    }

    public int getProjectileDamage() {
        return projectileDamage;
    }

    public int getBaseProjectileDamage() {
        return baseProjectileDamage;
    }

    public void setProjectileDamage(int projectileDamage) {
        this.projectileDamage = Math.max(1, projectileDamage);
    }

    public int getFireIntervalTicks() {
        return fireIntervalTicks;
    }

    public int getBaseFireIntervalTicks() {
        return baseFireIntervalTicks;
    }

    public void setFireIntervalTicks(int fireIntervalTicks) {
        this.fireIntervalTicks = Math.max(1, fireIntervalTicks);
    }
}
