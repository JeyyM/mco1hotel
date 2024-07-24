package mco1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Modals {
    public static void showCreateHotelDialog(JFrame parent, MVC_Model model, Runnable updateHotelCount, Runnable updateHotels) {
        JDialog modal1 = new JDialog(parent, "Create Hotel", true);
        modal1.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nameEntry = new JTextField(20);
        JTextField roomCountEntry = new JTextField(20);
        JButton createButton = new JButton("Finish Creation");

        inputArea.add(new JLabel("Hotel Name:"));
        inputArea.add(nameEntry);
        inputArea.add(new JLabel("Room Count:"));
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
                JOptionPane.showMessageDialog(modal1, "Hotel name cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (model.isNameTaken(newName)) {
                JOptionPane.showMessageDialog(modal1, "Hotel name has been taken. Please choose another name.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Valid integer string
            try {
                roomCount = Integer.parseInt(roomCountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal1, "Room count must be a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!model.isValidRoomCount(roomCount)) {
                JOptionPane.showMessageDialog(modal1, "Invalid room count. Must be between 1 and 50.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            if (model.createHotel(newName, roomCount)) {
                updateHotelCount.run();
                modal1.dispose();
                JOptionPane.showMessageDialog(parent, "Hotel successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // To update the hotels list
                updateHotels.run();
            } else {
                JOptionPane.showMessageDialog(modal1, "Failed to create hotel. Please try again.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        modal1.add(inputArea, BorderLayout.CENTER);
        modal1.pack();
        modal1.setLocationRelativeTo(parent);
        modal1.setVisible(true);
    }

    public static void showRenameHotelDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, Runnable updateSpecificHotelPanel, Runnable updateHotels) {
        JDialog modal1 = new JDialog(parent, "Rename Hotel", true);
        modal1.setLayout(new BorderLayout());

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
                JOptionPane.showMessageDialog(modal1, "Hotel name cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (model.isNameTaken(newName)) {
                JOptionPane.showMessageDialog(modal1, "Hotel name has been taken. Please choose another name.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            model.changeName(chosenHotel, newName);
            modal1.dispose();
            JOptionPane.showMessageDialog(parent, "Hotel successfully renamed.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Update previous panels
            updateSpecificHotelPanel.run();
            updateHotels.run();
        });

        modal1.add(inputArea, BorderLayout.CENTER);
        modal1.pack();
        modal1.setLocationRelativeTo(parent);
        modal1.setVisible(true);
    }

    public static void showAddRoomsDialog(JFrame parent, MVC_Model model, Hotel chosenHotel, Runnable updateHotels, Runnable updateRoomsShown) {
        JDialog modal1 = new JDialog(parent, "Add Rooms", true);
        modal1.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(2, 2, 5, 5));
        inputArea.add(new JLabel("Room Count:"));
        inputArea.add(new JLabel());
        JTextField roomCountEntry = new JTextField(20);
        JButton addButton = new JButton("Finish Adding");
        inputArea.add(roomCountEntry);
        inputArea.add(addButton);

        addButton.addActionListener(e -> {
            int roomCount;
            String roomCountText = roomCountEntry.getText();

            // Valid integer string
            try {
                roomCount = Integer.parseInt(roomCountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal1, "Room count must be a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!model.isValidRoomCount(roomCount + chosenHotel.getTotalRooms())) {
                JOptionPane.showMessageDialog(modal1, "Invalid room count. Room count must be between 1 and 50.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            model.addRooms(chosenHotel, roomCount);
            updateRoomsShown.run();
            modal1.dispose();
            JOptionPane.showMessageDialog(parent, "Rooms successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateHotels.run();
        });

        modal1.add(inputArea, BorderLayout.CENTER);
        modal1.pack();
        modal1.setLocationRelativeTo(parent);
        modal1.setVisible(true);
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
        JDialog modal1 = new JDialog(parent, "Modify Base Price", true);
        modal1.setLayout(new BorderLayout());

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
                JOptionPane.showMessageDialog(modal1, "Price must be a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (basePrice < 100) {
                JOptionPane.showMessageDialog(modal1, "Invalid price, minimum is 100", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Success
            model.modifyBasePrice(chosenHotel, basePrice);
            modal1.dispose();
            JOptionPane.showMessageDialog(parent, "Base price successfully changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateSpecificHotelPanel.run();
            updateHotels.run();
        });

        modal1.add(inputArea, BorderLayout.CENTER);
        modal1.pack();
        modal1.setLocationRelativeTo(parent);
        modal1.setVisible(true);
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
    
    public static int showReserveRoomDialog(JFrame parent, MVC_Model model, Room room, String name, float cost, int startDay, int endDay) {
        // Format the message string
        String message = String.format("Is the information listed below correct?\nName: %s\nRoom Name: %s\nStart Day: %d\n End Day: %d\n", name, room.getName(), startDay, endDay);
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
