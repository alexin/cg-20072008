package cg.rayTracingViewer.gui.events;

import java.util.EventListener;
import rayTracing.Settings;

/**
 * @author Alexansdre Vieira
 */
public interface ControlsPanelListener extends EventListener {

    void onTrace(Settings settings);

    void onStop();

    void onSave();
}
