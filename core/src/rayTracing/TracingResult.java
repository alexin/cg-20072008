package rayTracing;

import java.awt.image.BufferedImage;

/**
 * @author Alexandre Vieira
 */
public class TracingResult {

    public static final int FAILED = 0;
    public static final int SUCCEEDED = 1;
    private BufferedImage frame;
    private long renderingTime;
    private int status;

    public TracingResult(BufferedImage frame, long renderingTime, int status) {
        setStatus(status);

        if (status == SUCCEEDED) {
            this.frame = frame;
            this.renderingTime = renderingTime;
        }
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public long getRenderingTime() {
        return renderingTime;
    }

    public int getStatus() {
        return status;
    }

    private void setStatus(int status) {
        if (status != FAILED && status != SUCCEEDED) {
            throw new IllegalArgumentException("Invalid status.");
        }

        this.status = status;
    }
}
