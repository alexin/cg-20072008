package cg.rayTracingViewer.gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Alexandre Vieira
 */
public abstract class CustomDialog {

    public final static int APPROVE_OPTION = 0;
    public final static int CANCEL_OPTION = 1;
    private JDialog dialog;
    private JOptionPane optionPane;
    private Frame owner;
    private String title;

    public CustomDialog(Frame owner, String title) {
        this.owner = owner;
        this.title = title;
        JButton bApprove = new JButton("Ok");
        bApprove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (validate()) {
                    optionPane.setValue(APPROVE_OPTION);
                    dialog.setVisible(false);
                    dialog.dispose();
                } else {
                    onFailedValidation();
                }
            }
        });
        JButton bCancel = new JButton("Cancel");
        bCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                optionPane.setValue(CANCEL_OPTION);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        optionPane = new JOptionPane(new Object[]{createMessage()}, JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, new Object[]{bApprove, bCancel});
    }

    public JDialog getDialog() {
        return dialog;
    }

    public JOptionPane getOptionPane() {
        return optionPane;
    }

    public Frame getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public abstract JPanel createMessage();

    public abstract boolean validate();

    public int showDialog() {
        dialog = optionPane.createDialog(owner, title);

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                optionPane.setValue(CANCEL_OPTION);
            }
        });

        dialog.pack();
        dialog.setVisible(true);

        return (Integer) optionPane.getValue();
    }

    public void onFailedValidation() {
        JOptionPane.showMessageDialog(dialog, "The dialog is invalid", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
