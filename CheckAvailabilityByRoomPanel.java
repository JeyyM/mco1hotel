package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CheckAvailabilityByRoomPanel extends ShowRooms {
    public CheckAvailabilityByRoomPanel(Hotel hotel) {
        super(hotel, hotel.getName(), new JLabel("Select Room to Check Availability", JLabel.CENTER));
    }
    
    @Override
    public void addRoomButtonListener(JButton roomButton, Room room) {
        roomButton.addActionListener(e -> {
            controller.switchToViewCalendarFromRoom(room);
        });
    }
    
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