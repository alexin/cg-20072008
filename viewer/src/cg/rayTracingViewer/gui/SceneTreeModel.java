package cg.rayTracingViewer.gui;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author Alexandre Vieira
 */
public class SceneTreeModel extends DefaultTreeModel {

    private DefaultMutableTreeNode primitivesRoot;
    private DefaultMutableTreeNode lightsRoot;

    public SceneTreeModel(DefaultMutableTreeNode root) {
        super(root);

        primitivesRoot = new DefaultMutableTreeNode("Primitives");
        lightsRoot = new DefaultMutableTreeNode("Lights");
        insertNodeInto(primitivesRoot, root, 0);
        insertNodeInto(lightsRoot, root, 0);
    }

    public DefaultMutableTreeNode getLightsRoot() {
        return lightsRoot;
    }

    public DefaultMutableTreeNode getPrimitivesRoot() {
        return primitivesRoot;
    }

    public void addPrimitiveNode(PrimitiveNode node) {
        insertNodeInto(node, primitivesRoot, 0);
    }

    public void addLightNode(LightNode node) {
        insertNodeInto(node, lightsRoot, 0);
    }

    public void removePrimitiveNode(PrimitiveNode node) {
        removeNodeFromParent(node);
    }

    public void removeLightNode(LightNode node) {
        removeNodeFromParent(node);
    }

    public ArrayList<String> getIDsFor(DefaultMutableTreeNode node) {
        ArrayList<String> ids = new ArrayList<String>(node.getChildCount());

        for (int index = 0; index < node.getChildCount(); index++) {
            ids.add(((DefaultMutableTreeNode)node.getChildAt(index)).getUserObject().toString());
        }

        return ids;
    }
}
