package cg.rayTracingViewer.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author Alexandre Vieira
 */
public class Wizard {

    public static final int FINISH_CODE = 0;
    public static final int CANCEL_CODE = 1;
    public static final int SINGLE_PAGE = 3;
    public static final int MULTIPLE_PAGES = 4;
    private HashMap<String, WizardPage> pages;
    private Controller controller;
    private JDialog dialog;
    private JButton backButton;
    private JButton nextButton;
    private JButton finishButton;
    private JButton cancelButton;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private String currentPageID;
    private int returnCode;
    private int type;

    public Wizard(Frame onwer, int type) {
        this.type = type;
        this.dialog = new JDialog(onwer);
        this.pages = new HashMap<String, WizardPage>();
        this.controller = new Controller();
        this.returnCode = CANCEL_CODE;
        switch (type) {
            case SINGLE_PAGE:
                createUISingle();
                break;
            case MULTIPLE_PAGES:
                createUIMultiple();
                break;
            default:
                throw new IllegalArgumentException("Illegal wizard type: " + type);
        }
        this.dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                returnCode = CANCEL_CODE;
            }
        });
        setModal(true);
        setResizable(false);
    }

    public Wizard(Dialog onwer, int type) {
        this.type = type;
        this.dialog = new JDialog(onwer);
        this.pages = new HashMap<String, WizardPage>();
        this.controller = new Controller();
        this.returnCode = CANCEL_CODE;
        switch (type) {
            case SINGLE_PAGE:
                createUISingle();
                break;
            case MULTIPLE_PAGES:
                createUIMultiple();
                break;
            default:
                throw new IllegalArgumentException("Illegal wizard type: " + type);
        }
        this.dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                returnCode = CANCEL_CODE;
            }
        });
        setModal(true);
        setResizable(false);
    }

    protected void setCurrentPageID(String id) {
        currentPageID = id;
    }

    public void setSize(int width, int height) {
        dialog.setSize(width, height);
    }

    public void setResizable(boolean resizable) {
        dialog.setResizable(resizable);
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }

    public void setModal(boolean modal) {
        dialog.setModalityType(modal ? JDialog.ModalityType.APPLICATION_MODAL
                : JDialog.ModalityType.MODELESS);
    }

    public HashMap<String, WizardPage> getPages() {
        return pages;
    }

    public WizardPage getPage(String id) {
        return pages.get(id);
    }

    public WizardPage getCurrentPage() {
        return pages.get(currentPageID);
    }

    public JDialog getDialog() {
        return dialog;
    }

    public String getTitle() {
        return dialog.getTitle();
    }

    public String getCurrentPageID() {
        return currentPageID;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public int getType() {
        return type;
    }

    public boolean isModal() {
        return dialog.isModal();
    }

    public void enableBackButton() {
        if (type == SINGLE_PAGE) {
            throw new UnsupportedOperationException("Unavailable button.");
        }
        if (currentPageID.equals(WizardPage.FIRST_PAGE_ID)) {
            throw new UnsupportedOperationException("There is no previous page.");
        }
        backButton.setEnabled(true);
    }

    public void enableNextButton() {
        if (type == SINGLE_PAGE) {
            throw new UnsupportedOperationException("Unavailable button.");
        }
        if (currentPageID.equals(WizardPage.LAST_PAGE_ID)) {
            throw new UnsupportedOperationException("There is no next page.");
        }
        nextButton.setEnabled(true);
    }

    public void enableFinishButton() {
        finishButton.setEnabled(true);
    }

    public void disableBackButton() {
        if (type == SINGLE_PAGE) {
            throw new UnsupportedOperationException("Unavailable button.");
        }
        backButton.setEnabled(false);
    }

    public void disableNextButton() {
        if (type == SINGLE_PAGE) {
            throw new UnsupportedOperationException("Unavailable button.");
        }
        nextButton.setEnabled(false);
    }

    public void disableFinishButton() {
        finishButton.setEnabled(false);
    }

    public int show() {
        switch (type) {
            case SINGLE_PAGE:
                if (!pages.containsKey(WizardPage.LAST_PAGE_ID)) {
                    throw new IllegalStateException("No last page was specified.");
                }
                goToPage(WizardPage.LAST_PAGE_ID);
                break;
            case MULTIPLE_PAGES:
                if (!pages.containsKey(WizardPage.FIRST_PAGE_ID) ||
                        !pages.containsKey(WizardPage.LAST_PAGE_ID)) {
                    throw new IllegalStateException("No first and/or last pages were specified.");
                }
                goToPage(WizardPage.FIRST_PAGE_ID);
                break;
        }
        dialog.setVisible(true);
        return returnCode;
    }

    public void close(int code) {
        dialog.setVisible(false);
        dialog.dispose();
        returnCode = code;
    }

    public boolean addPage(WizardPage page) {
        if (type == SINGLE_PAGE && !page.getID().equals(WizardPage.LAST_PAGE_ID)) {
            throw new IllegalArgumentException("Invalid page ID: " + page.getID());
        }
        if (pages.containsKey(page.getID())) {
            return false;
        }
        pages.put(page.getID(), page);
        cardPanel.add(page.getComponent(), page.getID());
        page.setWizard(this);
        return true;
    }

    public void goToPage(String id) {
        if (currentPageID != null) {
            pages.get(currentPageID).onHide();
        }
        setCurrentPageID(id);
        if (currentPageID.equals(WizardPage.FIRST_PAGE_ID)) {
            onFirstPage();
        } else if (currentPageID.equals(WizardPage.LAST_PAGE_ID)) {
            onLastPage();
        } else {
            enableBackButton();
        }
        pages.get(getCurrentPageID()).onShow();
        cardLayout.show(cardPanel, getCurrentPageID());
    }

    private void onFirstPage() {
        if (type == MULTIPLE_PAGES) {
            disableBackButton();
            enableNextButton();
        }
    }

    private void onLastPage() {
        if (type == MULTIPLE_PAGES) {
            disableNextButton();
            enableBackButton();
        }
    }

    private void createUISingle() {
        finishButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        finishButton.addActionListener(controller);
        cancelButton.addActionListener(controller);

        JPanel buttonsPanel = new JPanel(new BorderLayout());
        Box buttonsBox = Box.createHorizontalBox();
        buttonsBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.add(finishButton);
        buttonsBox.add(Box.createHorizontalStrut(5));
        buttonsBox.add(cancelButton);
        buttonsBox.add(Box.createHorizontalStrut(10));

        buttonsPanel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        buttonsPanel.add(buttonsBox, BorderLayout.CENTER);
        dialog.setLayout(new BorderLayout());
        dialog.add(cardPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void createUIMultiple() {
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        finishButton = new JButton("Finish");
        cancelButton = new JButton("Cancel");
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        backButton.addActionListener(controller);
        nextButton.addActionListener(controller);
        finishButton.addActionListener(controller);
        cancelButton.addActionListener(controller);

        JPanel buttonsPanel = new JPanel(new BorderLayout());
        Box buttonsBox = Box.createHorizontalBox();
        buttonsBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonsBox.add(Box.createHorizontalGlue());
        buttonsBox.add(backButton);
        buttonsBox.add(Box.createHorizontalStrut(5));
        buttonsBox.add(nextButton);
        buttonsBox.add(Box.createHorizontalStrut(5));
        buttonsBox.add(finishButton);
        buttonsBox.add(Box.createHorizontalStrut(5));
        buttonsBox.add(cancelButton);
        buttonsBox.add(Box.createHorizontalStrut(10));

        buttonsPanel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
        buttonsPanel.add(buttonsBox, BorderLayout.CENTER);
        dialog.setLayout(new BorderLayout());
        dialog.add(cardPanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private class Controller implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            Object source = ae.getSource();
            if (source.equals(backButton)) {
                onBack();
            } else if (source.equals(nextButton)) {
                onNext();
            } else if (source.equals(finishButton)) {
                onFinish();
            } else if (source.equals(cancelButton)) {
                onCancel();
            }
        }

        private void onBack() {
            goToPage(getCurrentPage().getPreviousPageID());
        }

        private void onNext() {
            goToPage(getCurrentPage().getNextPageID());
        }

        private void onFinish() {
            close(Wizard.FINISH_CODE);
        }

        private void onCancel() {
            close(Wizard.CANCEL_CODE);
        }
    }
}
