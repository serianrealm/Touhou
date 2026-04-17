package com.touhou.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Layout {
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;

    private final JFrame frame;

    public Layout() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            // The app can run with the default Swing look and feel.
        }

        frame = new JFrame("Touhou");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(15, 12, 30));
    }

    public void showPanel(JPanel panel) {
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        panel.setBackground(new Color(15, 12, 30));

        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);

        if (!frame.isVisible()) {
            frame.setVisible(true);
        }

        panel.requestFocusInWindow();
    }
}
