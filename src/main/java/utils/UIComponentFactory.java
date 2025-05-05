package utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIComponentFactory {

    public static JPanel createFormField(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setBackground(UIConstants.SECONDARY_COLOR);

        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.LABEL_FONT);
        label.setPreferredSize(new Dimension(100, 25));
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(UIConstants.SECONDARY_COLOR);
        wrapperPanel.add(panel, BorderLayout.CENTER);
        wrapperPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);

        return wrapperPanel;
    }

    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(UIConstants.BUTTON_FONT);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public static JPanel createTitledPanel(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(UIConstants.PRIMARY_COLOR),
                        title,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        UIConstants.TITLE_FONT,
                        UIConstants.PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(UIConstants.SECONDARY_COLOR);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel createHeaderPanel(String title, JButton actionButton) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        if (actionButton != null) {
            headerPanel.add(actionButton, BorderLayout.EAST);
        }

        return headerPanel;
    }

    public static JPanel createStatusBar(String status, String dateInfo) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.setBackground(UIConstants.PRIMARY_COLOR);

        JLabel statusLabel = new JLabel(status);
        statusLabel.setForeground(Color.WHITE);
        statusBar.add(statusLabel, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(dateInfo);
        dateLabel.setForeground(Color.WHITE);
        statusBar.add(dateLabel, BorderLayout.EAST);

        return statusBar;
    }
}
