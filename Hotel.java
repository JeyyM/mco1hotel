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
public class Hotel {
    // Hotel details
    private String name;
    private float basePrice = 1299.0f;
    // Hotel's rooms, contain the reservations
    private ArrayList<Room> rooms = new ArrayList<>();
    // totalRooms is the current amount
    // originalTotalRooms is for maintaining name scheme
    private int totalRooms = 0;
    private int originalTotalRooms = 0;

    // Constructor
    Hotel(String name, float basePrice, int totalRooms) {
        this.name = name;
        this.basePrice = basePrice;
        this.totalRooms = 0;

        // Loops to addRooms based on inital total
        for (int i = 0; i < totalRooms; i++) {
            addRoom();
        }
    }

    // Getters and Setters
    public String getName() {
        return this.name;
    }

    public float getBasePrice() {
        return this.basePrice;
    }

    public void setBasePrice(float basePrice){
        this.basePrice = basePrice;
    }

    public int getTotalRooms() {
        return this.totalRooms;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom(int index){
        return rooms.get(index);
    }

    /* addRoom - Adds a new room, sets up naming scheme for each new room
       @param none
       @return - none, modifies rooms Arraylist
       Precondition: Hotel has been initialized
    */
    public void addRoom() {
        // Limited to 50 rooms
        if (this.totalRooms < 50) {
            // Naming scheme goes from a letter then followed by 01 up to 10
            char letterID = (char) ('A' + this.totalRooms / 10);
            int roomNumber = this.originalTotalRooms % 10 + 1;
            // Java allows for formatting similar to printf
            String roomName = String.format("%c%02d", letterID, roomNumber);

            // Adds the rooms to that AL and modifies counters
            this.rooms.add(new Room(roomName));
            this.totalRooms++;
            this.originalTotalRooms++;
        } else {
            // More than 50 rooms
            System.out.printf("%s is full. Unable to add more rooms.\n", this.name);
        }
    }

    /* displayRooms - Prints rooms in a grid format in their name scheme together with their index
       @param none
       @return - none
       Precondition: None
    */
    public void displayRooms() {
        for (int i = 0; i < this.totalRooms; i++) {
            System.out.printf("| [%-2d] %s", i + 1, rooms.get(i).getName());
            if ((i + 1) % 5 == 0) {
                System.out.printf("| \n");
            }
        }

        if (totalRooms % 5 != 0){
            System.out.printf("|\n");
        }
    }

    /* deleteRooms - Deletes a room based on their name, prints if it is successful
       @param String[] deleteIds - Array of IDs to be found and deleted
       @return int - number of deleted elements
       Precondition: None
    */
    public int deleteRooms(String[] deleteIds) {
        // Totals number of deletions
        int counter = 0;

        // Will loop through all deleteIds
        // Then loops through whole rooms array
        for (int j = 0; j < deleteIds.length; j++) {
            // Will set if a deletion was successful or not
            boolean found = false;
            for (int i = 0; i < this.rooms.size(); i++) {
                // Deletes the item, will print that it successfully found a name
                if (this.rooms.get(i).getName().equals(deleteIds[j])) {
                    System.out.printf("Room with ID %s was found, now deleting...\n", deleteIds[j]);
                    this.rooms.remove(i);
                    counter++;
                    found = true;
                    // upon finding a match, break will terminate the loop to save time
                    break;
                }
            }
            // Name was not found
            if (!found) {
                System.out.printf("Room with ID %s not found.\n", deleteIds[j]);
            }
        }

        // Changes values, originalTotalRooms stays the same
        this.totalRooms -= counter;
        return counter;
    }


    public boolean checkReservations(){
        for (Room room : this.rooms) {
            if (room.getReservationTimelineLength() != 0){
                return false;
            }
        }
        return true;
    }
}
