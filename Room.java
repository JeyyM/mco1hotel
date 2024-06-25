/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

import java.util.ArrayList;

/**
 *
 * @author Job D. Trocino
 */
public class Room {
    private String name;
    private ArrayList<Reservation> reservations = new ArrayList<>();
    // Sorted list of Reservation AL to keep a linear timeline
    ArrayList<ArrayList<Integer>> reservationTimeline = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return name;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public int getReservationTimelineLength() {
        return reservationTimeline.size();
    }

    public float getMonthlyEarnings(){
        float total = 0.0f;
        for (Reservation reservation : reservations){
            total += reservation.getCost();
        }

        return total;
    }

    /* printCalendarLine - prints --------------------------
       @param none
       @return - none
       Precondition: none
    */
    public void printCalendarLine(){
        System.out.printf("-----------------------------------------\n");
    }

    /* displayCalendar - will print a 31-day calendar with markers on availability
       @param none
       @return - none
       Precondition: none
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
                        System.out.printf("|{%-2d}", i);
                    } else {
                        // Fully taken days in between head and tail
                        System.out.printf("|*%-2d*", i);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Day is free
                System.out.printf("| %-2d ", i);
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

    /* displayCalendar2 - Same as displayCalendar but will include a preview of the dates picked
       @param int checkInDay - Tail of day range
       @param int checkOutDay - Head of day range
       @return - none
       Precondition: none
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
                    System.out.printf("|<%-2d>", i);
                } else if (i == checkInDay) {
                    System.out.printf("|<%-2d>", i);
                } else if (i == checkOutDay) {
                    System.out.printf("|>%-2d<", i);
                } else {
                    System.out.printf("|=%-2d=", i);
                }
                found = true;
            }

            // Prior reservations
            if (!found) {
                for (ArrayList<Integer> range : reservationTimeline) {
                    if (range.contains(i)) {
                        if (range.get(0) == i || range.get(range.size() - 1) == i) {
                            System.out.printf("|{%-2d}", i);
                        } else {
                            System.out.printf("|*%-2d*", i);
                        }
                        found = true;
                        // To stop checking after finding a reservation
                        break;
                    }
                }
            }

            // If no reservation or preview marker, print day number
            if (!found) {
                System.out.printf("| %-2d ", i);
            }

            if (i % 7 == 0) {
                System.out.printf("|\n");
                printCalendarLine();
            }
        }
        System.out.printf("|\n");
        printCalendarLine();
    }


    /* sortTime - Uses insertion sort to check for the first days of reservations to keep timeline linear
       @param none
       @return - none, sorts the array
       Precondition: none
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
    }

    /* addReservation - adds it to Room's list and adds it to the timeline
       @param Reservation newReservation - Contains all reservation details
       @return - none, adds to the list of Reservations
       Precondition: none
    */
    public void addReservation(Reservation newReservation) {
        reservations.add(newReservation);
        reservationTimeline.add(newReservation.getDayRange());
        // Sorts at the end
        sortTime();
    }

    /* checkDayAvailability - Checks if a day is found but is a head or tail, returns result code
       @param int givenDay - Day being searched in timeline
       @return int - Result code
       Precondition: none
    */
    public int checkDayAvailability(int givenDay) {
        for (ArrayList<Integer> range : reservationTimeline) {
            // Only the head or tail is worth validating if a number is found
            if (range.contains(givenDay)) {
                if (range.get(0) == givenDay || range.get(range.size() - 1) == givenDay) {
                    return 0;
                }
                return -1;
            }
        }
        return 1;
    }

    /* checkDayAvailability2 - Checks for days between head and tail too
       @param int checkInDay - Reservation tail
       @param int checkOutDay - Reservation head
       @return int - Result code
       Precondition: none
    */
    public int checkDayAvailability2(int checkInDay, int checkOutDay) {
        // First pass will check for days between
        for (int day = checkInDay; day <= checkOutDay; day++) {
            for (ArrayList<Integer> range : reservationTimeline) {
                if (range.contains(day)) {
                    if (range.get(0) == day || range.get(range.size() - 1) == day) {
                        // do nothing since head and tails can be booked
                    } else {
                        return -1;
                    }
                }
            }
        }

        // Will validate specific days as usual
        for (ArrayList<Integer> range : reservationTimeline) {
            if (range.contains(checkOutDay)) {
                if (range.get(0) == checkOutDay || range.get(range.size() - 1) == checkOutDay) {
                    return 0;
                }
                return -1;
            }
        }
        return 1;
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

    /* displayReservations - Displays the reservation details of a room
       @param none
       @return none
       Precondition: none
    */
    public void displayReservations(){
        for (Reservation reservation : reservations){
            System.out.printf("By: %s\n", reservation.getCustomerName());
            System.out.printf("     From Day %d to Day %d\n", reservation.getStartDay(), reservation.getEndDay());
            System.out.printf("     Total cost: %.2f\n\n", reservation.getCost());
        }
    }
}
