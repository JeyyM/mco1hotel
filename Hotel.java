/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Representation of the different hotels managed
 * by the hotel manager, with its own attributes,
 * rooms and reservations
 */
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

    //!!!
    private ArrayList<ImageIcon> images = new ArrayList<>();
    public void addImage(ImageIcon image) {
        images.add(image);
    }

    public ArrayList<ImageIcon> getImages() {
        return images;
    }

    private int buttonIndex;
    public void setIndex(int index) {
        this.buttonIndex = index;
    }
    public int getIndex() {
        return buttonIndex;
    }
    private float[] dayMultipliers = new float[31];

    public float[] getDayMultipliers() {
        return dayMultipliers;
    }

    public float getTotalEarnings(){
        float totalEarnings = 0.0f;
        for (Room room : rooms) {
            totalEarnings += room.getMonthlyEarnings();
        }

        return totalEarnings;
    }

    public void setDayMultiplier(int day, float multiplier) {
        dayMultipliers[day] = multiplier;
    }

    // Constructor
    /**
    * Constructor for the Hotel class
    * @param name           name of the hotel
    * @param basePrice      base price of the hotel
    * @param totalRooms     max number of rooms of the hotel
    */
    public Hotel(String name, float basePrice, int totalRooms) {
        this.name = name;
        this.basePrice = basePrice;
        this.totalRooms = 0;

        // Loops to addRooms based on initial total
        for (int i = 0; i < totalRooms; i++) {
            addRoom(1.0f);
        }

        for (int j = 0; j < 31; j++){
//            dayMultipliers[j] = 1.0f + (0.01f * (j+1));
            //dayMultipliers[j] = 1.0f * (j+1);
            dayMultipliers[j] = 1.0f;
        }
    }

    // Getters and Setters
    /**
    * Getter for the name of this hotel
    * @return   the name of this hotel
    */
    public String getName() {
        return this.name;
    }
    
    /**
    * Setter for the name of this hotel
    * @param name   the new name of this hotel; cannot be
    *               the same as the name of another hotel
    */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
    * Getter for the base price of the hotel
    * @return   the base price of the hotel
    */
    public float getBasePrice() {
        return this.basePrice;
    }
    
    /**
    * Setter for the base price of this hotel
    * @param basePrice  the new base price of this hotel; cannot be less than 100.0f
    */
    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    /**
    * Getter for the total rooms of this hotel
    * @return   the total rooms of this hotel
    */
    public int getTotalRooms() {
        return this.totalRooms;
    }
    
    /**
    * Getter for a specific room of this hotel
    * @param index  the index of the room in the rooms ArrayList
    * @return       the specific room instance chosen
    */
    public Room getRoom(int index) {
        return rooms.get(index);
    }
    
    /**
    * Getter for all the room instances of this hotel
    * @return   an ArrayList of all the room instances in this hotel
    */
    public ArrayList<Room> getAllRooms() {
        return rooms;
    }

    /**
    * Getter for all the rooms with reservations of this hotel
    * @return  an ArrayList of all the rooms with reservations in this hotel
    */
    public ArrayList<Room> getAllReservedRooms() {
        ArrayList<Room> reservedRooms = new ArrayList<>();

        for (Room room : this.rooms) {
            if (room.getReservationTimelineLength() != 0) {
                reservedRooms.add(room);
            }
        }
        return reservedRooms;
    }

    /**
    * Getter for all the rooms with reservations in a certain day
    * @param givenDay   the selected day to check rooms with reservations on
    * @return           an ArrayList of all the rooms with reservations on a certain day
    */
    public ArrayList<Room> getReservedRoomsByDay(int givenDay) {
        ArrayList<Room> reservedRooms = new ArrayList<>();

        for (Room room : this.rooms) {
            if (room.checkDayAvailability(givenDay) != 1) {
                reservedRooms.add(room);
            }
        }
        return reservedRooms;
    }
    
    /**
    * Getter for the total number of rooms
    * with reservations in this hotel
    * @return   total number of rooms with reservations
    */
    public int getTotalReservationCount() {
        int counter = 0;
        for (Room room : this.rooms) {
            if (room.getReservationTimelineLength() != 0) {
                counter += room.getReservationTimelineLength();
            }
        }
        return counter;
    }
    
    /**
    * Adds a new room using the specific naming scheme for this hotel
    */
    public void addRoom(float baseRate) {
        // Limited to 50 rooms
        if (this.totalRooms < 50) {
            // Naming scheme goes from a letter then followed by 01 up to 10
            char letterID = (char) ('A' + this.totalRooms / 10);
            int roomNumber = this.originalTotalRooms % 10 + 1;
            // Java allows for formatting similar to printf
            String roomName = String.format("%c%02d", letterID, roomNumber);

            // Adds the rooms to that AL and modifies counters
            this.rooms.add(new Room(roomName, baseRate));
            this.totalRooms++;
            this.originalTotalRooms++;
        } else {
            // More than 50 rooms
            System.out.printf("%s is full. Unable to add more rooms.\n", this.name);
        }
    }
    
    /**
    * Prints the rooms of this hotel in a grid format with the rooms index
    */
    public void displayRooms() {
        for (int i = 0; i < this.totalRooms; i++) {
            System.out.printf("| [%02d] %s", i + 1, rooms.get(i).getName());
            if ((i + 1) % 5 == 0) {
                System.out.printf("| \n");
            }
        }

        if (totalRooms % 5 != 0) {
            System.out.printf("|\n");
        }
    }
    
    /**
    * Deletes one or more rooms based on the room's name
    * and notifies the user if the deletion is successful
    * @param deleteIds  the room names of the rooms to be deleted
    * @return           the number of deleted elements
    */
    public int deleteRooms(ArrayList<String> deleteIds) {
        // Totals number of deletions
        int counter = 0;

        // Will loop through all deleteIds
        // Then loops through whole rooms array
        for (int j = 0; j < deleteIds.size(); j++) {
            // Will set if a deletion was successful or not
            boolean found = false;
            for (int i = 0; i < this.rooms.size(); i++) {
                if (this.rooms.get(i).getName().equals(deleteIds.get(j))) {

                    // Room has a reservation, can't delete
                    if (this.rooms.get(i).getReservations().size() > 0) {
                        System.out.printf("Room with ID %s was found with at least 1 reservation, cancelling...\n", deleteIds.get(j));
                        found = true;
                        break;
                    }

                    // Deletes the item, will print that it successfully found a name
                    System.out.printf("Room with ID %s was found, now deleting...\n", deleteIds.get(j));
                    this.rooms.remove(i);
                    counter++;
                    found = true;
                    // upon finding a match, break will terminate the loop to save time
                    break;
                }
            }
            // Name was not found
            if (!found) {
                System.out.printf("Room with ID %s not found.\n", deleteIds.get(j));
            }
        }

        // Changes values, originalTotalRooms stays the same
        this.totalRooms -= counter;
        return counter;
    }
    
    /**
    * Deletes a reservation after receiving the name of the room
    * and the check-in and check-out days of the reservation
    * @param roomName   the name of the room where the reservation is deleted from
    * @param startDay   the check-in date of the reservation to be deleted
    * @param endDay     the check-out date of the reservation to be deleted
    * @return           true or false if the deletion is successful or not
    */
    public boolean deleteReservation(String roomName, int startDay, int endDay) {
        boolean success = false;
        Room chosenRoom = null;
        Reservation chosenReservation = null;
        ArrayList<Integer> chosenTimelineItem = null;

        for (Room room : rooms) {
            if (room.getName().equals(roomName)) {
                chosenRoom = room;
                break;
            }
        }

        for (Reservation reservation : chosenRoom.getReservations()) {
            if (reservation.getStartDay() == startDay && reservation.getEndDay() == endDay) {
                chosenReservation = reservation;
                break;
            }
        }

        for (ArrayList<Integer> timeline : chosenRoom.getReservationTimeline()) {
            if (timeline.get(0) == startDay && timeline.get(timeline.size() - 1) == endDay) {
                chosenTimelineItem = timeline;
            }
        }

        if (chosenReservation != null) {
            chosenRoom.getReservations().remove(chosenReservation);
            chosenRoom.getReservationTimeline().remove(chosenTimelineItem);
            success = true;
        }

        return success;
    }
}
