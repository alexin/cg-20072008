package cg.rayTracingViewer.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Alexandre Vieira
 */
public class FrameCanvas extends Canvas {

    private BufferedImage frame;

    public FrameCanvas() {
        super();

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    void postTranslation( Graphics2D g) {
        if (frame != null) {
            int x = (getWidth() - frame.getWidth()) / 2;
            int y = (getHeight() - frame.getHeight()) / 2;
            g.drawImage(frame, x, y, null);
        }
    }

    public void setFrame(BufferedImage frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
    }

    public BufferedImage getFrame() {
        return frame;
    }
}
