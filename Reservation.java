package mco1;

import java.util.ArrayList;

/**
 * Represents a reservation created by a user
 * containing all the information necessary.
 */
public class Reservation {
    private String customerName;
    private int startDay;
//    private int startHour;
    private int endDay;
//    private int endHour;
    private float cost = 0.0f;
    private ArrayList<Integer> dayRange = new ArrayList<>();
    private String roomName;

    /**
    * Constructor for the Reservation class
    * @param customerName   name of the customer reserving the room
    * @param cost           base cost of the room
    * @param startDay       day of the month when the customer checks in
    * @param endDay         day of the month when the customer checks out
    * @param roomName       name of the room that is being reserved
    */
    public Reservation(String customerName, float cost, int startDay, int endDay, String roomName) {
        this.customerName = customerName;
        this.cost = cost;
        this.startDay = startDay;
        this.endDay = endDay;
        this.roomName = roomName;
        // Adds the occupied ranges for each reservation
        for (int i = startDay; i <= endDay; i++) {
            this.dayRange.add(i);
        }
    }

    // Getters
    /**
    * Getter for the range of days of the reservation
    * @return   returns the days in integer format of the days the reservation exists
    */
    public ArrayList<Integer> getDayRange() {
        return this.dayRange;
    }
    
    /**
    * Getter for the check in day
    * @return   the day of the month the user will check in the room
    */
    public int getStartDay() {
        return this.startDay;
    }
    
    /*
    public int getStartHour() {
        return this.startHour;
    }
    */
    
    /**
    * Getter for the check out day
    * @return   the day of the month the user will check out of the room
    */
    public int getEndDay() {
        return this.endDay;
    }
    
    /*
    public int getEndHour() {
        return this.endHour;
    }
    */
    
    /**
    * Getter for the base cost of the room
    * @return   the base cost of the room dictated by the base price of the hotel
    */
    public float getCost(){
        return this.cost;
    }
    
    /**
    * Getter for the customer name
    * @return   the name of the person who reserved the room
    */
    public String getCustomerName() {
        return this.customerName;
    }
    
    /**
    * Getter for the room name
    * @return   the name of the room reserved
    */
    public String getRoomName(){
        return this.roomName;
    }
    
    private int buttonIndex;
    
    public void setIndex(int index) {
        this.buttonIndex = index;
    }
    
    public int getIndex() {
        return buttonIndex;
    }
}
