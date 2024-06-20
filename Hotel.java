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

import java.util.Scanner;
import java.util.ArrayList;
public class Hotel {
    private String name;
    // Changed Room to arraylist since deletion is easier
    private ArrayList<Room> rooms = new ArrayList<>();
    private int totalRooms = 0;
    private int originalTotalRooms = 0;
    private float basePrice = 1299.0f;
    private Scanner sc = new Scanner(System.in);
    
    public Hotel(String name) {
        this.name = name;
    }
    
    public Hotel(String name, float basePrice, int totalRooms) {
        this.name = name;
        this.basePrice = basePrice;
        this.totalRooms = 0;

        for (int i = 0; i < totalRooms; i++) {
            addRoom();
        }
    }
    
    public String getName() {
        return this.name;
    }

    public float getBasePrice() {
        return this.basePrice;
    }

    public void setBasePrice(float basePrice){
        this.basePrice = basePrice;
    }

    public int getTotalRooms() {
        return this.totalRooms;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void addRoom() {
        if (this.totalRooms < 50) {
            char letterID = (char) ('A' + this.totalRooms / 10);
            int roomNumber = this.originalTotalRooms % 10 + 1;
            String roomName = String.format("%c%02d", letterID, roomNumber);

            this.rooms.add(new Room(roomName));
            this.totalRooms++;
            this.originalTotalRooms++;
        } else {
            System.out.printf("%s is full. Unable to add more rooms.\n", this.name);
        }
    }


    public void displayRooms() {
        for (int i = 0; i < this.totalRooms; i++) {
            System.out.printf("| [%-2d] %s", i + 1, rooms.get(i).getName());
            if ((i + 1) % 5 == 0) {
                System.out.printf("| \n");
            }
        }

        if (totalRooms % 5 != 0){
            System.out.printf("|\n");
        }
    }

    public int deleteRooms(String[] deleteIds) {
        int counter = 0;

        for (int j = 0; j < deleteIds.length; j++) {
            boolean found = false;
            for (int i = 0; i < this.rooms.size(); i++) {
                if (this.rooms.get(i).getName().equals(deleteIds[j])) {
                    System.out.printf("Room with ID %s was found, now deleting...\n", deleteIds[j]);
                    this.rooms.remove(i);
                    counter++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.printf("Room with ID %s not found.\n", deleteIds[j]);
            }
        }

        this.totalRooms -= counter;
        return counter;
    }

    public Room getRoom(int index){
        return rooms.get(index);
    }

    public boolean checkReservations(){

        return false;
    }
}
