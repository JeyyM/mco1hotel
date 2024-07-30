package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This is the panel for the selection of a hotel to reserve
 * */
public class ReserveHotelSelectPanel extends ShowHotels {
    private JTextField nameEntry;

    // Size variables
    private int northHeight = 70;

    public void addHotelButtonListener(JButton hotelButton, Hotel hotel) {
        hotelButton.addActionListener(e -> {
            if (nameEntry.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panelCenter, "Enter name before choosing reservation.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                controller.switchToReserveSpecificRoomPanel(hotel, nameEntry.getText());
            }
        });
    }

    public ReserveHotelSelectPanel(ArrayList<Hotel> hotels) {
        super(hotels, new JLabel("Select a Hotel to Reserve", JLabel.CENTER));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Create the input panel for name entry
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameEntry = new JTextField(20);
        inputPanel.add(new JLabel("Enter your name:"));
        inputPanel.add(nameEntry);
        super.panelNorth.add(inputPanel, BorderLayout.SOUTH);

        add(panelNorth, BorderLayout.NORTH);

        initializeRows();
    }
}
