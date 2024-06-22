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
    private Scanner sc = new Scanner(System.in);

    // Initializers
    private ArrayList<Hotel> hotels = new ArrayList<>();
    HotelManager(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
    }

    public void printHotels() {
        for (int i = 0; i < hotels.size(); i++)
            System.out.printf("%s\n", hotels.get(i).getName());
    }

    /* checkName - For finding a previously existing name
       @param String givenName - Name to be checked
       @return boolean - whether a name was found
       Precondition: None
    */
    public boolean checkName(String givenName) {
        int hotelCount = hotels.size();
            for (int i = 0; i < hotelCount; i++) {
                if (hotels.get(i).getName().equals(givenName)){
                    return false;
                }
        }
        return true;
    }

    /* checkRoomCount - For limiting to 50 rooms, returns different ints for different errors, used for hotel creation
        @param int givenRoomCount - Number to validate
        @return int - Error code
        Precondition: None
    */
    public int checkRoomCount(int givenRoomCount){
        if (givenRoomCount < 1){
            return -1;
        } else if (givenRoomCount > 50){
            return 0;
        }
        return 1;
    }

    /* createHotel - For creating a hotel, contains all the processes
        @param none
        @return none - Adds hotel to the AL
        Precondition: None
    */
    public void createHotel(){
        // Validations for while loops
        int viewLevel = 1;

        int hotelCount = hotels.size();

        // Defaults
        String newHotelName = "";
        float newBasePrice = 1299.0f;
        int newRoomCount = 1;

        System.out.printf("There are currently: ");
        if (hotelCount == 0) {
            System.out.printf("no hotels.\n");
        } else if (hotelCount == 1) {
            System.out.printf("1 hotel.\n");
        } else {
            System.out.printf("%d hotels.\n", hotelCount);
        }

        // Name selection
        while (viewLevel == 1) {
            System.out.printf("Enter the name of the new hotel, input blank to go back: ");
            newHotelName = sc.nextLine();

            if (newHotelName.equals("")){
                viewLevel = 0;
            } else {
                if (!checkName(newHotelName)){
                    System.out.printf("Hotel name has been taken, please pick a new one\n");
                } else {
                    viewLevel++;
                    // Room counting
                    while (viewLevel == 2) {
                        System.out.printf("Enter how many rooms the hotel will have (Max of 50), 0 to go back: ");
                        newRoomCount = sc.nextInt();
                        sc.nextLine();

                        if (newRoomCount == 0){
                            viewLevel--;
                        } else if (checkRoomCount(newRoomCount) == -1){
                            System.out.printf("Invalid choice, room count must be positive\n");
                        } else if (checkRoomCount(newRoomCount) == 0){
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

                                if (option == -1){
                                    // adds hotel at the end
                                    hotels.add(new Hotel(newHotelName, 1299.0f, newRoomCount));
                                    viewLevel=0;
                                    System.out.printf("Hotel successfully added\n");

                                    System.out.printf("Press any key to continue");
                                    sc.nextLine();

                                } else if (option == 0){
                                    viewLevel--;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* select - Creates a quick number-1 which results in a range of -1 to max-1 with some validation
                Added here too to not have to make a static in DisplayManager then .select
       @param int max - highest possible integer input
       @return - Returns an input integer - 1
       Precondition: None
    */
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

    /* changeName - Changes hotel name, contains validation
       @param Hotel chosenHotel - Hotel to be modified
       @return - none, modified hotel AL
       Precondition: a hotel exists
    */
    public void changeName(Hotel chosenHotel) {
        String newHotelName;
        boolean validateName = true;

        while (validateName) {
            System.out.printf("Enter the name of the new hotel: ");
            newHotelName = sc.nextLine();
            int option;

            // Does name exist
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

    /* addRooms - Adds a room to the room AL, contains all validations
       @param Hotel chosenHotel - hotel to modify
       @return - none, adds a new room
       Precondition: None
    */
    public void addRooms(Hotel chosenHotel){
        int additions;
        boolean validateAdd = true;
        int option;

        // Shows rooms
        System.out.printf("Room Count: %d\n", chosenHotel.getTotalRooms());
        System.out.printf("Current Rooms\n");
        chosenHotel.displayRooms();

        while (validateAdd){
            System.out.printf("How many rooms would you like to add (0 to cancel): ");
            additions = sc.nextInt();

            if (additions == 0){
                validateAdd = false;
                System.out.printf("Room adding cancelled.\n");
                // Room validation
            } else if (chosenHotel.getTotalRooms() + additions > 50){
                System.out.printf("Maximum rooms of 50 rooms exceeded, please try again\n");
            } else {
                System.out.printf("Are you sure you want to add %d rooms?\n", additions);
                System.out.printf("[0] Yes\n");
                System.out.printf("[1] No\n");
                option = select(1);

                if (option == -1){
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
        System.out.printf("Press any key to continue");
        sc.nextLine();
    }

    /* removeRoom - Sets up room deletions
       @param Hotel chosenHotel - hotel to modify
       @return - none, sets up for deleteRoom call
       Precondition: None
    */
    public void removeRoom(Hotel chosenHotel){
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

        if (input.equals("QUIT")){
            System.out.printf("Cancelled deletion\n");
        } else {
            toDelete = input.split(" ");
            System.out.printf("Are you sure you want to delete the following: \n");

            // Prints all ids to delete
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

            // Calls deletion with deleteRooms
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

    /* modifyBasePrice - To change base price to a minimum of 100.00f
       @param Hotel chosenHotel - hotel to modify
       @return - none, changes the base price
       Precondition: None
    */
    public void modifyBasePrice(Hotel chosenHotel){
        boolean editable = chosenHotel.checkReservedRooms() != 0;
        float newPrice = 0.0f;
        boolean validatePrice = true;
        int option;

        // Cancelled by a reservation
        if (editable){
            System.out.printf("You cannot change the base price because there is an affected reservation\n");
            System.out.printf("Press any key to continue...\n");
            sc.nextLine();
        } else {
            // Validations
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

    /* manageHotels - Holds menu and calls to each function
       @param none
       @return - none, shows menu and calls change functions
       Precondition: None
    */
    public void manageHotels(){
        // viewLevel is for while menuing
        int viewLevel = 1;
        Hotel currentHotel = null;
        int hotelCount = hotels.size();
        int option = 0;

        // Choose hotel
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
                // Function choices
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
                            viewLevel = 0;
                            System.out.printf("Hotel has been removed.\n");
                            hotels.remove((currentHotel));
                            System.out.printf("Press any key to continue...\n");
                            sc.nextLine();
                        } else if (option == 0){
                            System.out.printf("Hotel deletion cancelled.\n");
                        }
                    }
                    }
                }
            }
        }

/* reserveHotels - To choose a room within a hotel to reserve and at which timelines
                   currently only goes as deep as day and hour options
   @param none
   @return - none, adds a Reservation to a Room
   Precondition: a Room exists
*/
    public void reserveHotels(){
        // viewLevel is for while menuing
        int viewLevel = 1;
        Hotel currentHotel = null;
        int hotelCount = hotels.size();
        int option = 0;
        Room chosenRoom = null;

        // hotel selection
        while (viewLevel >= 1){
            System.out.printf("Which hotel would you like make a reservation at?\n");
            for (int i = 0; i < hotelCount; i++) {
                System.out.printf("[%d] %s \n", i + 1, hotels.get(i).getName());
            }
            System.out.printf("[0] Exit Reserving\n");
            option = select(hotels.size()+1);

            if (option == -1){
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

                    if (suboption1 == -1){
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

                                if (customerName.length()==0){
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

                            if (checkInDay == 0){
                                viewLevel--;
                            } else {
                                // No check-in on 31 allowed
                                if (checkInDay == 31){
                                    System.out.printf("Cannot check-in on the 31st day, please pick another\n");
                                    System.out.printf("Press any key to continue");
                                    sc.nextLine();
                                // taken days
                                } else if (availabilityStatus == -1){
                                    System.out.printf("Day is fully booked, please pick another\n");
                                    System.out.printf("Press any key to continue");
                                } else {
                                    viewLevel++;
                                    //Fully available day
                                    if (availabilityStatus == 1){
                                        System.out.printf("Day is fully available!\n");
                                    } else if (availabilityStatus == 0){
                                        // Day is a start or end of stay
                                        System.out.printf("Day is partially available, here are the occupied hours:\n");
                                        // Shows hour ranges of other reservations
                                        chosenRoom.displayOccupiedHours(checkInDay);
                                    }
                                    while (viewLevel >= 4){
                                        // For picking check-in hour
                                        int checkInHr;

                                        System.out.printf("Select the check-in hour (1-24)\n");
                                        System.out.printf("[0] Back\n");
                                        checkInHr = select(24)+1;

                                        if (checkInHr == 0){
                                            viewLevel--;
                                        // Invalid hours
                                        } else if (checkInHr < 0 || checkInHr > 24) {
                                            System.out.printf("Invalid hour, please select again\n");
                                            // Checks hour ranges
                                        } else if (!chosenRoom.checkHourAvailability(checkInDay, checkInHr)){
                                            System.out.printf("Chosen hours are occupied, please select again\n");
                                        } else {
                                            viewLevel++;
                                            // All check-in times valid
                                            System.out.printf("Your check-in day is %d and your check-in hour is %d\n\n", checkInDay, checkInHr);

                                            // Will show calendar with <##> for only their currently chosen check-in day
                                            System.out.printf("Room %s's Reservation Calendar\n", chosenRoom.getName());
                                            chosenRoom.displayCalendar2(checkInDay, checkInDay);

                                            while (viewLevel >= 5){
                                                int checkOutDay;
                                                int availabilityStatus2;

                                                System.out.printf("Now selecting check-out day.\n");
                                                System.out.printf("[0] Back\n\n");
                                                checkOutDay = select(31) + 1;

                                                // Will check for day availability again but this time will catch in-betweens
                                                availabilityStatus2 = chosenRoom.checkDayAvailability2(checkInDay, checkOutDay);

                                                if (checkOutDay == 0){
                                                    viewLevel--;
                                                } else {
                                                    if (checkOutDay < checkInDay){
                                                        System.out.printf("Invalid day, booking is only limited to 31 days\n");
                                                        System.out.printf("Press any key to continue");
                                                        sc.nextLine();
                                                    } else if (checkOutDay == 1){
                                                        // No check-out in first day allowed
                                                        System.out.printf("Cannot check-out on the 1st day, please pick another\n");
                                                        System.out.printf("Press any key to continue");
                                                        sc.nextLine();
                                                    } else if (availabilityStatus2 == -1){
                                                        // This time checks ranges af other reservations
                                                        System.out.printf("A prior reservation in that range, please pick another\n");
                                                        System.out.printf("Press any key to continue");
                                                    } else {
                                                        viewLevel++;
                                                        if (availabilityStatus2 == 1){
                                                            System.out.printf("Day is fully available!\n");
                                                        } else if (chosenRoom.checkDayAvailability2(checkInDay, checkOutDay) == 0){
                                                            System.out.printf("Day is partially available, here are the occupied hours:\n");
                                                            chosenRoom.displayOccupiedHours(checkOutDay);
                                                        }
                                                        while (viewLevel >= 6){
                                                            int checkOutHr;

                                                            System.out.printf("Select the check-out hour (1-24)\n");
                                                            System.out.printf("[0] Back\n");
                                                            checkOutHr = select(24)+1;

                                                            if (checkOutHr == -1){
                                                                viewLevel--;
                                                            } else if (checkOutHr <=0 || checkOutHr > 24) {
                                                                System.out.printf("Invalid hour, please select again\n");
                                                            } else if (checkInDay == checkOutDay && checkInHr >= checkOutHr){
                                                                System.out.printf("Hour goes to the past, please select again\n");
                                                            } else if (!chosenRoom.checkHourAvailability(checkOutDay, checkOutHr)){
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
                                                                System.out.printf("It will cost you %.2f for %d day/s in room %s\n", (checkOutDay-checkInDay+1) * currentHotel.getBasePrice(), checkOutDay-checkInDay+1, chosenRoom.getName());

                                                                while (viewLevel >= 7){
                                                                    int suboption2;
                                                                    System.out.printf("[0] Yes\n");
                                                                    System.out.printf("[1] No\n");
                                                                    suboption2 = select(1);

                                                                    if (suboption2 == 0){
                                                                        viewLevel--;
                                                                    } else if (suboption2 == -1){
                                                                        // Reservation will now be created
                                                                        Reservation newReservation = new Reservation(customerName, (checkOutDay-checkInDay+1) * currentHotel.getBasePrice(), checkInDay,checkInHr, checkOutDay, checkOutHr);
                                                                        chosenRoom.addReservation(newReservation);
                                                                        System.out.printf("Reservation booked, thank you for choosing %s!\n", currentHotel.getName());
                                                                        System.out.printf("Press any key to continue");
                                                                        sc.nextLine();
                                                                        viewLevel=0;
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

    public void viewHotels(){
        for (Hotel hotel : hotels){
            ArrayList<Room> bookedRooms = new ArrayList<>();
            System.out.printf("Hotel name: %s\n", hotel.getName());
            System.out.printf("Hotel base price: %s\n", hotel.getBasePrice());
            System.out.printf("%d/%d rooms available\n",  hotel.getTotalRooms() - hotel.checkReservedRooms() ,hotel.getTotalRooms());

            for (Room room : hotel.getAllRooms()){
                if (room.getReservations().size() > 0){
                    bookedRooms.add(room);
                }
            }

            if (bookedRooms.size() == 0){
                System.out.printf("     There are currently no reservations this month\n");
                System.out.printf("     Monthly earnings: 0.00\n");
            } else {
                int reservationCount = 0;
                float monthlyEarnings = 0.0f;

                for (Room room : bookedRooms){
                    for (Reservation reservation : room.getReservations()){
                        reservationCount++;
                        monthlyEarnings += reservation.getCost();
                    }
                }
                System.out.printf("     There are currently %d reservations\n", reservationCount);
                System.out.printf("     Monthly earnings: %f\n\n", monthlyEarnings);

                System.out.printf("     Booked Rooms:");
                for (Room room : bookedRooms){
                    System.out.printf("         Room %s:\n", room.getName());
                }
            }
        }
    }
}
