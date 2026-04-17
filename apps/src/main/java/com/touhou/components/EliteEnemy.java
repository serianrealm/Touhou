package com.touhou.components;

import java.awt.Color;
import java.util.List;

import com.touhou.strategy.StraightRowFireStrategy;

public class EliteEnemy extends Enemy {
    public EliteEnemy(int x, int velocityX, int velocityY) {
        super(
                x,
                -50,
                70,
                70,
                velocityX,
                velocityY,
                3,
                new Color(255, 194, 102),
                1,
                72,
                new StraightRowFireStrategy(3, 1, 8, 7),
                3,
                0.45,
                List.of(ItemType.HEALTH, ItemType.FIREPOWER, ItemType.SUPER_FIREPOWER));
    }

    @Override
    protected void advanceNormally(int playfieldWidth, int playfieldHeight) {
        tick();
        bounceHorizontally(playfieldWidth);
    }

    @Override
    public String getSpriteResource() {
        return "/textures/elite.png";
    }
}
