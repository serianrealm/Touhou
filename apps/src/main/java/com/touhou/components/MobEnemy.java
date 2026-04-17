package com.touhou.components;

import java.awt.Color;
import java.util.List;

public class MobEnemy extends Enemy {
    public MobEnemy(int x, int velocityY) {
        super(
                x,
                -40,
                60,
                60,
                0,
                velocityY,
                1,
                new Color(255, 122, 162),
                1,
                9999,
                null,
                1,
                0.0,
                List.of());
    }

    @Override
    public String getSpriteResource() {
        return "/textures/mob.png";
    }
}
