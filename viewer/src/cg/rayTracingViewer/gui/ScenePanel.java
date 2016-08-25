package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.LightType;
import cg.rayTracingViewer.gui.events.ScenePanelListener;
import cg.rayTracingViewer.PrimitiveType;
import cg.rayTracingViewer.gui.dialogs.CustomDialog;
import cg.rayTracingViewer.gui.dialogs.LightPage;
import cg.rayTracingViewer.gui.dialogs.LightPropertiesDialog;
import cg.rayTracingViewer.gui.dialogs.PrimitivePage;
import cg.rayTracingViewer.gui.dialogs.PrimitivePropertiesDialog;
import cg.rayTracingViewer.gui.dialogs.PrismPropertiesDialog;
import cg.rayTracingViewer.gui.dialogs.SceneChooser;
import cg.rayTracingViewer.gui.dialogs.SpherePropertiesDialog;
import cg.rayTracingViewer.gui.dialogs.TrianglePropertiesDialog;
import cg.rayTracingViewer.gui.dialogs.Wizard;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import rayTracing.scene.Light;
import rayTracing.scene.PointLight;
import rayTracing.scene.Primitive;
import rayTracing.scene.Rectangle;
import rayTracing.scene.RectangularPrism;
import rayTracing.scene.Scene;
import rayTracing.scene.Sphere;
import rayTracing.scene.Triangle;

/**
 * @author Alexandre Vieira
 */
public class ScenePanel extends JPanel {

    private TransformPanel transformPanel;
    private JButton openButton;
    private JButton saveButton;
    private JButton addPrimitiveButton;
    private JButton addLightButton;
    private JPanel sceneButtonsPanel;
    private JTree sceneTree;
    private SceneTreeModel treeModel;
    private JPanel treePanel;
    private JScrollPane treeScroll;
    private JPanel propertiesPanel;
    private JButton removeButton;
    private Controller controller;
    private RayTracingViewer viewer;
    private ScenePanelListener listener;

    public ScenePanel(RayTracingViewer viewer) {
        this.controller = new Controller();
        this.viewer = viewer;
        this.treeModel = new SceneTreeModel(new DefaultMutableTreeNode());

        createUI();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        addPrimitiveButton.setEnabled(enabled);
        addLightButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
        sceneTree.setEnabled(enabled);
        transformPanel.setEnabled(enabled);
    }

    public void setScenePanelListener(ScenePanelListener listener) {
        this.listener = listener;
    }

    public TransformPanel getTransformPanel() {
        return transformPanel;
    }

    protected boolean fireOnPrimitiveAdded(Primitive primitive) {
        if (listener != null) {
            return listener.onPrimitiveAdded(this, primitive);
        }
        return false;
    }

    protected boolean fireOnPrimitiveAdded(Object id, PrimitiveType primitiveType) {
        if (listener != null) {
            return listener.onPrimitiveAdded(this, id, primitiveType);
        }
        return false;
    }

    protected boolean fireOnLightAdded(Light light) {
        if (listener != null) {
            return listener.onLightAdded(this, light);
        }
        return false;
    }

    protected boolean fireOnLightAdded(Object id, LightType lightType) {
        if (listener != null) {
            return listener.onLightAdded(this, id, lightType);
        }
        return false;
    }

    protected void fireOnPrimitiveRemoved(Object id) {
        if (listener != null) {
            listener.onPrimitiveRemoved(this, id);
        }
    }

    protected void fireOnLightRemoved(Object id) {
        if (listener != null) {
            listener.onLightRemoved(this, id);
        }
    }

    protected void fireOnPrimitiveSelected(Object id) {
        if (listener != null) {
            listener.onPrimitiveSelected(this, id);
        }
    }

    protected void fireOnLightSelected(Object id) {
        if (listener != null) {
            listener.onLightSelected(this, id);
        }
    }

