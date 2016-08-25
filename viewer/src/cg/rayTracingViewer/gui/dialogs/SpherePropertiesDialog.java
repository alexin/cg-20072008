package cg.rayTracingViewer.gui.dialogs;

import cg.rayTracingViewer.gui.Utils;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rayTracing.scene.Primitive;
import rayTracing.scene.Sphere;

/**
 * @author Alexandre Vieira
 */
public class SpherePropertiesDialog extends PrimitivePropertiesDialog {

    private JFormattedTextField radiusField;
    private JLabel radiusLabel;
    private JPanel radiusPanel;
    private JPanel messagePanel;

    public SpherePropertiesDialog(Frame owner, Sphere sphere) {
        super(owner, sphere);

        radiusField.setValue(sphere.getRadius());
    }

    @Override
    public boolean validate() {
        return super.validate() && radiusField.getInputVerifier().verify(radiusField);
    }

    @Override
    public JPanel createSpecificMessage() {
        createUI();

        return messagePanel;
    }

    @Override
    public void extractTo(Primitive primitive) {
        super.extractTo(primitive);
        ((Sphere) primitive).setRadius(getRadius());
    }

    public double getRadius() {
        return (Double) radiusField.getValue();
    }

    private void createUI() {
        messagePanel = new JPanel();
        radiusPanel = new JPanel();
        radiusLabel = new JLabel();
        radiusField = new JFormattedTextField(Utils.createDoubleFormatter());


        messagePanel.setName("Form");
        messagePanel.setLayout(new BorderLayout());

        radiusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        radiusPanel.setName("radiusPanel");
        radiusPanel.setLayout(new BoxLayout(radiusPanel, BoxLayout.LINE_AXIS));

        radiusLabel.setText("Radius:");
        radiusLabel.setName("radiusLabel");
        radiusPanel.add(radiusLabel);

        radiusField.setColumns(4);
        radiusField.setInputVerifier(Utils.createDoubleVerifier());

        radiusField.setHorizontalAlignment(JTextField.TRAILING);
        radiusField.setName("radiusField");
        radiusPanel.add(radiusField);

        messagePanel.add(radiusPanel, BorderLayout.NORTH);
    }
}
