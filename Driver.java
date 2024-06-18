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
        
        int option = 0, hotelSelected = 0;
        while (option != -1) {
            if (hotels.size() == 0) {
                manager.createHotel();

            } else {
                option = display.displayGUI();

                if (option == 0) {
                    manager.createHotel();
                } else if (option == 1) {
                    System.out.printf("View hotels");
                } else if (option == 2) {
                    manager.manageHotels();
                }
            }
        }
    }
}
