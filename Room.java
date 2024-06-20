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
    ArrayList<ArrayList<Integer>> reservationTimeline = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void printCalendarLine(){
        System.out.printf("-----------------------------------------\n");
    }

    public void displayCalendar() {
        printCalendarLine();

        for (int i = 1; i <= 31; i++) {
            boolean found = false;
            for (ArrayList<Integer> range : reservationTimeline) {
                if (range.contains(i)) {
                    if (range.get(0) == i || range.get(range.size()-1) == i) {
                        System.out.printf("|{%-2d}", i);
                    } else {
                        System.out.printf("|*%-2d*", i);
                    }
                    found = true;
                    break;
                }
            }
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

    public void displayCalendar2(int checkInDay, int checkOutDay) {
        ArrayList<Integer> daysCovered = new ArrayList<>();

        for (int i = checkInDay; i <= checkOutDay; i++) {
            daysCovered.add(i);
        }

        printCalendarLine();

        for (int i = 1; i <= 31; i++) {
            boolean found = false;
            for (ArrayList<Integer> range : reservationTimeline) {
                if (range.contains(i)) {
                    if (range.get(0) == i || range.get(range.size() - 1) == i) {
                        System.out.printf("|{%-2d}", i);
                    } else {
                        System.out.printf("|*%-2d*", i);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (daysCovered.contains(i)) {
                    if (checkInDay == checkOutDay && checkInDay == i) {
                        System.out.printf("|<%-2d>", i);
                    } else if (i == checkInDay) {
                        System.out.printf("|<%-2d>", i);
                    } else if (i == checkOutDay) {
                        System.out.printf("|>%-2d<", i);
                    } else {
                        System.out.printf("|=%-2d=", i);
                    }
                } else {
                    System.out.printf("| %-2d ", i);
                }
            }
            if (i % 7 == 0) {
                System.out.printf("|\n");
                printCalendarLine();
            }
        }
        System.out.printf("|\n");
        printCalendarLine();
    }

    public void sortTime() {
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

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservationTimeline.add(reservation.getDayRange());
        sortTime();
    }

    public int checkDayAvailability(int givenDay) {
        for (ArrayList<Integer> range : reservationTimeline) {
            if (range.contains(givenDay)) {
                if (range.get(0) == givenDay || range.get(range.size() - 1) == givenDay) {
                    return 0;
                }
                return -1;
            }
        }
        return 1;
    }

    public void displayOccupiedHours(int day) {
        ArrayList<String> occupiedHrs = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getDayRange().contains(day)) {
                int startHour = reservation.getStartHour();
                int endHour = reservation.getEndHour();

                if (reservation.getStartDay() == day && reservation.getEndDay() == day) {
                    occupiedHrs.add(startHour + "-" + endHour);
                } else if (reservation.getStartDay() == day) {
                    occupiedHrs.add(startHour + "-*");
                } else if (reservation.getEndDay() == day) {
                    occupiedHrs.add("*-" + endHour);
                }
            }
        }

        for (String taken : occupiedHrs) {
            System.out.printf("%s\n", taken);
        }
    }

    public boolean checkHourAvailability(int day, int hour) {
        for (Reservation reservation : reservations) {
            if (reservation.getDayRange().contains(day)) {
                int startHour = reservation.getStartHour();
                int endHour = reservation.getEndHour();

                if (reservation.getStartDay() == day && reservation.getEndDay() == day) {
                    if (hour >= startHour && hour <= endHour) {
                        return false;
                    }
                } else if (reservation.getStartDay() == day) {
                    if (hour >= startHour && hour <= 24) {
                        return false;
                    }
                } else if (reservation.getEndDay() == day) {
                    if (hour <= endHour && hour >= 0) {
                        return false;
                    }                   }
            }
        }

        return true;
    }
}
