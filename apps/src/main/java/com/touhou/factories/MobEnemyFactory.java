package com.touhou.factories;

import java.util.Random;

import com.touhou.components.Enemy;
import com.touhou.components.MobEnemy;

public class MobEnemyFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(Random random, int playfieldWidth) {
        int x = random.nextInt(40, Math.max(41, playfieldWidth - 40));
        int velocityY = random.nextInt(2, 5);
        return new MobEnemy(x, velocityY);
    }
}
