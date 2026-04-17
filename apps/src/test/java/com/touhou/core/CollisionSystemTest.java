package com.touhou.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.touhou.audio.AudioManager;
import com.touhou.components.BossEnemy;
import com.touhou.components.Enemy;
import com.touhou.components.Hero;
import com.touhou.components.Item;
import com.touhou.components.ItemType;
import com.touhou.components.Projectile;
import com.touhou.difficulty.NormalDifficultyTemplate;
import com.touhou.factories.ItemFactory;
import com.touhou.observer.PowerUpDispatcher;

class CollisionSystemTest {
    @Test
    void overlapsReturnsTrueForIntersectingBounds() {
        CollisionSystem collisionSystem = new CollisionSystem(
                new AudioManager(),
                new ItemEffectContext(new AudioManager(), new NormalDifficultyTemplate(), new PowerUpDispatcher()),
                new Random(0));
        Hero hero = new Hero(100, 100);
        Enemy enemy = new BossEnemy(100, 0);

        assertTrue(collisionSystem.overlaps(hero, enemy));
    }

    @Test
    void resolveRemovesEnemyAfterHeroProjectileHit() {
        CollisionSystem collisionSystem = new CollisionSystem(
                new AudioManager(),
                new ItemEffectContext(new AudioManager(), new NormalDifficultyTemplate(), new PowerUpDispatcher()),
                new Random(0));
        Hero hero = new Hero(200, 400);
        List<Enemy> enemies = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        Enemy enemy = new com.touhou.components.MobEnemy(200, 0);
        enemies.add(enemy);
        projectiles.add(new Projectile(200, -40, 0, 0, 1, Projectile.Owner.HERO));

        int scoreGain = collisionSystem.resolve(hero, enemies, projectiles, items);

        assertEquals(1, scoreGain);
        assertTrue(enemies.isEmpty());
        assertTrue(projectiles.isEmpty());
    }

    @Test
    void healthItemPickupRestoresHeroHealthWithoutExceedingMaximum() {
        CollisionSystem collisionSystem = new CollisionSystem(
                new AudioManager(),
                new ItemEffectContext(new AudioManager(), new NormalDifficultyTemplate(), new PowerUpDispatcher()),
                new Random(0));
        Hero hero = new Hero(200, 400);
        hero.applyDamage(3);

        List<Enemy> enemies = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        items.add(ItemFactory.create(ItemType.HEALTH, hero.getX(), hero.getY()));

        collisionSystem.resolve(hero, enemies, projectiles, items);

        assertEquals(hero.getMaxHealth() - 1, hero.getHealth());
        assertTrue(items.isEmpty());
    }

    @Test
    void bossDropsThreeItemsOnDeath() {
        CollisionSystem collisionSystem = new CollisionSystem(
                new AudioManager(),
                new ItemEffectContext(new AudioManager(), new NormalDifficultyTemplate(), new PowerUpDispatcher()),
                new Random(0));
        Hero hero = new Hero(200, 400);
        List<Enemy> enemies = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        BossEnemy boss = new BossEnemy(200, 0);
        boss.applyDamage(boss.getHealth());
        enemies.add(boss);

        int scoreGain = collisionSystem.resolve(hero, enemies, projectiles, items);

        assertEquals(25, scoreGain);
        assertEquals(3, items.size());
    }
}
