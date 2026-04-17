package com.touhou.observer;

import java.util.List;

import com.touhou.components.Enemy;
import com.touhou.components.ItemType;
import com.touhou.components.Projectile;

public class EnemyPowerUpObserver implements PowerUpObserver {
    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;
    private final int freezeDurationTicks;

    public EnemyPowerUpObserver(List<Enemy> enemies, List<Projectile> projectiles, int freezeDurationTicks) {
        this.enemies = enemies;
        this.projectiles = projectiles;
        this.freezeDurationTicks = freezeDurationTicks;
    }

    @Override
    public void onPowerUpActivated(PowerUpEvent event) {
        if (event.type() == ItemType.BOMB) {
            for (Enemy enemy : enemies) {
                if (enemy.isBoss()) {
                    enemy.applyDamage(10);
                } else {
                    enemy.vanish();
                }
            }
            projectiles.removeIf(projectile -> projectile.getOwner() == Projectile.Owner.ENEMY);
            return;
        }

        if (event.type() == ItemType.FREEZE) {
            enemies.forEach(enemy -> enemy.freeze(freezeDurationTicks));
        }
    }
}
