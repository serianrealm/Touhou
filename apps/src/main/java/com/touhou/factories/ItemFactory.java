package com.touhou.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.touhou.components.BombItem;
import com.touhou.components.Enemy;
import com.touhou.components.FirepowerItem;
import com.touhou.components.FreezeItem;
import com.touhou.components.HealthItem;
import com.touhou.components.Item;
import com.touhou.components.ItemType;
import com.touhou.components.SuperFirepowerItem;

public final class ItemFactory {
    private ItemFactory() {
    }

    public static Item create(ItemType type, int x, int y) {
        return switch (type) {
            case HEALTH -> new HealthItem(x, y);
            case FIREPOWER -> new FirepowerItem(x, y);
            case SUPER_FIREPOWER -> new SuperFirepowerItem(x, y);
            case BOMB -> new BombItem(x, y);
            case FREEZE -> new FreezeItem(x, y);
        };
    }

    public static List<Item> createDrops(Enemy enemy, Random random) {
        List<Item> drops = new ArrayList<>();
        if (enemy.getDroppableItems().isEmpty()) {
            return drops;
        }

        if (!enemy.isBoss() && random.nextDouble() > enemy.getDropChance()) {
            return drops;
        }

        int centerIndex = enemy.getDropCount() / 2;
        for (int index = 0; index < enemy.getDropCount(); index++) {
            ItemType selectedType = enemy.getDroppableItems().get(random.nextInt(enemy.getDroppableItems().size()));
            drops.add(create(selectedType, enemy.getX() + (index - centerIndex) * 26, enemy.getY()));
        }

        return drops;
    }
}
