package com.touhou.components;

import java.awt.Color;
import java.util.List;

import com.touhou.strategy.RadialShotStrategy;

public class BossEnemy extends Enemy {
    private static final int HOVER_Y = 110;

    public BossEnemy(int x, int velocityX) {
        super(
                x,
                HOVER_Y,
                180,
                128,
                velocityX,
                0,
                30,
                new Color(255, 72, 72),
                1,
                80,
                new RadialShotStrategy(20, 6.0),
                25,
                1.0,
                List.of(ItemType.HEALTH, ItemType.FIREPOWER, ItemType.SUPER_FIREPOWER, ItemType.BOMB, ItemType.FREEZE));
    }

    @Override
    protected void advanceNormally(int playfieldWidth, int playfieldHeight) {
        tick();
        setPosition(getX(), HOVER_Y);
        bounceHorizontally(playfieldWidth);
    }

    @Override
    public int getDropCount() {
        return 3;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    @Override
    public String getSpriteResource() {
        return "/textures/boss.png";
    }
}
