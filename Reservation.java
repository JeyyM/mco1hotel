package mco1;

import java.util.ArrayList;

public class Reservation {
    private String customerName;
    private int startDay;
    private int startHour;
    private int endDay;
    private int endHour;
    private float cost = 0.0f;
    private ArrayList<Integer> dayRange = new ArrayList<>();
    private String roomName;

    Reservation(String customerName, float cost, int startDay, int startHour, int endDay, int endHour, String roomName) {
        this.customerName = customerName;
        this.cost = cost;
        this.startDay = startDay;
        this.startHour = startHour;
        this.endDay = endDay;
        this.endHour = endHour;
        this.roomName = roomName;
        // Adds the occupied ranges for each reservation
        for (int i = startDay; i <= endDay; i++) {
            this.dayRange.add(i);
        }
    }

    // Getters
    public ArrayList<Integer> getDayRange() {
        return this.dayRange;
    }

    public int getStartDay() {
        return this.startDay;
    }

    public int getStartHour() {
        return this.startHour;
    }

    public int getEndDay() {
        return this.endDay;
    }

    public int getEndHour() {
        return this.endHour;
    }

    public float getCost(){
        return this.cost;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getRoomName(){
        return this.roomName;
    }
}
