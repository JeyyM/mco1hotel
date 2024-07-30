package mco1;

import java.util.ArrayList;

public class MVC_Model {
    private ArrayList<Hotel> hotels;

    public MVC_Model(ArrayList<Hotel> hotels) {
        this.hotels = hotels;}


    // Getters and Setters
    public int getHotelCount() {
        return hotels.size();
    }

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
     * @return                   Error code
     */
    public boolean createHotel(String name, int roomCount) {
        if (checkName(name) && checkRoomCount(roomCount) == 1) {
            hotels.add(new Hotel(name, 1299.0f, roomCount));
            return true;
        }
        return false;
    }

    public boolean isNameTaken(String name) {
        return !checkName(name);
    }

    public boolean isValidRoomCount(int roomCount) {
        return checkRoomCount(roomCount) == 1;
    }

    public void changeName(Hotel chosenHotel, String newHotelName) {
        chosenHotel.setName(newHotelName);
    }

    public void addRooms(Hotel chosenHotel, int newAdds, float baseRate) {
        for (int i = 0; i < newAdds; i++) {
            chosenHotel.addRoom(baseRate);
        }
    }

    public void modifyBasePrice(Hotel chosenHotel, float basePrice) {
        chosenHotel.setBasePrice(basePrice);
    }
    
    public void modifyDayMultiplier(Hotel chosenHotel, float multiplier, int day) {
        chosenHotel.setDayMultiplier(day, multiplier);
    }
    
    // Reservations
    public void reserveRoom(Room room, float cost, String name, int startDay, int endDay) {
        Reservation reservation = new Reservation(name, cost, startDay, endDay, room.getName());
        room.addReservation(reservation);
    }
    
    public void removeReservation(Hotel hotel, Reservation reservation) {
        hotel.deleteReservation(reservation.getRoomName(), reservation.getStartDay(), reservation.getEndDay());
    }
}
