package com.touhou.strategy;

import java.util.ArrayList;
import java.util.List;

import com.touhou.components.Aircraft;
import com.touhou.components.Projectile;

public class RadialShotStrategy implements FireStrategy {
    private final int bulletCount;
    private final double speed;

    public RadialShotStrategy(int bulletCount, double speed) {
        this.bulletCount = bulletCount;
        this.speed = speed;
    }

    @Override
    public List<Projectile> fire(Aircraft aircraft) {
        List<Projectile> bullets = new ArrayList<>();
        double step = 2 * Math.PI / bulletCount;

        for (int index = 0; index < bulletCount; index++) {
            bullets.add(Projectile.fromPolar(
                    aircraft.getX(),
                    aircraft.getY(),
                    speed,
                    -Math.PI / 2 + index * step,
                    aircraft.getProjectileDamage(),
                    aircraft.getProjectileOwner()));
        }

        return bullets;
    }
}
