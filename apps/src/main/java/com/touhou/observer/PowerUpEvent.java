package com.touhou.observer;

import com.touhou.components.Hero;
import com.touhou.components.ItemType;

public record PowerUpEvent(ItemType type, Hero hero) {
}
