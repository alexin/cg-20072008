package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.LightType;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 * @author Alexandre Vieira
 */
public class LightTypeBoxModel extends AbstractListModel implements MutableComboBoxModel {

    private ArrayList<LightType> types;
    private LightType selectedType;

    public LightTypeBoxModel() {
        this(new ArrayList<LightType>());
    }

    public LightTypeBoxModel(LightType[] types) {
        this.types = new ArrayList<LightType>(types.length);

        for (LightType type : types) {
            this.types.add(type);
        }

        if (this.types.size() > 0) {
            selectedType = this.types.get(0);
        }
    }

    public LightTypeBoxModel(ArrayList<LightType> types) {
        this.types = types;

        if (this.types.size() > 0) {
            selectedType = this.types.get(0);
        }
    }

    public void setSelectedItem(Object o) {
        if (!(o instanceof LightType)) {
            throw new IllegalArgumentException("Only LightType allowed.");
        }

        if ((selectedType != null && !selectedType.equals(o)) ||
                selectedType == null && o != null) {
            selectedType = (LightType) o;
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
        if (!(o instanceof LightType)) {
            throw new IllegalArgumentException("Only LightType allowed.");
        }

        types.add((LightType) o);
        fireIntervalAdded(this, types.size() - 1, types.size() - 1);
        if (types.size() == 1 && selectedType == null && o != null) {
            setSelectedItem(o);
        }
    }

    public void insertElementAt(Object o, int index) {
        if (!(o instanceof LightType)) {
            throw new IllegalArgumentException("Only LightType allowed.");
        }

        types.add(index, (LightType) o);
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
        if (!(o instanceof LightType)) {
            throw new IllegalArgumentException("Only LightType allowed.");
        }

        int index = types.indexOf(o);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    public LightType getSelectedLightType() {
        return (LightType) getSelectedItem();
    }

    public int indexOf(LightType type) {
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
