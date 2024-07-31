package mco2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI Panel for showing all the rooms of a hotel
 * and shows the number of reservations per room.
 * When a room button is clicked, shows the calendar
 * for the reservations of the room.
 */
public class CheckAvailabilityByRoomPanel extends ShowRooms {
    /**
     * Constructor for the panel, inherits the ShowRooms class
     * @param hotel     hotel that will have its rooms shown
     */
    public CheckAvailabilityByRoomPanel(Hotel hotel) {
        super(hotel, hotel.getName(), new JLabel("Select Room to Check Availability", JLabel.CENTER));
    }
    
    /**
     * Overridden method because this function leads to
     * a viewing calendar instead of a reservation calendar
     * @param roomButton    button class that can be clicked
     * @param room          room that is selected to show calendar
     */
    @Override
    public void addRoomButtonListener(JButton roomButton, Room room) {
        roomButton.addActionListener(e -> {
            controller.switchToViewCalendarFromRoom(room);
        });
    }
    
    /**
     * Overridden method to change the text of the JButtons
     * to include the number of reservations each room has
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
                    JButton roomButton = new JButton("<html>" + room.getName() + "<br>" + room.getReservations().size() + " Reservations</html>");
                    roomButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    roomButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

                    float baseRate = room.getBaseRate();
                    if (baseRate == 1.0f) {
                        roomButton.setBackground(null);
                    } else if (baseRate == 1.20f) {
                        roomButton.setBackground(Color.decode("#fae105"));
                    } else if (baseRate == 1.35f) {
                        roomButton.setBackground(Color.decode("#00fff2"));
                    }

                    addRoomButtonListener(roomButton, room);
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
}
