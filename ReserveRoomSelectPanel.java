package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This is the panel for the selection of a room to reserve
 * */
public class ReserveRoomSelectPanel extends ShowRooms {
    public ReserveRoomSelectPanel(Hotel hotel, String name) {
        super(hotel, name, new JLabel("Select Hotel to Manage", JLabel.CENTER));
    }
}
