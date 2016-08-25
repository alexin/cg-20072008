package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.Manager;
import cg.rayTracingViewer.gui.events.TransformPanelListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import rayTracing.math.Matrix;

public class TransformPanel extends JPanel {

    private static final String TRANSLATION = "Translation";
    private static final String ROTATION = "Rotation";
    private static final String SCALE = "Scale";
    private JFormattedTextField angleField;
    private JLabel angleLabel;
    private ButtonGroup rotateGroup;
    private JPanel commonPanel;
    private JPanel rotatePanel;
    private JPanel scalePanel;
    private JPanel specificPanel;
    private JComboBox transformBox;
    private JButton transformButton;
    private JPanel translatePanel;
    private JRadioButton xRadio;
    private JFormattedTextField xScaleField;
    private JLabel xScaleLabel;
    private JFormattedTextField xTranslateField;
    private JLabel xTranslateLabel;
    private JRadioButton yRadio;
    private JFormattedTextField yScaleField;
    private JLabel yScaleLabel;
    private JFormattedTextField yTranslateField;
    private JLabel yTranslateLabel;
    private JRadioButton zRadio;
    private JFormattedTextField zScaleField;
    private JLabel zScaleLabel;
    private JFormattedTextField zTranslateField;
    private JLabel zTranslateLabel;
    private CardLayout cardLayout;
    private Controller controller;
    private Object selectedPrimitive;
    private TransformPanelListener listener;

    public TransformPanel() {
        this.controller = new Controller();
        createUI();
        showTranslatePanel();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        transformBox.setEnabled(enabled);
        transformButton.setEnabled(enabled);
        xTranslateField.setEnabled(enabled);
        yTranslateField.setEnabled(enabled);
        zTranslateField.setEnabled(enabled);
        xRadio.setEnabled(enabled);
        yRadio.setEnabled(enabled);
        zRadio.setEnabled(enabled);
        angleField.setEnabled(enabled);
        xScaleField.setEnabled(enabled);
        yScaleField.setEnabled(enabled);
        zScaleField.setEnabled(enabled);
    }

    public void setSelectedPrimitive(Object selectedPrimitive) {
        this.selectedPrimitive = selectedPrimitive;
    }

    public Object getSelectedPrimitive() {
        return selectedPrimitive;
    }

    public void setTransformPanelListener(TransformPanelListener listener) {
        this.listener = listener;
    }

    protected void fireOnTranslate(double x, double y, double z) {
        if (listener != null) {
            System.out.println(selectedPrimitive);
            listener.onTransform(selectedPrimitive, Manager.TRANSLATE, x, y,z);
        }
    }

    protected void fireOnRotateX(double angle) {
        if (listener != null) {
            listener.onTransform(selectedPrimitive, Manager.ROTATE_X, angle);
        }
    }
    
    protected void fireOnRotateY(double angle) {
        if (listener != null) {
            listener.onTransform(selectedPrimitive, Manager.ROTATE_Y, angle);
        }
    }
    protected void fireOnRotateZ(double angle) {
        if (listener != null) {
            listener.onTransform(selectedPrimitive, Manager.ROTATE_Z, angle);
        }
    }

    protected void fireOnScale(double x, double y, double z) {
        if (listener != null) {
            listener.onTransform(selectedPrimitive, Manager.SCALE, x, y, z);
        }
    }

