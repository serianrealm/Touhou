package com.touhou.components;

import java.awt.Color;
import java.util.List;

import com.touhou.strategy.FanShotStrategy;

public class AceEnemy extends Enemy {
    public AceEnemy(int x, int velocityX, int velocityY) {
        super(
                x,
                -70,
                82,
                82,
                velocityX,
                velocityY,
                6,
                new Color(219, 102, 255),
                1,
                62,
                new FanShotStrategy(3, 7.5, Math.PI / 5, Math.PI / 2),
                8,
                0.65,
                List.of(ItemType.HEALTH, ItemType.FIREPOWER, ItemType.SUPER_FIREPOWER, ItemType.BOMB, ItemType.FREEZE));
    }

    @Override
    protected void advanceNormally(int playfieldWidth, int playfieldHeight) {
        tick();
        bounceHorizontally(playfieldWidth);
    }

    @Override
    public String getSpriteResource() {
        return "/textures/elitePro.png";
    }

    @Override
    public boolean isAce() {
        return true;
    }
}
