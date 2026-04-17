package com.touhou.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.touhou.components.Enemy;
import com.touhou.components.GameCharacter;
import com.touhou.components.Hero;
import com.touhou.components.Item;
import com.touhou.components.Projectile;
import com.touhou.leaderboard.GameDifficulty;
import com.touhou.leaderboard.LeaderboardEntry;

public class GameUI {
    private final BufferedImage backgroundImage;
    private final Map<String, BufferedImage> imageCache;

    public GameUI() {
        backgroundImage = loadImage("/textures/bg.jpg");
        imageCache = new HashMap<>();
    }

    public void render(
            Graphics2D canvas,
            int width,
            int height,
            Hero hero,
            List<Enemy> enemies,
            List<Projectile> projectiles,
            List<Item> items,
            int score,
            int tickCount,
            GameDifficulty difficulty,
            List<LeaderboardEntry> leaderboardEntries,
            boolean soundMuted) {
        int actualWidth = width > 0 ? width : Layout.WINDOW_WIDTH;
        int actualHeight = height > 0 ? height : Layout.WINDOW_HEIGHT;

        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(canvas, actualWidth, actualHeight, tickCount);
        items.forEach(item -> paintCharacter(canvas, item));
        projectiles.forEach(projectile -> paintCharacter(canvas, projectile));
        enemies.forEach(enemy -> paintCharacter(canvas, enemy));
        paintCharacter(canvas, hero);
        paintHud(canvas, hero, score, difficulty, soundMuted);

        if (!hero.isAlive()) {
            paintGameOver(canvas, actualWidth, actualHeight, difficulty, leaderboardEntries);
        }
    }

    private void paintBackground(Graphics2D canvas, int width, int height, int tickCount) {
        if (backgroundImage == null) {
            canvas.setPaint(new GradientPaint(0, 0, new Color(6, 7, 20), width, height, new Color(35, 23, 55)));
            canvas.fillRect(0, 0, width, height);
            canvas.setColor(new Color(255, 255, 255, 30));
            for (int index = 0; index < 60; index++) {
                int x = (index * 97) % width;
                int y = (index * 149 + tickCount * 2) % height;
                canvas.fillOval(x, y, 3, 3);
            }
            return;
        }

        int scroll = tickCount % height;
        canvas.drawImage(backgroundImage, 0, scroll - height, width, height, null);
        canvas.drawImage(backgroundImage, 0, scroll, width, height, null);
    }

    private void paintCharacter(Graphics2D canvas, GameCharacter character) {
        int drawX = character.getX() - character.getWidth() / 2;
        int drawY = character.getY() - character.getHeight() / 2;
        BufferedImage image = loadSprite(character.getSpriteResource());

        if (image != null) {
            canvas.drawImage(image, drawX, drawY, character.getWidth(), character.getHeight(), null);
            return;
        }

        canvas.setColor(character.getFallbackColor());
        canvas.fillRoundRect(drawX, drawY, character.getWidth(), character.getHeight(), 12, 12);
    }

    private void paintHud(Graphics2D canvas, Hero hero, int score, GameDifficulty difficulty, boolean soundMuted) {
        canvas.setColor(new Color(255, 214, 102));
        canvas.setFont(new Font("SansSerif", Font.BOLD, 26));
        canvas.drawString("SCORE: " + score, 18, 36);
        canvas.drawString("HP: " + hero.getHealth() + "/" + hero.getMaxHealth(), 18, 70);
        canvas.drawString("SHOT: " + hero.getFireStrategy().getClass().getSimpleName(), 18, 104);
        canvas.drawString("DIFFICULTY: " + difficulty.getDisplayName(), 18, 138);
        canvas.drawString("SOUND: " + (soundMuted ? "OFF" : "ON") + " (M)", 18, 172);
    }

    private void paintGameOver(
            Graphics2D canvas,
            int width,
            int height,
            GameDifficulty difficulty,
            List<LeaderboardEntry> leaderboardEntries) {
        canvas.setColor(new Color(0, 0, 0, 190));
        canvas.fillRect(0, 0, width, height);
        canvas.setColor(Color.WHITE);
        canvas.setFont(new Font("SansSerif", Font.BOLD, 52));
        canvas.drawString("GAME OVER", width / 2 - 170, 110);

        canvas.setFont(new Font("Monospaced", Font.BOLD, 22));
        canvas.drawString("Leaderboard [" + difficulty.getDisplayName() + "]", 180, 190);
        canvas.drawString("RANK  NAME            SCORE  TIME", 180, 225);

        canvas.setFont(new Font("Monospaced", Font.PLAIN, 18));
        if (leaderboardEntries.isEmpty()) {
            canvas.drawString("No scores yet.", 180, 260);
            return;
        }

        int maxRows = Math.min(8, leaderboardEntries.size());
        for (int index = 0; index < maxRows; index++) {
            LeaderboardEntry entry = leaderboardEntries.get(index);
            String line = String.format(
                    "%-5d %-15s %-6d %s",
                    index + 1,
                    truncate(entry.playerName(), 15),
                    entry.score(),
                    entry.formattedPlayedAt());
            canvas.drawString(line, 180, 260 + index * 28);
        }
    }

    private BufferedImage loadImage(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }
            return ImageIO.read(inputStream);
        } catch (IOException exception) {
            return null;
        }
    }

    private BufferedImage loadSprite(String resourcePath) {
        if (resourcePath == null) {
            return null;
        }

        return imageCache.computeIfAbsent(resourcePath, this::loadImage);
    }

    private String truncate(String value, int maxLength) {
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
