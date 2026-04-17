package com.touhou.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.touhou.components.BossEnemy;
import com.touhou.components.EliteEnemy;
import com.touhou.components.Hero;
import com.touhou.components.ItemType;
import com.touhou.components.Projectile;

class EnemyPowerUpObserverTest {
    @Test
    void bombDestroysRegularEnemiesAndClearsEnemyBullets() {
        List<com.touhou.components.Enemy> enemies = new ArrayList<>();
        enemies.add(new EliteEnemy(200, 0, 2));
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(10, 10, 0, 2, 1, Projectile.Owner.ENEMY));

        EnemyPowerUpObserver observer = new EnemyPowerUpObserver(enemies, projectiles, 180);

        observer.onPowerUpActivated(new PowerUpEvent(ItemType.BOMB, new Hero(100, 100)));

        assertTrue(enemies.get(0).getHealth() == 0);
        assertTrue(projectiles.isEmpty());
    }

    @Test
    void freezeMarksEnemiesAsFrozen() {
        List<com.touhou.components.Enemy> enemies = new ArrayList<>();
        BossEnemy bossEnemy = new BossEnemy(300, 3);
        enemies.add(bossEnemy);

        EnemyPowerUpObserver observer = new EnemyPowerUpObserver(enemies, new ArrayList<>(), 120);

        observer.onPowerUpActivated(new PowerUpEvent(ItemType.FREEZE, new Hero(100, 100)));

        assertTrue(bossEnemy.isFrozen());
    }
}
