package cg.rayTracingViewer.gui.dialogs;

import cg.rayTracingViewer.gui.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rayTracing.math.Color3d;
import rayTracing.math.Point3d;
import rayTracing.scene.Material;
import rayTracing.scene.Primitive;
import rayTracing.scene.Triangle;

/**
 * @author Alexandre Vieira
 */
public class TrianglePropertiesDialog extends PrimitivePropertiesDialog {

    private JPanel aPanel;
    private JFormattedTextField axField;
    private JLabel axLabel;
    private JFormattedTextField ayField;
    private JLabel ayLabel;
    private JFormattedTextField azField;
    private JLabel azLabel;
    private JPanel bPanel;
    private JFormattedTextField bxField;
    private JLabel bxLabel;
    private JFormattedTextField byField;
    private JLabel byLabel;
    private JFormattedTextField bzField;
    private JLabel bzLabel;
    private JPanel cPanel;
    private JFormattedTextField cxField;
    private JLabel cxLabel;
    private JFormattedTextField cyField;
    private JLabel cyLabel;
    private JFormattedTextField czField;
    private JLabel czLabel;
    private JPanel specificMessage;

    public TrianglePropertiesDialog(Frame owner, Triangle triangle) {
        super(owner, triangle);

        Material material = triangle.getMaterial();
        Color3d color = material.getColor();

        colorButton.setColor(new Color((float) color.red, (float) color.green, (float) color.blue));
        diffuseField.setValue(material.getDiffuse());
        specularField.setValue(material.getSpecular());
        reflectionField.setValue(material.getReflection());
        refractionField.setValue(material.getRefraction());
        indexField.setValue(material.getRefractionIndex());

        Point3d[] vertices = triangle.getVertices();

        axField.setValue(vertices[0].x);
        ayField.setValue(vertices[0].y);
        azField.setValue(vertices[0].z);

        bxField.setValue(vertices[1].x);
        byField.setValue(vertices[1].y);
        bzField.setValue(vertices[1].z);

        cxField.setValue(vertices[2].x);
        cyField.setValue(vertices[2].y);
        czField.setValue(vertices[2].z);
    }

    @Override
    public boolean validate() {
        return super.validate() && validateVertexA() && validateVertexB() && validateVertexC();
    }

    @Override
    public JPanel createMessage() {
        JPanel p = super.createMessage();

        xLocationField.setEnabled(false);
        yLocationField.setEnabled(false);
        zLocationField.setEnabled(false);

        return p;
    }

    @Override
    public JPanel createSpecificMessage() {
        createUI();

        return specificMessage;
    }

    @Override
    public void extractTo(Primitive primitive) {
        float[] rgb = colorButton.getColor().getRGBColorComponents(null);

        primitive.getMaterial().getColor().red = rgb[0];
        primitive.getMaterial().getColor().green = rgb[1];
        primitive.getMaterial().getColor().blue = rgb[2];
        primitive.getMaterial().setDiffuse((Double) diffuseField.getValue());
        primitive.getMaterial().setSpecular((Double) specularField.getValue());
        primitive.getMaterial().setReflection((Double) reflectionField.getValue());
        primitive.getMaterial().setRefraction((Double) refractionField.getValue());
        primitive.getMaterial().setRefractionIndex((Double) indexField.getValue());

        ((Triangle) primitive).setVertices(new Point3d[]{getVertexA(), getVertexB(), getVertexC()});
    }

    public Point3d getVertexA() {
        return new Point3d((Double) axField.getValue(), (Double) ayField.getValue(), (Double) azField.getValue());
    }

    public Point3d getVertexB() {
        return new Point3d((Double) bxField.getValue(), (Double) byField.getValue(), (Double) bzField.getValue());
    }

    public Point3d getVertexC() {
        return new Point3d((Double) cxField.getValue(), (Double) cyField.getValue(), (Double) czField.getValue());
    }

    private boolean validateVertexA() {
        return axField.getInputVerifier().verify(axField) &&
                ayField.getInputVerifier().verify(ayField) &&
                azField.getInputVerifier().verify(azField);
    }

    private boolean validateVertexB() {
        return bxField.getInputVerifier().verify(bxField) &&
                byField.getInputVerifier().verify(byField) &&
                bzField.getInputVerifier().verify(bzField);
    }

    private boolean validateVertexC() {
        return cxField.getInputVerifier().verify(cxField) &&
                cyField.getInputVerifier().verify(cyField) &&
                czField.getInputVerifier().verify(czField);
    }

