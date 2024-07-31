package mco2;

import java.util.ArrayList;

/**
 * The model of the program and manager
 * of the different classes. Has the
 * capability to create different instances
 * of hotels, rooms, and reservations.
 */
public class MVC_Model {
    private ArrayList<Hotel> hotels;

    /**
     * Constructor of the model class.
     * Takes in the main list of the hotels created
     * in the driver as a parameter.
     * @param hotels    list of hotels created in the driver, the actual list of hotels used by everything in the program
     */
    public MVC_Model(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
    }

    // Getters and Setters
    /**
     * Getter for the total number of hotels in the system
     * @return  the number of hotels in the system
     */
    public int getHotelCount() {
        return hotels.size();
    }

    /**
     * Getter for the list of hotels in the system
     * @return  the actual instances of the hotels in the system
     */
    public ArrayList<Hotel> getHotels() {
        return hotels;
    }

    /**
     * For checking if a name already exists
     * @param givenName  Name to be checked from the list
     * @return           True or False depending on if a name was found
     */
    public boolean checkName(String givenName) {
        int hotelCount = hotels.size();
        for (int i = 0; i < hotelCount; i++) {
            if (hotels.get(i).getName().equals(givenName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * For limiting to a Hotel to 50 rooms, returns different integers for different errors, used for hotel creation
     * @param givenRoomCount     Number to validate
     * @return                   Error code
     */
    public int checkRoomCount(int givenRoomCount) {
        if (givenRoomCount < 1) {
            return -1;
        } else if (givenRoomCount > 50) {
            return 0;
        }
        return 1;
    }

    /**
     * For limiting to a Hotel to 50 rooms, returns different integers for different errors, used for hotel creation
     * @param name          Name of new hotel
     * @param roomCount     Number to validate
     * @return              Error code
     */
    public boolean createHotel(String name, int roomCount) {
        if (checkName(name) && checkRoomCount(roomCount) == 1) {
            hotels.add(new Hotel(name, 1299.0f, roomCount));
            return true;
        }
        return false;
    }

    /**
     * For determining whether a certain name is unique
     * @param name  the name that is being checked
     * @return      whether the name is unique or not
     */
    public boolean isNameTaken(String name) {
        return !checkName(name);
    }

    /**
     * Determines whether a room count is valid or invalid for any reason
     * @param roomCount     the number of rooms being checked
     * @return              whether the room count is valid or not
     */
    public boolean isValidRoomCount(int roomCount) {
        return checkRoomCount(roomCount) == 1;
    }

    /**
     * Used for changing the name of a hotel to a new valid name
     * @param chosenHotel       the hotel being renamed
     * @param newHotelName      the new valid name
     */
    public void changeName(Hotel chosenHotel, String newHotelName) {
        chosenHotel.setName(newHotelName);
    }

    /**
     * Function used to add rooms to a hotel
     * @param chosenHotel   the hotel where the new rooms will be added to
     * @param newAdds       the number of new rooms to be added
     * @param baseRate      the type of the room to be added in the form of the price multiplier
     */
    public void addRooms(Hotel chosenHotel, int newAdds, float baseRate) {
        for (int i = 0; i < newAdds; i++) {
            chosenHotel.addRoom(baseRate);
        }
    }
    
    /**
     * Function used to remove rooms in a hotel
     * @param chosenHotel   the hotel where the rooms will be removed from
     * @param toDelete      the list of the names of the rooms that will be removed
     */
    public void removeRooms(Hotel chosenHotel, ArrayList<String> toDelete) {
        chosenHotel.deleteRooms(toDelete);
    }

    /**
     * Function used to set the base price of a chosen hotel
     * @param chosenHotel   hotel with its base price being changed
     * @param basePrice     the new valid base price of the hotel
     */
    public void modifyBasePrice(Hotel chosenHotel, float basePrice) {
        chosenHotel.setBasePrice(basePrice);
    }
    
    /**
     * Function used to set the new day multiplier of a chosen hotel on a specific day
     * @param chosenHotel   hotel with its day multiplier being changed
     * @param multiplier    the new valid multiplier for a specified day
     * @param day           the specified day being changed
     */
    public void modifyDayMultiplier(Hotel chosenHotel, float multiplier, int day) {
        chosenHotel.setDayMultiplier(day, multiplier);
    }
    
    // Reservations
    /**
     * Function used to create the instance of a reservation
     * @param room      the room being reserved
     * @param cost      the final cost of the reservation
     * @param name      the customer name
     * @param startDay  the check-in day of the reservation
     * @param endDay    the check-out day of the reservation
     */
    public void reserveRoom(Room room, float cost, String name, int startDay, int endDay) {
        Reservation reservation = new Reservation(name, cost, startDay, endDay, room.getName());
        room.addReservation(reservation);
    }
    
    /**
     * Function used to remove an instance of a reservation
     * @param hotel         the hotel where the reservation is getting removed from
     * @param reservation   the reservation instance being removed
     */
    public void removeReservation(Hotel hotel, Reservation reservation) {
        hotel.deleteReservation(reservation.getRoomName(), reservation.getStartDay(), reservation.getEndDay());
    }
}
