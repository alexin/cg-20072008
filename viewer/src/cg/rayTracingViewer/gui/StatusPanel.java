package cg.rayTracingViewer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Alexandre Vieira
 */
public class StatusPanel extends JPanel {

    private JLabel messageLabel;
    private JProgressBar progressBar;

    public StatusPanel() {
        createUI();
        
        setMessage("");
        setProgress(0);
        stopProgress();
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }

    public void startProgress() {
        progressBar.setVisible(true);
    }

    public void stopProgress() {
        progressBar.setVisible(false);
    }

    private void createUI() {
        GridBagConstraints gbc;

        messageLabel = new JLabel();
        progressBar = new JProgressBar(0, 100);

        setMaximumSize(new Dimension(2147483647, 28));
        setMinimumSize(new Dimension(142, 28));
        setName("Form");
        setPreferredSize(new Dimension(1024, 28));
        setLayout(new GridBagLayout());

        messageLabel.setName("messageLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.9;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(messageLabel, gbc);

        progressBar.setStringPainted(true);
        progressBar.setMaximumSize(new Dimension(100, 20));
        progressBar.setMinimumSize(new Dimension(100, 20));
        progressBar.setName("progressBar");
        progressBar.setPreferredSize(new Dimension(100, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(2, 2, 2, 2);
        add(progressBar, gbc);
    }
}
