package com.touhou.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import com.touhou.leaderboard.GameDifficulty;
import com.touhou.leaderboard.LeaderboardDao;
import com.touhou.leaderboard.LeaderboardEntry;

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

        LeaderboardTableModel tableModel = new LeaderboardTableModel(leaderboardDao, initialDifficulty);
        JTable leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        leaderboardTable.setRowHeight(28);
        leaderboardTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        leaderboardTable.setBackground(new Color(24, 28, 44));
        leaderboardTable.setForeground(Color.WHITE);
        leaderboardTable.setGridColor(new Color(70, 76, 98));
        leaderboardTable.getTableHeader().setReorderingAllowed(false);

        difficultyComboBox.addActionListener(event -> {
            GameDifficulty difficulty = (GameDifficulty) difficultyComboBox.getSelectedItem();
            tableModel.load(difficulty);
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        toolbar.add(new JScrollPane(leaderboardTable), constraints);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(event -> {
            int selectedRow = leaderboardTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.delete(leaderboardTable.convertRowIndexToModel(selectedRow));
            }
        });
        constraints.gridy = 2;
        toolbar.add(deleteButton, constraints);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> appController.showMenu());
        constraints.gridy = 3;
        toolbar.add(backButton, constraints);
    }

    private static final class LeaderboardTableModel extends AbstractTableModel {
        private static final String[] COLUMNS = {"Rank", "Player", "Score", "Time"};

        private final LeaderboardDao leaderboardDao;
        private GameDifficulty difficulty;
        private List<LeaderboardEntry> entries;

        private LeaderboardTableModel(LeaderboardDao leaderboardDao, GameDifficulty difficulty) {
            this.leaderboardDao = leaderboardDao;
            this.entries = new ArrayList<>();
            load(difficulty);
        }

        private void load(GameDifficulty difficulty) {
            this.difficulty = difficulty;
            this.entries = new ArrayList<>(leaderboardDao.findAll(difficulty));
            fireTableDataChanged();
        }

        private void delete(int row) {
            if (row < 0 || row >= entries.size()) {
                return;
            }
            leaderboardDao.delete(difficulty, entries.get(row));
            load(difficulty);
        }

        @Override
        public int getRowCount() {
            return entries.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            LeaderboardEntry entry = entries.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> rowIndex + 1;
                case 1 -> entry.playerName();
                case 2 -> entry.score();
                case 3 -> entry.formattedPlayedAt();
                default -> "";
            };
        }
    }
}
