package com.touhou.components;

import java.awt.Color;

import com.touhou.core.ItemEffectContext;
import com.touhou.observer.PowerUpEvent;

public class BombItem extends Item {
    public BombItem(int x, int y) {
        super(ItemType.BOMB, x, y, new Color(255, 155, 84));
    }

    @Override
    public void activate(Hero hero, ItemEffectContext context) {
        context.powerUpDispatcher().notifyObservers(new PowerUpEvent(ItemType.BOMB, hero));
        vanish();
    }
}
