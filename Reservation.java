package mco1;

import java.util.ArrayList;

public class Reservation {
    String customerName;
    int startDay;
    int startHour;
    int endDay;
    int endHour;
    float cost = 0.0f;
    ArrayList<Integer> dayRange;

    Reservation(String customerName, float cost, int startDay, int startHour, int endDay, int endHour) {
        this.customerName = customerName;
        this.cost = cost;
        this.startDay = startDay;
        this.startHour = startHour;
        this.endDay = endDay;
        this.endHour = endHour;
        this.dayRange = new ArrayList<>();
        // Adds the occupied ranges for each reservation
        for (int i = startDay; i <= endDay; i++) {
            dayRange.add(i);
        }
    }

    // Getters
    public ArrayList<Integer> getDayRange() {
        return dayRange;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndDay() {
        return endDay;
    }

    public int getEndHour() {
        return endHour;
    }

    public float getCost(){
        return cost;
    }

    public String getCustomerName() {
        return customerName;
    }
}
