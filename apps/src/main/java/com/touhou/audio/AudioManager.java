package com.touhou.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class AudioManager {
    private final ExecutorService audioExecutor;

    private volatile boolean muted;
    private Clip backgroundClip;

    public AudioManager() {
        audioExecutor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "touhou-audio");
            thread.setDaemon(true);
            return thread;
        });
    }

    public void playEffect(String resourcePath) {
        if (muted) {
            return;
        }

        audioExecutor.execute(() -> {
            if (muted) {
                return;
            }
            Clip clip = loadClip(resourcePath);
            if (clip == null) {
                return;
            }
            clip.addLineListener(event -> closeOnStop(clip, event));
            clip.start();
        });
    }

    public void playBackgroundLoop(String resourcePath) {
        audioExecutor.execute(() -> {
            stopBackgroundClip();
            if (muted) {
                return;
            }

            Clip clip = loadClip(resourcePath);
            if (clip == null) {
                return;
            }
            backgroundClip = clip;
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        });
    }

    public void stopBackground() {
        audioExecutor.execute(this::stopBackgroundClip);
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        if (muted) {
            stopBackground();
        }
    }

    public void toggleMuted() {
        setMuted(!muted);
    }

    public boolean isMuted() {
        return muted;
    }

    private Clip loadClip(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream))) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                return clip;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    private void stopBackgroundClip() {
        if (backgroundClip == null) {
            return;
        }

        Clip clip = backgroundClip;
        backgroundClip = null;
        clip.stop();
        clip.close();
    }

    private void closeOnStop(Clip clip, LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP && clip != backgroundClip) {
            clip.close();
        }
    }
}
