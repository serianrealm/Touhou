package com.touhou.strategy;

import java.util.ArrayList;
import java.util.List;

import com.touhou.components.Aircraft;
import com.touhou.components.Projectile;

public class SpreadShotStrategy implements FireStrategy {
    private final int bulletCount;
    private final int velocityY;
    private final int horizontalVelocityStep;

    public SpreadShotStrategy(int bulletCount, int velocityY, int horizontalVelocityStep) {
        this.bulletCount = bulletCount;
        this.velocityY = velocityY;
        this.horizontalVelocityStep = horizontalVelocityStep;
    }

    @Override
    public List<Projectile> fire(Aircraft aircraft) {
        List<Projectile> bullets = new ArrayList<>();
        int middle = bulletCount / 2;

        for (int index = 0; index < bulletCount; index++) {
            int velocityX = (index - middle) * horizontalVelocityStep;
            bullets.add(new Projectile(
                    aircraft.getX(),
                    aircraft.getY() - aircraft.getHeight() / 2,
                    velocityX,
                    velocityY,
                    aircraft.getProjectileDamage(),
                    aircraft.getProjectileOwner()));
        }

        return bullets;
    }
}
