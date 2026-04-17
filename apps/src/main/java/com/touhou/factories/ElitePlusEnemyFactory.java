package com.touhou.factories;

import java.util.Random;

import com.touhou.components.ElitePlusEnemy;
import com.touhou.components.Enemy;

public class ElitePlusEnemyFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(Random random, int playfieldWidth) {
        int x = random.nextInt(70, Math.max(71, playfieldWidth - 70));
        int velocityX = random.nextBoolean() ? 4 : -4;
        int velocityY = random.nextInt(2, 4);
        return new ElitePlusEnemy(x, velocityX, velocityY);
    }
}
