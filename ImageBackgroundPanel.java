package mco1;

import javax.swing.*;
import java.awt.*;

// Class to be extended for image backgrounds
public class ImageBackgroundPanel extends JPanel {
    private ImageIcon image;

    public ImageBackgroundPanel(ImageIcon image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getIconWidth(), image.getIconHeight());
    }
}
