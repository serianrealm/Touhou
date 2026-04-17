package com.touhou.components;

import java.awt.Color;

import com.touhou.core.ItemEffectContext;

public class SuperFirepowerItem extends Item {
    public SuperFirepowerItem(int x, int y) {
        super(ItemType.SUPER_FIREPOWER, x, y, new Color(255, 238, 102));
    }

    @Override
    public void activate(Hero hero, ItemEffectContext context) {
        hero.applySuperFirepowerSupply(context.difficultyTemplate().weaponPowerUpDurationSeconds());
        context.audioManager().playEffect("/audios/get_supply.wav");
        vanish();
    }
}
