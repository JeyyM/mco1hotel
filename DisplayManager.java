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
    
    public int select(int max) {
        System.out.printf("Select an option: ");
        int option = sc.nextInt();
        sc.nextLine();
        
        while (option < 0 || option > max) {
            System.out.printf("Option is incorrect!\nSelect: ");
            option = sc.nextInt();
            sc.nextLine();
        }
        
        return option - 1;
    }
    
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
    
    public int displayManager() {
        return 0;
    }
}
