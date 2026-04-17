package com.touhou.components;

import java.awt.Color;

import com.touhou.core.ItemEffectContext;

public class HealthItem extends Item {
    public HealthItem(int x, int y) {
        super(ItemType.HEALTH, x, y, new Color(152, 255, 152));
    }

    @Override
    public void activate(Hero hero, ItemEffectContext context) {
        hero.applyHealthSupply();
        context.audioManager().playEffect("/audios/get_supply.wav");
        vanish();
    }
}
