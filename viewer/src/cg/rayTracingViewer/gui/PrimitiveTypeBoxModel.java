package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.PrimitiveType;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 * @author Alexandre Vieira
 */
public class PrimitiveTypeBoxModel extends AbstractListModel implements MutableComboBoxModel {

    private ArrayList<PrimitiveType> types;
    private PrimitiveType selectedType;

    public PrimitiveTypeBoxModel() {
        this(new ArrayList<PrimitiveType>());
    }

    public PrimitiveTypeBoxModel(PrimitiveType[] types) {
        this.types = new ArrayList<PrimitiveType>(types.length);

        for (PrimitiveType type : types) {
            this.types.add(type);
        }

        if (this.types.size() > 0) {
            selectedType = this.types.get(0);
        }
    }

    public PrimitiveTypeBoxModel(ArrayList<PrimitiveType> types) {
        this.types = types;

        if (this.types.size() > 0) {
            selectedType = this.types.get(0);
        }
    }

    public void setSelectedItem(Object o) {
        if (!(o instanceof PrimitiveType)) {
            throw new IllegalArgumentException("Only PrimitiveType allowed.");
        }

        if ((selectedType != null && !selectedType.equals(o)) ||
                selectedType == null && o != null) {
            selectedType = (PrimitiveType) o;
            fireContentsChanged(this, -1, -1);
        }
    }

    public Object getSelectedItem() {
        return selectedType;
    }

    public int getSize() {
        return types.size();
    }

    public Object getElementAt(int index) {
        if (index >= 0 && index < types.size()) {
            return types.get(index);
        } else {
            return null;
        }
    }

    public void addElement(Object o) {
        if (!(o instanceof PrimitiveType)) {
            throw new IllegalArgumentException("Only PrimitiveType allowed.");
        }

        types.add((PrimitiveType) o);
        fireIntervalAdded(this, types.size() - 1, types.size() - 1);
        if (types.size() == 1 && selectedType == null && o != null) {
            setSelectedItem(o);
        }
    }

    public void insertElementAt(Object o, int index) {
        if (!(o instanceof PrimitiveType)) {
            throw new IllegalArgumentException("Only PrimitiveType allowed.");
        }

        types.add(index, (PrimitiveType) o);
        fireIntervalAdded(this, index, index);
    }

    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedType) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        types.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    public void removeElement(Object o) {
        if (!(o instanceof PrimitiveType)) {
            throw new IllegalArgumentException("Only PrimitiveType allowed.");
        }

        int index = types.indexOf(o);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    public PrimitiveType getSelectedPrimitiveType() {
        return (PrimitiveType) getSelectedItem();
    }

    public int indexOf(PrimitiveType type) {
        return types.indexOf(type);
    }

    public void clear() {
        if (types.size() > 0) {
            int firstIndex = 0;
            int lastIndex = types.size() - 1;
            types.clear();
            selectedType = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedType = null;
        }
    }
}
