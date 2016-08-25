package cg.rayTracingViewer.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * @author Alexandre Vieira
 */
public class FramePanel extends JPanel {
    
    private FrameCanvas frameCanvas;

    public FramePanel(){
        createUI();
    }

    public FrameCanvas getFrameCanvas() {
        return frameCanvas;
    }
    
    private void createUI(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        frameCanvas = new FrameCanvas();
        
        add(frameCanvas);
    }
}
