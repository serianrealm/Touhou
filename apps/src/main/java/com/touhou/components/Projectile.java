package com.touhou.components;

import java.awt.Color;

public class Projectile extends GameCharacter {
    public enum Owner {
        HERO,
        ENEMY
    }

    private final int damage;
    private final Owner owner;
    private int frozenTicks;

    public Projectile(int x, int y, int velocityX, int velocityY, int damage, Owner owner) {
        super(
                x,
                y,
                owner == Owner.HERO ? 12 : 14,
                owner == Owner.HERO ? 28 : 20,
                velocityX,
                velocityY,
                1,
                owner == Owner.HERO ? new Color(120, 225, 255) : new Color(255, 180, 90));
        this.damage = damage;
        this.owner = owner;
    }

    public static Projectile fromPolar(int x, int y, double speed, double angleRadians, int damage, Owner owner) {
        int velocityX = (int) Math.round(Math.cos(angleRadians) * speed);
        int velocityY = (int) Math.round(Math.sin(angleRadians) * speed);
        return new Projectile(x, y, velocityX, velocityY, damage, owner);
    }

    public int getDamage() {
        return damage;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public void tick() {
        if (frozenTicks > 0) {
            frozenTicks--;
            return;
        }
        super.tick();
    }

    public void freeze(int ticks) {
        frozenTicks = Math.max(frozenTicks, ticks);
    }

    public boolean isFrozen() {
        return frozenTicks > 0;
    }

    @Override
    public String getSpriteResource() {
        return owner == Owner.HERO ? "/textures/bullet_hero.png" : "/textures/bullet_enemy.png";
    }
}
