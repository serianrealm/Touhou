package com.touhou.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.touhou.audio.AudioManager;
import com.touhou.leaderboard.GameDifficulty;

public class MenuPanel extends JPanel {
    public MenuPanel(AppController appController, AudioManager audioManager) {
        setLayout(new BorderLayout());
        setBackground(new Color(12, 14, 28));

        JLabel title = new JLabel("Touhou", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 54));
        add(title, BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridBagLayout());
        controls.setOpaque(false);
        add(controls, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Player");
        nameLabel.setForeground(Color.WHITE);
        controls.add(nameLabel, constraints);

        JTextField playerNameField = new JTextField(System.getProperty("user.name", "Player"), 18);
        constraints.gridx = 1;
        controls.add(playerNameField, constraints);

        JLabel difficultyLabel = new JLabel("Difficulty");
        difficultyLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 1;
        controls.add(difficultyLabel, constraints);

        JComboBox<GameDifficulty> difficultyComboBox = new JComboBox<>(GameDifficulty.values());
        difficultyComboBox.setSelectedItem(GameDifficulty.NORMAL);
        constraints.gridx = 1;
        controls.add(difficultyComboBox, constraints);

        JCheckBox soundToggle = new JCheckBox("Sound enabled");
        soundToggle.setOpaque(false);
        soundToggle.setForeground(Color.WHITE);
        soundToggle.setSelected(!audioManager.isMuted());
        soundToggle.addActionListener(event -> audioManager.setMuted(!soundToggle.isSelected()));
        constraints.gridx = 1;
        constraints.gridy = 2;
        controls.add(soundToggle, constraints);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(event -> appController.startGame(
                (GameDifficulty) difficultyComboBox.getSelectedItem(),
                normalizePlayerName(playerNameField.getText())));
        constraints.gridx = 0;
        constraints.gridy = 3;
        controls.add(startButton, constraints);

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(event -> appController.showLeaderboard(
                (GameDifficulty) difficultyComboBox.getSelectedItem()));
        constraints.gridx = 1;
        controls.add(leaderboardButton, constraints);

        JLabel hint = new JLabel("Press M in-game to toggle sound.");
        hint.setForeground(new Color(200, 200, 220));
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        add(hint, BorderLayout.SOUTH);
    }

    private String normalizePlayerName(String playerName) {
        String normalized = playerName == null ? "" : playerName.trim();
        return normalized.isEmpty() ? "Player" : normalized;
    }
}
