package com.touhou.components;

import java.awt.Color;
import java.util.List;

import com.touhou.strategy.StraightRowFireStrategy;

public class ElitePlusEnemy extends Enemy {
    public ElitePlusEnemy(int x, int velocityX, int velocityY) {
        super(
                x,
                -60,
                76,
                76,
                velocityX,
                velocityY,
                4,
                new Color(255, 152, 96),
                1,
                68,
                new StraightRowFireStrategy(3, 2, 8, 7),
                5,
                0.5,
                List.of(ItemType.HEALTH, ItemType.FIREPOWER, ItemType.SUPER_FIREPOWER, ItemType.BOMB));
    }

    @Override
    protected void advanceNormally(int playfieldWidth, int playfieldHeight) {
        tick();
        bounceHorizontally(playfieldWidth);
    }

    @Override
    public String getSpriteResource() {
        return "/textures/elitePlus.png";
    }
}
