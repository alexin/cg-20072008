package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.gui.events.ControlsPanelListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import rayTracing.Settings;

/**
 * @author Alexandre Vieira
 */
public class ControlsPanel extends JPanel {

    private JButton saveButton;
    private JButton stopButton;
    private JButton traceButton;
    private JPanel blaPanel;
    private JLabel depthLabel;
    private JSpinner depthSpinner;
    private JToggleButton diffuseButton;
    private JToggleButton reflectionButton;
    private JToggleButton refractionButton;
    private JPanel settingsPanel;
    private JToggleButton shadowsButton;
    private JToggleButton specularButton;
    private JToggleButton cullFaceButton;
    private Controller controller;
    private GridBagConstraints gridBagConstraints;

    public ControlsPanel() {
        this.controller = new Controller();

        createUI();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        traceButton.setEnabled(enabled);
        stopButton.setEnabled(!enabled);
        saveButton.setEnabled(enabled);
        diffuseButton.setEnabled(enabled);
        specularButton.setEnabled(enabled);
        reflectionButton.setEnabled(enabled);
        refractionButton.setEnabled(enabled);
        shadowsButton.setEnabled(enabled);
        depthSpinner.setEnabled(enabled);
        cullFaceButton.setEnabled(enabled);
    }

    public void addControlsPanelListener(ControlsPanelListener listener) {
        this.listenerList.add(ControlsPanelListener.class, listener);
    }

    public void removeControlsPanelListener(ControlsPanelListener listener) {
        this.listenerList.remove(ControlsPanelListener.class, listener);
    }

    protected void fireOnTrace(Settings settings) {
        ControlsPanelListener[] listeners = listenerList.getListeners(ControlsPanelListener.class);
        for (ControlsPanelListener listener : listeners) {
            listener.onTrace(settings);
        }
    }

    protected void fireOnStop() {
        ControlsPanelListener[] listeners = listenerList.getListeners(ControlsPanelListener.class);
        for (ControlsPanelListener listener : listeners) {
            listener.onStop();
        }
    }

    private void fireOnSave() {
        ControlsPanelListener[] listeners = listenerList.getListeners(ControlsPanelListener.class);
        for (ControlsPanelListener listener : listeners) {
            listener.onSave();
        }
    }

    private void onTrace() {
        Settings settings = new Settings();
        settings.setTracingDepth(((SpinnerNumberModel) depthSpinner.getModel()).getNumber().intValue());
        settings.setUseDiffuse(diffuseButton.isSelected());
        settings.setUseSpecular(specularButton.isSelected());
        settings.setUseReflection(reflectionButton.isSelected());
        settings.setUseRefraction(refractionButton.isSelected());
        settings.setUseShadows(shadowsButton.isSelected());
        settings.setUseCullFace(cullFaceButton.isSelected());

        fireOnTrace(settings);
    }

    private void onStop() {
        fireOnStop();
    }

    private void onSave() {
        fireOnSave();
    }

    private void createUI() {
        GridBagConstraints gbc;

        blaPanel = new JPanel();
        traceButton = new JButton();
        stopButton = new JButton();
        saveButton = new JButton();
        settingsPanel = new JPanel();
        depthLabel = new JLabel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 16, 1));
        diffuseButton = new JToggleButton();
        specularButton = new JToggleButton();
        reflectionButton = new JToggleButton();
        refractionButton = new JToggleButton();
        shadowsButton = new JToggleButton();
        cullFaceButton = new JToggleButton();

        setBorder(BorderFactory.createTitledBorder("Tracing Controls"));
        setMaximumSize(new Dimension(4096, 80));
        setMinimumSize(new Dimension(800, 80));
        setName("Form");
        setPreferredSize(new Dimension(800, 80));
        setLayout(new GridBagLayout());

        blaPanel.setName("blaPanel");
        blaPanel.setLayout(new GridBagLayout());

        traceButton.setText("Trace");
        traceButton.setName("traceButton");
        traceButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        blaPanel.add(traceButton, gbc);

        stopButton.setText("Stop");
        stopButton.setName("stopButton");
        stopButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        blaPanel.add(stopButton, gbc);

        saveButton.setText("Save Frame As...");
        saveButton.setName("saveButton");
        saveButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 8, 0, 0);
        blaPanel.add(saveButton, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.2;
        add(blaPanel, gbc);

        settingsPanel.setName("settingsPanel");
        settingsPanel.setLayout(new GridBagLayout());

        depthLabel.setText("Max. Tracing Depth:");
        depthLabel.setName("depthLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        settingsPanel.add(depthLabel, gbc);

        //depthSpinner.setToolTipText(""); 
        depthSpinner.setMaximumSize(new Dimension(50, 32767));
        depthSpinner.setMinimumSize(new Dimension(50, 20));
        depthSpinner.setName("depthSpinner");
        depthSpinner.setPreferredSize(new Dimension(40, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 20);
        settingsPanel.add(depthSpinner, gbc);

        diffuseButton.setIcon(Utils.createIcon("diffuse.png"));
        diffuseButton.setToolTipText("Use diffuse shading");
        diffuseButton.setName("diffuseButton");
        diffuseButton.setPreferredSize(new Dimension(32, 32));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 10, 2, 2);
        settingsPanel.add(diffuseButton, gbc);

        specularButton.setIcon(Utils.createIcon("specular.png"));
        specularButton.setToolTipText("Use specular shading");
        specularButton.setName("specularButton");
        specularButton.setPreferredSize(new Dimension(32, 32));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        settingsPanel.add(specularButton, gbc);

        reflectionButton.setIcon(Utils.createIcon("reflection.png"));
        reflectionButton.setToolTipText("Use reflections");
        reflectionButton.setName("reflectionButton");
        reflectionButton.setPreferredSize(new Dimension(32, 32));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        settingsPanel.add(reflectionButton, gbc);

        refractionButton.setIcon(Utils.createIcon("refraction.png"));
        refractionButton.setToolTipText("Use refractions");
        refractionButton.setName("refractionButton");
        refractionButton.setPreferredSize(new Dimension(32, 32));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        settingsPanel.add(refractionButton, gbc);

        shadowsButton.setIcon(Utils.createIcon("shadows.png"));
        shadowsButton.setToolTipText("Use shadows");
        shadowsButton.setName("shadowsButton");
        shadowsButton.setPreferredSize(new Dimension(32, 32));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        settingsPanel.add(shadowsButton, gbc);

        cullFaceButton.setIcon(Utils.createIcon("cull.png"));
        cullFaceButton.setToolTipText("Cull back-faces");
        cullFaceButton.setName("cullFaceButton");
        cullFaceButton.setPreferredSize(new Dimension(32, 32));
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        settingsPanel.add(cullFaceButton, gbc);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.8;
        add(settingsPanel, gbc);
    }

    private class Controller implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource().equals(traceButton)) {
                onTrace();
            } else if (ae.getSource().equals(stopButton)) {
                onStop();
            } else if (ae.getSource().equals(saveButton)) {
                onSave();
            }
        }
    }
}
