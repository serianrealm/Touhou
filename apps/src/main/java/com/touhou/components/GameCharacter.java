package com.touhou.components;

import java.awt.Color;
import java.awt.Rectangle;

public abstract class GameCharacter {
    private int x;
    private int y;
    private int width;
    private int height;
    private int velocityX;
    private int velocityY;
    private final int maxHealth;
    private int health;
    private final Color fallbackColor;

    protected GameCharacter(
            int x,
            int y,
            int width,
            int height,
            int velocityX,
            int velocityY,
            int maxHealth,
            Color fallbackColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.fallbackColor = fallbackColor;
    }

    public void tick() {
        x += velocityX;
        y += velocityY;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public void applyDamage(int damage) {
        health = Math.max(0, health - Math.max(0, damage));
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + Math.max(0, amount));
    }

    public void vanish() {
        health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public Color getFallbackColor() {
        return fallbackColor;
    }

    public String getSpriteResource() {
        return null;
    }
}
