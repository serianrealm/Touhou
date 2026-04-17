package com.touhou.core;

import com.touhou.audio.AudioManager;
import com.touhou.difficulty.DifficultyTemplate;
import com.touhou.observer.PowerUpDispatcher;

public record ItemEffectContext(
        AudioManager audioManager,
        DifficultyTemplate difficultyTemplate,
        PowerUpDispatcher powerUpDispatcher) {
}
