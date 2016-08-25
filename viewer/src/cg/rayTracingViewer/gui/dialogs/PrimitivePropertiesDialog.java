package cg.rayTracingViewer.gui.dialogs;

import cg.rayTracingViewer.gui.ColorButton;
import cg.rayTracingViewer.gui.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import rayTracing.math.Color3d;
import rayTracing.math.Point3d;
import rayTracing.scene.Material;
import rayTracing.scene.Primitive;

/**
 * @author Alexandre Vieira
 */
public abstract class PrimitivePropertiesDialog extends CustomDialog {

    JPanel messagePanel;
    JPanel commonPanel;
    JPanel materialPanel;
    JPanel propertiesPanel;
    JPanel specificPanel;
    JPanel panel;
    JTabbedPane tabPane;
    JFormattedTextField xLocationField;
    JLabel xLocationLabel;
    JFormattedTextField yLocationField;
    JLabel yLocationLabel;
    JFormattedTextField zLocationField;
    JLabel zLocationLabel;
    ColorButton colorButton;
    JLabel colorLabel;
    JFormattedTextField emittanceField;
    JLabel emittanceLabel;
    JFormattedTextField diffuseField;
    JLabel diffuseLabel;
    JFormattedTextField indexField;
    JLabel indexLabel;
    JFormattedTextField reflectionField;
    JLabel reflectionLabel;
    JFormattedTextField refractionField;
    JLabel refractionLabel;
    JFormattedTextField specularField;
    JLabel specularLabel;
    Controller controller;

    public PrimitivePropertiesDialog(Frame owner, Primitive primitive) {
        super(owner, primitive.getId() + " Properties");

        xLocationField.setValue(primitive.getLocation().x);
        yLocationField.setValue(primitive.getLocation().y);
        zLocationField.setValue(primitive.getLocation().z);

        Material material = primitive.getMaterial();
        Color3d color = material.getColor();

        colorButton.setColor(new Color((float) color.red, (float) color.green, (float) color.blue));
        emittanceField.setValue(material.getEmittance());
        diffuseField.setValue(material.getDiffuse());
        specularField.setValue(material.getSpecular());
        reflectionField.setValue(material.getReflection());
        refractionField.setValue(material.getRefraction());
        indexField.setValue(material.getRefractionIndex());
    }

    public abstract JPanel createSpecificMessage();

    @Override
    public JPanel createMessage() {
        controller = new Controller();

        createUI();

        return messagePanel;
    }

    @Override
    public boolean validate() {
        return xLocationField.getInputVerifier().verify(xLocationField) &&
                yLocationField.getInputVerifier().verify(yLocationField) &&
                zLocationField.getInputVerifier().verify(zLocationField) &&
                emittanceField.getInputVerifier().verify(emittanceField) &&
                diffuseField.getInputVerifier().verify(diffuseField) &&
                specularField.getInputVerifier().verify(specularField) &&
                reflectionField.getInputVerifier().verify(reflectionField) &&
                refractionField.getInputVerifier().verify(refractionField) &&
                indexField.getInputVerifier().verify(indexField);
    }

    public void extractTo(Primitive primitive) {
        float[] rgb = colorButton.getColor().getRGBColorComponents(null);

        primitive.getMaterial().getColor().red = rgb[0];
        primitive.getMaterial().getColor().green = rgb[1];
        primitive.getMaterial().getColor().blue = rgb[2];
        primitive.getMaterial().setEmittance((Double) emittanceField.getValue());
        primitive.getMaterial().setDiffuse((Double) diffuseField.getValue());
        primitive.getMaterial().setSpecular((Double) specularField.getValue());
        primitive.getMaterial().setReflection((Double) reflectionField.getValue());
        primitive.getMaterial().setRefraction((Double) refractionField.getValue());
        primitive.getMaterial().setRefractionIndex((Double) indexField.getValue());
        primitive.setLocation(getLocation());
    }

    public Point3d getLocation() {
        Point3d point = new Point3d();

        point.x = (Double) xLocationField.getValue();
        point.y = (Double) yLocationField.getValue();
        point.z = (Double) zLocationField.getValue();

        return point;
    }

    private void onColor() {
        Color color = JColorChooser.showDialog(getDialog(), "Primitive Color", colorButton.getColor());

        if (color != null) {
            colorButton.setColor(color);
        }
    }

