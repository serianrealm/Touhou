package com.touhou.factories;

import java.util.Random;

import com.touhou.components.EliteEnemy;
import com.touhou.components.Enemy;

public class EliteEnemyFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(Random random, int playfieldWidth) {
        int x = random.nextInt(60, Math.max(61, playfieldWidth - 60));
        int velocityX = random.nextBoolean() ? 3 : -3;
        int velocityY = random.nextInt(2, 4);
        return new EliteEnemy(x, velocityX, velocityY);
    }
}
