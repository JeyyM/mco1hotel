package mco2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class has all the pop-ups that are displayed
 * from clicking the different buttons in the program.
 * Pop-ups can be used to display information and
 * ask for confirmation, or ask the user for information.
 * Since all the functions are static and are called using
 * the class itself, there is no constructor for this class.
 */
public class Modals {
    /**
     * This shows the dialog for creating a hotel including
     * all the checks and validations needed
     * @param parent            JFrame where the dialog will be shown on
     * @param model             stores the function needed to create a hotel
     * @param updateHotelCount  function that updates the number of hotels shown in the GUI
     * @param updateHotels      function that updates the actual list of hotels
     */
    public static void showCreateHotelDialog(JFrame parent, MVC_Model model, Runnable updateHotelCount, Runnable updateHotels) {
        JDialog modal = new JDialog(parent, "Create Hotel", true);
        modal.setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputArea = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nameEntry = new JTextField(20);
        JTextField roomCountEntry = new JTextField(20);
        JButton createButton = new JButton("Finish Creation");

        inputArea.add(new JLabel("Hotel Name:"));
        inputArea.add(nameEntry);
        inputArea.add(new JLabel("Room Count (Set to Base Rate):"));
        inputArea.add(roomCountEntry);
        inputArea.add(new JLabel());
        inputArea.add(createButton);

        createButton.addActionListener(e -> {
            int roomCount;
            String newName = nameEntry.getText();
            String roomCountText = roomCountEntry.getText();

            // Validations
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(modal, "Hotel name cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (model.isNameTaken(newName)) {
                JOptionPane.showMessageDialog(modal, "Hotel name has been taken. Please choose another name.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Valid integer string
            try {
                roomCount = Integer.parseInt(roomCountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal, "Room count must be a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!model.isValidRoomCount(roomCount)) {
                JOptionPane.showMessageDialog(modal, "Invalid room count. Must be between 1 and 50.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            model.createHotel(newName, roomCount);
            updateHotelCount.run();
            modal.dispose();
            JOptionPane.showMessageDialog(parent, "Hotel successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateHotels.run();
        });

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }

    /**
     * Dialog that shows the viewing options for a hotel once
     * a hotel is selected for viewing.
     * @param parent                    JFrame where the dialog will be shown on
     * @param controller                stores the functions to switch to a different GUI panel after an option is selected
     * @param chosenHotel               chosen hotel being viewed currently
     */
    public static void showViewOptions(JFrame parent, MVC_Controller controller, Hotel chosenHotel) {
        JDialog modal = new JDialog(parent, "Viewing Options", true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton dateButton = new JButton("Check Availability by Date");
        JButton roomButton = new JButton("Check Availability by Room");
        JButton reservationButton = new JButton("View by Reservation");

        dateButton.setPreferredSize(new Dimension(300, 30));
        roomButton.setPreferredSize(new Dimension(300, 30));
        reservationButton.setPreferredSize(new Dimension(300, 30));

        dateButton.addActionListener(e -> {
            controller.switchToViewRoomsByDate(chosenHotel);
            modal.dispose();
        });

        roomButton.addActionListener(e -> {
            controller.selectRoomByAvailability(chosenHotel);
            modal.dispose();
        });

        reservationButton.addActionListener(e -> {
            controller.switchToViewReservations(chosenHotel);
            modal.dispose();
        });

        inputArea.add(dateButton);
        inputArea.add(roomButton);
        inputArea.add(reservationButton);

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }
    
    /**
     * Dialog showing all the rooms reserved on a hotel
     * on a specified date.
     * @param parent        JFrame where the dialog will be shown on
     * @param chosenHotel   chosen hotel being viewed currently
     * @param day           day chosen to be checked
     */
    public static void showReservedRoomsByDate(JFrame parent, Hotel chosenHotel, int day) {
        String title = "Reserved Rooms on Day " + day;
        ArrayList<Room> rooms = chosenHotel.getReservedRoomsByDay(day);
        StringBuilder message;
        
        if (rooms.isEmpty()) {
            message = new StringBuilder("There are no rooms reserved on day " + day + ".\n");
        }
        else {
            message = new StringBuilder("These rooms are reserved on day " + day + ".\n");
            for (Room room : rooms) {
                message.append(room.getName()).append(" ");
            }
        }
        
        JOptionPane.showMessageDialog(parent, message.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows the dialog for showing the name of the customer
     * who reserved a room on a certain date chosen from
     * the room's reservation calendar.
     * @param parent    JFrame where the dialog will be shown on
     * @param room      room that had its reservation calendar displayed
     * @param day       day selected from the room's reservation calendar
     */
    public static void showReservedRoomsByRoom(JFrame parent, Room room, int day) {
        String title = "Reservations on Day " + day;
        StringBuilder message = new StringBuilder();
        boolean isReserved = false;
        
        ArrayList<Reservation> reservations = room.getReservations();
        
        for (Reservation reservation : reservations) {
            if (reservation.getDayRange().contains(day)) {
                message = new StringBuilder("" + reservation.getCustomerName() + " reserved " + room.getName() + " on day " + day + ".\n");
                isReserved = true;
            }
        }
        
        if (!isReserved) {
            message = new StringBuilder("There are no reservations on day " + day + ".\n");
        }
        
        JOptionPane.showMessageDialog(parent, message.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows the dialog for displaying the information when
     * a reservation is selected from all the reservations in
     * the hotel. Shows all the information about the reservation
     * and the room being reserved.
     * @param parent            JFrame where the dialog will be shown on
     * @param reservation       the actual reservation containing all the details to be shown
     * @param roomMultiplier    the multiplier for the rate of the room used to determine the type of the room
     */
    public static void showReservationDetails(JFrame parent, Reservation reservation, float roomMultiplier) {
        String title = "Reservation by " + reservation.getCustomerName();
        String roomType;
        
        if (roomMultiplier == 1.2f) {
            roomType = "Deluxe";
        }
        else if (roomMultiplier == 1.35f) {
            roomType = "Executive";
        }
        else {
            roomType = "Regular";
        }
        
        String message = String.format("Reservation by: %s\nRoom Reserved: %s\nRoom Type %s\nCheck In Day: %d\nCheck Out Day: %d\nCost: %.2f", reservation.getCustomerName(), reservation.getRoomName(), roomType, reservation.getStartDay(), reservation.getEndDay(), reservation.getCost());
        
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This shows the dialog for renaming a hotel including
     * all the checks and validations needed
     * @param parent                    JFrame where the dialog will be shown on
     * @param model                     stores the function needed to rename a hotel
     * @param chosenHotel               hotel chosen to be renamed
     * @param updateSpecificHotelPanel  function that is used to update the name of the hotel in the manage hotel panel
     * @param updateHotels              function that updates the actual list of hotels
     */
    public static void showRenameHotelDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, Runnable updateSpecificHotelPanel, Runnable updateHotels) {
        JDialog modal = new JDialog(parent, "Rename Hotel", true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameEntry = new JTextField(20);
        JButton createButton = new JButton("Finish Rename");

        inputArea.add(new JLabel("Hotel Name:"));
        inputArea.add(nameEntry);
        // put a blank label to keep that area clear
        inputArea.add(new JLabel());
        inputArea.add(createButton);

        createButton.addActionListener(e -> {
            String newName = nameEntry.getText();

            // Validations
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(modal, "Hotel name cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (model.isNameTaken(newName)) {
                JOptionPane.showMessageDialog(modal, "Hotel name has been taken. Please choose another name.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            model.changeName(chosenHotel, newName);
            modal.dispose();
            JOptionPane.showMessageDialog(parent, "Hotel successfully renamed.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Update previous panels
            updateSpecificHotelPanel.run();
            updateHotels.run();
        });

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }

    /**
     * This shows the dialog for adding a room in
     * a specified hotel including all the checks
     * and validations needed
     * @param parent            JFrame where the dialog will be shown on
     * @param model             stores the function needed to add rooms in a hotel
     * @param chosenHotel       hotel chosen to have rooms added
     * @param updateHotels      function that updates the actual list of hotels and the information about the hotels
     * @param updateRoomsShown  function that refreshes the GUI to reflect the new number of rooms
     */
    public static void showAddRoomsDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, Runnable updateHotels, Runnable updateRoomsShown) {
        JDialog modal = new JDialog(parent, "Add Rooms", true);
        JComboBox<String> basePriceDropdown;

        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(3, 2, 5, 5));
        inputArea.add(new JLabel("Room Count:"));
        JTextField roomCountEntry = new JTextField(20);
        inputArea.add(roomCountEntry);

        inputArea.add(new JLabel("Room Rate:"));
        String[] roomTypes = {"Regular", "Deluxe", "Executive"};
        basePriceDropdown = new JComboBox<>(roomTypes);
        inputArea.add(basePriceDropdown);

        inputArea.add(new JLabel()); // Placeholder to align the button
        JButton addButton = new JButton("Finish Adding");
        inputArea.add(addButton);

        addButton.addActionListener(e -> {
            int roomCount;
            String roomCountText = roomCountEntry.getText();

            // Valid integer string
            try {
                roomCount = Integer.parseInt(roomCountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal, "Room count must be a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!model.isValidRoomCount(roomCount + chosenHotel.getTotalRooms())) {
                JOptionPane.showMessageDialog(modal, "Invalid room count. Room count must be between 1 and 50.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // getting dropdown item
            String selectedType = (String) basePriceDropdown.getSelectedItem();

            float baseRate;
            switch (selectedType) {
                case "Deluxe":
                    baseRate = 1.2f;
                    break;
                case "Executive":
                    baseRate = 1.35f;
                    break;
                default:
                    baseRate = 1.0f;
                    break;
            }

            // Success: add rooms with the selected base rate
            model.addRooms(chosenHotel, roomCount, baseRate);
            updateRoomsShown.run();
            modal.dispose();
            JOptionPane.showMessageDialog(parent, "Rooms successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateHotels.run();
        });

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }

    /**
     * Dialog that has a drop down which is used to
     * edit the base rate of the room from regular,
     * deluxe, or executive when managing the rooms
     * of a specified hotel
     * @param parent            JFrame where the dialog will be shown on
     * @param model             stores the function needed to change the base multiplier of a room in a hotel
     * @param room              room selected to have its base multiplier changed
     * @param updateHotels      function that updates the actual list of hotels and the information about the hotels
     * @param updateRoomsShown  function that refreshes the GUI to reflect the new color of the room buttons
     */
    public static void showEditRateDialog(JFrame parent, MVC_Model model, Room room, Runnable updateHotels, Runnable updateRoomsShown) {
        JDialog modal = new JDialog(parent, "Change Room Type", true);
        JComboBox<String> basePriceDropdown;

        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(2, 2, 5, 5));
        inputArea.add(new JLabel("Room Rate:"));
        String[] roomTypes = {"Regular", "Deluxe", "Executive"};
        basePriceDropdown = new JComboBox<>(roomTypes);
        inputArea.add(basePriceDropdown);

        inputArea.add(new JLabel()); // Placeholder to align the button
        JButton addButton = new JButton("Finish Adding");
        inputArea.add(addButton);

        addButton.addActionListener(e -> {
            // getting dropdown item
            String selectedType = (String) basePriceDropdown.getSelectedItem();

            float baseRate;
            switch (selectedType) {
                case "Deluxe":
                    baseRate = 1.2f;
                    break;
                case "Executive":
                    baseRate = 1.35f;
                    break;
                default:
                    baseRate = 1.0f;
                    break;
            }

            // Success: add rooms with the selected base rate
            room.setBaseRate(baseRate);
            updateRoomsShown.run();
            modal.dispose();
            JOptionPane.showMessageDialog(parent, "Base Rate of " + room.getName() + " Successfully Changed", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateHotels.run();
        });

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }

    /**
     * Dialog that shows a confirmation screen after pressing
     * the confirm deletion button when removing rooms from a
     * hotel. Shows all the rooms that are about to be deleted.
     * @param parent            JFrame where the dialog will be shown on
     * @param model             stores the function needed to remove rooms in a hotel
     * @param chosenHotel       chosen hotel that will have rooms removed
     * @param toDelete          list of the names of the rooms that will be deleted
     * @param updateHotels      function that updates the actual list of hotels and the information about the hotels
     * @param updateRoomsShown  function that refreshes the GUI to reflect the new number of rooms
     * @return                  returns the option chosen by the user, whether they proceeded with the deletion or cancelled it
     */
    public static int showRemoveRoomsDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, ArrayList<String> toDelete, Runnable updateHotels, Runnable updateRoomsShown) {
        StringBuilder message = new StringBuilder("Are you sure you want to delete these rooms?\n");
        for (String room : toDelete) {
            message.append(room).append(" ");
        }

        int option = JOptionPane.showConfirmDialog(parent, message.toString(), "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            model.removeRooms(chosenHotel, toDelete);
            updateRoomsShown.run();
            JOptionPane.showMessageDialog(parent, "Rooms successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateHotels.run();
        }

        return option;
    }
    
    /**
     * Dialog that shows the input field for editing
     * the base price of a chosen hotel. Includes
     * all the checks and validations needed.
     * @param parent                    JFrame where the dialog will be shown on
     * @param model                     stores the function needed to modify the base price of a hotel
     * @param chosenHotel               chosen hotel that will have its base price changed
     * @param updateSpecificHotelPanel  function that updates the GUI to show the updated base price of the hotel selected
     * @param updateHotels              function that updates the actual list of hotels and the information about the hotels
     */
    public static void showModifyBasePriceDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, Runnable updateSpecificHotelPanel, Runnable updateHotels) {
        JDialog modal = new JDialog(parent, "Modify Base Price", true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField basePriceEntry = new JTextField(20);
        JButton createButton = new JButton("Confirm Change");

        inputArea.add(new JLabel("New Base Price:"));
        inputArea.add(basePriceEntry);
        // put a blank label to keep that area clear
        inputArea.add(new JLabel());
        inputArea.add(createButton);

        createButton.addActionListener(e -> {
            float basePrice;
            String basePriceText = basePriceEntry.getText();

            // Valid integer string
            try {
                basePrice = Float.parseFloat(basePriceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal, "Price must be a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (basePrice < 100) {
                JOptionPane.showMessageDialog(modal, "Invalid price, minimum is 100", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            model.modifyBasePrice(chosenHotel, basePrice);
            modal.dispose();
            JOptionPane.showMessageDialog(parent, "Base price successfully changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateSpecificHotelPanel.run();
            updateHotels.run();
        });

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }
    
    /**
     * Dialog that shows the input field for changing
     * the multiplier for a selected day in a hotel's
     * calendar. Includes the checks and validations needed.
     * @param parent        JFrame where the dialog will be shown on
     * @param model         stores the function needed to modify the day multiplier of a hotel
     * @param chosenHotel   chosen hotel that will have its day multiplier changed
     * @param day           the day selected to have its multiplier changed
     */
    public static void showChangeDayMultiplierDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, int day) {
        String title = String.format("Modify Multiplier for day %d", day + 1);
        JDialog modal = new JDialog(parent, title, true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField basePriceEntry = new JTextField(20);
        JButton createButton = new JButton("Confirm Change");

        inputArea.add(new JLabel("New Day Multiplier:"));
        inputArea.add(basePriceEntry);
        // put a blank label to keep that area clear
        inputArea.add(new JLabel());
        inputArea.add(createButton);
        
        createButton.addActionListener(e -> {
            float basePrice;
            String basePriceText = basePriceEntry.getText();

            // Valid integer string
            try {
                basePrice = Float.parseFloat(basePriceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal, "Multiplier must be a valid real number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (basePrice < 0) {
                JOptionPane.showMessageDialog(modal, "Invalid multiplier, minimum is 0", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            model.modifyDayMultiplier(chosenHotel, basePrice, day);
            modal.dispose();
            JOptionPane.showMessageDialog(parent, "Multiplier successfully changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }

    /**
     * Dialog that shows the confirmation for
     * deleting a hotel.
     * @param parent        JFrame where the dialog will be shown on
     * @param model         stores the function needed to delete a hotel
     * @param hotels        the list of hotels needed to remove the chosen hotel from the list
     * @param chosenHotel   the hotel that will be removed from the list of hotels
     * @param updateHotels  function that updates the actual list of hotels and the information about the hotels
     * @return              the option chosen by the user and whether they proceeded with deleting the hotel or cancelled it
     */
    public static int showDeleteHotelDialog(JFrame parent, MVC_Model model, ArrayList<Hotel> hotels, Hotel chosenHotel, Runnable updateHotels) {
        // Format the message string
        String message = String.format("Are you sure you want to delete hotel %s?\nThis cannot be undone.", chosenHotel.getName());

        // Show the confirmation dialog with an exclamation mark symbol
        int option = JOptionPane.showConfirmDialog(parent, message, "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Handle the user's response
        if (option == JOptionPane.YES_OPTION) {
            // Perform the deletion
            hotels.remove(chosenHotel);
            updateHotels.run();
            JOptionPane.showMessageDialog(parent, "Hotel successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        return option;
    }

    /**
     * Shows the dialog that prompts the user for a discount code.
     * Does not have validations for valid or invalid discount codes.
     * @param parent                JFrame where the dialog will be shown on
     * @param discountCodeStorage   the instance of the class that has all the information regarding the reservation stored
     */
    public static void showDiscountDialog(JFrame parent, ReserveCalendar discountCodeStorage) {
        JDialog modal = new JDialog(parent, "Enter Discount Code", true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField basePriceEntry = new JTextField(20);
        JButton submitButton = new JButton("Submit Discount");

        inputArea.add(new JLabel("Discount Code:"));
        inputArea.add(basePriceEntry);
        // put a blank label to keep that area clear
        inputArea.add(new JLabel());
        inputArea.add(submitButton);
        
        submitButton.addActionListener(e -> {
            String discountCode = basePriceEntry.getText();
            discountCodeStorage.setDiscountCode(discountCode);
            modal.dispose();
        });
        
        
        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }
    
    /**
     * Shows the dialog to show all the information of a
     * reservation before said reservation is created. Also asks
     * the user to confirm whether all the information is correct
     * or not. Contains the validation for the discount code
     * and does the math for the final cost of the reservation.
     * @param parent        JFrame where the dialog will be shown on
     * @param model         stores the function needed to create a reservation
     * @param hotel         stores the values for the day multipliers of a hotel
     * @param room          room that the customer wants to reserve
     * @param name          name of the customer
     * @param cost          base cost of the room
     * @param startDay      check-in day of the reservation
     * @param endDay        check-out day of the reservation
     * @param discountCode  discount code provided by the customer
     * @return              whether the user confirmed their reservation or not
     */
    public static int showReserveRoomDialog(JFrame parent, MVC_Model model, Hotel hotel, Room room, String name, float cost, int startDay, int endDay, String discountCode) {
        float hotelBasePrice = cost;
        float roomTypeMultiplier = room.getBaseRate();
        float discountMultiplier = 1.0f;
        float[] dayMultiplier = hotel.getDayMultipliers();
        float totalPayment = 0.0f;
        int daysFree = 0;

        ArrayList<Integer> daysBetween = new ArrayList<>();

        for (int i = startDay; i < endDay; i++) {
            daysBetween.add(i);
        }

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Is the information listed below correct?\n");
        messageBuilder.append(String.format("Name: %s\n", name));
        messageBuilder.append(String.format("Room Name: %s\n", room.getName()));
        messageBuilder.append(String.format("Room Type: %s (%.2fx)\n", roomTypeMultiplier == 1.2f ? "Deluxe" : roomTypeMultiplier == 1.35f ? "Executive" : "Regular", roomTypeMultiplier));
        
        if (discountCode.equals("I_WORK_HERE")) {
            messageBuilder.append(String.format("Discount code: %s (0.90x)\n", discountCode));
            discountMultiplier = 0.9f;
        }
        else if (discountCode.equals("STAY4_GET1") && endDay - startDay >= 5) {
            messageBuilder.append(String.format("Discount code: %s (Day %d is free)\n", discountCode, startDay));
            daysFree = 1;
        }
        else if (discountCode.equals("PAYDAY") && ((startDay <= 15 && endDay > 15) || (startDay <= 30 && endDay > 30))) {
            messageBuilder.append(String.format("Discount code: %s (0.93x)\n", discountCode));
            discountMultiplier = 0.93f;
        }
        else {
            messageBuilder.append("Discount code: No valid discount code provided\n");
        }
        
        messageBuilder.append(String.format("Hotel Nightly Rate: %.2f\n\n", hotelBasePrice));

        messageBuilder.append(String.format("Price Breakdown from Day %d to Day %d\n", startDay, endDay - 1));
        for (int j = daysFree; j < daysBetween.size(); j++) {
            messageBuilder.append(String.format("Day %d (%.2fx): %.2f\n", daysBetween.get(j), dayMultiplier[daysBetween.get(j) - 1], hotelBasePrice * dayMultiplier[daysBetween.get(j) - 1]));
            totalPayment += hotelBasePrice * dayMultiplier[daysBetween.get(j) - 1];
        }

        messageBuilder.append(String.format("Rate Total: %.2f\n", totalPayment));
        messageBuilder.append(String.format("Total: %.2f * %.2f * %.2f = %.2f\n", totalPayment, roomTypeMultiplier, discountMultiplier, totalPayment * roomTypeMultiplier * discountMultiplier));

        cost = totalPayment * roomTypeMultiplier * discountMultiplier;

        // Format the message string
        String message = messageBuilder.toString();
        int option = JOptionPane.showConfirmDialog(parent, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            model.reserveRoom(room, cost, name, startDay, endDay);
            JOptionPane.showMessageDialog(parent, "Reservation successfully booked.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        return option;
    }

    /**
     * Shows the confirmation for whether a selected
     * reservation is to be deleted or not.
     * @param parent        JFrame where the dialog will be shown on
     * @param model         stores the function needed to delete a reservation
     * @param hotel         hotel with the reservation needed as information for the deletion of said reservation
     * @param reservation   the actual reservation being deleted
     * @return              whether the deletion of the reservation was confirmed or cancelled
     */
    public static int showRemoveReservationDialog(JFrame parent, MVC_Model model, Hotel hotel, Reservation reservation) {
        // Format the message string
        String message = String.format("Are you sure you want to remove the reservation by %s in room %s from %d to %d?", reservation.getCustomerName(), reservation.getRoomName(), reservation.getStartDay(), reservation.getEndDay());
        int option = JOptionPane.showConfirmDialog(parent, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            model.removeReservation(hotel, reservation);
            
            JOptionPane.showMessageDialog(parent, "Reservation successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        
        return option;
    }
}
