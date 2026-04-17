package com.touhou.strategy;

import java.util.List;

import com.touhou.components.Aircraft;
import com.touhou.components.Projectile;

public class SingleForwardFireStrategy implements FireStrategy {
    private final int velocityY;

    public SingleForwardFireStrategy(int velocityY) {
        this.velocityY = velocityY;
    }

    @Override
    public List<Projectile> fire(Aircraft aircraft) {
        return List.of(new Projectile(
                aircraft.getX(),
                aircraft.getY() + (velocityY < 0 ? -aircraft.getHeight() / 2 : aircraft.getHeight() / 2),
                0,
                velocityY,
                aircraft.getProjectileDamage(),
                aircraft.getProjectileOwner()));
    }
}
