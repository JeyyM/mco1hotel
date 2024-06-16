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
            option = display.displayGUI();
            
            if (option == 0) {
                hotels.add(new Hotel("Resort"));
                hotels.add(new Hotel("Besort"));
                hotels.add(new Hotel("Desort"));
                
                display.displayHotels(hotels);
                hotelSelected = display.select(hotels.size());
                if (hotelSelected != -1)
                    System.out.printf("Hotel: %s\n", hotels.get(hotelSelected).getName());
            }
        }
        
        manager.changeName("Resort", "Cesort");
        
    }
    
}