    private void createUI() {
        GridBagConstraints gbc;

        specificMessage = new JPanel();
        aPanel = new JPanel();
        axLabel = new JLabel();
        axField = new JFormattedTextField(Utils.createDoubleFormatter());
        ayLabel = new JLabel();
        ayField = new JFormattedTextField(Utils.createDoubleFormatter());
        azLabel = new JLabel();
        azField = new JFormattedTextField(Utils.createDoubleFormatter());
        bPanel = new JPanel();
        bxLabel = new JLabel();
        bxField = new JFormattedTextField(Utils.createDoubleFormatter());
        byLabel = new JLabel();
        byField = new JFormattedTextField(Utils.createDoubleFormatter());
        bzLabel = new JLabel();
        bzField = new JFormattedTextField(Utils.createDoubleFormatter());
        cPanel = new JPanel();
        cxLabel = new JLabel();
        cxField = new JFormattedTextField(Utils.createDoubleFormatter());
        cyLabel = new JLabel();
        cyField = new JFormattedTextField(Utils.createDoubleFormatter());
        czLabel = new JLabel();
        czField = new JFormattedTextField(Utils.createDoubleFormatter());

        specificMessage.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        specificMessage.setName("Form");
        specificMessage.setPreferredSize(new Dimension(190, 220));
        specificMessage.setLayout(new BoxLayout(specificMessage, BoxLayout.PAGE_AXIS));

        aPanel.setBorder(BorderFactory.createTitledBorder("Vertex A (Location)"));
        aPanel.setName("aPanel");
        aPanel.setLayout(new GridBagLayout());

        axLabel.setText("X:");
        axLabel.setName("axLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        aPanel.add(axLabel, gbc);

        axField.setColumns(4);
        axField.setHorizontalAlignment(JTextField.TRAILING);
        axField.setName("axField");
        axField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        aPanel.add(axField, gbc);

        ayLabel.setText("Y:");
        ayLabel.setName("ayLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        aPanel.add(ayLabel, gbc);

        ayField.setColumns(4);
        ayField.setHorizontalAlignment(JTextField.TRAILING);
        ayField.setName("ayField");
        ayField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        aPanel.add(ayField, gbc);

        azLabel.setText("Z:");
        azLabel.setName("azLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        aPanel.add(azLabel, gbc);

        azField.setColumns(4);
        azField.setHorizontalAlignment(JTextField.TRAILING);
        azField.setName("azField");
        azField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        aPanel.add(azField, gbc);

        specificMessage.add(aPanel);

        bPanel.setBorder(BorderFactory.createTitledBorder("Vertex B"));
        bPanel.setName("bPanel");
        bPanel.setLayout(new GridBagLayout());

        bxLabel.setText("X:");
        bxLabel.setName("bxLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        bPanel.add(bxLabel, gbc);

        bxField.setColumns(4);
        bxField.setHorizontalAlignment(JTextField.TRAILING);
        bxField.setName("bxField");
        bxField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        bPanel.add(bxField, gbc);

        byLabel.setText("Y:");
        byLabel.setName("byLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        bPanel.add(byLabel, gbc);

        byField.setColumns(4);
        byField.setHorizontalAlignment(JTextField.TRAILING);
        byField.setName("byField");
        byField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        bPanel.add(byField, gbc);

        bzLabel.setText("Z:");
        bzLabel.setName("bzLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        bPanel.add(bzLabel, gbc);

        bzField.setColumns(4);
        bzField.setHorizontalAlignment(JTextField.TRAILING);
        bzField.setName("bzField");
        bzField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        bPanel.add(bzField, gbc);

        specificMessage.add(bPanel);

        cPanel.setBorder(BorderFactory.createTitledBorder("Vertex C"));
        cPanel.setName("cPanel");
        cPanel.setLayout(new GridBagLayout());

        cxLabel.setText("X:");
        cxLabel.setName("cxLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        cPanel.add(cxLabel, gbc);

        cxField.setColumns(4);
        cxField.setHorizontalAlignment(JTextField.TRAILING);
        cxField.setName("cxField");
        cxField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        cPanel.add(cxField, gbc);

        cyLabel.setText("Y:");
        cyLabel.setName("cyLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        cPanel.add(cyLabel, gbc);

        cyField.setColumns(4);
        cyField.setHorizontalAlignment(JTextField.TRAILING);
        cyField.setName("cyField");
        cyField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        cPanel.add(cyField, gbc);

        czLabel.setText("Z:");
        czLabel.setName("czLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        cPanel.add(czLabel, gbc);

        czField.setColumns(4);
        czField.setHorizontalAlignment(JTextField.TRAILING);
        czField.setName("czField");
        czField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        cPanel.add(czField, gbc);

        specificMessage.add(cPanel);
    }
}
