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

public class DisplayManager {
    private Scanner sc;


    public DisplayManager() {
        this.sc = new Scanner(System.in);
    }

    /* select - Creates a quick number-1 which results in a range of -1 to max-1 with some validation
       @param int max - highest possible integer input
       @return - Returns an input integer - 1
       Precondition: None
    */
    public int select(int max) {
        System.out.printf("Select an option: ");
        int option = sc.nextInt();
        // Clears buffer
        sc.nextLine();

        // Validates input limitations
        while (option < 0 || option > max) {
            System.out.printf("Option is invalid!\nSelect: ");
            option = sc.nextInt();
            sc.nextLine();
        }
        
        return option - 1;
    }

    /* displayGUI - Prints menu options, contains a select to choose an option
       @param none
       @return - input - 1
       Precondition: None
    */
    public int displayGUI() {
        System.out.printf("Hotel Management System v4521\n");
        System.out.printf("------------------------------\n");
        System.out.printf("[1] Create Hotel\n");
        System.out.printf("[2] View Hotels\n");
        System.out.printf("[3] Manage Hotels\n");
        System.out.printf("[4] Reserve a Hotel\n");
        System.out.printf("[0] Exit Program\n");
        
        int option = select(5);
        
        return option;
    }
    
    public void displayHotels(ArrayList<Hotel> hotels) {
        System.out.printf("Hotels Managed by J&J Co.\n");
        for (int i = 0; i < hotels.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, hotels.get(i).getName());
        }
        System.out.printf("[0] Cancel Selection\n");
    }
}