    private void createUI() {
        GridBagConstraints gbc;

        messagePanel = new JPanel();
        tabPane = new JTabbedPane();
        propertiesPanel = new JPanel();
        commonPanel = new JPanel();
        xLocationLabel = new JLabel();
        xLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        yLocationLabel = new JLabel();
        yLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        zLocationLabel = new JLabel();
        zLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        specificPanel = new JPanel();
        materialPanel = new JPanel();
        panel = new JPanel();
        colorLabel = new JLabel();
        colorButton = new ColorButton(Color.RED);
        emittanceLabel = new JLabel();
        emittanceField = new JFormattedTextField(Utils.createDoubleFormatter());
        diffuseLabel = new JLabel();
        diffuseField = new JFormattedTextField(Utils.createDoubleFormatter());
        specularLabel = new JLabel();
        specularField = new JFormattedTextField(Utils.createDoubleFormatter());
        reflectionLabel = new JLabel();
        reflectionField = new JFormattedTextField(Utils.createDoubleFormatter());
        refractionLabel = new JLabel();
        refractionField = new JFormattedTextField(Utils.createDoubleFormatter());
        indexLabel = new JLabel();
        indexField = new JFormattedTextField(Utils.createDoubleFormatter());

        materialPanel.setLayout(new BorderLayout());

        messagePanel.setMaximumSize(new Dimension(300, 400));
        messagePanel.setMinimumSize(new Dimension(300, 400));
        messagePanel.setName("Form");
        messagePanel.setLayout(new BorderLayout());

        tabPane.setName("tabPane");

        propertiesPanel.setName("propertiesPanel");
        propertiesPanel.setPreferredSize(new Dimension(300, 400));
        propertiesPanel.setLayout(new GridBagLayout());

        commonPanel.setBorder(BorderFactory.createTitledBorder("Common"));
        commonPanel.setName("commonPanel");
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.LINE_AXIS));

        xLocationLabel.setText("X:");
        xLocationLabel.setName("xLocationLabel");
        commonPanel.add(xLocationLabel);

        xLocationField.setColumns(4);
        xLocationField.setInputVerifier(Utils.createDoubleVerifier());
        xLocationField.setHorizontalAlignment(JTextField.TRAILING);
        xLocationField.setText("");
        xLocationField.setName("xLocationField");
        commonPanel.add(xLocationField);

        yLocationLabel.setText("Y:");
        yLocationLabel.setName("yLocationLabel");
        commonPanel.add(yLocationLabel);

        yLocationField.setColumns(4);
        yLocationField.setInputVerifier(Utils.createDoubleVerifier());
        yLocationField.setHorizontalAlignment(JTextField.TRAILING);
        yLocationField.setText("");
        yLocationField.setName("yLocationField");
        commonPanel.add(yLocationField);

        zLocationLabel.setText("Z:");
        zLocationLabel.setName("zLocationLabel");
        commonPanel.add(zLocationLabel);

        zLocationField.setColumns(4);
        zLocationField.setInputVerifier(Utils.createDoubleVerifier());
        zLocationField.setHorizontalAlignment(JTextField.TRAILING);
        zLocationField.setText("");
        zLocationField.setName("zLocationField");
        commonPanel.add(zLocationField);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        propertiesPanel.add(commonPanel, gbc);

        specificPanel.setBorder(BorderFactory.createTitledBorder("Specific"));
        specificPanel.setName("specificPanel");
        specificPanel.setLayout(new BorderLayout());
        specificPanel.add(createSpecificMessage(), BorderLayout.CENTER);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        propertiesPanel.add(specificPanel, gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setName("panel");
        panel.setLayout(new GridBagLayout());

        colorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        colorLabel.setText("Color:");
        colorLabel.setName("colorLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(colorLabel, gbc);

        colorButton.setName("colorButton");
        Dimension dim = new Dimension(24, 24);
        colorButton.setPreferredSize(dim);
        colorButton.setMaximumSize(dim);
        colorButton.setMinimumSize(dim);
        colorButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(colorButton, gbc);

        diffuseLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        diffuseLabel.setText("Diffuse:");
        diffuseLabel.setName("diffuseLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(diffuseLabel, gbc);

        diffuseField.setName("diffuseField");
        diffuseField.setHorizontalAlignment(JTextField.TRAILING);
        diffuseField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(diffuseField, gbc);

        specularLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        specularLabel.setText("Specular:");
        specularLabel.setName("specularLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(specularLabel, gbc);

        specularField.setName("specularField");
        specularField.setHorizontalAlignment(JTextField.TRAILING);
        specularField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(specularField, gbc);

        reflectionLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        reflectionLabel.setText("Reflection:");
        reflectionLabel.setName("reflectionLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(reflectionLabel, gbc);

        reflectionField.setName("reflectionField");
        reflectionField.setHorizontalAlignment(JTextField.TRAILING);
        reflectionField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(reflectionField, gbc);

        refractionLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        refractionLabel.setText("Refraction:");
        refractionLabel.setName("refractionLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(refractionLabel, gbc);

        refractionField.setName("refractionField");
        refractionField.setHorizontalAlignment(JTextField.TRAILING);
        refractionField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(refractionField, gbc);

        indexLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        indexLabel.setText("Refraction Index:");
        indexLabel.setName("indexLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 0);
        panel.add(indexLabel, gbc);

        indexField.setName("indexField");
        indexField.setHorizontalAlignment(JTextField.TRAILING);
        indexField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(indexField, gbc);

        emittanceLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        emittanceLabel.setText("Emittance:");
        emittanceLabel.setName("emittanceLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(emittanceLabel, gbc);

        emittanceField.setName("emittanceField");
        emittanceField.setHorizontalAlignment(JTextField.TRAILING);
        emittanceField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(emittanceField, gbc);

        materialPanel.add(panel, BorderLayout.NORTH);

        tabPane.addTab("Properties", propertiesPanel);

        materialPanel.setName("materialPanel");

        tabPane.addTab("Material", materialPanel);

        messagePanel.add(tabPane, BorderLayout.CENTER);
    }

    class Controller implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource().equals(colorButton)) {
                onColor();
            }
        }
    }
}
