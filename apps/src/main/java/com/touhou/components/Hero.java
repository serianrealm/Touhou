package com.touhou.components;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.touhou.strategy.FireStrategy;
import com.touhou.strategy.RadialShotStrategy;
import com.touhou.strategy.SingleForwardFireStrategy;
import com.touhou.strategy.SpreadShotStrategy;

public class Hero extends Aircraft {
    private static final int HEAL_AMOUNT = 2;
    private static Hero instance;

    private final FireStrategy defaultFireStrategy;
    private final ScheduledExecutorService powerUpScheduler;

    private ScheduledFuture<?> restoreFuture;

    public static synchronized Hero getInstance(int x, int y) {
        if (instance == null) {
            instance = new Hero(x, y, createPowerUpScheduler(), new SingleForwardFireStrategy(-14));
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }

    private static ScheduledExecutorService createPowerUpScheduler() {
        return Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "hero-powerup");
            thread.setDaemon(true);
            return thread;
        });
    }

    private Hero(int x, int y, ScheduledExecutorService powerUpScheduler, FireStrategy defaultFireStrategy) {
        super(
                x,
                y,
                84,
                96,
                0,
                0,
                8,
                new Color(102, 217, 255),
                Projectile.Owner.HERO,
                1,
                12,
                defaultFireStrategy);
        this.powerUpScheduler = powerUpScheduler;
        this.defaultFireStrategy = defaultFireStrategy;
    }

    public void moveTo(int x, int y) {
        setPosition(x, y);
    }

    public void applyHealthSupply() {
        heal(HEAL_AMOUNT);
    }

    public synchronized void applyFirepowerSupply(int durationSeconds) {
        activateTemporaryWeapon(new SpreadShotStrategy(3, -13, 4), durationSeconds);
    }

    public synchronized void applySuperFirepowerSupply(int durationSeconds) {
        activateTemporaryWeapon(new RadialShotStrategy(12, 9.0), durationSeconds);
    }

    public synchronized void restoreDefaultWeapon() {
        if (restoreFuture != null) {
            restoreFuture.cancel(false);
            restoreFuture = null;
        }
        setFireStrategy(defaultFireStrategy);
    }

    public void shutdown() {
        if (restoreFuture != null) {
            restoreFuture.cancel(false);
            restoreFuture = null;
        }
        if (powerUpScheduler != null && !powerUpScheduler.isShutdown()) {
            powerUpScheduler.shutdownNow();
        }
    }

    @Override
    public String getSpriteResource() {
        return "/textures/hero.png";
    }

    private void activateTemporaryWeapon(FireStrategy upgradedFireStrategy, int durationSeconds) {
        setFireStrategy(upgradedFireStrategy);

        if (restoreFuture != null) {
            restoreFuture.cancel(false);
        }

        restoreFuture = powerUpScheduler.schedule(
                () -> SwingUtilities.invokeLater(this::restoreDefaultWeapon),
                durationSeconds,
                TimeUnit.SECONDS);
    }
}
