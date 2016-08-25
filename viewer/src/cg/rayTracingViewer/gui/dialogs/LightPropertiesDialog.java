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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import rayTracing.math.Point3d;
import rayTracing.scene.Light;

/**
 * @author Alexandre Vieira
 */
public class LightPropertiesDialog extends CustomDialog {

    private JPanel messagePanel;
    private ColorButton colorButton;
    private JLabel colorLabel;
    private JPanel locationPanel;
    private JPanel panel;
    private JFormattedTextField xLocationField;
    private JLabel xLocationLabel;
    private JFormattedTextField yLocationField;
    private JLabel yLocationLabel;
    private JFormattedTextField zLocationField;
    private JLabel zLocationLabel;
    private Controller controller;

    public LightPropertiesDialog(Frame owner, Light light) {
        super(owner, light.getId() + " Properties");

        xLocationField.setValue(light.getLocation().x);
        yLocationField.setValue(light.getLocation().y);
        zLocationField.setValue(light.getLocation().z);
        colorButton.setColor(new Color((float) light.getColor().red,
                (float) light.getColor().green, (float) light.getColor().blue));
    }

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
                zLocationField.getInputVerifier().verify(zLocationField);
    }

    public void extractTo(Light light) {
        float[] rgb = colorButton.getColor().getRGBColorComponents(null);

        light.getColor().red = rgb[0];
        light.getColor().green = rgb[1];
        light.getColor().blue = rgb[2];
        light.setLocation(getLocation());
    }

    public Point3d getLocation() {
        Point3d point = new Point3d();

        point.x = (Double) xLocationField.getValue();
        point.y = (Double) yLocationField.getValue();
        point.z = (Double) zLocationField.getValue();

        return point;
    }

    private void onColor() {
        Color color = JColorChooser.showDialog(getDialog(), "Light Color", colorButton.getColor());

        if (color != null) {
            colorButton.setColor(color);
        }
    }

    private void createUI() {
        GridBagConstraints gbc;

        messagePanel = new JPanel();
        panel = new JPanel();
        locationPanel = new JPanel();
        xLocationLabel = new JLabel();
        xLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        yLocationLabel = new JLabel();
        yLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        zLocationLabel = new JLabel();
        zLocationField = new JFormattedTextField(Utils.createDoubleFormatter());
        colorLabel = new JLabel();
        colorButton = new ColorButton(Color.WHITE);

        messagePanel.setMaximumSize(new Dimension(300, 400));
        messagePanel.setMinimumSize(new Dimension(300, 400));
        messagePanel.setName("Form");
        messagePanel.setLayout(new BorderLayout());

        panel.setName("panel");
        panel.setLayout(new GridBagLayout());

        locationPanel.setBorder(BorderFactory.createTitledBorder("Location"));
        locationPanel.setName("locationPanel");
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.LINE_AXIS));

        xLocationLabel.setText("X:");
        xLocationLabel.setName("xLocationLabel");
        locationPanel.add(xLocationLabel);

        xLocationField.setColumns(4);
        xLocationField.setInputVerifier(Utils.createDoubleVerifier());
        xLocationField.setHorizontalAlignment(JTextField.TRAILING);
        xLocationField.setName("xLocationField");
        locationPanel.add(xLocationField);

        yLocationLabel.setText("Y:");
        yLocationLabel.setName("yLocationLabel");
        locationPanel.add(yLocationLabel);

        yLocationField.setColumns(4);
        yLocationField.setInputVerifier(Utils.createDoubleVerifier());
        yLocationField.setHorizontalAlignment(JTextField.TRAILING);
        yLocationField.setName("yLocationField");
        locationPanel.add(yLocationField);

        zLocationLabel.setText("Z:");
        zLocationLabel.setName("zLocationLabel");
        locationPanel.add(zLocationLabel);

        zLocationField.setColumns(4);
        zLocationField.setInputVerifier(Utils.createDoubleVerifier());
        zLocationField.setHorizontalAlignment(JTextField.TRAILING);
        zLocationField.setName("zLocationField");
        locationPanel.add(zLocationField);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(locationPanel, gbc);

        colorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        colorLabel.setText("Color:");
        colorLabel.setName("colorLabel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 3, 3, 3);
        panel.add(colorButton, gbc);

        messagePanel.add(panel, BorderLayout.NORTH);
    }
    
    class Controller implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource().equals(colorButton)) {
                onColor();
            }
        }
    }
}
