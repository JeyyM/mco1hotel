package mco2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI Panel for choosing a hotel to reserve.
 * Inherits the ShowHotels class as it has the same formatting.
 */
public class ReserveHotelSelectPanel extends ShowHotels {
    private JTextField nameEntry;

    // Size variables
    private int northHeight = 70;

    /**
     * Constructor for the reserve hotel panel. Uses the
     * constructor of the ShowHotels class and shows the
     * different hotels that the customer can reserve out
     * of the list of hotels created. Also adds a text
     * input for the customer to input their name.
     * @param hotels    list of the hotels created
     */
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
    
    /**
     * Overridden method from the ShowHotels class as clicking
     * a hotel button leads to the customer selecting a room
     * to reserve instead of managing the selected hotel.
     * @param hotelButton   button of the hotel selected to be reserved
     * @param hotel         hotel that is selected and to have its rooms shown
     */
    @Override
    public void addHotelButtonListener(JButton hotelButton, Hotel hotel) {
        hotelButton.addActionListener(e -> {
            if (nameEntry.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panelCenter, "Enter name before choosing reservation.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                controller.switchToReserveSpecificRoomPanel(hotel, nameEntry.getText());
            }
        });
    }
}
