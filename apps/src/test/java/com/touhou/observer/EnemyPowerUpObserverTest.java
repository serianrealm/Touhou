package com.touhou.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.touhou.components.BossEnemy;
import com.touhou.components.AceEnemy;
import com.touhou.components.EliteEnemy;
import com.touhou.components.ElitePlusEnemy;
import com.touhou.components.Hero;
import com.touhou.components.ItemType;
import com.touhou.components.MobEnemy;
import com.touhou.components.Projectile;

class EnemyPowerUpObserverTest {
    @BeforeEach
    void setUp() {
        Hero.resetInstance();
    }

    @AfterEach
    void tearDown() {
        Hero.resetInstance();
    }

    @Test
    void bombDestroysRegularEnemiesAndClearsEnemyBullets() {
        List<com.touhou.components.Enemy> enemies = new ArrayList<>();
        enemies.add(new EliteEnemy(200, 0, 2));
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(10, 10, 0, 2, 1, Projectile.Owner.ENEMY));

        EnemyPowerUpObserver observer = new EnemyPowerUpObserver(enemies, projectiles, 180);

        observer.onPowerUpActivated(new PowerUpEvent(ItemType.BOMB, Hero.getInstance(100, 100)));

        assertTrue(enemies.get(0).getHealth() == 0);
        assertTrue(projectiles.isEmpty());
    }

    @Test
    void freezeMarksEnemiesAsFrozen() {
        List<com.touhou.components.Enemy> enemies = new ArrayList<>();
        MobEnemy mobEnemy = new MobEnemy(200, 2);
        ElitePlusEnemy elitePlusEnemy = new ElitePlusEnemy(300, 0, 3);
        BossEnemy bossEnemy = new BossEnemy(400, 3);
        enemies.add(mobEnemy);
        enemies.add(elitePlusEnemy);
        enemies.add(bossEnemy);
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(10, 10, 0, 2, 1, Projectile.Owner.ENEMY));

        EnemyPowerUpObserver observer = new EnemyPowerUpObserver(enemies, projectiles, 120);

        observer.onPowerUpActivated(new PowerUpEvent(ItemType.FREEZE, Hero.getInstance(100, 100)));

        assertTrue(mobEnemy.isFrozen());
        assertTrue(elitePlusEnemy.isFrozen());
        assertFalse(bossEnemy.isFrozen());
        assertTrue(projectiles.get(0).isFrozen());
    }

    @Test
    void bombDamagesAceAndDoesNotAffectBoss() {
        List<com.touhou.components.Enemy> enemies = new ArrayList<>();
        AceEnemy aceEnemy = new AceEnemy(200, 0, 3);
        BossEnemy bossEnemy = new BossEnemy(300, 3);
        enemies.add(aceEnemy);
        enemies.add(bossEnemy);

        EnemyPowerUpObserver observer = new EnemyPowerUpObserver(enemies, new ArrayList<>(), 120);

        observer.onPowerUpActivated(new PowerUpEvent(ItemType.BOMB, Hero.getInstance(100, 100)));

        assertEquals(3, aceEnemy.getHealth());
        assertEquals(bossEnemy.getMaxHealth(), bossEnemy.getHealth());
    }
}