    private void onOpen() {
        SceneChooser chooser = new SceneChooser();

        if (chooser.showOpenDialog(viewer) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

                Scene scene = (Scene) in.readObject();

                HashMap<Object, Primitive> primitives = scene.getPrimitives();
                Primitive primitive;

                for (Object id : primitives.keySet()) {
                    primitive = primitives.get(id);

                    if (
                        primitive.getClass().equals(Sphere.class) ||
                        primitive.getClass().equals(Rectangle.class) ||
                        primitive.getClass().equals(Triangle.class) ||
                        primitive.getClass().equals(RectangularPrism.class)
                    ) {
                        if (fireOnPrimitiveAdded(primitive)) {
                            PrimitiveType type;

                            if (primitive.getClass().equals(Sphere.class)) {
                                type = new PrimitiveType(Sphere.class, "Sphere");
                            } else if (primitive.getClass().equals(Rectangle.class)) {
                                type = new PrimitiveType(Rectangle.class, "Rectangle");
                            } else if (primitive.getClass().equals(Triangle.class)) {
                                type = new PrimitiveType(Triangle.class, "Triangle");
                            } else if (primitive.getClass().equals(RectangularPrism.class)) {
                                type = new PrimitiveType(RectangularPrism.class, "Rectangular Prism");
                            }else
                            {
                                throw new Exception("Unknown PrimitiveType");
                            }

                            treeModel.addPrimitiveNode(new PrimitiveNode(id, type));
                        } else {
                            JOptionPane.showMessageDialog(viewer, "Unable to add the Primitive '"
                                    + id + "'", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }

                HashMap<Object, Light> lights = scene.getLights();
                Light light;

                for (Object id : lights.keySet()) {
                    light = lights.get(id);

                    if (fireOnLightAdded(light)) {
                        LightType type;

                        if (light.getClass().equals(PointLight.class)) {
                            type = new LightType(PointLight.class, "Point Light");
                        } else
                        {
                            throw new Exception("Unknown LightType");
                        }

                        treeModel.addLightNode(new LightNode(id));
                    } else {
                        JOptionPane.showMessageDialog(viewer, "Unable to add the Light '"
                                + id + "'", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(chooser, "Unlucky!");
            }
        }
    }

    private void onSave() {
        SceneChooser chooser = new SceneChooser();

        if (chooser.showSaveDialog(viewer) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if(!file.getPath().toLowerCase().endsWith(".scn"))
            {
                file = new File(file.getPath() + ".scn");
            }

            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

                Scene scene = viewer.getManager().getScene();

                if(scene != null)
                {
                    out.writeObject(scene);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(chooser, "Unlucky...");
            }
        }
    }

    private void onAddPrimitive() {
        Wizard wizard = new Wizard(viewer, Wizard.SINGLE_PAGE);
        wizard.setSize(350, 230);
        wizard.setTitle("New Primitive");
        Utils.center(wizard.getDialog());

        PrimitivePage page = new PrimitivePage(treeModel.getIDsFor(treeModel.
                getPrimitivesRoot()));
        wizard.addPage(page);

        if (wizard.show() == Wizard.FINISH_CODE) {
            if (fireOnPrimitiveAdded(page.getPrimitiveName(), page.
                    getPrimitiveType())) {
                treeModel.addPrimitiveNode(new PrimitiveNode(page.
                        getPrimitiveName(), page.getPrimitiveType()));
            } else {
                JOptionPane.showMessageDialog(viewer, "Unable to create the Primitive '"
                        + page.getPrimitiveName() + "'", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void onAddLight() {
        Wizard wizard = new Wizard(viewer, Wizard.SINGLE_PAGE);
        wizard.setSize(350, 230);
        wizard.setTitle("New Light");
        Utils.center(wizard.getDialog());

        LightPage page = new LightPage(treeModel.getIDsFor(treeModel.
                getLightsRoot()));
        wizard.addPage(page);

        if (wizard.show() == Wizard.FINISH_CODE) {
            if (fireOnLightAdded(page.getLightName(), page.getLightType())) {
                treeModel.addLightNode(new LightNode(page.getLightName()));
            } else {
                JOptionPane.showMessageDialog(viewer, "Unable to create the Light '"
                        + page.getLightName() + "'", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void onRemove(DefaultMutableTreeNode node) {
        if (node instanceof PrimitiveNode) {
            fireOnPrimitiveRemoved(node.getUserObject());
            treeModel.removePrimitiveNode((PrimitiveNode) node);
            if (transformPanel.getSelectedPrimitive().equals(node.getUserObject())) {
                transformPanel.setSelectedPrimitive(null);
                transformPanel.setEnabled(false);
            }
        } else if (node instanceof LightNode) {
            fireOnLightRemoved(node.getUserObject());
            treeModel.removeLightNode((LightNode) node);
        }
    }

    private void onSelected(DefaultMutableTreeNode node) {
        if (node instanceof PrimitiveNode) {
            transformPanel.setEnabled(true);
            transformPanel.setSelectedPrimitive(node.getUserObject());
            fireOnPrimitiveSelected(node.getUserObject());
        } else if (node instanceof LightNode) {
            transformPanel.setEnabled(false);
            fireOnLightSelected(node.getUserObject());
        }
    }

    private void onProperties(DefaultMutableTreeNode node) {
        if (node instanceof PrimitiveNode) {
            Primitive primitive = listener.onPrimitiveProperties(this, node.
                    getUserObject());

            if (primitive != null) {
                if (primitive.getClass().equals(Sphere.class)) {
                    SpherePropertiesDialog dialog = new SpherePropertiesDialog(viewer, (Sphere) primitive);
                    if (dialog.showDialog() == CustomDialog.APPROVE_OPTION) {
                        dialog.extractTo(primitive);
                        listener.onPrimitivePropertiesUpdated(this, primitive.
                                getId());
                    }
                } else if (primitive.getClass().equals(Rectangle.class)) {
                    PrimitivePropertiesDialog dialog = new PrimitivePropertiesDialog(viewer, primitive) {

                        @Override
                        public JPanel createSpecificMessage() {
                            return new JPanel();
                        }
                    };
                    if (dialog.showDialog() == CustomDialog.APPROVE_OPTION) {
                        dialog.extractTo(primitive);
                        listener.onPrimitivePropertiesUpdated(this, primitive.
                                getId());
                    }
                } else if (primitive.getClass().equals(Triangle.class)) {
                    TrianglePropertiesDialog dialog = new TrianglePropertiesDialog(viewer, (Triangle) primitive);

                    if (dialog.showDialog() == CustomDialog.APPROVE_OPTION) {
                        dialog.extractTo(primitive);
                        listener.onPrimitivePropertiesUpdated(this, primitive.
                                getId());
                    }
                } else if (primitive.getClass().equals(RectangularPrism.class)) {
                    PrismPropertiesDialog dialog = new PrismPropertiesDialog(viewer, (RectangularPrism) primitive);

                    if (dialog.showDialog() == CustomDialog.APPROVE_OPTION) {
                        dialog.extractTo(primitive);
                        listener.onPrimitivePropertiesUpdated(this, primitive.
                                getId());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(viewer, "Unable to read the properties.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (node instanceof LightNode) {
            Light light = listener.onLightProperties(this, node.getUserObject());

            if (light != null) {
                if (light.getClass().equals(PointLight.class)) {
                    LightPropertiesDialog dialog = new LightPropertiesDialog(viewer, light);
                    if (dialog.showDialog() == CustomDialog.APPROVE_OPTION) {
                        dialog.extractTo(light);
                        listener.onLightPropertiesUpdated(this, light.getId());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(viewer, "Unable to read the properties.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void createUI() {
        setBorder(BorderFactory.createTitledBorder("Scene"));
        setName("Form");
        setLayout(new BorderLayout());

        transformPanel = new TransformPanel();
        propertiesPanel = new JPanel();
        treePanel = new JPanel();
        sceneButtonsPanel = new JPanel(new GridBagLayout());
        openButton = new JButton();
        saveButton = new JButton();
        addPrimitiveButton = new JButton();
        addLightButton = new JButton();
        removeButton = new JButton();
        treeScroll = new JScrollPane();
        sceneTree = new JTree(treeModel);
        GridBagConstraints gbc = new GridBagConstraints();

        sceneTree.addMouseListener(controller);
        sceneTree.addTreeSelectionListener(controller);

        propertiesPanel.setBorder(BorderFactory.createTitledBorder("Transforms"));
        propertiesPanel.setName("propertiesPanel");
        propertiesPanel.setLayout(new BorderLayout());

        JScrollPane tsp = new JScrollPane();
        tsp.setViewportView(transformPanel);
        propertiesPanel.add(tsp);

        add(propertiesPanel, BorderLayout.CENTER);

        //treePanel.setBorder(BorderFactory.createTitledBorder("Primitives"));
        treePanel.setName("primitivesPanel");
        treePanel.setLayout(new GridBagLayout());

        sceneButtonsPanel.setName("listButtonsPanel");
        //sceneButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        openButton.setText("Open");
        openButton.setName("openButton");
        openButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 2, 2, 2);
        sceneButtonsPanel.add(openButton, gbc);

        saveButton.setText("Save As...");
        saveButton.setName("saveButton");
        saveButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 2, 2, 2);
        sceneButtonsPanel.add(saveButton, gbc);

        addPrimitiveButton.setText("Add Primitive");
        addPrimitiveButton.setName("addPrimitiveButton");
        addPrimitiveButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 5, 2, 5);
        sceneButtonsPanel.add(addPrimitiveButton, gbc);

        addLightButton.setText("Add Light");
        addLightButton.setName("addLightButton");
        addLightButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 5, 2, 5);
        sceneButtonsPanel.add(addLightButton, gbc);

        removeButton.setText("Remove");
        removeButton.setName("removeButton");
        removeButton.addActionListener(controller);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 5, 2, 5);
        sceneButtonsPanel.add(removeButton, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 5, 2, 5);
        treePanel.add(sceneButtonsPanel, gbc);

        sceneTree.setCellRenderer(new SceneTreeRenderer());
        sceneTree.setRootVisible(false);
        sceneTree.setVisibleRowCount(10);
        sceneTree.setName("sceneTree");
        sceneTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeScroll.setName("primitivesScroll");
        treeScroll.setViewportView(sceneTree);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0f;
        gbc.weighty = 1.0f;
        gbc.insets = new Insets(2, 5, 2, 5);
        treePanel.add(treeScroll, gbc);

        add(treePanel, BorderLayout.PAGE_START);
    }

    private class Controller implements ActionListener, MouseListener, TreeSelectionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource().equals(openButton)) {
                onOpen();
            } else if (ae.getSource().equals(saveButton)) {
                onSave();
            } else if (ae.getSource().equals(addPrimitiveButton)) {
                onAddPrimitive();
            } else if (ae.getSource().equals(addLightButton)) {
                onAddLight();
            } else if (ae.getSource().equals(removeButton)) {
                Object o = sceneTree.getSelectionPath().getLastPathComponent();

                if ((o != null) && (o instanceof DefaultMutableTreeNode)) {
                    onRemove((DefaultMutableTreeNode) o);
                }
            }
        }

        public void mouseClicked(MouseEvent me) {
            if (me.getSource().equals(sceneTree)) {
                if (me.getClickCount() == 2) {
                    TreePath path = sceneTree.getPathForLocation(me.getX(), me.
                            getY());

                    if (path != null) {
                        Object o = path.getLastPathComponent();

                        if ((o != null) && (o instanceof DefaultMutableTreeNode)) {
                            onProperties((DefaultMutableTreeNode) o);
                        }
                    }
                }
            }
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
        }

        public void mousePressed(MouseEvent me) {
        }

        public void mouseReleased(MouseEvent me) {
        }

        public void valueChanged(TreeSelectionEvent tse) {
            Object o = tse.getPath().getLastPathComponent();

            if ((o != null) && (o instanceof DefaultMutableTreeNode)) {
                onSelected((DefaultMutableTreeNode) o);
            }
        }
    }
}
