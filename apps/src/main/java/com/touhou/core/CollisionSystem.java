package com.touhou.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.touhou.audio.AudioManager;
import com.touhou.components.Enemy;
import com.touhou.components.GameCharacter;
import com.touhou.components.Hero;
import com.touhou.components.Item;
import com.touhou.components.Projectile;
import com.touhou.factories.ItemFactory;

public class CollisionSystem {
    private final Random random;
    private final AudioManager audioManager;
    private final ItemEffectContext itemEffectContext;

    public CollisionSystem(AudioManager audioManager, ItemEffectContext itemEffectContext) {
        this(audioManager, itemEffectContext, new Random());
    }

    CollisionSystem(AudioManager audioManager, ItemEffectContext itemEffectContext, Random random) {
        this.audioManager = audioManager;
        this.itemEffectContext = itemEffectContext;
        this.random = random;
    }

    public boolean overlaps(GameCharacter left, GameCharacter right) {
        return left.getBounds().intersects(right.getBounds());
    }

    public int resolve(Hero hero, List<Enemy> enemies, List<Projectile> projectiles, List<Item> items) {
        int scoreGained = 0;

        for (Projectile projectile : projectiles) {
            if (!projectile.isAlive()) {
                continue;
            }

            if (projectile.getOwner() == Projectile.Owner.HERO) {
                for (Enemy enemy : enemies) {
                    if (!enemy.isAlive() || !overlaps(projectile, enemy)) {
                        continue;
                    }

                    enemy.applyDamage(projectile.getDamage());
                    projectile.applyDamage(1);
                    audioManager.playEffect("/audios/bullet_hit.wav");
                    break;
                }
            } else if (overlaps(projectile, hero)) {
                hero.applyDamage(projectile.getDamage());
                projectile.applyDamage(1);
                audioManager.playEffect("/audios/bullet_hit.wav");
            }
        }

        projectiles.removeIf(projectile -> !projectile.isAlive());

        List<Item> droppedItems = new ArrayList<>();
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (!enemy.isAlive()) {
                scoreGained += enemy.getScoreValue();
                if (enemy.canDropItems()) {
                    droppedItems.addAll(ItemFactory.createDrops(enemy, random));
                }
                enemyIterator.remove();
                continue;
            }

            if (overlaps(enemy, hero)) {
                hero.applyDamage(1);
                scoreGained += enemy.getScoreValue();
                droppedItems.addAll(ItemFactory.createDrops(enemy, random));
                enemyIterator.remove();
            }
        }

        items.addAll(droppedItems);

        Iterator<Item> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if (overlaps(item, hero)) {
                item.activate(hero, itemEffectContext);
                itemIterator.remove();
            }
        }

        return scoreGained;
    }
}
