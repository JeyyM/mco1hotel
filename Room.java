/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

import java.util.ArrayList;

/**
 * Representation of a room in one of the hotels.
 * Can have its own reservations.
 */
public class Room {
    private String name;
    private ArrayList<Reservation> reservations = new ArrayList<>();
    // Sorted list of Reservation AL to keep a linear timeline
    ArrayList<ArrayList<Integer>> reservationTimeline = new ArrayList<>();

    //!!!
    float baseRate = 1.0f;
    public float getBaseRate(){
        return this.baseRate;
    }

    public void setBaseRate(float baseRate) {
        this.baseRate = baseRate;
    }

    private int buttonIndex;
    public void setIndex(int index) {
        this.buttonIndex = index;
    }
    public int getIndex() {
        return buttonIndex;
    }
    
    /**
    * Constructor for Room
    * 
    * @param name   name of the specific room created
    */
    public Room(String name, float baseRate) {
        this.name = name;
        this.baseRate = baseRate;
    }

    // Getters
    /**
    * Getter for room name
    * @return   name of the specific room
    */
    public String getName() {
        return name;
    }
    
    /**
    * Getter for room reservations
    * @return   ArrayList of the reservations of the specific room
    */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    /**
    * Getter for the timeline of all the reservations for this room
    * @return   the days when the room are reserved
    */
    public ArrayList<ArrayList<Integer>> getReservationTimeline() {
        return reservationTimeline;
    }
    
    /**
    * Getter for length of the room reservation timeline
    * @return   length of the reservation timeline of the specific room
    */
    public int getReservationTimelineLength() {
        return reservationTimeline.size();
    }
    
    /**
    * Computes and returns the monthly earnings of the room
    * @return   total amount of earnings of the room
    */
    public float getMonthlyEarnings() {
        float total = 0.0f;
        for (Reservation reservation : reservations) {
            total += reservation.getCost();
        }

        return total;
    }

    /**
    * Prints --------------------------
    */
    public void printCalendarLine() {
        System.out.printf("------------------------------------\n");
    }

