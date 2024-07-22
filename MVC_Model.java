package mco1;

import java.util.ArrayList;

public class MVC_Model {
    private ArrayList<Hotel> hotels;
    private HotelManager manager;

    public MVC_Model(ArrayList<Hotel> hotels, HotelManager manager) {
        this.hotels = hotels;
        this.manager = manager;
    }

    // Getters and Setters
    public int getHotelCount() {
        return hotels.size();
    }

    public ArrayList<Hotel> getHotels() {
        return hotels;
    }

    public HotelManager getManager(){
        return manager;
    }

    // Hotel Creation
    public boolean createHotel(String name, int roomCount) {
        return manager.createHotel2(name, roomCount);
    }

    public boolean isNameTaken(String name) {
        return !manager.checkName(name);
    }

    public boolean isValidRoomCount(int roomCount) {
        return manager.checkRoomCount(roomCount) == 1;
    }

    public void changeName(Hotel chosenHotel, String newHotelName) {
        manager.changeName2(chosenHotel, newHotelName);
    }
}
