package com.touhou.factories;

import java.util.Random;

import com.touhou.components.AceEnemy;
import com.touhou.components.Enemy;

public class AceEnemyFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(Random random, int playfieldWidth) {
        int x = random.nextInt(80, Math.max(81, playfieldWidth - 80));
        int velocityX = random.nextBoolean() ? 5 : -5;
        int velocityY = random.nextInt(2, 4);
        return new AceEnemy(x, velocityX, velocityY);
    }
}
