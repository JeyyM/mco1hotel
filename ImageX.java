package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ImageX extends ImageBackgroundPanel {
    private JButton closeButton;

    public ImageX(ImageIcon image, ActionListener closeAction) {
        super(image);
        this.setLayout(null);

        closeButton = new JButton("X");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.addActionListener(closeAction);

        this.add(closeButton);

        // Adjust the button position whenever the component is resized
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustButtonPosition();
            }
        });

        // Initial adjustment
        adjustButtonPosition();
    }

    private void adjustButtonPosition() {
        int buttonWidth = closeButton.getPreferredSize().width;
        int buttonHeight = closeButton.getPreferredSize().height;
        int x = this.getWidth() - buttonWidth - 5;
        int y = 5;
        closeButton.setBounds(x, y, buttonWidth, buttonHeight);
    }
}
