package com.touhou.strategy;

import java.util.ArrayList;
import java.util.List;

import com.touhou.components.Aircraft;
import com.touhou.components.Projectile;

public class StraightRowFireStrategy implements FireStrategy {
    private final int bulletsPerRow;
    private final int rowCount;
    private final int spacing;
    private final int velocityY;

    public StraightRowFireStrategy(int bulletsPerRow, int rowCount, int spacing, int velocityY) {
        this.bulletsPerRow = bulletsPerRow;
        this.rowCount = rowCount;
        this.spacing = spacing;
        this.velocityY = velocityY;
    }

    @Override
    public List<Projectile> fire(Aircraft aircraft) {
        List<Projectile> bullets = new ArrayList<>();
        int noseY = aircraft.getY() + aircraft.getHeight() / 2;
        int rowWidth = (bulletsPerRow - 1) * spacing;

        for (int row = 0; row < rowCount; row++) {
            int rowY = noseY + row * 16;
            for (int index = 0; index < bulletsPerRow; index++) {
                int x = aircraft.getX() - rowWidth / 2 + index * spacing;
                bullets.add(new Projectile(
                        x,
                        rowY,
                        0,
                        velocityY,
                        aircraft.getProjectileDamage(),
                        aircraft.getProjectileOwner()));
            }
        }

        return bullets;
    }
}
