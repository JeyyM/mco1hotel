package mco1;

import javax.swing.*;

/**
 * GUI Panel for reserving a specific room in a hotel
 * that inherits the ShowRooms class and shares the same functionality.
 */
public class ReserveRoomSelectPanel extends ShowRooms {
    /**
     * Constructor for the panel for selecting a room to be reserved
     * @param hotel     hotel chosen that will have its rooms displayed
     * @param name      name of the hotel chosen
     */
    public ReserveRoomSelectPanel(Hotel hotel, String name) {
        super(hotel, name, new JLabel("Select Room to Reserve", JLabel.CENTER));
    }
}
