package com.touhou.observer;

import com.touhou.audio.AudioManager;
import com.touhou.components.ItemType;

public class AudioPowerUpObserver implements PowerUpObserver {
    private final AudioManager audioManager;

    public AudioPowerUpObserver(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void onPowerUpActivated(PowerUpEvent event) {
        if (event.type() == ItemType.BOMB) {
            audioManager.playEffect("/audios/bomb_explosion.wav");
        } else if (event.type() == ItemType.FREEZE) {
            audioManager.playEffect("/audios/get_supply.wav");
        }
    }
}
