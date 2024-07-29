package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CheckAvailabilityByRoomPanel extends ShowRooms {
    public CheckAvailabilityByRoomPanel(Hotel hotel, String name) {
        super(hotel, name, new JLabel("Select Room to Check Availability", JLabel.CENTER));
    }
    
    @Override
    public void addRoomButtonListener(JButton roomButton, Room room) {
        roomButton.addActionListener(e -> {
            controller.switchToViewCalendarFromRoom(room);
        });
    }
}
