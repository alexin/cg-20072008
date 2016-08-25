package cg.rayTracingViewer.gui;

import cg.rayTracingViewer.CameraInfo;
import cg.rayTracingViewer.Manager;
import cg.rayTracingViewer.ManagerListener;
import cg.rayTracingViewer.ResetEvent;
import cg.rayTracingViewer.gl.GLCamera;
import cg.rayTracingViewer.gl.GLLight;
import cg.rayTracingViewer.gl.GLMaterial;
import cg.rayTracingViewer.gl.GLPreview;
import cg.rayTracingViewer.gl.GLScene;
import cg.rayTracingViewer.gl.GLSphere;
import cg.rayTracingViewer.gl.GLObject;
import cg.rayTracingViewer.gl.GLPolygon;
import cg.rayTracingViewer.gl.GLSolid;
import cg.rayTracingViewer.gui.events.ControlsPanelListener;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import rayTracing.Settings;
import rayTracing.TracingResult;
import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;
import rayTracing.scene.Light;
import rayTracing.scene.PointLight;
import rayTracing.scene.Primitive;
import rayTracing.scene.Rectangle;
import rayTracing.scene.RectangularPrism;
import rayTracing.scene.Sphere;
import rayTracing.scene.Triangle;

/**
 * @author Alexandre Vieira
 */
public class RayTracingViewer extends JFrame implements ManagerListener, ControlsPanelListener {

    private FramePanel framePanel;
    private CameraPanel cameraPanel;
    private ControlsPanel controlsPanel;
    private JTabbedPane tabPane;
    private JPanel eastPanel;
    private JPanel mainPanel;
    private ScenePanel scenePanel;
    private StatusPanel statusPanel;
    private JPanel westPanel;
    private Manager manager;
    private GLPreview gLPreview;
    private BufferedImage image;

    public RayTracingViewer() throws LWJGLException {
        super("Ray Tracing Viewer");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setLookAndFeel(WindowsLookAndFeel.class.getName(), this);
        setSize(1000, 800);

        createUI();
        Utils.center(this);
        scenePanel.setEnabled(false);
        cameraPanel.setEnabled(false);

        manager = new Manager();
        manager.addManagerListener(this);
        scenePanel.setScenePanelListener(manager);
        scenePanel.getTransformPanel().setTransformPanelListener(manager);
        cameraPanel.setCameraPanelListener(manager);
        controlsPanel.addControlsPanelListener(this);
        controlsPanel.addControlsPanelListener(manager);
        onNew();
    }

    public void onTracingFinished(TracingResult result) {
        scenePanel.setEnabled(true);
        cameraPanel.setEnabled(true);
        controlsPanel.setEnabled(true);
        statusPanel.stopProgress();
        statusPanel.setMessage("Tracing finished (" + result.getRenderingTime() + " ms)");

        image = result.getFrame();
        framePanel.getFrameCanvas().setFrame(result.getFrame());
        tabPane.setSelectedComponent(framePanel);
        framePanel.getFrameCanvas().repaint();
    }

    public void onTracingStarted() {
        scenePanel.setEnabled(false);
        cameraPanel.setEnabled(false);
        controlsPanel.setEnabled(false);
        statusPanel.setProgress(0);
        statusPanel.startProgress();
        statusPanel.setMessage("Tracing started.");
    }

    public void onTracingStopped() {
        scenePanel.setEnabled(true);
        cameraPanel.setEnabled(true);
        controlsPanel.setEnabled(true);
        statusPanel.stopProgress();
        statusPanel.setMessage("Tracing stopped.");
        image = null;
    }

    public void onFrameUpdate(float status) {
        statusPanel.setProgress((int) (status * 100.0f));
    }

    public void onReset(ResetEvent re) {
        Vector3d direction = re.getCameraDirection();
        Vector3d up = re.getCameraUp();
        Point3d location = re.getCameraLocation();

        cameraPanel.setNotifyListener(false);
        cameraPanel.updateFOVSlider(re.getFOV());
        cameraPanel.updateDistanceSlider(re.getDistance());
        cameraPanel.updateResolutionBox(re.getResolution());
        cameraPanel.updateDirectionField(direction.x, direction.y, direction.z);
        cameraPanel.updateUpField(up.x, up.y, up.z);
        cameraPanel.updateLocationField(location.x, location.y, location.z);
        cameraPanel.setNotifyListener(true);

        gLPreview = new GLPreview();
        gLPreview.create();

        onCameraInfoChanged(re.getCameraInfo());
    }

