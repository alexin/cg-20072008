package rayTracing;

import rayTracing.math.Point3d;
import rayTracing.math.Vector3d;
import rayTracing.scene.Scene;
import rayTracing.scene.Sphere;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import rayTracing.math.Color3d;
import rayTracing.math.Matrix;
import rayTracing.scene.Light;
import rayTracing.scene.PointLight;
import rayTracing.scene.Rectangle;
import rayTracing.scene.RectangularPrism;
import rayTracing.scene.Triangle;

/**
 * @author Alexandre Vieira
 */
public class RayTracing {

    /** The <code>RayTracer</code> to process the <code>Scene</code>. */
    private RayTracer tracer;
    /** The scene to ray trace. */
    private Scene scene;
    /** The <code>Camera</code> viewing the scene.*/
    private Camera camera;

    /**
     * Creates a <code>RayTracing</code>.
     */
    public RayTracing() {
        Settings settings = new Settings(3, true, true, false, false, false, false);
        this.tracer = new RayTracer(settings);
        this.buildScene();
        this.camera = createDefaultCamera();
        this.camera.configure();

        TracingResult result = this.tracer.trace(scene, camera);

        if (result.getStatus() == TracingResult.SUCCEEDED) {
            try {
                saveFrameAsPNG(result.getFrame(), new File("H:/CG/Projecto/RayTracing/res/frame-test.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates and builds the scene.
     */
    public void buildScene() {
        scene = new Scene(128.0d);

        Light l1 = new PointLight("light-1", 0.1d);
        l1.setLocation(3.0d, 5.0d, 5.0d);
        l1.setColor(1.0d, 1.0d, 1.0d);

        Light l2 = new PointLight("light-2", 0.1d);
        l2.setLocation(3.0d, 9.0d, 6.0d);
        l2.setColor(1.0d, 1.0d, 1.0d);

        Light l3 = new PointLight("light-3", 0.1d);
        l3.setLocation(-3.0d, 5.0d, 5.0d);
        l3.setColor(1.0d, 1.0d, 1.0d);

        Sphere s1 = new Sphere("sphere-white", 1.0d);
        s1.setLocation(new Point3d(0.0d, 1.0d, 4.0d));
        s1.getMaterial().setColor(new Color3d(1.0d, 1.0d, 1.0d));
        s1.getMaterial().setDiffuse(1.0d);
        s1.getMaterial().setSpecular(1.0d);
        s1.getMaterial().setReflection(0.0d);
        s1.getMaterial().setRefraction(1.0d);
        s1.getMaterial().setRefractionIndex(0.1d);

        Sphere s2 = new Sphere("sphere-green", 0.7d);
        s2.setLocation(new Point3d(1.0d, 1.0d, 6.0d));
        s2.getMaterial().setColor(new Color3d(0d, 1d, 0d));
        s2.getMaterial().setDiffuse(1.0d);
        s2.getMaterial().setSpecular(0.0d);
        s2.getMaterial().setReflection(0.0d);
        s2.getMaterial().setRefraction(0.0d);
        s2.getMaterial().setRefractionIndex(1.33d);

        Rectangle r1 = new Rectangle("rectangle-1", 2.0d, 2.0d);
        //r1.setLocation(0.0d, 0.0d, 6.0d);
        r1.getMaterial().setColor(new Color3d(0.7d, 0.3d, 0.3d));
        r1.getMaterial().setDiffuse(1.0d);
        r1.getMaterial().setSpecular(1.0d);
        r1.getMaterial().setReflection(1.0d);
        r1.getMaterial().setRefraction(1.0d);
        r1.getMaterial().setRefractionIndex(0.1d);
        
        Triangle t1 = new Triangle("triangle-1",
                new Point3d(0.0d, 0.0d, 0.0d), new Point3d(2.0d, 0.0d, 0.0d), new Point3d(0.0d, 2.0d, 0.0d));
        //r1.setLocation(0.0d, 0.0d, 6.0d);
        t1.getMaterial().setColor(new Color3d(0.7d, 0.3d, 0.3d));
        t1.getMaterial().setDiffuse(1.0d);
        t1.getMaterial().setSpecular(1.0d);
        t1.getMaterial().setReflection(1.0d);
        t1.getMaterial().setRefraction(1.0d);
        t1.getMaterial().setRefractionIndex(0.1d);
        
        RectangularPrism p1 = new RectangularPrism("cube", 1.0d, 1.0d, 1.0d);
        p1.getMaterial().setColor(new Color3d(0.7d, 0.3d, 0.3d));
        p1.getMaterial().setDiffuse(1.0d);
        p1.getMaterial().setSpecular(1.0d);
        p1.getMaterial().setReflection(1.0d);
        p1.getMaterial().setRefraction(1.0d);
        p1.getMaterial().setRefractionIndex(0.1d);

        Matrix t = new Matrix();

        //t.scale(2.0d, 2.5d, 1.0d);
        //t.rotateY(-90.0d);
        //t.translate(0.0d, 0.0d, 2.0d);
        //r1.transform(t);

        System.out.printf("s1: %b\n", scene.addPrimitive(p1));
        //System.out.printf("s2: %b\n", scene.addPrimitive(r1));
        //System.out.printf("p2: %b\n", scene.addPrimitive(t1));

        //System.out.printf("l1: %b\n", scene.addLight(l1));
        System.out.printf("l2: %b\n", scene.addLight(l2));
    //System.out.printf("l3: %b\n", scene.addLight(l3));
    }

    /**
     * Creates and returns a <code>Camera</code> located at the origin of the world 
     * and looking down the z-axis.
     * Its resolution is 256x256 and the Field Of View is of 90º.
     * @return A camera as default.
     */
    private Camera createDefaultCamera() {
        Vector3d direction = new Vector3d(0.0d, 0.0d, -1.0d);
        Vector3d up = new Vector3d(0.0d, 1.0d, 0.0d);
        Point3d location = new Point3d(1.0d, 1.0d, 7.0d);
        int width = 512;
        int height = 512;

        return new Camera(width, height, 90.0d, 1.0d, direction, up, location);
    }

    /**
     * Writes a <code>BufferedImage</code> as PNG.
     * @param frame The image to save.
     * @param out The reference of the image.
     * @throws java.io.IOException If an error occurs during writing.
     */
    private void saveFrameAsPNG(BufferedImage frame, File out) throws IOException {
        saveFrame(frame, "PNG", out);
    }

    /**
     * Writes a <code>BufferedImage</code> as JPG.
     * @param frame The image to save.
     * @param out The reference of the image.
     * @throws java.io.IOException If an error occurs during writing.
     */
    private void saveFrameAsJPG(BufferedImage frame, File out) throws IOException {
        saveFrame(frame, "JPG", out);
    }

    /**
     * Writes a <code>BufferedImage</code> as the specified format.
     * @param frame The image to save.
     * @param format The format of the image.
     * @param out The reference of the image.
     * @throws java.io.IOException If an error occurs during writing.
     */
    private void saveFrame(BufferedImage frame, String format, File out) throws IOException {
        if (!ImageIO.write(frame, format, out)) {
            throw new IllegalArgumentException("The specified format is invalid.");
        }
    }

    public static void main(String[] args) {

        RayTracing rayTracing = new RayTracing();
    }
}
