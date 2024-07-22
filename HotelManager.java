/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mco1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contains a majority of methods used to modify the hotels
 * It contains the validations and inputs gathering
 * Mostly leaves the detailed modification of a Hotel, Room, or Reservation to
 * their respective classes
 */
public class HotelManager {
    private Scanner sc = new Scanner(System.in);

    // Initializers
    private ArrayList<Hotel> hotels = new ArrayList<>();
    
    /**
    * Constructor for Hotel Manager
    * @param hotels     hotels that are managed by the hotel manager
    */
    public HotelManager(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
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
    * For creating a hotel to be stored in the manager,
    * contains all the processes and constructors for a Hotel
    */
    public void createHotel() {
        // Validations for while loops
        int viewLevel = 1;

        int hotelCount = hotels.size();

        // Defaults
        String newHotelName = "";
        float newBasePrice = 1299.0f;
        int newRoomCount = 1;

        System.out.printf("There %s currently: ", hotelCount == 1 ? "is" : "are");

        switch (hotelCount) {
            case 0:{
                System.out.printf("no hotels\n");
                break;
            }
            case 1:{
                System.out.printf("1 hotel\n");
                break;
            }

            default:{
                System.out.printf("%d hotels\n", hotelCount);
                break;
            }
        }

        // Name selection
        while (viewLevel == 1) {
            System.out.printf("Enter the name of the new hotel, input a blank to go back: ");
            newHotelName = sc.nextLine();

            //blank input exits
            if (newHotelName.equals("")) {
                viewLevel = 0;
            } else {
                if (!checkName(newHotelName)) {
                    System.out.printf("Hotel name has been taken, please pick a new one\n");
                } else {
                    viewLevel++;
                    // Room counting
                    while (viewLevel == 2) {
                        System.out.printf("Enter how many rooms the hotel will have (Max of 50), 0 to go back: ");
                        newRoomCount = sc.nextInt();
                        sc.nextLine();

                        if (newRoomCount == 0) {
                            viewLevel--;
                        } else if (checkRoomCount(newRoomCount) == -1) {
                            System.out.printf("Invalid choice, room count must be positive\n");
                        } else if (checkRoomCount(newRoomCount) == 0) {
                            System.out.printf("Invalid choice, maximum of 50 rooms\n");
                        } else {
                            viewLevel++;

                            while (viewLevel == 3) {
                                System.out.printf("Hotel Name: %s\n", newHotelName);
                                System.out.printf("Room Count: %d\n", newRoomCount);
                                System.out.printf("Are you sure you want to add this hotel?\n");
                                System.out.printf("[0] Yes\n");
                                System.out.printf("[1] No\n");
                                int option = select(1);

                                if (option == -1) {
                                    // adds hotel at the end
                                    hotels.add(new Hotel(newHotelName, 1299.0f, newRoomCount));
                                    viewLevel=0;
                                    System.out.printf("Hotel successfully added\n");

                                    System.out.printf("Press any key to continue\n");
                                    sc.nextLine();

                                } else if (option == 0) {
                                    viewLevel--;
                                }
                            }
                        }
                    }
                }
            }
        }
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
        sc.nextLine();

        while (option < 0 || option > max) {
            System.out.printf("Option is invalid!\nSelect: ");
            option = sc.nextInt();
        }

        return option - 1;
    }

    /**
    * Changes hotel name, contains validation
    * @param chosenHotel    the Hotel that will have its name changed
    */
    public void changeName(Hotel chosenHotel) {
        String newHotelName;
        boolean validateName = true;

        while (validateName) {
            System.out.printf("Enter the new name of the hotel, input a blank to go back: ");
            newHotelName = sc.nextLine();
            int option;

            // Go back
            if (newHotelName.equals("")) {
                validateName = false;
            } else if (newHotelName.equals(chosenHotel.getName())) {
                validateName = false;
                System.out.printf("Hotel name is the same, cancelling change\n");
                System.out.printf("Press any key to continue\n");
                sc.nextLine();
            }
            // Does name exist
            else if (!checkName(newHotelName)) {
                System.out.printf("Hotel name has been taken, please pick a new one\n");
                System.out.printf("Press any key to continue\n");
                sc.nextLine();
            } else {
                validateName = false;
                System.out.printf("%s is an available name, would you like to confirm the change?\n", newHotelName);
                System.out.printf("[0] Yes\n");
                System.out.printf("[1] No\n");
                option = select(1);

                if (option == -1) {
                    System.out.printf("Hotel name has been changed from %s to %s\n", chosenHotel.getName(), newHotelName);
                    chosenHotel.setName(newHotelName);
                } else if (option == 0) {
                    System.out.printf("Name change cancelled.\n");
                }
            }
        }
    }

    /**
    * Adds one or more rooms to the room ArrayList in the chosen hotel, contains all validations
    * @param chosenHotel    hotel that the room/s will be added to
    */
    public void addRooms(Hotel chosenHotel) {
        int additions;
        boolean validateAdd = true;
        int option;

        // Shows rooms
        System.out.printf("Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();

        while (validateAdd) {
            System.out.printf("How many rooms would you like to add (0 to cancel): ");
            additions = sc.nextInt();

            if (additions == 0) {
                validateAdd = false;
                System.out.printf("Room adding cancelled.\n");
                // Addition amount validation
            } else if (checkRoomCount(additions) == -1) {
                System.out.printf("Additions can't be less than 1, please try again\n");
            } else if (checkRoomCount(additions+chosenHotel.getTotalRooms()) == 0) {
                System.out.printf("Maximum rooms of 50 rooms exceeded, please try again\n");
            } else {
                // Valid sum
                System.out.printf("Are you sure you want to add %d rooms?\n", additions);
                System.out.printf("[0] Yes\n");
                System.out.printf("[1] No\n");
                option = select(1);

                if (option == -1) {
                    // Adds the room, loops through additions
                    System.out.printf("Successfully added %d rooms\n", additions);
                    for (int i = 0; i < additions; i++) {
                        chosenHotel.addRoom();
                    }
                    validateAdd = false;
                }
            }
        }

        // Displays new details
        System.out.printf("New Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();
        System.out.printf("Press any key to continue\n");
        sc.nextLine();
    }

    /**
    * Removes one or more rooms from the room ArrayList in the chosen hotel, contains all validations
    * @param chosenHotel    hotel that the room/s will be removed from
    */
    public void removeRoom(Hotel chosenHotel) {
        String input;
        String[] toDelete;
        int option;
        // number of successful deletions
        int deletions;

        System.out.printf("Current Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();

        System.out.printf("Write down the ids you want to delete with spaces 'A01 A02 A03'.\n");
        System.out.printf("Write 'QUIT' to cancel: ");

        input = sc.nextLine();

        if (input.equals("QUIT")) {
            System.out.printf("Cancelled deletion\n");
        } else {
            toDelete = input.split(" ");
            System.out.printf("Are you sure you want to delete the following: \n");

            // Prints all ids to delete
            if (toDelete.length == 1) {
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

            // Calls deletion with deleteRooms
            if (option == -1) {
                System.out.printf("Deletion in progress\n");
//                deletions = chosenHotel.deleteRooms(toDelete);

//                System.out.printf("Successfully deleted %d rooms\n", deletions);
//                System.out.printf("Press any key to continue...\n", deletions);
                sc.nextLine();
            } else if (option == 1) {
                System.out.printf("Deletion Cancelled\n");
            }
        }
    }
    
    /**
    * Modifies the base price of all the rooms in a chosen hotel,
    * as long as the new base price is above 100.0f
    * @param chosenHotel    hotel that the will have its base price changed
    */
    public void modifyBasePrice(Hotel chosenHotel) {
        boolean editable = chosenHotel.getTotalReservationCount() != 0;
        float newPrice = 0.0f;
        boolean validatePrice = true;
        int option;

        // Cancelled by a reservation
        if (editable) {
            System.out.printf("You cannot change the base price because there is an affected reservation\n");
            System.out.printf("Press any key to continue...\n");
            sc.nextLine();
        } else {
            // Validations
            while (validatePrice) {
                System.out.printf("Write new base price, minimum of 100.0 (0 to cancel): ");
                newPrice = sc.nextFloat();
                sc.nextLine();

                if (newPrice == 0) {
                    validatePrice = false;
                    System.out.printf("Price change cancelled.\n");
                } else if (newPrice < 100) {
                    System.out.printf("Minimum price of at least 100.00\n");
                } else {
                    System.out.printf("Are you sure you want to change the base price from %.2f to %.2f?\n", chosenHotel.getBasePrice(), newPrice);
                    System.out.printf("[0] Yes\n");
                    System.out.printf("[1] No\n");
                    option = select(1);

                    if (option == -1) {
                        System.out.printf("Successfully changed base price to %.2f\n", newPrice);
                        chosenHotel.setBasePrice(newPrice);
                        validatePrice = false;
                    }
                }
            }
        }
    }
    
    /**
    * Removes a reservation from a chosen room in a chosen hotel
    * @param chosenHotel    hotel that will have its rooms and reservations displayed
    */
    public void removeReservation(Hotel chosenHotel) {
        // for id counting to not have to input a room id and reservation id
        int counter;
        // for while menuing
        int viewLevel = 1;

        int option;
        int suboption;

        ArrayList<Room> hotelRooms = chosenHotel.getAllReservedRooms();
        // for totalling all reservations
        ArrayList<Reservation> hotelReservations = new ArrayList<>();
        // reservation to delete
        Reservation chosenReservation = null;

        if (hotelRooms.size() == 0) {
            System.out.printf("There are no reservations to remove\n");
            System.out.printf("Press any key to continue...\n");
            sc.nextLine();
        } else {
            while (viewLevel >= 1) {
                System.out.printf("Select which reservation would you like to remove:\n");
                counter = 0;

                // Getting total reservations and printing them
                for (Room room : hotelRooms) {
                    for (Reservation reservation : room.getReservations()) {
                        counter++;
                        hotelReservations.add(reservation);

                        System.out.printf("[%d] Room %s:\n", counter, reservation.getRoomName());
                        System.out.printf("     By: %s\n", reservation.getCustomerName());
                        System.out.printf("         Total cost: %s:\n", reservation.getCost());
                        System.out.printf("         From Day %d to Day %d:\n\n", reservation.getStartDay(), reservation.getEndDay());
                    }
                }
                System.out.printf("[0] Back\n");

                option = select(hotelReservations.size());

                if (option == -1) {
                    viewLevel = 0;
                } else {
                    viewLevel++;
                    // will finalize deletion
                    while (viewLevel>=2) {
                        chosenReservation = hotelReservations.get(option);
                        System.out.printf("Are you sure you want to remove this reservation?\n");
                        System.out.printf("Room %s:\n",chosenReservation.getRoomName());
                        System.out.printf("     By: %s\n", chosenReservation.getCustomerName());
                        System.out.printf("         Total cost: %s:\n", chosenReservation.getCost());
                        System.out.printf("         From Day %d to Day %d:\n\n", chosenReservation.getStartDay(), chosenReservation.getEndDay());
                        System.out.printf("[0] Yes\n");
                        System.out.printf("[1] No\n");
                        suboption = select(1);

                        if (suboption == -1) {
                            if (chosenHotel.deleteReservation(chosenReservation.getRoomName(), chosenReservation.getStartDay(), chosenReservation.getEndDay())) {
                                hotelReservations.remove(chosenReservation);
                                counter--;
                                viewLevel = 1;
                                System.out.printf("Reservation removal successful\n");

                                if (chosenHotel.getTotalReservationCount() == 0) {
                                    viewLevel--;
                                }
                            } else {
                                viewLevel = 1;
                                System.out.printf("Error in reservation removal\n");
                            }
                            System.out.printf("Press any key to continue...\n");
                            sc.nextLine();
                        } else {
                            viewLevel=0;
                            System.out.printf("Reservation removal cancelled\n");
                        }
                    }
                }
            }
        }
    }
    
    /**
    * Shows all the menus needed to manage all the hotels
    * and contains all the calls to necessary functions
    */
    public void manageHotels() {
        // viewLevel is for while menuing
        int viewLevel = 1;
        Hotel currentHotel = null;
        int hotelCount = hotels.size();
        int option = 0;

        // Choose hotel
        while (viewLevel >= 1) {
            System.out.printf("Which hotel would you like to manage?\n");
            for (int i = 0; i < hotelCount; i++) {
                System.out.printf("[%d] %s \n", i + 1, hotels.get(i).getName());
            }
            System.out.printf("[0] Exit Management\n");
            option = select(hotels.size());

            if (option == -1) {
                viewLevel = 0;
            } else {
                // Function choices
                int suboption;
                viewLevel++;
                while (viewLevel >= 2) {
                    currentHotel = hotels.get(option);
                    System.out.printf("Current Hotel: %s\n", currentHotel.getName());
                    System.out.printf("Room Count: %d\n", currentHotel.getTotalRooms());
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

                    if (suboption == 0) {
                        changeName(currentHotel);
                    } else if (suboption == 1) {
                        addRooms(currentHotel);
                    } else if (suboption == 2) {
                        removeRoom(currentHotel);
                    } else if (suboption == 3) {
                        modifyBasePrice(currentHotel);
                    } else if (suboption == 4) {
                        removeReservation(currentHotel);
                    } else if (suboption == 5) {
                        int suboption2;
                        System.out.printf("Are you sure you want to delete the hotel?\n");
                        System.out.printf("This cannot be undone.\n");
                        System.out.printf("[0] Yes\n");
                        System.out.printf("[1] No\n");
                        suboption2 = select(1);

                        if (suboption2 == -1) {
                            viewLevel = 0;
                            System.out.printf("Hotel has been removed.\n");
                            hotels.remove((currentHotel));
                            System.out.printf("Press any key to continue...\n");
                            sc.nextLine();
                        } else if (suboption2 == 0) {
                            System.out.printf("Hotel deletion cancelled.\n");
                        }
                    }
                }
            }
        }
    }
    /**
    * Displays the menu where the user is able to reserve rooms on certain days
    */
    public void reserveHotels() {
    // viewLevel is for while menuing
    int viewLevel = 1;
    Hotel currentHotel = null;
    int hotelCount = hotels.size();
    int option = 0;
    Room chosenRoom = null;

    // hotel selection
    while (viewLevel >= 1) {
        System.out.printf("Which hotel would you like make a reservation at?\n");
        for (int i = 0; i < hotelCount; i++) {
            System.out.printf("[%d] %s \n", i + 1, hotels.get(i).getName());
        }
        System.out.printf("[0] Exit Reserving\n");
        option = select(hotels.size());

        if (option == -1) {
            viewLevel = 0;
        }
        else {
            viewLevel++;
            while (viewLevel >= 2) {
                // Customer name
                String customerName = "";

                System.out.printf("What is your name, input a blank to go back: ");
                customerName = sc.nextLine();

                if (customerName.length() == 0) {
                    viewLevel--;
                }
                else {
                    viewLevel++;
                }

                // sets up chosen hotel
                while (viewLevel >= 3) {
                    currentHotel = hotels.get(option);
                    int suboption1;

                    if (currentHotel.getTotalRooms() == 0) {
                        System.out.printf("Sorry %s, there are no rooms to reserve\n", customerName);
                        System.out.printf("Press any key to continue\n");
                        sc.nextLine();
                        viewLevel = 0;
                        break;
                    }

                    // Shows list of rooms without status yet
                    System.out.printf("Which room would you like to reserve?\n");
                    currentHotel.displayRooms();
                    System.out.printf("[0] Back\n");

                    suboption1 = select(currentHotel.getTotalRooms());

                    if (suboption1 == -1) {
                        viewLevel--;
                    }
                    else {
                        viewLevel++;
                        chosenRoom = currentHotel.getRoom(suboption1);

                        // Room has been selected
                        while (viewLevel >= 4) {
                            // checkInDay and validation
                            int checkInDay;
                            int availabilityStatus;

                            // Prints room's reservation calendar
                            System.out.printf("Room %s's Reservation Calendar\n", chosenRoom.getName());
                            chosenRoom.displayCalendar();
                            System.out.printf("*##* - Day is fully booked\n");
                            System.out.printf("{##} - Day is partially booked\n");
                            System.out.printf("None - Day is fully available\n");
                            System.out.printf("[0] Back\n");

                            System.out.printf("Select a day to check-in: \n");
                            // Adds +1 since select has a -1
                            checkInDay = select(31) + 1;
                            availabilityStatus = chosenRoom.checkDayAvailability(checkInDay);

                            if (checkInDay == 0) {
                                viewLevel--;
                            }
                            else {
                                // No check-in on 31 allowed
                                if (checkInDay == 31) {
                                    System.out.printf("Cannot check-in on the 31st day, please pick another\n");
                                    System.out.printf("Press any key to continue\n");
                                    sc.nextLine();
                                    // taken days
                                }
                                else if (availabilityStatus == -1) {
                                    System.out.printf("Day is fully booked, please pick another\n");
                                    System.out.printf("Press any key to continue\n");
                                    sc.nextLine();
                                }
                                else {
                                    viewLevel++;
                                    // Fully available day
                                    if (availabilityStatus == 1) {
                                        System.out.printf("Day is fully available!\n");
                                    }
                                    else if (availabilityStatus == 0) {
                                        // Day is a start or end of stay
                                        System.out.printf("Day is partially available\n");
                                    }
                                    while (viewLevel >= 5) {
                                        int checkOutDay;
                                        int availabilityStatus2;

                                        System.out.printf("Select a day to check-out (after check-in day): \n");
                                        System.out.printf("[0] Back\n");
                                        checkOutDay = select(31) + 1;

                                        // Will check for day availability again but this time will catch in-betweens
                                        availabilityStatus2 = chosenRoom.checkDayAvailability2(checkInDay, checkOutDay);

                                        if (checkOutDay == 0) {
                                            viewLevel--;
                                        }
                                        else {
                                            if (checkOutDay <= checkInDay) {
                                                System.out.printf("Invalid day, check-out must be after check-in\n");
                                                System.out.printf("Press any key to continue\n");
                                                sc.nextLine();
                                            }
                                            else if (checkOutDay == 31) {
                                                // No check-out on 31 allowed
                                                System.out.printf("Cannot check-out on the 31st day, please pick another\n");
                                                System.out.printf("Press any key to continue\n");
                                                sc.nextLine();
                                            }
                                            else if (availabilityStatus2 == -1) {
                                                // This time checks ranges of other reservations
                                                System.out.printf("A prior reservation in that range, please pick another\n");
                                                System.out.printf("Press any key to continue\n");
                                                sc.nextLine();
                                            }
                                            else {
                                                viewLevel++;
                                                // Final look at selection
                                                System.out.printf("Booking dates chosen:\n");
                                                System.out.printf("Your check-in day is %d\n\n", checkInDay);
                                                System.out.printf("Your check-out day is %d\n\n", checkOutDay);

                                                // Shows range of dates to book
                                                System.out.printf("Room %s's Reservation Calendar\n", chosenRoom.getName());
                                                chosenRoom.displayCalendar2(checkInDay, checkOutDay);

                                                System.out.printf("Greetings %s, Are you sure you want to book these dates?\n", customerName);
                                                System.out.printf("It will cost you %.2f for %d %s in room %s\n",
                                                        (checkOutDay - checkInDay) * currentHotel.getBasePrice(),
                                                        checkOutDay - checkInDay,
                                                        "night(s)",
                                                        chosenRoom.getName());

                                                while (viewLevel >= 6) {
                                                    int suboption2;
                                                    System.out.printf("[0] Yes\n");
                                                    System.out.printf("[1] No\n");
                                                    suboption2 = select(1);

                                                    if (suboption2 == 0) {
                                                        viewLevel--;
                                                    }
                                                    else if (suboption2 == -1) {
                                                        // Reservation will now be created
                                                        Reservation newReservation = new Reservation(customerName,(checkOutDay - checkInDay) * currentHotel.getBasePrice(),checkInDay, checkOutDay, chosenRoom.getName());
                                                        chosenRoom.addReservation(newReservation);
                                                        System.out.printf("Reservation booked, thank you for choosing %s!\n", currentHotel.getName());
                                                        System.out.printf("Press any key to continue\n");
                                                        sc.nextLine();
                                                        viewLevel = 1;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
        }
    
    /**
    * Displays the menus that allows the user to view both high-
    * and low-level information about the hotels such as room count,
    * monthly earnings, room availability, and reservations for the month
    */
    public void viewHotels() {
        // hotel to be viewed
        Hotel currentHotel = null;

        // while menuing
        int viewLevel = 1;
        int option;
        int hotelCount = hotels.size();

        // Overview of all hotels
        for (Hotel hotel : hotels) {
            ArrayList<Room> bookedRooms = new ArrayList<>();

            for (Room room : hotel.getAllRooms()) {
                if (room.getReservations().size() > 0) {
                    bookedRooms.add(room);
                }
            }

            System.out.printf("Hotel name: %s\n", hotel.getName());
            System.out.printf("Hotel base price: %s\n", hotel.getBasePrice());
            System.out.printf("%d/%d rooms fully available\n", hotel.getTotalRooms() - bookedRooms.size(), hotel.getTotalRooms());

            // adds a room to the overview of a hotel

            // no reservations
            if (bookedRooms.size() == 0) {
                System.out.printf("     There are currently no reservations this month\n");
                System.out.printf("     Monthly earnings: 0.00\n\n");
            } else {
                // if reservations exist
                int reservationCount = 0;
                float monthlyEarnings = 0.0f;

                for (Room room : bookedRooms) {
                    for (Reservation reservation : room.getReservations()) {
                        reservationCount++;
                        monthlyEarnings += reservation.getCost();
                    }
                }
                System.out.printf("     There are currently %d reservations\n", reservationCount);
                System.out.printf("     Monthly earnings: %.2f\n\n", monthlyEarnings);
            }
        }

            // hotel selection
            while (viewLevel >= 1) {
                System.out.printf("Which hotel would you like to view?\n");
                for (int i = 0; i < hotelCount; i++) {
                    System.out.printf("[%d] %s \n", i + 1, hotels.get(i).getName());
                }
                System.out.printf("[0] Exit Viewing\n");
                option = select(hotels.size() + 1);

                if (option == -1) {
                    viewLevel = 0;
                } else {
                    viewLevel++;
                    while (viewLevel >= 2) {
                        currentHotel = hotels.get(option);
                        int suboption1;

                        System.out.printf("You chose to view hotel %s\n", currentHotel.getName());
                        System.out.printf("[1] Check available rooms by date\n");
                        System.out.printf("[2] Check room availability for the month\n");
                        System.out.printf("[3] View data by reservations\n");
                        System.out.printf("[0] Back\n");

                        suboption1 = select(3);

                        if (suboption1 == -1) {
                            viewLevel--;
                        } else if (currentHotel.getTotalRooms() == 0) {
                            // no hotels exist
                            viewLevel--;
                            System.out.printf("Cannot view data, there are currently 0 rooms\n");
                            System.out.printf("Press any key to continue\n");
                            sc.nextLine();
                        } else {
                            // hotels exist, viewing in proper
                            viewLevel++;
                            while (viewLevel >= 3) {
                                // for checking room availability on a certain day
                                if (suboption1 == 0) {
                                    int chosenDate = 0;
                                    int availableRooms = currentHotel.getTotalRooms();
                                    System.out.printf("Select a day from 1 to 31\n");
                                    System.out.printf("[0] Back\n");

                                    chosenDate = select(31) + 1;

                                    if (chosenDate == 0) {
                                        viewLevel--;
                                    } else {
                                        // Will use getReservedRoomsByDay
                                        ArrayList<Room> reservedRooms = currentHotel.getReservedRoomsByDay(chosenDate);

                                        if (reservedRooms.size() > 0) {
                                            System.out.printf("On day %d, %d/%d rooms available\n", chosenDate, availableRooms-reservedRooms.size(), availableRooms);
                                            // Prints reserved room names
                                            System.out.printf("%d rooms reserved:\n", reservedRooms.size());
                                            for (Room rooms : reservedRooms) {
                                                System.out.printf("Room %s\n", rooms.getName());
                                            }
                                        } else {
                                            System.out.printf("On day %d, no reservations found, %d/%d rooms available\n", chosenDate, availableRooms, availableRooms);
                                        }
                                    }
                                // For checking the calendar availability of a specific room
                                } else if (suboption1 == 1) {
                                    // room to be chosen
                                    int chosenRoomID = 0;
                                    Room chosenRoom = null;

                                    currentHotel.displayRooms();
                                    System.out.printf("Which room would you like to view?\n");
                                    System.out.printf("[0] Back\n");

                                    chosenRoomID = select(currentHotel.getTotalRooms());

                                    if (chosenRoomID == -1) {
                                        viewLevel--;
                                    } else {
                                        chosenRoom = currentHotel.getRoom(chosenRoomID);
                                        int reservationCount = chosenRoom.getReservationTimelineLength();
                                        chosenRoom = currentHotel.getRoom(chosenRoomID);

                                        // Room overview
                                        System.out.printf("You chose room %s\n", chosenRoom.getName());
                                        System.out.printf("Price per night: %.2f\n", currentHotel.getBasePrice());
                                        System.out.printf("Earnings this month: %.2f\n", chosenRoom.getMonthlyEarnings());
                                        if (reservationCount == 0) {
                                            System.out.printf("Room has 0 reservations and is fully free this month\n", chosenRoom.getName());
                                        } else {
                                            // show the calendar availability
                                            chosenRoom.displayCalendar();
                                            System.out.printf("*##* - Day is fully booked\n");
                                            System.out.printf("{##} - Day is partially booked\n");
                                            System.out.printf("None - Day is fully available\n");
                                            System.out.printf("There are %d reservations this month:\n", reservationCount);

                                            // displays the reservation data
                                            chosenRoom.displayReservations();
                                        }
                                        System.out.printf("Press any key to continue\n");
                                        sc.nextLine();
                                    }
                                // For viewing based only on reservations
                                } else if (suboption1 == 2) {
                                    ArrayList<Room> reservedRooms = currentHotel.getAllReservedRooms();

                                    if (reservedRooms.size() == 0) {
                                        System.out.printf("There are no reservations for hotel %s\n", currentHotel.getName());
                                    } else {
                                        System.out.printf("There are a total of %d reservations for hotel %s\n", currentHotel.getTotalReservationCount(), currentHotel.getName());

                                        // Shows reservation for only booked rooms
                                        for (Room room : reservedRooms) {
                                            System.out.printf("In Room %s:\n", room.getName());
                                            room.displayReservations();
                                        }
                                    }
                                    viewLevel--;
                                    System.out.printf("Press any key to continue\n");
                                    sc.nextLine();
                                }
                            }
                        }
                    }
                }
            }
    }

    public boolean createHotel2(String newHotelName, int newRoomCount) {
        if (checkName(newHotelName) && checkRoomCount(newRoomCount) == 1) {
            hotels.add(new Hotel(newHotelName, 1299.0f, newRoomCount));
//            for (int i = 0; i < 50; i++) {
//                hotels.add(new Hotel("hotel", 1299.0f, i));
//            }
            return true;
        }
        return false;
    }

    public void changeName2(Hotel chosenHotel, String newHotelName) {
        chosenHotel.setName(newHotelName);
    }

    public void addRooms2(Hotel chosenHotel, int newAdds) {
        for (int i = 0; i < newAdds; i++) {
            chosenHotel.addRoom();
        }
    }

    public void modifyBasePrice2(Hotel chosenHotel, float newPrice) {
        chosenHotel.setBasePrice(newPrice);
    }
}



















































// old version of reserveHotels with check in hours
/*
    public void reserveHotels() {
        // viewLevel is for while menuing
        int viewLevel = 1;
        Hotel currentHotel = null;
        int hotelCount = hotels.size();
        int option = 0;
        Room chosenRoom = null;

        // hotel selection
        while (viewLevel >= 1) {
            System.out.printf("Which hotel would you like make a reservation at?\n");
            for (int i = 0; i < hotelCount; i++) {
                System.out.printf("[%d] %s \n", i + 1, hotels.get(i).getName());
            }
            System.out.printf("[0] Exit Reserving\n");
            option = select(hotels.size()+1);

            if (option == -1) {
                viewLevel = 0;
            } else {
                viewLevel++;
                // sets up chosen hotel
                while (viewLevel >= 2) {
                    currentHotel = hotels.get(option);
                    int suboption1;

                    // Shows list of rooms without status yet
                    System.out.printf("Which room would you like to reserve?\n");
                    currentHotel.displayRooms();
                    System.out.printf("[0] Back\n");

                    suboption1 = select(currentHotel.getTotalRooms());

                    if (suboption1 == -1) {
                        viewLevel--;
                    } else {
                        viewLevel++;
                        chosenRoom = currentHotel.getRoom(suboption1);

                        // Room has been selected
                        while (viewLevel >= 3) {
                            String customerName = "";
                            // checkInDay and validation
                            int checkInDay;
                            int availabilityStatus;
                            boolean customerValidate = true;

                            while (customerValidate) {
                                System.out.printf("What is your name: ");
                                customerName = sc.nextLine();

                                if (customerName.length()==0) {
                                    System.out.printf("Invalid name, please try again \n");
                                } else {
                                    customerValidate = false;
                                }
                            }

                            // Prints room's reservation calendar
                            System.out.printf("Room %s's Reservation Calendar\n", chosenRoom.getName());
                            chosenRoom.displayCalendar();
                            System.out.printf("*##* - Day is fully booked\n");
                            System.out.printf("{##} - Day is partially booked\n");
                            System.out.printf("None - Day is fully available\n");
                            System.out.printf("[0] Back\n\n");

                            System.out.printf("Select a day to check-in: \n");
                            // Adds +1 since select has a -1
                            checkInDay = select(31) + 1;
                            availabilityStatus = chosenRoom.checkDayAvailability(checkInDay);

                            if (checkInDay == 0) {
                                viewLevel--;
                            } else {
                                // No check-in on 31 allowed
                                if (checkInDay == 31) {
                                    System.out.printf("Cannot check-in on the 31st day, please pick another\n");
                                    System.out.printf("Press any key to continue");
                                    sc.nextLine();
                                // taken days
                                } else if (availabilityStatus == -1) {
                                    System.out.printf("Day is fully booked, please pick another\n");
                                    System.out.printf("Press any key to continue");
                                } else {
                                    viewLevel++;
                                    //Fully available day
                                    if (availabilityStatus == 1) {
                                        System.out.printf("Day is fully available!\n");
                                    } else if (availabilityStatus == 0) {
                                        // Day is a start or end of stay
                                        System.out.printf("Day is partially available, here are the occupied hours:\n");
                                        // Shows hour ranges of other reservations
                                        chosenRoom.displayOccupiedHours(checkInDay);
                                    }
                                    while (viewLevel >= 4) {
                                        // For picking check-in hour
                                        int checkInHr;

                                        System.out.printf("Select the check-in hour (1-24)\n");
                                        System.out.printf("[0] Back\n");
                                        checkInHr = select(24)+1;

                                        if (checkInHr == 0) {
                                            viewLevel--;
                                        // Invalid hours
                                        } else if (checkInHr < 0 || checkInHr > 24) {
                                            System.out.printf("Invalid hour, please select again\n");
                                            // Checks hour ranges
                                        } else if (!chosenRoom.checkHourAvailability(checkInDay, checkInHr)) {
                                            System.out.printf("Chosen hours are occupied, please select again\n");
                                        } else {
                                            viewLevel++;
                                            // All check-in times valid
                                            System.out.printf("Your check-in day is %d and your check-in hour is %d\n\n", checkInDay, checkInHr);

                                            // Will show calendar with <##> for only their currently chosen check-in day
                                            System.out.printf("Room %s's Reservation Calendar\n", chosenRoom.getName());
                                            chosenRoom.displayCalendar2(checkInDay, checkInDay);

                                            while (viewLevel >= 5) {
                                                int checkOutDay;
                                                int availabilityStatus2;

                                                System.out.printf("Now selecting check-out day.\n");
                                                System.out.printf("[0] Back\n\n");
                                                checkOutDay = select(31) + 1;

                                                // Will check for day availability again but this time will catch in-betweens
                                                availabilityStatus2 = chosenRoom.checkDayAvailability2(checkInDay, checkOutDay);

                                                if (checkOutDay == 0) {
                                                    viewLevel--;
                                                } else {
                                                    if (checkOutDay < checkInDay) {
                                                        System.out.printf("Invalid day, booking is only limited to 31 days\n");
                                                        System.out.printf("Press any key to continue");
                                                        sc.nextLine();
                                                    } else if (checkOutDay == 1) {
                                                        // No check-out in first day allowed
                                                        System.out.printf("Cannot check-out on the 1st day, please pick another\n");
                                                        System.out.printf("Press any key to continue");
                                                        sc.nextLine();
                                                    } else if (availabilityStatus2 == -1) {
                                                        // This time checks ranges af other reservations
                                                        System.out.printf("A prior reservation in that range, please pick another\n");
                                                        System.out.printf("Press any key to continue");
                                                    } else {
                                                        viewLevel++;
                                                        if (availabilityStatus2 == 1) {
                                                            System.out.printf("Day is fully available!\n");
                                                        } else if (chosenRoom.checkDayAvailability2(checkInDay, checkOutDay) == 0) {
                                                            System.out.printf("Day is partially available, here are the occupied hours:\n");
                                                            chosenRoom.displayOccupiedHours(checkOutDay);
                                                        }
                                                        while (viewLevel >= 6) {
                                                            int checkOutHr;

                                                            System.out.printf("Select the check-out hour (1-24)\n");
                                                            System.out.printf("[0] Back\n");
                                                            checkOutHr = select(24)+1;

                                                            if (checkOutHr == -1) {
                                                                viewLevel--;
                                                            } else if (checkOutHr <=0 || checkOutHr > 24) {
                                                                System.out.printf("Invalid hour, please select again\n");
                                                            } else if (checkInDay == checkOutDay && checkInHr >= checkOutHr) {
                                                                System.out.printf("Hour goes to the past, please select again\n");
                                                            } else if (!chosenRoom.checkHourAvailability(checkOutDay, checkOutHr)) {
                                                                System.out.printf("Chosen hours are occupied, please select again\n");
                                                            } else {
                                                                viewLevel++;

                                                                // Final look at selection
                                                                System.out.printf("Booking hours chosen:\n");
                                                                System.out.printf("Your check-in day is %d and your check-in hour is %d\n\n", checkInDay, checkInHr);
                                                                System.out.printf("Your check-out day is %d and your check-out hour is %d\n\n", checkOutDay, checkOutHr);

                                                                // Shows range of dates to book
                                                                System.out.printf("Room %s's Reservation Calendar\n", chosenRoom.getName());
                                                                chosenRoom.displayCalendar2(checkInDay, checkOutDay);

                                                                System.out.printf("Greetings %s, Are you sure you want to book these dates?\n", customerName);
                                                                System.out.printf("It will cost you %.2f for %d %s in room %s\n", checkInDay != checkOutDay ? (checkOutDay-checkInDay) * currentHotel.getBasePrice() : currentHotel.getBasePrice(),
                                                                                                                                  checkInDay != checkOutDay ? checkOutDay - checkInDay : 1,
                                                                                                                                  checkInDay != checkOutDay ? "night/s" : "day",
                                                                                                                                  chosenRoom.getName());

                                                                while (viewLevel >= 7) {
                                                                    int suboption2;
                                                                    System.out.printf("[0] Yes\n");
                                                                    System.out.printf("[1] No\n");
                                                                    suboption2 = select(1);

                                                                    if (suboption2 == 0) {
                                                                        viewLevel--;
                                                                    } else if (suboption2 == -1) {
                                                                        // Reservation will now be created
                                                                        Reservation newReservation = new Reservation(customerName, checkInDay != checkOutDay ? (checkOutDay-checkInDay) * currentHotel.getBasePrice() : currentHotel.getBasePrice(), checkInDay,checkInHr, checkOutDay, checkOutHr, chosenRoom.getName());
                                                                        chosenRoom.addReservation(newReservation);
                                                                        System.out.printf("Reservation booked, thank you for choosing %s!\n", currentHotel.getName());
                                                                        System.out.printf("Press any key to continue");
                                                                        sc.nextLine();
                                                                        viewLevel=1;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
* */