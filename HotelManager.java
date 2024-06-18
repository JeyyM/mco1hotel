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
import java.util.ArrayList;
import java.util.Scanner;

public class HotelManager {
    private ArrayList<Hotel> hotels = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    public HotelManager(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
    }

    public void printHotels() {
        for (int i = 0; i < hotels.size(); i++)
            System.out.printf("%s\n", hotels.get(i).getName());
    }

    public boolean checkName(String givenName) {
        int hotelCount = hotels.size();
            for (int i = 0; i < hotelCount; i++) {
                if (hotels.get(i).getName().equals(givenName)){
                    return false;
                }
        }
        return true;
    }

    public boolean checkBasePrice(float givenPrice){
        if (givenPrice < 1299.0f){
            return false;
        }

        return true;
    }

    public int checkRoomCount(int givenRoomCount){
        if (givenRoomCount < 1){
            return -1;
        } else if (givenRoomCount > 50){
            return 0;
        }
        return 1;
    }

    public void createHotel(){
        boolean validateName = true;
        boolean validateBasePrice = true;
        boolean validateRoomCount = true;

        int hotelCount = hotels.size();

        String newHotelName = "";
        float newBasePrice = 1299.0f;
        int newRoomCount = 1;

        System.out.printf("There are currently: %d hotels.\n", hotelCount);

        while (validateName) {
            System.out.printf("Enter the name of the new hotel: ");
            newHotelName = sc.nextLine();

            if (!checkName(newHotelName)){
                System.out.printf("Hotel name has been taken, please pick a new one\n");
            } else {
                validateName = false;
            }
        }

        while (validateBasePrice) {
            System.out.printf("Enter the base price per room: ");
            newBasePrice = sc.nextFloat();
            sc.nextLine();

            if (!checkBasePrice(newBasePrice)){
                System.out.printf("Hotel base price must be at least 1299.00\n");
            } else {
                validateBasePrice = false;
            }
        }

        while (validateRoomCount) {
            System.out.printf("Enter how many rooms the hotel will have (Max of 50): ");
            newRoomCount = sc.nextInt();
            sc.nextLine();

            if (checkRoomCount(newRoomCount) == -1){
                System.out.printf("There must be at least 1 room\n");
            } else if (checkRoomCount(newRoomCount) == 0){
                System.out.printf("Invalid choice, there is a maximum of 50 rooms\n");
            } else {
                validateRoomCount = false;
            }
        }

        hotels.add(new Hotel(newHotelName, newBasePrice, newRoomCount));
    }

    public int select(int max) {
        System.out.printf("Select an option: ");
        int option = sc.nextInt();
        sc.nextLine();

        while (option < 0 || option > max) {
            System.out.printf("Option is incorrect!\nSelect: ");
            option = sc.nextInt();
        }

        return option - 1;
    }

    public void changeName(Hotel chosenHotel) {
        String newHotelName;
        boolean validateName = true;

        while (validateName) {
            System.out.printf("Enter the name of the new hotel: ");
            newHotelName = sc.nextLine();
            int option;

            if (!checkName(newHotelName)){
                System.out.printf("Hotel name has been taken, please pick a new one\n");
            } else {
                validateName = false;
                System.out.printf("%s is an available name, would you like to confirm the change?\n", newHotelName);
                System.out.printf("[0] Yes\n");
                System.out.printf("[1] No\n");
                option = select(1);

                if (option == -1){
                    chosenHotel.setName(newHotelName);
                } else if (option == 0){
                    System.out.printf("Name change cancelled.\n");
                }

            }
        }

    }

