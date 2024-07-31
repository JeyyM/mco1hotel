package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI Panel that shows all the rooms with reservations
 * from a selected hotel. Leads to the deletion of a
 * reservation from a selected room. Inherits the ShowRooms
 * class as this panel also shows all the rooms of a hotel.
 */
public class ManageRemoveReserveRoomChoice extends ShowRooms {
    /**
     * Constructor for the panel and uses the constructor for
     * the ShowRooms class but changes the list of rooms to only
     * the rooms with reservations in it
     * @param hotel     the hotel that will have its reservations removed
     */
    public ManageRemoveReserveRoomChoice(Hotel hotel) {
        super(hotel, hotel.getName(), new JLabel("Select Room to Remove a Reservation", JLabel.CENTER));
        this.rooms = hotel.getAllReservedRooms();
        initializeRows();
    }

    /**
     * Overridden method to initialize the row of buttons of rooms
     * that have reservations in them but with the added text in the
     * buttons that shows the number of reservations per room.
     */
    @Override
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();

        int totalRooms = rooms.size();
        int cols = 5;
        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalRooms / cols);

        for (int i = 0; i < totalRooms; i++) {
            rooms.get(i).setIndex(i);
        }

        int roomIndex = 0;
        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (roomIndex < totalRooms) {
                    Room room = rooms.get(roomIndex);
                    JButton roomButton = new JButton("<html>" + room.getName() + "<br>" + room.getReservations().size() + " reservations " + "</html>");
                    roomButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    roomButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
                    roomButton.addActionListener(e -> {
                        controller.switchToRemoveReservations(room);
                    });
                    rowWrapper.add(roomButton);
                    roomIndex++;
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
    
    /**
     * Overridden method because this function leads to
     * a panel that shows the reservations of the room selected
     * instead of a reservation calendar
     * @param roomButton    button class that can be clicked
     * @param room          room that is selected to show calendar
     */
    @Override
    public void addRoomButtonListener(JButton roomButton, Room room) {
        roomButton.addActionListener(e -> {
            controller.switchToRemoveReservations(room);
        });
    }
}
