package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.gui.events.CameraPanelListener;
import cg.rayTracingViewer.Manager;
import cg.rayTracingViewer.Resolution;
import cg.rayTracingViewer.gl.FreeCamera;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;

/**
 * @author Alexandre Vieira
 */
public class CameraPanel extends JPanel {

    private JPanel buttonPanel;
    private JButton cameraButton;
    private JPanel directionPanel;
    private JLabel distanceLabel;
    private JSlider distanceSlider;
    private JLabel fovLabel;
    private JSlider fovSlider;
    private JLabel resolutionLabel;
    private JPanel locationPanel;
    private JComboBox resolutionBox;
    private JPanel topPanel;
    private JPanel upPanel;
    private JFormattedTextField xDirectionField;
    private JLabel xDirectionLabel;
    private JFormattedTextField xLocationField;
    private JLabel xLocationLabel;
    private JFormattedTextField xUpField;
    private JLabel xUpLabel;
    private JFormattedTextField yDirectionField;
    private JLabel yDirectionLabel;
    private JFormattedTextField yLocationField;
    private JLabel yLocationLabel;
    private JFormattedTextField yUpField;
    private JLabel yUpLabel;
    private JFormattedTextField zDirectionField;
    private JLabel zDirectionLabel;
    private JFormattedTextField zLocationField;
    private JLabel zLocationLabel;
    private JFormattedTextField zUpField;
    private JLabel zUpLabel;
    private Controller controller;
    private CameraPanelListener listener;
    private boolean notifyListener;
    private RayTracingViewer viewer;

    public CameraPanel(RayTracingViewer viewer) {
        this.controller = new Controller();
        this.viewer = viewer;
        this.notifyListener = false;

        createUI();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        fovSlider.setEnabled(enabled);
        distanceSlider.setEnabled(enabled);
        resolutionBox.setEnabled(enabled);
        xDirectionField.setEnabled(enabled);
        yDirectionField.setEnabled(enabled);
        zDirectionField.setEnabled(enabled);
        xUpField.setEnabled(enabled);
        yUpField.setEnabled(enabled);
        zUpField.setEnabled(enabled);
        xLocationField.setEnabled(enabled);
        yLocationField.setEnabled(enabled);
        zLocationField.setEnabled(enabled);
        cameraButton.setEnabled(enabled);
    }

    public void setCameraPanelListener(CameraPanelListener listener) {
        this.listener = listener;
    }

    public void setNotifyListener(boolean notifyListener) {
        this.notifyListener = notifyListener;
    }

    public void updateFOVSlider(double fov) {
        fovSlider.setValue((int) fov);
    }

    public void updateDistanceSlider(double distance) {
        distanceSlider.setValue((int) distance);
    }

    public void updateResolutionBox(Resolution resolution) {
        int itemCount = resolutionBox.getItemCount();

        if (itemCount >= 0) {
            int index = -1;

            for (int i = 0; i < itemCount; i++) {
                if (((Resolution) resolutionBox.getItemAt(i)).equals(resolution)) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                resolutionBox.setSelectedIndex(index);
            } else {
                resolutionBox.addItem(resolution);
            }
        }
    }

    public void updateDirectionField(double x, double y, double z) {
        xDirectionField.setValue(x);
        yDirectionField.setValue(y);
        zDirectionField.setValue(z);
    }

    public void updateUpField(double x, double y, double z) {
        xUpField.setValue(x);
        yUpField.setValue(y);
        zUpField.setValue(z);
    }

    public void updateLocationField(double x, double y, double z) {
        xLocationField.setValue(x);
        yLocationField.setValue(y);
        zLocationField.setValue(z);
    }

    protected void fireOnCameraFOVChanged(double fov) {
        if (listener != null) {
            listener.onCameraFOVChanged(this, fov);
        }
    }

    protected void fireOnCameraDistanceChanged(double distance) {
        if (listener != null) {
            listener.onCameraDistanceChanged(this, distance);
        }
    }

    protected void fireOnCameraResolutionChanged(Resolution resolution) {
        if (listener != null) {
            listener.onCameraResolutionChanged(this, resolution);
        }
    }

    protected void fireOnCameraDirectionChanged(double x, double y, double z) {
        if (listener != null) {
            listener.onCameraDirectionChanged(this, x, y, z);
        }
    }