    private void onTransform(String transformation) {
        Matrix m = new Matrix();

        if (transformation.equals(TRANSLATION)) {

            if (((Double) xTranslateField.getValue() != 0.0d) ||
                    ((Double) yTranslateField.getValue() != 0.0d) |
                    ((Double) zTranslateField.getValue() != 0.0d)) {

                //m.translate((Double) xTranslateField.getValue(), (Double) yTranslateField.getValue(), (Double) zTranslateField.getValue());
                fireOnTranslate( (Double)xTranslateField.getValue(), (Double)yTranslateField.getValue(), (Double)zTranslateField.getValue());
            }
        } else if (transformation.equals(ROTATION)) {

            if (rotateGroup.getSelection().equals(xRadio.getModel())) {
                    //m.rotateX((Double) angleField.getValue());
                 fireOnRotateX((Double) angleField.getValue());
            } else if (rotateGroup.getSelection().equals(yRadio.getModel())) {
                //m.rotateY((Double) angleField.getValue());
                 fireOnRotateY((Double) angleField.getValue());    
            } else if (rotateGroup.getSelection().equals(zRadio.getModel())) {
               fireOnRotateZ((Double) angleField.getValue());
               //m.rotateZ((Double) angleField.getValue());
            }            
        } else if (transformation.equals(SCALE)) {

            if (!xScaleField.getInputVerifier().verify(xScaleField)) {
                xScaleField.setValue(1.0d);
            }
            if (!yScaleField.getInputVerifier().verify(yScaleField)) {
                yScaleField.setValue(1.0d);
            }
            if (!zScaleField.getInputVerifier().verify(zScaleField)) {
                zScaleField.setValue(1.0d);
            }

            if (((Double) xScaleField.getValue() != 1.0d) ||
                    ((Double) yScaleField.getValue() != 1.0d) ||
                    ((Double) zScaleField.getValue() != 1.0d)) {

                //m.scale((Double) xScaleField.getValue(), (Double) yScaleField.getValue(), (Double) zScaleField.getValue());
                fireOnScale((Double) xScaleField.getValue(), (Double) yScaleField.getValue(), (Double) zScaleField.getValue());
            }
        }
    }

    private void showTranslatePanel() {
        xTranslateField.setValue(0.0d);
        yTranslateField.setValue(0.0d);
        zTranslateField.setValue(0.0d);
        cardLayout.show(specificPanel, "translatePanel");
    }

    private void showRotatePanel() {
        xRadio.setSelected(true);
        angleField.setValue(0.0d);
        cardLayout.show(specificPanel, "rotatePanel");
    }

    private void showScalePanel() {
        xScaleField.setValue(0.0d);
        yScaleField.setValue(0.0d);
        zScaleField.setValue(0.0d);
        cardLayout.show(specificPanel, "scalePanel");
    }

