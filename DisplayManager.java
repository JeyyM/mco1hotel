/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

import java.util.Scanner;
import java.util.ArrayList;

public class DisplayManager {
    private Scanner sc;

    /**
    * Constructor for the display manager class
    */
    public DisplayManager() {
        this.sc = new Scanner(System.in);
    }

    /**
    * Gets user input after a list of numbers is shown from 0 to max and returns
    * the input minus 1 or prompts them again if their input is invalid
    * @param max    the highest integer that can be selected as an option
    * @return       the value selected by the user minus 1
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
    
    /**
    * Displays the main menu for the hotel management system
    * @return   the menu item chosen by the user
    */
    public int displayGUI() {
        System.out.printf("Hotel Management System\n");
        System.out.printf("------------------------------\n");
        System.out.printf("[1] Create Hotel\n");
        System.out.printf("[2] View Hotels\n");
        System.out.printf("[3] Manage Hotels\n");
        System.out.printf("[4] Reserve a Hotel\n");
        System.out.printf("[0] Exit Program\n");
        
        int option = select(5);
        
        return option;
    }
}
