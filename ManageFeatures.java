package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageFeatures extends ShowRooms {
    private JPanel modifyButtons;
    private JButton addFeatureButton = new JButton("Add Feature");
    private JButton setHotelImageButton = new JButton("Set Hotel Images");

    private int buttonWidth = 160;
    private int buttonHeight = 120;

    private ArrayList<Feature> features = new ArrayList<>();

    public void addFeatureEventListener(ActionListener listener){
        addFeatureButton.addActionListener(listener);
    }

    // have to override since there are many different functionalities
    @Override
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();
        if (features == null) {
            features = new ArrayList<>();
        }
        int totalFeatures = features.size();
        int cols = 3;

        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalFeatures / cols);

        for (int i = 0; i < totalFeatures; i++) {
            rooms.get(i).setIndex(i);
        }

        int featureIndex = 0;

        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));

            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (featureIndex < totalFeatures) {
                    Feature feature = features.get(featureIndex);
                    JButton featureButton = new JButton("<html>" + feature.getName() + "</html>");
                    featureButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    featureButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

                    rowWrapper.add(featureButton);
                    featureIndex++;
                } else {
                    // Put an empty label since the buttons will overgrow if not
                    rowWrapper.add(new JLabel());
                }
            }
            panelCenter.add(rowWrapper);
        }

        panelCenter.revalidate();
        panelCenter.repaint();
    }

    public ManageFeatures(Hotel hotel) {
        super(hotel, hotel.getName(), new JLabel("Manage Features", JLabel.CENTER));
        this.features = hotel.getFeatures();

        // Edit rooms button
        modifyButtons = new JPanel();
        modifyButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        modifyButtons.setBackground(Color.decode("#063970"));
        modifyButtons.add(addFeatureButton);
        modifyButtons.add(setHotelImageButton);
        panelNorth.add(modifyButtons, BorderLayout.EAST);

        add(panelNorth, BorderLayout.NORTH);

        initializeRows();
    }
}
