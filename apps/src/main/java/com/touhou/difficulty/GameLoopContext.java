package com.touhou.difficulty;

import com.touhou.core.CharacterSystem;
import com.touhou.core.CollisionSystem;
import com.touhou.core.Game;
import com.touhou.ui.Layout;

public record GameLoopContext(
        Game game,
        CharacterSystem characterSystem,
        CollisionSystem collisionSystem,
        int tickCount,
        int score) {
    public int playfieldWidth() {
        return Layout.WINDOW_WIDTH;
    }

    public int playfieldHeight() {
        return Layout.WINDOW_HEIGHT;
    }
}
