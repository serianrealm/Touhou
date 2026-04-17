package com.touhou.factories;

import java.util.Random;

import com.touhou.components.Enemy;

public abstract class EnemyFactory {
    public abstract Enemy createEnemy(Random random, int playfieldWidth);
}