    public void addRooms(Hotel chosenHotel){
        int additions;
        boolean validateAdd = true;
        int option;

        System.out.printf("Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();

        while (validateAdd){
            System.out.printf("How many rooms would you like to add (0 to cancel): ");
            additions = sc.nextInt();

            if (additions == 0){
                validateAdd = false;
                System.out.printf("Room adding cancelled.\n");
            } else if (chosenHotel.getTotalRooms() + additions > 50){
                System.out.printf("Maximum rooms of 50 rooms exceeded, please try again\n");
            } else {
                System.out.printf("Are you sure you want to add %d rooms?\n", additions);
                System.out.printf("[0] Yes\n");
                System.out.printf("[1] No\n");
                option = select(1);

                if (option == -1){
                    System.out.printf("Successfully added %d rooms\n", additions);
                    for (int i = 0; i < additions; i++) {
                        chosenHotel.addRoom();
                    }
                    validateAdd = false;
                }
            }
        }

        System.out.printf("New Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();
        System.out.printf("Press any key to continue");
        sc.nextLine();
    }

    public void removeRoom(Hotel chosenHotel){
        String input;
        String[] toDelete;
        int option;
        int deletions;

        System.out.printf("New Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();

        System.out.printf("Write down the ids you want to delete with spaces 'A01 A02 A03'.\n");
        System.out.printf("Write 'QUIT' to cancel: ");

        input = sc.nextLine();

        if (input.equals("QUIT")){
            System.out.printf("Cancelled deletion\n");
        } else {
            toDelete = input.split(" ");
            System.out.printf("Are you sure you want to delete the following: \n");

            if (toDelete.length == 1){
                System.out.println(toDelete[0]);
            } else {
                for (int i = 0; i < toDelete.length; i++) {
                    if (i == toDelete.length - 1) {
                        System.out.printf("and %s\n", toDelete[i]);
                    } else {
                        System.out.printf("%s", toDelete[i]);
                        if (i < toDelete.length - 2) {
                            System.out.printf(", ");
                        } else {
                            System.out.printf(" ");
                        }
                    }
                }
            }

            System.out.printf("[0] Yes\n");
            System.out.printf("[1] No\n");
            option = select(1);

            if (option == -1){
                System.out.printf("Deletion in progress\n");
                deletions = chosenHotel.deleteRooms(toDelete);

                System.out.printf("Successfully deleted %d rooms\n", deletions);
                System.out.printf("Press any key to continue...\n", deletions);
                sc.nextLine();
            } else if (option == 1){
                System.out.printf("Deletion Cancelled\n");
            }
        }
    }

    public void modifyBasePrice(Hotel chosenHotel){
        boolean editable = !chosenHotel.checkReservations();
        float newPrice = 0.0f;
        boolean validatePrice = true;
        int option;

        if (editable){
            System.out.printf("You cannot change the base price because there is an affected reservation\n");
        } else {
            while (validatePrice){
                System.out.printf("Write new base price, minimum of 100.0 (0 to cancel): ");
                newPrice = sc.nextFloat();
                sc.nextLine();

                if (newPrice == 0){
                    validatePrice = false;
                    System.out.printf("Price change cancelled.\n");
                } else if (newPrice < 100){
                    System.out.printf("Minimum price of at least 100.00\n");
                } else {
                    System.out.printf("Are you sure you want to change the base price from %.2f to %.2f?\n", chosenHotel.getBasePrice(), newPrice);
                    System.out.printf("[0] Yes\n");
                    System.out.printf("[1] No\n");
                    option = select(1);

                    if (option == -1){
                        System.out.printf("Successfully changed base price to %.2f\n", newPrice);
                        chosenHotel.setBasePrice(newPrice);
                        validatePrice = false;
                    }
                }
            }
        }
    }

    public void manageHotels(){
        int viewLevel = 1;
        Hotel currentHotel = null;
        int hotelCount = hotels.size();
        int option = 0;

        while (viewLevel >= 1){
            System.out.printf("Which hotel would you like to manage?\n");
            for (int i = 0; i < hotelCount; i++) {
                System.out.printf("[%d] %s \n", i + 1, hotels.get(i).getName());
            }
            System.out.printf("[0] Exit Management\n");
            option = select(hotels.size()+1);

            if (option == -1){
                viewLevel = 0;
            } else {
                int suboption;
                viewLevel++;
                while (viewLevel >= 2) {
                    currentHotel = hotels.get(option);
                    System.out.printf("Current Hotel: %s\n", currentHotel.getName());
                    System.out.printf("Base Price: %.2f\n", currentHotel.getBasePrice());

                    System.out.printf("[1] Rename Hotel\n");
                    System.out.printf("[2] Add Rooms\n");
                    System.out.printf("[3] Remove Rooms \n");
                    System.out.printf("[4] Modify Base Price\n");
                    System.out.printf("[5] Remove Reservations\n");
                    System.out.printf("[6] Delete Hotel\n");
                    System.out.printf("[0] Exit Management\n");

                    suboption = select(6);

                    if (suboption == -1) {
                        viewLevel--;
                    }

                    if (suboption == 0){
                        changeName(currentHotel);
                    } else if (suboption == 1){
                        addRooms(currentHotel);
                    } else if (suboption == 2){
                        removeRoom(currentHotel);
                    } else if (suboption == 3){
                        modifyBasePrice(currentHotel);
                    } else if (suboption == 4){
                        System.out.printf("Remove Reservation\n");
                    } else if (suboption == 5){
                        System.out.printf("Are you sure you want to delete the hotel?\n");
                        System.out.printf("This cannot be undone.\n");
                        System.out.printf("[0] Yes\n");
                        System.out.printf("[1] No\n");
                        option = select(1);

                        if (option == -1){
                            System.out.printf("Hotel has been removed.\n");
                        } else if (option == 0){
                            System.out.printf("Hotel deletion cancelled.\n");
                            // idk what the deal with this guy is
//                            hotels.remove(currentHotel);
                            viewLevel = 0;
                            System.out.printf("Press any key to continue...\n");
                            sc.nextLine();
                        }
                    }
                    }
                }
            }
        }
    }
