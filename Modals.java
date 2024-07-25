package mco1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Modals {
    public static void showCreateHotelDialog(JFrame parent, MVC_Model model, Runnable updateHotelCount, Runnable updateHotels) {
        JDialog modal = new JDialog(parent, "Create Hotel", true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nameEntry = new JTextField(20);
        JTextField roomCountEntry = new JTextField(20);
        JButton createButton = new JButton("Finish Creation");

        inputArea.add(new JLabel("Hotel Name:"));
        inputArea.add(nameEntry);
        inputArea.add(new JLabel("Room Count (Set to Base Rate):"));
        inputArea.add(roomCountEntry);
        // put a blank label to keep that area clear
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

            // Success
            if (model.createHotel(newName, roomCount)) {
                updateHotelCount.run();
                modal.dispose();
                JOptionPane.showMessageDialog(parent, "Hotel successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // To update the hotels list
                updateHotels.run();
            } else {
                JOptionPane.showMessageDialog(modal, "Failed to create hotel. Please try again.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        modal.add(inputArea, BorderLayout.CENTER);
        modal.pack();
        modal.setLocationRelativeTo(parent);
        modal.setVisible(true);
    }

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

    public static int showRemoveRoomsDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, ArrayList<String> toDelete, Runnable updateHotels, Runnable updateRoomsShown) {
        StringBuilder message = new StringBuilder("Are you sure you want to delete these rooms?\n");
        for (String room : toDelete) {
            message.append(room).append(" ");
        }

        int option = JOptionPane.showConfirmDialog(parent, message.toString(), "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            chosenHotel.deleteRooms(toDelete);
            updateRoomsShown.run();
            JOptionPane.showMessageDialog(parent, "Rooms successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateHotels.run();
        }

        return option;
    }

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
            int basePrice;
            String basePriceText = basePriceEntry.getText();

            // Valid integer string
            try {
                basePrice = Integer.parseInt(basePriceText);
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

    public static int showDeleteHotelDialog(JFrame parent, MVC_Model model, ArrayList<Hotel> hotels, Hotel chosenHotel, Runnable updateHotels, Runnable updateRoomsShown) {
        // Format the message string
        String message = String.format("Are you sure you want to delete hotel %s?\nThis cannot be undone.", chosenHotel.getName());

        // Show the confirmation dialog with an exclamation mark symbol
        int option = JOptionPane.showConfirmDialog(parent, message, "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Handle the user's response
        if (option == JOptionPane.YES_OPTION) {
            // Perform the deletion
            hotels.remove(chosenHotel);
            updateRoomsShown.run();
            updateHotels.run();

            JOptionPane.showMessageDialog(parent, "Hotel successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);

        }

        return option;
    }

    public static int showReserveRoomDialog(JFrame parent, MVC_Model model, Hotel hotel, Room room, String name, float cost, int startDay, int endDay) {
        float hotelBasePrice = hotel.getBasePrice();
        float roomTypeMultiplier = room.getBaseRate();
        float[] dayMultiplier = hotel.getDayMultipliers();
        float totalPayment = 0.0f;

        ArrayList<Integer> daysBetween = new ArrayList<>();

        for (int i = startDay; i <= endDay; i++) {
            daysBetween.add(i);
        }

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Is the information listed below correct?\n");
        messageBuilder.append(String.format("Name: %s\n", name));
        messageBuilder.append(String.format("Room Name: %s\n", room.getName()));
        messageBuilder.append(String.format("Room Type: %s (%.2fx)\n", roomTypeMultiplier == 1.2f ? "Deluxe" : roomTypeMultiplier == 1.35f ? "Executive" : "Regular", roomTypeMultiplier));
        messageBuilder.append(String.format("Hotel Nightly Rate: %.2f\n\n", hotelBasePrice));

        messageBuilder.append(String.format("Price Breakdown from Day %d to Day %d\n", startDay, endDay));
        for (int j = 0; j < daysBetween.size(); j++) {
            messageBuilder.append(String.format("Day %d (%.2fx): %.2f\n", daysBetween.get(j), dayMultiplier[daysBetween.get(j) - 1], hotelBasePrice * dayMultiplier[daysBetween.get(j) - 1]));
            totalPayment += hotelBasePrice * dayMultiplier[daysBetween.get(j) - 1];
        }

        messageBuilder.append(String.format("Rate Total: %.2f\n", totalPayment));
        messageBuilder.append(String.format("Total: %.2f * %.2f = %.2f\n", totalPayment, roomTypeMultiplier, totalPayment * roomTypeMultiplier));

        cost = totalPayment * roomTypeMultiplier;

        // Format the message string
        String message = messageBuilder.toString();
        int option = JOptionPane.showConfirmDialog(parent, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            model.reserveRoom(room, cost, name, startDay, endDay);
            JOptionPane.showMessageDialog(parent, "Reservation successfully booked.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        return option;
    }

    
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
