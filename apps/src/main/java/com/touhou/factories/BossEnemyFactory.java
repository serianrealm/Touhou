package com.touhou.factories;

import java.util.Random;

import com.touhou.components.BossEnemy;
import com.touhou.components.Enemy;

public class BossEnemyFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(Random random, int playfieldWidth) {
        int velocityX = random.nextBoolean() ? 5 : -5;
        return new BossEnemy(playfieldWidth / 2, velocityX);
    }
}
