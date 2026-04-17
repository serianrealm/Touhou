package com.touhou.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.touhou.audio.AudioManager;
import com.touhou.components.Enemy;
import com.touhou.components.Hero;
import com.touhou.components.Item;
import com.touhou.components.Projectile;
import com.touhou.difficulty.DifficultyTemplate;
import com.touhou.leaderboard.FileLeaderboardDao;
import com.touhou.leaderboard.GameDifficulty;
import com.touhou.leaderboard.LeaderboardDao;
import com.touhou.leaderboard.LeaderboardEntry;
import com.touhou.leaderboard.LeaderboardPresenter;
import com.touhou.observer.AudioPowerUpObserver;
import com.touhou.observer.EnemyPowerUpObserver;
import com.touhou.observer.PowerUpDispatcher;
import com.touhou.ui.GameUI;
import com.touhou.ui.Layout;

public class Game extends JPanel {
    public static final int TICKS_PER_SECOND = 60;

    private static final int LEADERBOARD_LIMIT = 8;

    private final Hero hero;
    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;
    private final List<Item> items;
    private final CharacterSystem characterSystem;
    private final CollisionSystem collisionSystem;
    private final GameUI gameUI;
    private final Timer timer;
    private final GameDifficulty difficulty;
    private final DifficultyTemplate difficultyTemplate;
    private final String playerName;
    private final LeaderboardDao leaderboardDao;
    private final Clock clock;
    private final AudioManager audioManager;

    private int tickCount;
    private int score;
    private boolean gameOverHandled;
    private List<LeaderboardEntry> leaderboardEntries;

    public Game() {
        this(GameDifficulty.NORMAL, defaultPlayerName());
    }

    public Game(GameDifficulty difficulty, String playerName) {
        this(
                difficulty,
                playerName,
                new FileLeaderboardDao(Path.of(System.getProperty("touhou.leaderboardDir", "leaderboards"))),
                new AudioManager(),
                Clock.systemDefaultZone());
    }

    public Game(GameDifficulty difficulty, String playerName, LeaderboardDao leaderboardDao, AudioManager audioManager) {
        this(difficulty, playerName, leaderboardDao, audioManager, Clock.systemDefaultZone());
    }

    Game(
            GameDifficulty difficulty,
            String playerName,
            LeaderboardDao leaderboardDao,
            AudioManager audioManager,
            Clock clock) {
        setDoubleBuffered(true);
        setFocusable(true);

        this.difficulty = difficulty;
        this.difficultyTemplate = difficulty.createTemplate();
        this.playerName = playerName;
        this.leaderboardDao = leaderboardDao;
        this.audioManager = audioManager;
        this.clock = clock;
        this.hero = new Hero(Layout.WINDOW_WIDTH / 2, Layout.WINDOW_HEIGHT - 120);
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.items = new ArrayList<>();
        this.leaderboardEntries = List.of();

        PowerUpDispatcher powerUpDispatcher = new PowerUpDispatcher();
        powerUpDispatcher.addObserver(new EnemyPowerUpObserver(enemies, projectiles, difficultyTemplate.freezeDurationTicks()));
        powerUpDispatcher.addObserver(new AudioPowerUpObserver(audioManager));

        ItemEffectContext itemEffectContext = new ItemEffectContext(audioManager, difficultyTemplate, powerUpDispatcher);

        characterSystem = new CharacterSystem(this, hero, enemies, projectiles, items, difficultyTemplate);
        collisionSystem = new CollisionSystem(audioManager, itemEffectContext);
        gameUI = new GameUI();
        timer = new Timer(1000 / TICKS_PER_SECOND, event -> {
            tick();
            repaint();
        });
        timer.setCoalesce(true);

        installSoundToggleBinding();
    }

    public void start() {
        audioManager.playBackgroundLoop("/audios/bgm.wav");
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void stop() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    public void tick() {
        if (gameOverHandled) {
            stop();
            return;
        }

        tickCount++;
        characterSystem.onTick(Layout.WINDOW_WIDTH, Layout.WINDOW_HEIGHT, score);
        score += collisionSystem.resolve(hero, enemies, projectiles, items);

        if (!hero.isAlive()) {
            handleGameOver();
        }
    }

    public Hero getHero() {
        return hero;
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getTickCount() {
        return tickCount;
    }

    public int getScore() {
        return score;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isGameOverHandled() {
        return gameOverHandled;
    }

    public List<LeaderboardEntry> getLeaderboardEntries() {
        return Collections.unmodifiableList(leaderboardEntries);
    }

    public boolean isSoundMuted() {
        return audioManager.isMuted();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            gameUI.render(
                    canvas,
                    getWidth(),
                    getHeight(),
                    hero,
                    enemies,
                    projectiles,
                    items,
                    score,
                    tickCount,
                    difficulty,
                    leaderboardEntries,
                    audioManager.isMuted());
        } finally {
            canvas.dispose();
        }
    }

    private void handleGameOver() {
        if (gameOverHandled) {
            return;
        }

        gameOverHandled = true;
        stop();
        audioManager.stopBackground();
        audioManager.playEffect("/audios/game_over.wav");
        hero.shutdown();

        try {
            LeaderboardEntry currentSession = new LeaderboardEntry(playerName, score, LocalDateTime.now(clock));
            leaderboardDao.save(difficulty, currentSession);
            leaderboardEntries = leaderboardDao.findAll(difficulty);
            System.out.println(LeaderboardPresenter.format(difficulty, leaderboardEntries, LEADERBOARD_LIMIT));
        } catch (RuntimeException exception) {
            leaderboardEntries = List.of();
            System.err.println("Failed to update leaderboard: " + exception.getMessage());
        }
    }

    private void installSoundToggleBinding() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('M'), "toggle-sound");
        getActionMap().put("toggle-sound", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                audioManager.toggleMuted();
                if (audioManager.isMuted()) {
                    audioManager.stopBackground();
                } else if (!gameOverHandled) {
                    audioManager.playBackgroundLoop("/audios/bgm.wav");
                }
                repaint();
            }
        });
    }

    private static String defaultPlayerName() {
        String systemPlayerName = System.getProperty("user.name", "Player").trim();
        return systemPlayerName.isEmpty() ? "Player" : systemPlayerName;
    }
}
