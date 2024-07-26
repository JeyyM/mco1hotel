package mco1;

import javax.swing.*;
import java.util.ArrayList;

/*
 * This is the panel for the selection of a hotel to manage
 * */
public class ManageHotelsPanel extends ShowHotels {

    public ManageHotelsPanel(ArrayList<Hotel> hotels, HotelManager manager) {
        super(hotels, manager, new JLabel("Select Hotel to Manage", JLabel.CENTER));
    }
}