    public void onPrimitiveAdded(Primitive primitive) {
        if (primitive instanceof Sphere) {
            GLScene gLScene = gLPreview.getScene();
            synchronized (gLScene) {
                GLSphere sphere = new GLSphere(primitive.getId().toString());
                sphere.setLocation(new Point3d(primitive.getLocation()));
                sphere.setRadius((float) ((Sphere) primitive).getRadius());
                sphere.setMaterial(new GLMaterial(primitive.getMaterial().getColor()));
                sphere.setValid(false);
                gLScene.addPrimitive(sphere);
            }
        } else if (primitive instanceof Rectangle) {
            GLScene gLScene = gLPreview.getScene();

            synchronized (gLScene) {
                GLPolygon rectangle = new GLPolygon(primitive.getId().toString());
                rectangle.setLocation(new Point3d(primitive.getLocation()));
                rectangle.setMaterial(new GLMaterial(primitive.getMaterial().getColor()));
                rectangle.setVertices(((Rectangle) primitive).getVertices());
                rectangle.setNormal(((Rectangle) primitive).getNormal());
                rectangle.setValid(false);
                gLScene.addPrimitive(rectangle);
            }
        } else if (primitive instanceof Triangle) {
            GLScene gLScene = gLPreview.getScene();

            synchronized (gLScene) {
                GLPolygon triangle = new GLPolygon(primitive.getId().toString());
                triangle.setLocation(new Point3d(primitive.getLocation()));
                triangle.setMaterial(new GLMaterial(primitive.getMaterial().getColor()));
                triangle.setVertices(((Triangle) primitive).getVertices());
                triangle.setNormal(((Triangle) primitive).getNormal());
                triangle.setValid(false);
                gLScene.addPrimitive(triangle);
            }
        } else if (primitive instanceof RectangularPrism) {
            GLScene gLScene = gLPreview.getScene();

            synchronized (gLScene) {
                GLSolid prism = new GLSolid(primitive.getId().toString());
                prism.setFaces(((RectangularPrism) primitive).getFaces());
                prism.setValid(false);
                gLScene.addPrimitive(prism);
            }
        }
    }

    public void onLightAdded(Light light) {
        if (light instanceof PointLight) {
            GLScene gLScene = gLPreview.getScene();
            synchronized (gLScene) {
                GLLight gLLight = new GLLight(light.getId().toString());
                gLLight.setLocation(new Point3d(light.getLocation()));
                gLLight.setColor(light.getColor());
                gLLight.setGLId(GL11.GL_LIGHT0);
                gLLight.setValid(false);
                gLScene.addLight(gLLight);
            }
        }
    }

    public void onPrimitiveRemoved(Object id) {
        gLPreview.getScene().removePrimitive(id.toString());
    }

    public void onLightRemoved(Object id) {
        gLPreview.getScene().removeLight(id.toString());
    }

    public void onPrimitiveChanged(Primitive primitive) {
        GLScene gLScene = gLPreview.getScene();
        GLObject object = gLScene.getPrimitive(primitive.getId().toString());

        synchronized (object) {
            object.setLocation(new Point3d(primitive.getLocation()));
            object.getMaterial().setColor(primitive.getMaterial().getColor());

            if (primitive instanceof Sphere) {
                GLSphere sphere = (GLSphere) object;
                sphere.setRadius((float) ((Sphere) primitive).getRadius());
            } else if (primitive instanceof Rectangle) {
                GLPolygon rectangle = (GLPolygon) object;
                rectangle.setVertices(((Rectangle) primitive).getVertices());
                rectangle.setNormal(((Rectangle) primitive).getNormal());
            } else if (primitive instanceof Triangle) {
                GLPolygon triangle = (GLPolygon) object;
                triangle.setLocation(((Triangle) primitive).getLocation());
                triangle.setVertices(((Triangle) primitive).getVertices());
                triangle.setNormal(((Triangle) primitive).getNormal());
            } else if (primitive instanceof RectangularPrism) {
                GLSolid prism = (GLSolid) object;
                prism.setLocation(((RectangularPrism) primitive).getLocation());
                prism.setFaces(((RectangularPrism) primitive).getFaces());
            }

            object.setValid(false);
        }
    }

