package com.touhou.components;

import java.awt.Color;

import com.touhou.core.ItemEffectContext;
import com.touhou.observer.PowerUpEvent;

public class FreezeItem extends Item {
    public FreezeItem(int x, int y) {
        super(ItemType.FREEZE, x, y, new Color(184, 255, 255));
    }

    @Override
    public void activate(Hero hero, ItemEffectContext context) {
        context.powerUpDispatcher().notifyObservers(new PowerUpEvent(ItemType.FREEZE, hero));
        vanish();
    }
}
