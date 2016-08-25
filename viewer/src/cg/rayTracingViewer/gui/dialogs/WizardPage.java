package cg.rayTracingViewer.gui.dialogs;

import javax.swing.JComponent;

/**
 * @author Alexandre Vieira
 */
public abstract class WizardPage {

    public static final String FIRST_PAGE_ID = "firstPageID";
    public static final String LAST_PAGE_ID = "lastPageID";
    private Wizard wizard;
    private String id;

    public WizardPage() {
    }

    public WizardPage(String id) {
        this.id = id;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public void setID(String id) {
        this.id = id;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public String getID() {
        return id;
    }

    public abstract JComponent getComponent();

    public abstract String getNextPageID();

    public abstract String getPreviousPageID();

    public abstract void onShow();

    public abstract void onHide();
}
