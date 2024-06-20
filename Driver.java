/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

/**
 *
 * @author Job D. Trocino
 */
import java.util.ArrayList;

public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Hotel> hotels = new ArrayList<>();
        DisplayManager display = new DisplayManager();
        HotelManager manager = new HotelManager(hotels);

        // Chosen menu option
        int option = 0;
        while (option != -1) {
            // Might remove this later
            if (hotels.size() == 0) {
                manager.createHotel();
            } else {
                option = display.displayGUI();

                if (option == 0) {
                    // Hotel creation
                    manager.createHotel();
                } else if (option == 1) {
                    // View hotel details
                    System.out.printf("View hotels");
                } else if (option == 2) {
                    // Rename, modify rooms, change base price, remove reservations, delete hotel
                    manager.manageHotels();
                } else if (option == 3) {
                    // Reserve a hotel room
                    manager.reserveHotels();
                }
            }
        }
    }
}
