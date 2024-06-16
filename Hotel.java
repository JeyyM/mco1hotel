/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

/**
 *
 * @author Job D. Trocino
 */
public class Hotel {
    private String name;
    private Room[] rooms = new Room[50];
    private int totalRooms = 0;
    private float basePrice = 1299.0f;
    
    public Hotel(String name) {
        this.name = name;
    }
    
    public Hotel(String name, float basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void addRoom() {
        if (totalRooms < 50) {
            String roomName;
            if (totalRooms < 9)
                roomName = this.name.charAt(0) + "0" + (this.totalRooms + 1);
            else
                roomName = this.name.charAt(0) + "" + (this.totalRooms + 1);

            this.rooms[totalRooms] = new Room(roomName, basePrice);
            totalRooms++;
        }
        else {
            System.out.printf("%s is full. Unable to add more rooms.\n", this.name);
        }
    }
}
