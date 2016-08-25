package cg.rayTracingViewer.gui.dialogs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author Alexandre Vieira
 */
public class SceneChooser extends JFileChooser {

    private static final long serialVersionUID = 1L;
    private static final String[] EXTENSIONS;

    static {
        EXTENSIONS = new String[]{"scn"};
    }

    public SceneChooser() {
        super();

        setFileFilter(new SceneFileFilter(EXTENSIONS, "Supported Formats (SCN)"));
        setMultiSelectionEnabled(false);
    }

    public boolean isSupported(File file) {
        for (String extension : EXTENSIONS) {
            if (file.getName().endsWith("." + extension)) {
                return true;
            }
        }

        return false;
    }

    private static class SceneFileFilter extends FileFilter {

        private String[] extensions;
        private String description;

        public SceneFileFilter(final String[] extensions, final String description) {
            this.extensions = extensions;
            this.description = description;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            for (String extension : extensions) {
                if (f.getName().endsWith("." + extension)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public String[] getExtensions() {
            return extensions;
        }
    }
}
