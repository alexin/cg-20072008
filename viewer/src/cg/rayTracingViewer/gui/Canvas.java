package cg.rayTracingViewer.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.JPanel;

/**
 * @author Alexandre Vieira
 */
public class Canvas extends JPanel {

    public static final HashMap<RenderingHints.Key, Object> HINTS;
    private Stack<AffineTransform> transforms;
    private Point translation;
    private Point mousePress;
    private Controller controller;
    private boolean dragging;

    static {
        HINTS = new HashMap<RenderingHints.Key, Object>(6);

        HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public Canvas() {
        this.transforms = new Stack<AffineTransform>();
        this.translation = new Point();
        this.mousePress = new Point();
        this.controller = new Controller();
        this.dragging = false;

        addMouseListener(controller);
        addMouseMotionListener(controller);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.addRenderingHints(HINTS);

        pushTransform(g);
        preTranslation(g);
        g.translate(translation.x, translation.y);
        postTranslation(g);
        popTransform(g);
    }

    void preTranslation(Graphics2D g) {
    }

    void postTranslation(Graphics2D g) {
    }

    void pushTransform(Graphics2D g) {
        transforms.push(g.getTransform());
    }

    void popTransform(Graphics2D g) {
        g.setTransform(transforms.pop());
    }

    class Controller implements MouseListener, MouseMotionListener {

        public void mouseClicked(MouseEvent me) {
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
            dragging = false;
        }

        public void mousePressed(MouseEvent me) {
            mousePress = me.getPoint();
            dragging = true;
        }

        public void mouseReleased(MouseEvent me) {
            dragging = false;
        }

        public void mouseDragged(MouseEvent me) {
            translation.x += me.getX() - mousePress.x;
            translation.y += me.getY() - mousePress.y;
            mousePress = me.getPoint();
            repaint();
        }

        public void mouseMoved(MouseEvent me) {
        }
    }
}
