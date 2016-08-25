package cg.rayTracingViewer.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.NumberFormatter;

/**
 * @author Alexandre Vieira
 */
public class Utils {

    private Utils() {
    }

    public static Icon createIcon(String reference) {
        return new ImageIcon(Utils.class.getResource(reference));
    }

    public static void saveImage(BufferedImage image) throws IOException {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "PNG";
            }
        });

        if (jfc.showSaveDialog(jfc) == JFileChooser.APPROVE_OPTION) {
            File out = jfc.getSelectedFile();

            if (!out.getPath().endsWith(".png") || !out.getPath().endsWith(".PNG")) {
                out = new File(out.getPath() + ".png");
            }

            saveImage(image, "PNG", jfc.getSelectedFile());
        }
    }

    private static void saveImage(BufferedImage frame, String format, File out) throws IOException {
        if (!ImageIO.write(frame, format, out)) {
            throw new IllegalArgumentException("The specified format is invalid.");
        }
    }

    public static void center(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = window.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        window.setLocation(x, y);
    }

    public static NumberFormatter createDoubleFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#0.0##", symbols);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);

        return formatter;
    }

    public static NumberFormatter createIntegerFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#0.0##", symbols);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);

        return formatter;
    }

    public static InputVerifier createDoubleVerifier() {
        InputVerifier verifier = new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                if ((input instanceof JFormattedTextField)) {
                    boolean valid = Pattern.matches("-?\\d+(\\.\\d+)?", ((JFormattedTextField) input).getText());

                    if (!valid) {
                        ((JFormattedTextField) input).selectAll();
                        input.requestFocusInWindow();
                    }

                    return valid;
                } else {
                    return false;
                }
            }
        };

        return verifier;
    }

    public static InputVerifier createNZDoubleVerifier() {
        InputVerifier verifier = new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                if ((input instanceof JFormattedTextField)) {
                    boolean valid = Pattern.matches("-?\\d+(\\.\\d+)?", ((JFormattedTextField) input).getText()) &&
                            (Double) ((JFormattedTextField) input).getValue() != 0.0d;

                    if (!valid) {
                        ((JFormattedTextField) input).selectAll();
                        input.requestFocusInWindow();
                    }

                    return valid;
                } else {
                    return false;
                }
            }
        };

        return verifier;
    }

    public static InputVerifier createIntegerVerifier() {
        InputVerifier verifier = new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                if ((input instanceof JFormattedTextField)) {
                    boolean valid = Pattern.matches("-?\\d+", ((JFormattedTextField) input).getText());

                    if (!valid) {
                        ((JFormattedTextField) input).selectAll();
                        input.requestFocusInWindow();
                    }

                    return valid;
                } else {
                    return false;
                }
            }
        };

        return verifier;
    }
}
