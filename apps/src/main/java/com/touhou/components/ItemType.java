package com.touhou.components;

public enum ItemType {
    HEALTH("/textures/prop_blood.png"),
    FIREPOWER("/textures/prop_bullet.png"),
    SUPER_FIREPOWER("/textures/prop_bulletPlus.png"),
    BOMB("/textures/prop_bomb.png"),
    FREEZE("/textures/prop_freeze.png");

    private final String spriteResource;

    ItemType(String spriteResource) {
        this.spriteResource = spriteResource;
    }

    public String getSpriteResource() {
        return spriteResource;
    }
}
