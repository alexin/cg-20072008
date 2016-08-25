package cg.rayTracingViewer.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author Alexandre Vieira
 */
public class ColorButton extends JButton {

    public static final HashMap<RenderingHints.Key, Object> HINTS;
    private static final int ICON_SIZE = 16;
    private Color color;

    static {

        HINTS = new HashMap<RenderingHints.Key, Object>(6);

        HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        HINTS.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        HINTS.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        HINTS.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public ColorButton() {
        this(Color.WHITE);
    }

    public ColorButton(String text) {
        this(text, Color.WHITE);
    }

    public ColorButton(Color color) {
        this(null, color);
    }

    public ColorButton(String text, Color color) {
        super(text, createColorIcon(color));
        setColor(color);
    }

    public void setColor(Color color) {
        this.color = color;
        setIcon(createColorIcon(color));
        float[] rgb = color.getColorComponents(null);
        setToolTipText(String.format("<%f, %f, %f>", rgb[0], rgb[1], rgb[2]));
    }

    public Color getColor() {
        return color;
    }

    private static Icon createColorIcon(Color color) {
        BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        //g.setRenderingHints(HINTS);

        g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(color);
        g.fillRoundRect(0, 0, image.getWidth() - 1, image.getHeight() - 1, 3, 3);
        g.setColor(Color.DARK_GRAY);
        //g.setStroke(new BasicStroke(2.0f));
        g.drawRoundRect(0, 0, image.getWidth() - 1, image.getHeight() - 1, 3, 3);

        return new ImageIcon(image);
    }
}
