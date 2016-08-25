package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.PrimitiveType;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Alexandre Vieira
 */
public class PrimitiveNode extends DefaultMutableTreeNode {

    private PrimitiveType type;

    public PrimitiveNode(Object id, PrimitiveType type) {
        super(id);

        this.type = type;
    }

    public PrimitiveType getPrimitiveType() {
        return type;
    }
}
