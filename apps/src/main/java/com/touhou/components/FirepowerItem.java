package com.touhou.components;

import java.awt.Color;

import com.touhou.core.ItemEffectContext;

public class FirepowerItem extends Item {
    public FirepowerItem(int x, int y) {
        super(ItemType.FIREPOWER, x, y, new Color(144, 227, 255));
    }

    @Override
    public void activate(Hero hero, ItemEffectContext context) {
        hero.applyFirepowerSupply(context.difficultyTemplate().weaponPowerUpDurationSeconds());
        context.audioManager().playEffect("/audios/get_supply.wav");
        vanish();
    }
}
