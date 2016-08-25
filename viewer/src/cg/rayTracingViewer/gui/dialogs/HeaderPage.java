package cg.rayTracingViewer.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Alexandre Vieira
 */
public abstract class HeaderPage extends WizardPage {

    private Header header;
    private JComponent contentPane;

    public HeaderPage() {
    }

    public HeaderPage(String id, Icon icon, String title, String description) {
        this(id, new Header(icon, title, description), new JPanel());
    }

    public HeaderPage(String id, Header header, JComponent content) {
        super(id);
        this.header = header;
        this.contentPane = content;
    }

    @Override
    public JComponent getComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(header, BorderLayout.NORTH);
        panel.add(contentPane, BorderLayout.CENTER);
        return panel;
    }

    public abstract String getNextPageID();

    public abstract String getPreviousPageID();

    @Override
    public void onHide() {
    }

    @Override
    public void onShow() {
    }
    
    public void setHeader(Header header) {
        this.header = header;
    }

    public void setContentPane(JComponent contentPane) {
        this.contentPane = contentPane;
    }

    public Header getHeader() {
        return header;
    }

    public JComponent getContentPane() {
        return contentPane;
    }

    public static class Header extends JPanel {

        private static final int HEADER_HEIGHT = 72;
        private Icon icon;
        private String title;
        private String description;

        public Header(Icon icon, String title, String description) {
            this.icon = icon;
            this.title = title;
            this.description = description;
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(300, HEADER_HEIGHT));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, HEADER_HEIGHT));
            setMinimumSize(new Dimension(0, HEADER_HEIGHT));
            createUI();
            setBackground(Color.WHITE);
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Icon getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        private void createUI() {
            JLabel iconLabel = new JLabel(icon, SwingConstants.LEFT);
            JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
            JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
            GridBagConstraints gbc = new GridBagConstraints();

            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 16));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 2;
            add(iconLabel, gbc);
            gbc.weightx = 1.0f;
            gbc.weighty = 1.0f;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            add(titleLabel, gbc);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.gridheight = 1;
            add(descriptionLabel, gbc);
        }
    }
}