    /**
    * Prints a 31-day calendar with markers on availability
    */
    public void displayCalendar() {
        printCalendarLine();

        for (int i = 1; i <= 31; i++) {
            boolean found = false;
            // Checks the items within the reservationTimeline
            for (ArrayList<Integer> range : reservationTimeline) {
                if (range.contains(i)) {
                    // The day is a head or tail of a reservation
                    if (range.get(0) == i || range.get(range.size()-1) == i) {
                        System.out.printf("|{%02d}", i);
                    } else {
                        // Fully taken days in between head and tail
                        System.out.printf("|*%02d*", i);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Day is free
                System.out.printf("| %02d ", i);
            }
            // To set up 7-day rows
            if (i % 7 == 0) {
                System.out.printf("|\n");
                printCalendarLine();
            }
        }
        System.out.printf("|\n");
        printCalendarLine();
    }

    /**
    * Same as displayCalendar but will include a preview of the dates picked
    * @param checkInDay     Tail of day range
    * @param checkOutDay    Head of day range
    */
    public void displayCalendar2(int checkInDay, int checkOutDay) {
        // Array of days between check-in and check-out
        ArrayList<Integer> daysCovered = new ArrayList<>();
        for (int i = checkInDay; i <= checkOutDay; i++) {
            daysCovered.add(i);
        }

        printCalendarLine();

        // Print calendar with reservations and preview markers
        for (int i = 1; i <= 31; i++) {
            boolean found = false;

            // Priority check for the preview markers
            if (daysCovered.contains(i)) {
                if (checkInDay == checkOutDay && checkInDay == i) {
                    // If just a one-day reservation
                    System.out.printf("|<%02d>", i);
                } else if (i == checkInDay) {
                    System.out.printf("|<%02d>", i);
                } else if (i == checkOutDay) {
                    System.out.printf("|>%02d<", i);
                } else {
                    System.out.printf("|=%02d=", i);
                }
                found = true;
            }

            // Prior reservations
            if (!found) {
                for (ArrayList<Integer> range : reservationTimeline) {
                    if (range.contains(i)) {
                        if (range.get(0) == i || range.get(range.size() - 1) == i) {
                            System.out.printf("|{%02d}", i);
                        } else {
                            System.out.printf("|*%02d*", i);
                        }
                        found = true;
                        // To stop checking after finding a reservation
                        break;
                    }
                }
            }

            // If no reservation or preview marker, print day number
            if (!found) {
                System.out.printf("| %02d ", i);
            }

            if (i % 7 == 0) {
                System.out.printf("|\n");
                printCalendarLine();
            }
        }
        System.out.printf("|\n");
        printCalendarLine();
    }
    
    /**
    * Uses insertion sort to check for the first days of reservations to keep timeline linear
    */
    public void sortTime() {
        // Uses insertion sort
        for (int i = 1; i < reservationTimeline.size(); i++) {
            ArrayList<Integer> key = reservationTimeline.get(i);
            int j = i - 1;

            while (j >= 0 && reservationTimeline.get(j).get(0) > key.get(0)) {
                reservationTimeline.set(j + 1, reservationTimeline.get(j));
                j = j - 1;
            }
            reservationTimeline.set(j + 1, key);
        }

        for (int i = 1; i < reservations.size(); i++) {
            Reservation key = reservations.get(i);
            int j = i - 1;

            while (j >= 0 && reservations.get(j).getStartDay() > key.getStartDay()) {
                reservations.set(j + 1, reservations.get(j));
                j = j - 1;
            }
            reservations.set(j + 1, key);
        }
    }
    
    /**
    * Adds a reservation to this Room's list and adds it to the timeline
    * @param newReservation     Contains all reservation details
    */
    public void addReservation(Reservation newReservation) {
        reservations.add(newReservation);
        reservationTimeline.add(newReservation.getDayRange());
        // Sorts at the end
        sortTime();
    }
    
    /**
    * Checks if a day is found but is a head or tail, returns result code
    * @param givenDay   Day being searched in timeline
    * @return           Result code
    */
    public int checkDayAvailability(int givenDay) {
        for (ArrayList<Integer> range : reservationTimeline) {
            // Only the head or tail is worth validating if a number is found
            if (range.contains(givenDay)) {
                if (range.get(range.size() - 1) == givenDay || range.get(0) == givenDay) {
                    return 0;
                }
                return -1;
            }
        }
        return 1;
    }
    
    /**
    * Checks for days between head and tail too
    * @param checkInDay     First day of reservation
    * @param checkOutDay    Last day of reservation
    * @return               Result code
    */
    public int checkDayAvailability2(int checkInDay, int checkOutDay) {
        // First pass will check for days between
        for (int day = checkInDay; day <= checkOutDay; day++) {
            for (ArrayList<Integer> range : reservationTimeline) {
                if (range.contains(day)) {
                    if (range.get(range.size() - 1) == day) {
                        // Day matches the end of another reservation
                        // Allow check-in on this day if it's the start day
                        if (day != checkInDay) {
                            return -1;
                        }
                    } else if (range.get(0) == day) {
                        // Day matches the start of another reservation
                        // Allow check-out on this day if it's the end day
                        if (day != checkOutDay) {
                            return -1;
                        }
                    } else {
                        // Day is fully between another reservation range
                        return -1;
                    }
                }
            }
        }

        // Validate specific days as usual
        for (ArrayList<Integer> range : reservationTimeline) {
            if (range.contains(checkOutDay) && range.get(0) != checkOutDay) {
                // If the checkout day is within a range and not the start of the range
                return -1;
            }
        }

        return 0;
    }
    
    /**
    * Displays the reservation details of a room
    */
    public void displayReservations() {
        for (Reservation reservation : reservations) {
            System.out.printf("By: %s\n", reservation.getCustomerName());
            System.out.printf("     From Day %d to Day %d\n", reservation.getStartDay(), reservation.getEndDay());
            System.out.printf("     Total cost: %.2f\n\n", reservation.getCost());
        }
    }

    //MIGHT BE RETURNED FOR MCO2
    /* displayOccupiedHours - loops through a reserved head or tail to show taken ranges
                              since many reservations can be done as long as it is a head/tail
       @param int day - Day to check
       @return none - only prints invalid slots
       Precondition: none
    */
//    public void displayOccupiedHours(int day) {
//        ArrayList<String> occupiedHrs = new ArrayList<>();
//
//        // Puts all outputs in a String array
//        for (Reservation reservation : reservations) {
//            if (reservation.getDayRange().contains(day)) {
//                int startHour = reservation.getStartHour();
//                int endHour = reservation.getEndHour();
//
//                // If a reservation has the same start and end day, it is a one-day reservation
//                if (reservation.getStartDay() == day && reservation.getEndDay() == day) {
//                    // print limited range
//                    occupiedHrs.add(startHour + " to " + endHour);
//                    //The rest are for showing that it is part of a continuum
//                } else if (reservation.getStartDay() == day) {
//                    occupiedHrs.add(startHour + " to next day");
//                } else if (reservation.getEndDay() == day) {
//                    occupiedHrs.add("previous day to " + endHour);
//                }
//            }
//        }
//
//        // Will print out taken strings
//        for (String taken : occupiedHrs) {
//            System.out.printf("%s\n", taken);
//        }
//    }

    //MIGHT BE RETURNED FOR MCO2
    /* checkHourAvailability - Checks for a head/tail day's hour inputs
       @param int day - Day to check
       @param int hour - Hour to check
       @return boolean - If the timeslot is valid
       Precondition: none
    */
//    public boolean checkHourAvailability(int day, int hour) {
//        for (Reservation reservation : reservations) {
//            // Checks the list of reservations timelines
//            if (reservation.getDayRange().contains(day)) {
//                int startHour = reservation.getStartHour();
//                int endHour = reservation.getEndHour();
//
//                // The remaining checks are for in between days
//                // One-days
//                if (startHour == day && endHour == day) {
//                    if (hour >= startHour && hour <= endHour) {
//                        return false;
//                    }
//                    // Continuum ends
//                } else if (reservation.getStartDay() == day) {
//                    if (hour >= startHour && hour <= 24) {
//                        return false;
//                    }
//                } else if (reservation.getEndDay() == day) {
//                    if (hour <= endHour && hour >= 0) {
//                        return false;
//                    }                   }
//            }
//        }
//        return true;
//    }
}
