package cg.rayTracingViewer.gui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import rayTracing.scene.Rectangle;
import rayTracing.scene.Sphere;
import rayTracing.scene.Triangle;

/**
 * @author Alexandre Vieira
 */
public class SceneTreeRenderer extends DefaultTreeCellRenderer {

    private static final Icon SPHERE_ICON;
    private static final Icon RECTANGLE_ICON;
    private static final Icon TRIANGLE_ICON;
    private Icon defaultLeafIcon;
    private Icon defaultNodeIcon;

    static {
        SPHERE_ICON = Utils.createIcon("sphere.png");
        RECTANGLE_ICON = Utils.createIcon("rectangle.png");
        TRIANGLE_ICON = Utils.createIcon("triangle.png");
    }

    public SceneTreeRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {

        if (tree.getPathForRow(row) != null) {
            Object component = tree.getPathForRow(row).getLastPathComponent();

            if (component instanceof PrimitiveNode) {
                PrimitiveNode node = (PrimitiveNode) component;

                if (node.getPrimitiveType().getType().equals(Sphere.class)) {
                    setLeafIcon(SPHERE_ICON);
                } else if (node.getPrimitiveType().getType().equals(Rectangle.class)) {
                    setLeafIcon(RECTANGLE_ICON);
                } else if (node.getPrimitiveType().getType().equals(Triangle.class)) {
                    setLeafIcon(TRIANGLE_ICON);
                }
            }
        }

        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }
}
