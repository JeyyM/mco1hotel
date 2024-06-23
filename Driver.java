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
import java.util.Scanner;

public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Hotel> hotels = new ArrayList<>();
        DisplayManager display = new DisplayManager();
        HotelManager manager = new HotelManager(hotels);

        // Chosen menu option
        int option = 0;
        while (option != -1) {
            option = display.displayGUI();

            if (option == 0) {
                // Hotel creation
                manager.createHotel();
            } else if (option == 1 && hotels.size() > 0) {
                // View hotel details
                manager.viewHotels();
            } else if (option == 2 && hotels.size() > 0) {
                // Rename, modify rooms, change base price, remove reservations, delete hotel
                manager.manageHotels();
            } else if (option == 3 && hotels.size() > 0) {
                // Reserve a hotel room
                manager.reserveHotels();
            } else {
                // No hotels
                if (option != -1){
                    System.out.printf("There are currently no hotels to %s\n", option == 1 ? "view." : option == 2 ? "manage." : option == 3 ? "reserve." : "");
                    System.out.printf("Press any key to continue");
                    sc.nextLine();
                }
            }
        }
    }
}
