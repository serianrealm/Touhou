package com.touhou.components;

import java.awt.Color;

import com.touhou.core.ItemEffectContext;

public abstract class Item extends GameCharacter {
    private final ItemType type;

    protected Item(ItemType type, int x, int y, Color fallbackColor) {
        super(x, y, 28, 28, 0, 2, 1, fallbackColor);
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public abstract void activate(Hero hero, ItemEffectContext context);

    @Override
    public String getSpriteResource() {
        return type.getSpriteResource();
    }
}
