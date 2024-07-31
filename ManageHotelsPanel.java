package mco2;

import javax.swing.*;
import java.util.ArrayList;

/**
 * GUI panel for the selection of a hotel to manage.
 * Inherits the ShowHotels class
 */
public class ManageHotelsPanel extends ShowHotels {
    /**
     * Constructor for the panel that uses the constructor
     * of the ShowHotels with a different title
     * @param hotels    list of hotels that can be managed by the GUI
     */
    public ManageHotelsPanel(ArrayList<Hotel> hotels) {
        super(hotels, new JLabel("Select Hotel to Manage", JLabel.CENTER));
    }
}
