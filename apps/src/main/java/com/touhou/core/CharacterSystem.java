package com.touhou.core;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

import com.touhou.components.Enemy;
import com.touhou.components.Hero;
import com.touhou.components.Item;
import com.touhou.components.Projectile;
import com.touhou.difficulty.DifficultyTemplate;
import com.touhou.factories.AceEnemyFactory;
import com.touhou.factories.BossEnemyFactory;
import com.touhou.factories.EliteEnemyFactory;
import com.touhou.factories.ElitePlusEnemyFactory;
import com.touhou.factories.EnemyFactory;
import com.touhou.factories.MobEnemyFactory;

public class CharacterSystem {
    private final Random random;
    private final Hero hero;
    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;
    private final List<Item> items;
    private final List<EnemyFactory> enemyFactories;
    private final EnemyFactory bossEnemyFactory;
    private final DifficultyTemplate difficultyTemplate;

    private int ticks;
    private int targetX;
    private int targetY;
    private int nextBossScoreThreshold;

    public CharacterSystem(
            JComponent inputSurface,
            Hero hero,
            List<Enemy> enemies,
            List<Projectile> projectiles,
            List<Item> items,
            DifficultyTemplate difficultyTemplate) {
        this(inputSurface, hero, enemies, projectiles, items, difficultyTemplate, new Random());
    }

    CharacterSystem(
            JComponent inputSurface,
            Hero hero,
            List<Enemy> enemies,
            List<Projectile> projectiles,
            List<Item> items,
            DifficultyTemplate difficultyTemplate,
            Random random) {
        this.random = random;
        this.hero = hero;
        this.enemies = enemies;
        this.projectiles = projectiles;
        this.items = items;
        this.difficultyTemplate = difficultyTemplate;
        this.enemyFactories = List.of(
                new MobEnemyFactory(),
                new EliteEnemyFactory(),
                new ElitePlusEnemyFactory(),
                new AceEnemyFactory());
        this.bossEnemyFactory = new BossEnemyFactory();
        this.targetX = hero.getX();
        this.targetY = hero.getY();
        this.nextBossScoreThreshold = difficultyTemplate.initialBossScoreThreshold();

        MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                updateTarget(event.getX(), event.getY());
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                updateTarget(event.getX(), event.getY());
            }
        };

        inputSurface.addMouseMotionListener(mouseAdapter);
    }

    public void onTick(int playfieldWidth, int playfieldHeight, int score) {
        ticks++;

        hero.tick();
        hero.moveTo(
                clamp(targetX, hero.getWidth() / 2, playfieldWidth - hero.getWidth() / 2),
                clamp(targetY, hero.getHeight() / 2, playfieldHeight - hero.getHeight() / 2));
        projectiles.addAll(hero.tryFire());

        spawnEnemies(playfieldWidth, score);

        List<Projectile> enemyShots = new ArrayList<>();
        for (Enemy enemy : enemies) {
            enemy.advance(playfieldWidth, playfieldHeight);
            if (!enemy.isFrozen()) {
                enemyShots.addAll(enemy.tryFire());
            }
        }
        projectiles.addAll(enemyShots);

        projectiles.forEach(Projectile::tick);
        items.forEach(Item::tick);

        enemies.removeIf(enemy -> !enemy.isBoss() && enemy.getY() - enemy.getHeight() / 2 > playfieldHeight);
        projectiles.removeIf(projectile -> projectile.getY() + projectile.getHeight() < 0
                || projectile.getY() - projectile.getHeight() > playfieldHeight
                || projectile.getX() + projectile.getWidth() < 0
                || projectile.getX() - projectile.getWidth() > playfieldWidth
                || !projectile.isAlive());
        items.removeIf(item -> item.getY() - item.getHeight() / 2 > playfieldHeight || !item.isAlive());
    }

    private void spawnEnemies(int playfieldWidth, int score) {
        boolean bossPresent = enemies.stream().anyMatch(Enemy::isBoss);
        if (!bossPresent && score >= nextBossScoreThreshold) {
            Enemy boss = bossEnemyFactory.createEnemy(random, playfieldWidth);
            difficultyTemplate.configureEnemy(boss);
            enemies.add(boss);
            nextBossScoreThreshold += difficultyTemplate.bossThresholdStep();
            return;
        }

        long nonBossCount = enemies.stream().filter(enemy -> !enemy.isBoss()).count();
        if (ticks % difficultyTemplate.enemySpawnIntervalTicks() == 0
                && nonBossCount < difficultyTemplate.maxNonBossEnemies()) {
            EnemyFactory enemyFactory = enemyFactories.get(random.nextInt(enemyFactories.size()));
            Enemy enemy = enemyFactory.createEnemy(random, playfieldWidth);
            difficultyTemplate.configureEnemy(enemy);
            enemies.add(enemy);
        }
    }

    private void updateTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