    public void onLightChanged(Light light) {
        GLScene gLScene = gLPreview.getScene();
        GLObject object = gLScene.getLight(light.getId().toString());

        synchronized (object) {
            GLLight gLLight = (GLLight) object;
            gLLight.setLocation(new Point3d(light.getLocation()));
            gLLight.setColor(light.getColor());
            object.setValid(false);
        }
    }

    public void onCameraInfoChanged(CameraInfo info) {
        if (gLPreview != null && gLPreview.getScene() != null) {
            GLCamera camera = gLPreview.getScene().getCamera();

            synchronized (camera) {
                if (camera == null) {

                    camera = new GLCamera("camera");
                    camera.setValid(false);
                }
                camera.setLocation(info.getLocation());
                camera.setLowerLeft(info.getLowerLeft());
                camera.setLowerRight(info.getLowerRight());
                camera.setUpperLeft(info.getUpperLeft());
                camera.setUpperRight(info.getUpperRight());
                camera.setValid(false);
            }
        }
    }

    public void onTrace(Settings settings) {

    }

    public void onStop() {

    }

    public void onSave() {
        try {
            Utils.saveImage(image);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Unable to save the image", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Manager getManager() {
        return manager;
    }

    public GLPreview getGLPreview() {
        return gLPreview;
    }

    public ScenePanel getScenePanel() {
        return scenePanel;
    }

    private void onNew() throws LWJGLException {
        manager.reset();
        scenePanel.setEnabled(true);
        cameraPanel.setEnabled(true);
        controlsPanel.setEnabled(true);
    }

    private void createUI() {
        mainPanel = new JPanel();
        westPanel = new JPanel();
        scenePanel = new ScenePanel(this);
        framePanel = new FramePanel();
        cameraPanel = new CameraPanel(this);
        eastPanel = new JPanel();
        tabPane = new JTabbedPane();
        controlsPanel = new ControlsPanel();
        statusPanel = new StatusPanel();

        mainPanel.setName("mainPanel");
        mainPanel.setLayout(new BorderLayout());

        westPanel.setMaximumSize(new Dimension(300, 32767));
        westPanel.setMinimumSize(new Dimension(300, 700));
        westPanel.setName("westPanel");
        westPanel.setPreferredSize(new Dimension(300, 700));
        westPanel.setLayout(new GridLayout(2, 0));

        scenePanel.setName("scenePanel");
        westPanel.add(scenePanel);

        cameraPanel.setMaximumSize(new Dimension(300, 310));
        cameraPanel.setMinimumSize(new Dimension(300, 310));
        cameraPanel.setName("cameraPanel");
        cameraPanel.setPreferredSize(new Dimension(300, 310));
        westPanel.add(cameraPanel);

        mainPanel.add(westPanel, BorderLayout.WEST);

        eastPanel.setName("eastPanel");
        eastPanel.setLayout(new BorderLayout());

        tabPane.setName("tabPane");
        //tabPane.addTab("Overview", overviewPanel);
        tabPane.addTab("Frame", framePanel);

        eastPanel.add(tabPane, BorderLayout.CENTER);

        controlsPanel.setName("controlsPanel");
        eastPanel.add(controlsPanel, BorderLayout.SOUTH);

        mainPanel.add(eastPanel, BorderLayout.CENTER);

        statusPanel.setName("statusPanel");

        setLayout(new BorderLayout());

        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public static void setLookAndFeel(String lnf, Component component) {
        try {
            UIManager.setLookAndFeel(lnf);
            SwingUtilities.updateComponentTreeUI(component);
        } catch (ClassNotFoundException cnfe) {
            Logger.getLogger(RayTracingViewer.class.getName()).log(Level.SEVERE, null, cnfe);
        } catch (InstantiationException ie) {
            Logger.getLogger(RayTracingViewer.class.getName()).log(Level.SEVERE, null, ie);
        } catch (IllegalAccessException iae) {
            Logger.getLogger(RayTracingViewer.class.getName()).log(Level.SEVERE, null, iae);
        } catch (UnsupportedLookAndFeelException ulafe) {
            Logger.getLogger(RayTracingViewer.class.getName()).log(Level.SEVERE, null, ulafe);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    RayTracingViewer viewer = new RayTracingViewer();
                    viewer.setVisible(true);
                } catch (LWJGLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
