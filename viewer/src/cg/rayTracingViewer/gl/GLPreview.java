package cg.rayTracingViewer.gl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * @author Alexandre Vieira
 */
public class GLPreview implements Runnable {

    protected static final HashMap<Integer, GLPreview.Bool> KEYBOARD;
    protected static final HashMap<Integer, GLPreview.Bool> MOUSE;
    private Thread thread;
    private Grid grid;
    private GLScene scene;
    private FreeCamera camera;
    private long storedDelta;
    private long lastFrame;
    private long minimumLogicInterval;
    private long maximumLogicInterval;
    private int gLWidth;
    private int gLHeight;
    private boolean running;

    static {
        KEYBOARD = new HashMap<Integer, Bool>();
        GLPreview.addKey(Keyboard.KEY_W);
        GLPreview.addKey(Keyboard.KEY_D);
        GLPreview.addKey(Keyboard.KEY_S);
        GLPreview.addKey(Keyboard.KEY_A);
        GLPreview.addKey(Keyboard.KEY_LSHIFT);
        GLPreview.addKey(Keyboard.KEY_C);

        MOUSE = new HashMap<Integer, GLPreview.Bool>();
        MOUSE.put(0, new GLPreview.Bool());
    }

    public GLPreview() {
        this.thread = new Thread(this, "GLPreview");
        this.thread.setDaemon(true);
        this.storedDelta = 0L;
        this.lastFrame = 0L;
        this.minimumLogicInterval = 24L;
        this.maximumLogicInterval = 32L;
        this.running = false;
    }

    public void run() {
        try {
            initGL();
            getDelta();

            while (running) {
                updateAndRender(getDelta());
            }

            Display.destroy();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }
    }

    public FreeCamera getCamera() {
        return camera;
    }

    public GLScene getScene() {
        return scene;
    }

    public int getGLHeight() {
        return gLHeight;
    }

    public int getGLWidth() {
        return gLWidth;
    }

    /**
     * Starts the thread and initializes LWJGL, eventually.
     */
    public void create() {
        running = true;
        thread.start();
    }

    public void destroy() {
        running = false;
    }

    public static void addKey(int key) {
        KEYBOARD.put(key, new Bool());
    }

    /**
     * Initializes LWJGL related stuff.
     * @throws org.lwjgl.LWJGLException
     */
    private void initGL() throws LWJGLException {
        Display.setVSyncEnabled(true);
        Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(640, 480));
        Display.create();

        gLWidth = Display.getDisplayMode().getWidth();
        gLHeight = Display.getDisplayMode().getHeight();

        GL11.glViewport(0, 0, gLWidth, gLHeight);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.rewind();
        buffer.put(0.2f);
        buffer.put(0.2f);
        buffer.put(0.2f);
        buffer.put(1.0f);
        buffer.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, buffer);

        grid = new Grid("grid", 10.0f);
        scene = new GLScene();
        camera = new FreeCamera((float) gLWidth / (float) gLHeight);
        camera.refresh();
    }

    private void updateAndRender(long delta) throws LWJGLException {
        storedDelta += delta;
        pollInput();

        if (storedDelta >= minimumLogicInterval) {

            if (maximumLogicInterval != 0L) {
                long cycles = storedDelta / maximumLogicInterval;

                for (long index = 0L; index < cycles; index++) {
                    update(maximumLogicInterval);
                }

                update(delta % maximumLogicInterval);
            } else {
                update(storedDelta);
            }
            storedDelta = 0L;

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();
            render();
            Display.update();
        }
    }

    /**
     * Updates the context.
     * @param delta
     */
    private void update(long delta) {
        if (KEYBOARD.get(Keyboard.KEY_W).value) {
            camera.forward();
        }
        if (KEYBOARD.get(Keyboard.KEY_D).value) {
            camera.strafeRight();
        }
        if (KEYBOARD.get(Keyboard.KEY_S).value) {
            camera.backward();
        }
        if (KEYBOARD.get(Keyboard.KEY_A).value) {
            camera.strafeLeft();
        }
        if (KEYBOARD.get(Keyboard.KEY_LSHIFT).value) {
            camera.up();
        }
        if (KEYBOARD.get(Keyboard.KEY_C).value) {
            camera.down();
        }
        if (MOUSE.get(0).value) {
            if (!Mouse.isGrabbed()) {
                Mouse.setGrabbed(true);
            }
            camera.addToYaw(Mouse.getDX() * 0.2f);
            camera.addToPitch(Mouse.getDY() * 0.2f);
        } else {
            Mouse.setGrabbed(false);
        }

        scene.update(delta);
    }

    /**
     * Renders to the window.
     */
    private void render() {
        camera.refresh();

//        Set<String> lightsKeys = scene.getLights().keySet();
//        GLLight light;
//
//        synchronized (scene.getLights()) {
//            Iterator<String> it = lightsKeys.iterator();
//
//            it = lightsKeys.iterator();
//            while (it.hasNext()) {
//                light = (GLLight) scene.getLights().get(it.next());
//                light.orient(camera);
//            }
//        }

        grid.render();
        scene.render();
    }

    /**
     * Updates the keys.
     */
    private void pollInput() {
        Keyboard.poll();
        Mouse.poll();
        int key;

        while (Keyboard.next()) {
            key = Keyboard.getEventKey();

            if (KEYBOARD.containsKey(key)) {
                KEYBOARD.get(key).value = Keyboard.getEventKeyState();
            }
        }

        while (Mouse.next()) {
            key = Mouse.getEventButton();

            if (MOUSE.containsKey(key)) {
                MOUSE.get(key).value = Mouse.getEventButtonState();
            }
        }
    }

    /**
     * Returns the elapsed time since the last call.
     * @return
     */
    private long getDelta() {
        long time = Sys.getTime();
        long delta = time - lastFrame;
        lastFrame = time;

        return delta;
    }

    public static BufferedImage getImage(String reference) {
        BufferedImage image;

        try {
            image = ImageIO.read(GLLight.class.getResource(reference));
        } catch (Exception e) {
            e.printStackTrace();

            image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            g.dispose();
        }

        return image;
    }

    public static class Bool {

        public boolean value = false;
    }
}
