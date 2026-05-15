package com.touhou.observer;

import java.util.List;

import com.touhou.components.Enemy;
import com.touhou.components.ItemType;
import com.touhou.components.Projectile;

public class EnemyPowerUpObserver implements PowerUpObserver {
    private static final int TICKS_PER_SECOND = 60;
    private static final int MOB_PERMANENT_FREEZE_TICKS = Integer.MAX_VALUE;
    private static final int ELITE_FREEZE_TICKS = 4 * TICKS_PER_SECOND;
    private static final int ELITE_PLUS_FREEZE_TICKS = 3 * TICKS_PER_SECOND;
    private static final int ACE_SLOW_TICKS = 5 * TICKS_PER_SECOND;
    private static final int PROJECTILE_FREEZE_TICKS = 5 * TICKS_PER_SECOND;
    private static final double ACE_SLOW_MULTIPLIER = 0.45;
    private static final int ACE_BOMB_DAMAGE = 3;

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
                    continue;
                }
                if (enemy.isAce()) {
                    enemy.applyDamage(ACE_BOMB_DAMAGE);
                } else {
                    enemy.destroyWithoutDrop();
                }
            }
            projectiles.removeIf(projectile -> projectile.getOwner() == Projectile.Owner.ENEMY);
            return;
        }

        if (event.type() == ItemType.FREEZE) {
            for (Enemy enemy : enemies) {
                if (enemy.isBoss()) {
                    continue;
                }
                if (enemy.isMob()) {
                    enemy.freeze(MOB_PERMANENT_FREEZE_TICKS);
                } else if (enemy.isElite()) {
                    enemy.freeze(ELITE_FREEZE_TICKS);
                } else if (enemy.isElitePlus()) {
                    enemy.freeze(ELITE_PLUS_FREEZE_TICKS);
                } else if (enemy.isAce()) {
                    enemy.slow(ACE_SLOW_TICKS, ACE_SLOW_MULTIPLIER);
                } else {
                    enemy.freeze(freezeDurationTicks);
                }
            }
            for (Projectile projectile : projectiles) {
                if (projectile.getOwner() == Projectile.Owner.ENEMY) {
                    projectile.freeze(PROJECTILE_FREEZE_TICKS);
                }
            }
        }
    }
}
