package com.touhou.strategy;

import java.util.ArrayList;
import java.util.List;

import com.touhou.components.Aircraft;
import com.touhou.components.Projectile;

public class FanShotStrategy implements FireStrategy {
    private final int bulletCount;
    private final double speed;
    private final double arcRadians;
    private final double centerAngleRadians;

    public FanShotStrategy(int bulletCount, double speed, double arcRadians, double centerAngleRadians) {
        this.bulletCount = bulletCount;
        this.speed = speed;
        this.arcRadians = arcRadians;
        this.centerAngleRadians = centerAngleRadians;
    }

    @Override
    public List<Projectile> fire(Aircraft aircraft) {
        List<Projectile> bullets = new ArrayList<>();
        double startAngle = centerAngleRadians - arcRadians / 2;
        double step = bulletCount == 1 ? 0.0 : arcRadians / (bulletCount - 1);

        for (int index = 0; index < bulletCount; index++) {
            double angle = startAngle + index * step;
            bullets.add(Projectile.fromPolar(
                    aircraft.getX(),
                    aircraft.getY() - aircraft.getHeight() / 2,
                    speed,
                    angle,
                    aircraft.getProjectileDamage(),
                    aircraft.getProjectileOwner()));
        }

        return bullets;
    }
}
