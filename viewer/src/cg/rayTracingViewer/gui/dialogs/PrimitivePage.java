package cg.rayTracingViewer.gui.dialogs;

import cg.rayTracingViewer.gui.*;
import cg.rayTracingViewer.PrimitiveType;
import cg.rayTracingViewer.Manager;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Alexandre Vieira
 */
public class PrimitivePage extends HeaderPage {

    private static final String HEADER_TITLE;
    private static final String HEADER_DESCRIPTION;
    private JTextField nameField;
    private JComboBox typeBox;
    private JLabel warningLabel;
    private PrimitiveTypeBoxModel boxModel;
    private Controller controller;
    private ArrayList<String> ids;

    static {
        HEADER_TITLE = "Create a new Primitive";
        HEADER_DESCRIPTION = "Type the name of the Primitive and select its type.";
    }

    public PrimitivePage(ArrayList<String> ids) {
        this.controller = new Controller();
        this.boxModel = new PrimitiveTypeBoxModel(Manager.PRIMITIVE_TYPES);
        this.ids = ids;
        
        setHeader(createHeader());
        setContentPane(createContentPane());
        setID(WizardPage.LAST_PAGE_ID);
    }

    @Override
    public String getNextPageID() {
        return null;
    }

    @Override
    public String getPreviousPageID() {
        return null;
    }

    @Override
    public void onShow() {
        getWizard().disableFinishButton();
        nameField.requestFocus();
    }
    
    public PrimitiveType getPrimitiveType(){
        return boxModel.getSelectedPrimitiveType();
    }

    public String getPrimitiveName() {
        return nameField.getText();
    }

    private HeaderPage.Header createHeader() {
        HeaderPage.Header header = new Header(null, HEADER_TITLE, HEADER_DESCRIPTION);

        return header;
    }

    private JPanel createContentPane() {
        nameField = new JTextField();
        typeBox = new JComboBox(boxModel);
        warningLabel = new JLabel();

        nameField.getDocument().addDocumentListener(controller);
        warningLabel.setForeground(Color.RED);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        panel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0f;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0f;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0f;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(typeBox, gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0f;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(warningLabel, gbc);

        return panel;
    }

    private class Controller implements ActionListener, DocumentListener {

        public void actionPerformed(ActionEvent ae) {
        }

        public void changedUpdate(DocumentEvent de) {
        }

        public void insertUpdate(DocumentEvent de) {
            warningLabel.setText("");
            if (!nameField.getText().equals("") && nameField.getText() != null) {
                if (ids.contains(nameField.getText())) {
                    getWizard().disableFinishButton();
                    warningLabel.setText("* The Primitive '" + nameField.getText() + "' already exists");
                } else {
                    getWizard().enableFinishButton();
                }
            } else {
                getWizard().disableFinishButton();
            }
        }

        public void removeUpdate(DocumentEvent de) {
            warningLabel.setText("");
            if (!nameField.getText().equals("") && nameField.getText() != null) {
                if (ids.contains(nameField.getText())) {
                    getWizard().disableFinishButton();
                    warningLabel.setText("* The Primitive '" + nameField.getText() + "' already exists");
                } else {
                    getWizard().enableFinishButton();
                }
            } else {
                getWizard().disableFinishButton();
            }
        }
    }
}