    protected void fireOnCameraUpChanged(double x, double y, double z) {
        if (listener != null) {
            listener.onCameraUpChanged(this, x, y, z);
        }
    }

    protected void fireOnCameraLocationChanged(double x, double y, double z) {
        if (listener != null) {
            listener.onCameraLocationChanged(this, x, y, z);
        }
    }

    protected void fireOnAsPreview(Vector3d direction, Vector3d up, Point3d location, double fov) {
        if (listener != null) {
            listener.onAsPreview(direction, up, location);
        }
    }

    private void onFOVChanged() {
        fireOnCameraFOVChanged(fovSlider.getValue());

        if (viewer.getGLPreview() != null) {
            viewer.getGLPreview().getCamera().setFov(fovSlider.getValue());
        }
    }

    private void onDistanceChanged() {
        fireOnCameraDistanceChanged(distanceSlider.getValue());
    }

    private void onResolutionChanged() {
        fireOnCameraResolutionChanged((Resolution) resolutionBox.getSelectedItem());
    }

    private void onDirectionChanged() {
        fireOnCameraDirectionChanged((Double) xDirectionField.getValue(),
                (Double) yDirectionField.getValue(), (Double) zDirectionField.getValue());
    }

    private void onUpChanged() {
        fireOnCameraUpChanged((Double) xUpField.getValue(),
                (Double) yUpField.getValue(), (Double) zUpField.getValue());
    }

    private void onLocationChanged() {
        fireOnCameraLocationChanged((Double) xLocationField.getValue(),
                (Double) yLocationField.getValue(), (Double) zLocationField.getValue());
    }

    private void onAsPreview() {
        FreeCamera preview = viewer.getGLPreview().getCamera();
        Point3d location = new Point3d(preview.getLocation().x, preview.getLocation().y, -preview.getLocation().z);
        Vector3d direction = new Vector3d(preview.getUAxis().x, preview.getUAxis().y, -preview.getUAxis().z);

        fireOnAsPreview(direction, preview.getVAxis(), location, preview.getFov());

        setNotifyListener(false);
        updateDirectionField(direction.x, direction.y, direction.z);
        updateUpField(preview.getVAxis().x, preview.getVAxis().y, preview.getVAxis().z);
        updateLocationField(location.x, location.y, location.z);
        updateFOVSlider(preview.getFov());
        setNotifyListener(true);
    }

