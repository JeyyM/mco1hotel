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

public class HotelManager {
    private ArrayList<Hotel> hotels = new ArrayList<>();
    // hi
    public HotelManager(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
    }
    
    public void printHotels() {
        for (int i = 0; i < hotels.size(); i++)
            System.out.printf("%s\n", hotels.get(i).getName());
    }
    
    public void changeName(String hotelName, String newName) {
        boolean found = false;
        int index = 0;
        
        for (int i = 0; i < hotels.size() && found == false; i++) {
            if (hotels.get(i).getName() == hotelName) {
                found = true;
                index = i;
            }
        }
        
        if (found) {
            found = false;
            for (int i = 0; i < hotels.size() && found == false; i++) {
                if (hotels.get(i).getName() == newName)
                    found = true;
            }
            
            if (!found) {
                hotels.get(index).setName(newName);
            }
            else
                System.out.printf("not unique name\n");
        }
        else
            System.out.printf("hotel not found\n");
    }
    
    public void addRoom(String hotelName) {
        
    }
}
