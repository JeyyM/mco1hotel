package mco2;

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
    float baseRate = 1.0f;
    private int buttonIndex;
    
    /**
    * Constructor for Room
    * @param name       name of the specific room created
    * @param baseRate   the multiplier of the room, depending on its type
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
     * Getter for the base rate of the room, used to determine 
     * the type of the room from Regular, Deluxe, or Executive
     * @return  the multiplier of the cost depending on the room's type
     */
    public float getBaseRate(){
        return this.baseRate;
    }

    /**
     * Setter for the multiplier of the room, used for changing
     * the room's type when managing the rooms of a hotel
     * @param baseRate  the new multiplier and type of the room
     */
    public void setBaseRate(float baseRate) {
        this.baseRate = baseRate;
    }

    /**
     * Setter for the index of the room button needed for adding
     * a listener corresponding to it in the GUI panels
     * @param index     the designated index of the room
     */
    public void setIndex(int index) {
        this.buttonIndex = index;
    }
    
    /**
     * Getter for the index of the room button used when linking
     * the room to its designated button
     * @return  the designated index of the reservation
     */
    public int getIndex() {
        return buttonIndex;
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
}
