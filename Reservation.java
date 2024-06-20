package mco1;

import java.util.ArrayList;

public class Reservation {
    String name;
    int startDay;
    int startHour;
    int endDay;
    int endHour;
    ArrayList<Integer> dayRange;

    Reservation(int startDay, int startHour, int endDay, int endHour) {
        this.startDay = startDay;
        this.startHour = startHour;
        this.endDay = endDay;
        this.endHour = endHour;
        this.dayRange = new ArrayList<>();
        for (int i = startDay; i <= endDay; i++) {
            dayRange.add(i);
        }
    }

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
}