    private void createUI() {
        GridBagConstraints gbc;

        cardLayout = new CardLayout();
        rotateGroup = new ButtonGroup();
        commonPanel = new JPanel();
        transformBox = new JComboBox(new String[]{TRANSLATION, ROTATION, SCALE});
        transformButton = new JButton();
        specificPanel = new JPanel();
        translatePanel = new JPanel();
        xTranslateLabel = new JLabel();
        xTranslateField = new JFormattedTextField(Utils.createDoubleFormatter());
        yTranslateLabel = new JLabel();
        yTranslateField = new JFormattedTextField(Utils.createDoubleFormatter());
        zTranslateLabel = new JLabel();
        zTranslateField = new JFormattedTextField(Utils.createDoubleFormatter());
        rotatePanel = new JPanel();
        xRadio = new JRadioButton();
        yRadio = new JRadioButton();
        zRadio = new JRadioButton();
        angleLabel = new JLabel();
        angleField = new JFormattedTextField(Utils.createDoubleFormatter());
        scalePanel = new JPanel();
        xScaleLabel = new JLabel();
        xScaleField = new JFormattedTextField(Utils.createDoubleFormatter());
        yScaleLabel = new JLabel();
        yScaleField = new JFormattedTextField(Utils.createDoubleFormatter());
        zScaleLabel = new JLabel();
        zScaleField = new JFormattedTextField(Utils.createDoubleFormatter());

        rotateGroup.add(xRadio);
        rotateGroup.add(yRadio);
        rotateGroup.add(zRadio);

        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        setName("Form");
        setLayout(new BorderLayout());

        commonPanel.setName("commonPanel");
        commonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        transformBox.addActionListener(controller);
        transformBox.setMaximumSize(new Dimension(100, 32767));
        transformBox.setMinimumSize(new Dimension(100, 18));
        transformBox.setName("transformBox");
        transformBox.setPreferredSize(new Dimension(100, 20));
        commonPanel.add(transformBox);

        transformButton.addActionListener(controller);
        transformButton.setText("Transform");
        transformButton.setName("transformButton");
        commonPanel.add(transformButton);

        add(commonPanel, BorderLayout.NORTH);

        specificPanel.setName("specificPane");
        specificPanel.setLayout(cardLayout);

        translatePanel.setName("translatePanel");
        translatePanel.setLayout(new GridBagLayout());

        xTranslateLabel.setText("X:");
        xTranslateLabel.setName("xTranslateLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        translatePanel.add(xTranslateLabel, gbc);

        xTranslateField.setColumns(4);
        xTranslateField.setHorizontalAlignment(JTextField.TRAILING);
        xTranslateField.setName("xTranslateField");
        xTranslateField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        translatePanel.add(xTranslateField, gbc);

        yTranslateLabel.setText("Y:");
        yTranslateLabel.setName("yTranslateLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        translatePanel.add(yTranslateLabel, gbc);

        yTranslateField.setColumns(4);
        yTranslateField.setHorizontalAlignment(JTextField.TRAILING);
        yTranslateField.setName("yTranslateField");
        yTranslateField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        translatePanel.add(yTranslateField, gbc);

        zTranslateLabel.setText("Z:");
        zTranslateLabel.setName("zTranslateLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        translatePanel.add(zTranslateLabel, gbc);

        zTranslateField.setColumns(4);
        zTranslateField.setHorizontalAlignment(JTextField.TRAILING);
        zTranslateField.setName("zTranslateField");
        zTranslateField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        translatePanel.add(zTranslateField, gbc);

        specificPanel.add("translatePanel", translatePanel);

        rotatePanel.setName("rotatePanel");
        rotatePanel.setLayout(new GridBagLayout());

        xRadio.setText("X");
        xRadio.setName("xRadio");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 3, 2, 3);
        rotatePanel.add(xRadio, gbc);

        yRadio.setText("Y");
        yRadio.setName("yRadio");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 3, 2, 3);
        rotatePanel.add(yRadio, gbc);

        zRadio.setText("Z");
        zRadio.setName("zRadio");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 3, 2, 3);
        rotatePanel.add(zRadio, gbc);

        angleLabel.setText("Angle:");
        angleLabel.setName("angleLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.insets = new Insets(3, 3, 3, 2);
        rotatePanel.add(angleLabel, gbc);

        angleField.setColumns(4);
        angleField.setHorizontalAlignment(JTextField.TRAILING);
        angleField.setName("angleField");
        angleField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        rotatePanel.add(angleField, gbc);

        specificPanel.add("rotatePanel", rotatePanel);

        scalePanel.setName("scalePanel");
        scalePanel.setLayout(new GridBagLayout());

        xScaleLabel.setText("X:");
        xScaleLabel.setName("xScaleLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        scalePanel.add(xScaleLabel, gbc);

        xScaleField.setColumns(4);
        xScaleField.setHorizontalAlignment(JTextField.TRAILING);
        xScaleField.setName("xScaleField");
        xScaleField.setInputVerifier(Utils.createNZDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        scalePanel.add(xScaleField, gbc);

        yScaleLabel.setText("Y:");
        yScaleLabel.setName("yScaleLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        scalePanel.add(yScaleLabel, gbc);

        yScaleField.setColumns(4);
        yScaleField.setHorizontalAlignment(JTextField.TRAILING);
        yScaleField.setName("yScaleField");
        yScaleField.setInputVerifier(Utils.createNZDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        scalePanel.add(yScaleField, gbc);

        zScaleLabel.setText("Z:");
        zScaleLabel.setName("zScaleLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        scalePanel.add(zScaleLabel, gbc);

        zScaleField.setColumns(4);
        zScaleField.setHorizontalAlignment(JTextField.TRAILING);
        zScaleField.setName("zScaleField");
        zScaleField.setInputVerifier(Utils.createNZDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        scalePanel.add(zScaleField, gbc);

        specificPanel.add("scalePanel", scalePanel);

        add(specificPanel, BorderLayout.CENTER);
    }

    class Controller implements ActionListener, PropertyChangeListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource().equals(transformButton)) {
                onTransform(transformBox.getSelectedItem().toString());
            } else if (ae.getSource().equals(transformBox)) {
                if (transformBox.getSelectedItem().toString().equals(TRANSLATION)) {
                    xTranslateField.setValue(0.0d);
                    yTranslateField.setValue(0.0d);
                    zTranslateField.setValue(0.0d);
                    cardLayout.show(specificPanel, "translatePanel");
                } else if (transformBox.getSelectedItem().toString().equals(ROTATION)) {
                    angleField.setValue(0.0d);
                    cardLayout.show(specificPanel, "rotatePanel");
                } else if (transformBox.getSelectedItem().toString().equals(SCALE)) {
                    xScaleField.setValue(1.0d);
                    yScaleField.setValue(1.0d);
                    zScaleField.setValue(1.0d);
                    cardLayout.show(specificPanel, "scalePanel");
                }
            }
        }

        public void propertyChange(PropertyChangeEvent pce) {
        }
    }
}