    private void createUI() {
        setMinimumSize(new Dimension(300, 300));
        setMaximumSize(new Dimension(300, 300));
        setPreferredSize(new Dimension(300, 300));

        GridBagConstraints gbc;

        topPanel = new JPanel();
        upPanel = new JPanel();
        xUpLabel = new JLabel();
        xUpField = new JFormattedTextField(Utils.createDoubleFormatter());
        yUpLabel = new JLabel();
        yUpField = new JFormattedTextField(Utils.createDoubleFormatter());
        zUpLabel = new JLabel();
        zUpField = new JFormattedTextField(Utils.createDoubleFormatter());
        fovSlider = new JSlider();
        directionPanel = new JPanel();
        xDirectionLabel = new JLabel();
        xDirectionField = new JFormattedTextField(Utils.createDoubleFormatter());
        yDirectionLabel = new JLabel();
        yDirectionField = new JFormattedTextField(Utils.createDoubleFormatter());
        zDirectionLabel = new JLabel();
        zDirectionField = new JFormattedTextField(Utils.createDoubleFormatter());
        distanceSlider = new JSlider();
        fovLabel = new JLabel();
        distanceLabel = new JLabel();
        resolutionBox = new JComboBox(Manager.RESOLUTIONS);
        resolutionLabel = new JLabel();
        locationPanel = new JPanel();
        xLocationLabel = new JLabel();
        xLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        yLocationLabel = new JLabel();
        yLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        zLocationLabel = new JLabel();
        zLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        buttonPanel = new JPanel();
        cameraButton = new JButton();

        setBorder(BorderFactory.createTitledBorder("Camera Properties"));
        setName("Form");
        setLayout(new java.awt.BorderLayout());

        topPanel.setName("topPanel");
        topPanel.setLayout(new java.awt.GridBagLayout());
        topPanel.setMinimumSize(new Dimension(280, 100));
        topPanel.setMaximumSize(new Dimension(280, 100));
        topPanel.setPreferredSize(new Dimension(280, 100));

        upPanel.setBorder(BorderFactory.createTitledBorder("Up"));
        upPanel.setName("upPanel");
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.LINE_AXIS));
        upPanel.setMinimumSize(new Dimension(300, 50));
        upPanel.setMaximumSize(new Dimension(300, 50));
        upPanel.setPreferredSize(new Dimension(300, 50));

        xUpLabel.setText("X:");
        xUpLabel.setName("xUpLabel");
        upPanel.add(xUpLabel);

        xUpField.setColumns(4);
        xUpField.setInputVerifier(Utils.createDoubleVerifier());
        xUpField.setHorizontalAlignment(JTextField.TRAILING);
        xUpField.setName("xUpField");
        xUpField.addPropertyChangeListener("value", controller);
        upPanel.add(xUpField);

        yUpLabel.setText("Y:");
        yUpLabel.setName("yUpLabel");
        upPanel.add(yUpLabel);

        yUpField.setColumns(4);
        yUpField.setInputVerifier(Utils.createDoubleVerifier());
        yUpField.setHorizontalAlignment(JTextField.TRAILING);
        yUpField.setName("yUpField");
        yUpField.addPropertyChangeListener("value", controller);
        upPanel.add(yUpField);

        zUpLabel.setText("Z:");
        zUpLabel.setName("zUpLabel");
        upPanel.add(zUpLabel);

        zUpField.setColumns(4);
        zUpField.setInputVerifier(Utils.createDoubleVerifier());
        zUpField.setHorizontalAlignment(JTextField.TRAILING);
        zUpField.setName("zUpField");
        zUpField.addPropertyChangeListener("value", controller);
        upPanel.add(zUpField);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        topPanel.add(upPanel, gbc);

        fovSlider.setMajorTickSpacing(10);
        fovSlider.setMaximum(90);
        fovSlider.setMinorTickSpacing(5);
        fovSlider.setPaintLabels(true);
        fovSlider.setPaintTicks(true);
        fovSlider.setSnapToTicks(true);
        fovSlider.setValue(45);
        fovSlider.addChangeListener(controller);
        fovSlider.setName("fovSlider");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);
        topPanel.add(fovSlider, gbc);

        directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));
        directionPanel.setName("directionPanel");
        directionPanel.setLayout(new BoxLayout(directionPanel, BoxLayout.LINE_AXIS));
        directionPanel.setMinimumSize(new Dimension(300, 50));
        directionPanel.setMaximumSize(new Dimension(300, 50));
        directionPanel.setPreferredSize(new Dimension(300, 50));

        xDirectionLabel.setText("X:");
        xDirectionLabel.setName("xDirectionLabel");
        directionPanel.add(xDirectionLabel);

        xDirectionField.setColumns(4);
        xDirectionField.setInputVerifier(Utils.createDoubleVerifier());
        xDirectionField.setHorizontalAlignment(JTextField.TRAILING);
        xDirectionField.setName("xDirectionField");
        xDirectionField.addPropertyChangeListener("value", controller);
        directionPanel.add(xDirectionField);

        yDirectionLabel.setText("Y:");
        yDirectionLabel.setName("yDirectionLabel");
        directionPanel.add(yDirectionLabel);

        yDirectionField.setColumns(4);
        yDirectionField.setInputVerifier(Utils.createDoubleVerifier());
        yDirectionField.setHorizontalAlignment(JTextField.TRAILING);
        yDirectionField.setName("yDirectionField");
        yDirectionField.addPropertyChangeListener("value", controller);
        directionPanel.add(yDirectionField);

        zDirectionLabel.setText("Z:");
        zDirectionLabel.setName("zDirectionLabel");
        directionPanel.add(zDirectionLabel);

        zDirectionField.setColumns(4);
        zDirectionField.setInputVerifier(Utils.createDoubleVerifier());
        zDirectionField.setHorizontalAlignment(JTextField.TRAILING);
        zDirectionField.setName("zDirectionField");
        zDirectionField.addPropertyChangeListener("value", controller);
        directionPanel.add(zDirectionField);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 0, 0, 0);
        topPanel.add(directionPanel, gbc);

        distanceSlider.setMajorTickSpacing(1);
        distanceSlider.setMaximum(10);
        distanceSlider.setPaintLabels(true);
        distanceSlider.setPaintTicks(true);
        distanceSlider.setSnapToTicks(true);
        distanceSlider.setValue(1);
        distanceSlider.addChangeListener(controller);
        distanceSlider.setName("distanceSlider");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);
        topPanel.add(distanceSlider, gbc);

        fovLabel.setText("FOV:");
        fovLabel.setName("fovLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2, 5, 2, 5);
        topPanel.add(fovLabel, gbc);

        distanceLabel.setText("Distance:");
        distanceLabel.setName("distanceLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2, 5, 2, 5);
        topPanel.add(distanceLabel, gbc);

        resolutionBox.setName("resolutionBox");
        resolutionBox.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);
        topPanel.add(resolutionBox, gbc);

        resolutionLabel.setText("Resolution:");
        resolutionLabel.setName("resolutionLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(2, 5, 2, 5);
        topPanel.add(resolutionLabel, gbc);

        locationPanel.setBorder(BorderFactory.createTitledBorder("Location"));
        locationPanel.setName("locationPanel");
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.LINE_AXIS));
        locationPanel.setMinimumSize(new Dimension(300, 50));
        locationPanel.setMaximumSize(new Dimension(300, 50));
        locationPanel.setPreferredSize(new Dimension(300, 50));

        xLocationLabel.setText("X:");
        xLocationLabel.setName("xLocationLabel");
        locationPanel.add(xLocationLabel);

        xLocationField.setColumns(4);
        xLocationField.setInputVerifier(Utils.createDoubleVerifier());
        xLocationField.setHorizontalAlignment(JTextField.TRAILING);
        xLocationField.setName("xLocationField");
        xLocationField.addPropertyChangeListener("value", controller);
        locationPanel.add(xLocationField);

        yLocationLabel.setText("Y:");
        yLocationLabel.setName("yLocationLabel");
        locationPanel.add(yLocationLabel);

        yLocationField.setColumns(4);
        yLocationField.setInputVerifier(Utils.createDoubleVerifier());
        yLocationField.setHorizontalAlignment(JTextField.TRAILING);
        yLocationField.setName("yLocationField");
        yLocationField.addPropertyChangeListener("value", controller);
        locationPanel.add(yLocationField);

        zLocationLabel.setText("Z:");
        zLocationLabel.setName("zLocationLabel");
        locationPanel.add(zLocationLabel);

        zLocationField.setColumns(4);
        zLocationField.setInputVerifier(Utils.createDoubleVerifier());
        zLocationField.setHorizontalAlignment(JTextField.TRAILING);
        zLocationField.setName("zLocationField");
        zLocationField.addPropertyChangeListener("value", controller);
        locationPanel.add(zLocationField);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        topPanel.add(locationPanel, gbc);

        add(topPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setName("buttonPanel");

        cameraButton.setText("As Preview");
        cameraButton.setName("cameraButton");
        cameraButton.addActionListener(controller);
        buttonPanel.add(cameraButton);

        add(buttonPanel, java.awt.BorderLayout.NORTH);
    }

    private class Controller implements ActionListener, ChangeListener, PropertyChangeListener {

        public void actionPerformed(ActionEvent ae) {
            Object src = ae.getSource();

            if (src.equals(resolutionBox)) {
                onResolutionChanged();
            } else if (src.equals(cameraButton)) {
                onAsPreview();
            }
        }

        public void stateChanged(ChangeEvent ce) {
            if (ce.getSource().equals(fovSlider)) {
                onFOVChanged();
            } else if (ce.getSource().equals(distanceSlider)) {
                onDistanceChanged();
            }
        }

        public void propertyChange(PropertyChangeEvent pce) {
            if (notifyListener) {
                Object src = pce.getSource();

                if (src.equals(xDirectionField) || src.equals(yDirectionField) || src.equals(zDirectionField)) {
                    onDirectionChanged();
                } else if (src.equals(xUpField) || src.equals(yUpField) || src.equals(zUpField)) {
                    onUpChanged();
                } else if (src.equals(xLocationField) || src.equals(yLocationField) || src.equals(zLocationField)) {
                    onLocationChanged();
                }
            }
        }
    }
}
