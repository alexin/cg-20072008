package cg.rayTracingViewer.gui;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 * @author Alexandre Vieira
 */
public class PrimitiveListModel extends AbstractListModel {

    private ArrayList<Object> primitivesIDs;

    public PrimitiveListModel() {
        this(new ArrayList<Object>());
    }

    public PrimitiveListModel(ArrayList<Object> ids) {
        this.primitivesIDs = ids;
    }

    public int getSize() {
        return primitivesIDs.size();
    }

    public Object getElementAt(int index) {
        return primitivesIDs.get(index);
    }

    public boolean isEmpty() {
        return primitivesIDs.isEmpty();
    }

    public int indexOf(Object id) {
        return primitivesIDs.indexOf(id);
    }

    public void add(Object id, int index) {
        primitivesIDs.add(index, id);
        fireIntervalAdded(this, index, index);
    }

    public void add(Object id) {
        int index = primitivesIDs.size();
        primitivesIDs.add(id);
        fireIntervalAdded(this, index, index);
    }

    public void remove(int index) {
        primitivesIDs.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public boolean remove(Object id) {
        int index = indexOf(id);
        boolean rv = primitivesIDs.remove(id);

        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }

        return rv;
    }

    public Object get(int index) {
        return primitivesIDs.get(index);
    }

    public boolean contains(Object id) {
        return primitivesIDs.contains(id);
    }

    public void clear() {
        int index = primitivesIDs.size() - 1;
        primitivesIDs.clear();

        if (index >= 0) {
            fireIntervalRemoved(this, 0, index);
        }
    }

    public void removeRange(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex must be <= toIndex");
        }

        for (int i = toIndex; i >= fromIndex; i--) {
            primitivesIDs.remove(i);
        }
        fireIntervalRemoved(this, fromIndex, toIndex);
    }
}
