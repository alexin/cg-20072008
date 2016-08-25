package cg.rayTracingViewer.gui.dialogs;

import cg.rayTracingViewer.gui.Utils;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rayTracing.scene.Primitive;
import rayTracing.scene.RectangularPrism;

/**
 * @author Alexandre Vieira
 */
public class PrismPropertiesDialog extends PrimitivePropertiesDialog {

    private JPanel dimensionPanel;
    private JFormattedTextField xField;
    private JLabel xLabel;
    private JFormattedTextField yField;
    private JLabel yLabel;
    private JFormattedTextField zField;
    private JLabel zLabel;
    private JPanel specificMessage;

    public PrismPropertiesDialog(Frame owner, RectangularPrism prism) {
        super(owner, prism);
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

        //((RectangularPrism) primitive).setVertices(new Point3d[]{getVertexA(), getVertexB(), getVertexC()});
    }

    public double getX() {
        return (Double) xField.getValue();
    }

    public double getY() {
        return (Double) yField.getValue();
    }

    public double getZ() {
        return (Double) zField.getValue();
    }

    private void createUI() {
        GridBagConstraints gbc;

        specificMessage = new JPanel();
        dimensionPanel = new JPanel();
        xLabel = new JLabel();
        xField = new JFormattedTextField(Utils.createDoubleFormatter());
        yLabel = new JLabel();
        yField = new JFormattedTextField(Utils.createDoubleFormatter());
        zLabel = new JLabel();
        zField = new JFormattedTextField(Utils.createDoubleFormatter());

        specificMessage.setName("Form");
        specificMessage.setLayout(new BorderLayout());

        dimensionPanel.setName("dimensionPanel");
        dimensionPanel.setLayout(new GridBagLayout());

        xLabel.setText("X:");
        xLabel.setName("xLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        dimensionPanel.add(xLabel, gbc);

        xField.setColumns(4);
        xField.setHorizontalAlignment(JTextField.TRAILING);
        xField.setName("xField");
        xField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        dimensionPanel.add(xField, gbc);

        yLabel.setText("Y:");
        yLabel.setName("yLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        dimensionPanel.add(yLabel, gbc);

        yField.setColumns(4);
        yField.setHorizontalAlignment(JTextField.TRAILING);
        yField.setName("yField");
        yField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        dimensionPanel.add(yField, gbc);

        zLabel.setText("Z:");
        zLabel.setName("zLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 3, 2);
        dimensionPanel.add(zLabel, gbc);

        zField.setColumns(4);
        zField.setHorizontalAlignment(JTextField.TRAILING);
        zField.setName("zField");
        zField.setInputVerifier(Utils.createDoubleVerifier());
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(3, 0, 3, 3);
        dimensionPanel.add(zField, gbc);

        specificMessage.add(dimensionPanel, BorderLayout.NORTH);
    }
}
