package com.touhou.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.touhou.leaderboard.GameDifficulty;
import com.touhou.leaderboard.LeaderboardDao;
import com.touhou.leaderboard.LeaderboardPresenter;

public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel(AppController appController, LeaderboardDao leaderboardDao, GameDifficulty initialDifficulty) {
        setLayout(new BorderLayout());
        setBackground(new Color(12, 14, 28));

        JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 42));
        add(title, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setOpaque(false);
        add(toolbar, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.gridx = 0;
        constraints.gridy = 0;

        JComboBox<GameDifficulty> difficultyComboBox = new JComboBox<>(GameDifficulty.values());
        difficultyComboBox.setSelectedItem(initialDifficulty);
        toolbar.add(difficultyComboBox, constraints);

        JTextArea leaderboardArea = new JTextArea(16, 48);
        leaderboardArea.setEditable(false);
        leaderboardArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        leaderboardArea.setBackground(new Color(24, 28, 44));
        leaderboardArea.setForeground(Color.WHITE);
        leaderboardArea.setText(LeaderboardPresenter.format(
                initialDifficulty,
                leaderboardDao.findAll(initialDifficulty),
                20));

        difficultyComboBox.addActionListener(event -> {
            GameDifficulty difficulty = (GameDifficulty) difficultyComboBox.getSelectedItem();
            leaderboardArea.setText(LeaderboardPresenter.format(difficulty, leaderboardDao.findAll(difficulty), 20));
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        toolbar.add(new JScrollPane(leaderboardArea), constraints);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> appController.showMenu());
        constraints.gridy = 2;
        toolbar.add(backButton, constraints);
    }
}
