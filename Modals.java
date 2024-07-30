package mco1;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Modals {
    public static void showCreateHotelDialog(JFrame parent, MVC_Model model, Runnable updateHotelCount, Runnable updateHotels) {
        JDialog modal = new JDialog(parent, "Create Hotel", true);
        modal.setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputArea = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField nameEntry = new JTextField(20);
        JTextField roomCountEntry = new JTextField(20);
        JButton createButton = new JButton("Finish Creation");
        JButton addImageButton = new JButton("Add Image");

        inputArea.add(new JLabel("Hotel Name:"));
        inputArea.add(nameEntry);
        inputArea.add(new JLabel("Room Count (Set to Base Rate):"));
        inputArea.add(roomCountEntry);
        inputArea.add(new JLabel("Add Hotel Image:"));
        inputArea.add(addImageButton);
        inputArea.add(new JLabel());
        inputArea.add(createButton);

        // Panel for image gallery
        JPanel imageGallery = new JPanel(new GridLayout(0, 2, 5, 5));
        JScrollPane imageScrollPane = new JScrollPane(imageGallery);
        imageScrollPane.setPreferredSize(new Dimension(100, 750));

        ArrayList<ImageIcon> images = new ArrayList<>();

        addImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(modal);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                ImageIcon originalImage = new ImageIcon(selectedFile.getAbsolutePath());
                Image scaledImage = originalImage.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                images.add(scaledIcon);

                imageGallery.removeAll();
                for (ImageIcon image : images) {
                    imageGallery.add(new JLabel(image));
                }
                imageGallery.revalidate();
                imageGallery.repaint();

                JOptionPane.showMessageDialog(modal, "Image added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

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
        modal.add(imageScrollPane, BorderLayout.SOUTH);
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

    public static void showViewOptions(JFrame parent, MVC_Model model, MVC_Controller controller, Hotel chosenHotel, Runnable updateSpecificHotelPanel, Runnable updateHotels) {
        JDialog modal = new JDialog(parent, "Viewing Options", true);
        modal.setLayout(new BorderLayout());

        JPanel inputArea = new JPanel(new GridLayout(3, 1, 5, 5));
        JButton dateButton = new JButton("Check Availability by Date");
        JButton roomButton = new JButton("Check Availability by Room");
        JButton reservationButton = new JButton("View by Reservation");

        dateButton.setPreferredSize(new Dimension(300, 30));
        roomButton.setPreferredSize(new Dimension(300, 30));
        reservationButton.setPreferredSize(new Dimension(300, 30));

        ArrayList<Room> availableRooms;

        dateButton.addActionListener(e -> {
            controller.switchToViewRoomsByDate(chosenHotel);
            modal.dispose();
            /*
            String input = JOptionPane.showInputDialog(modal, "Enter a Day to Check (1 to 31):", "Enter Date", JOptionPane.PLAIN_MESSAGE);
            // for clicking cancel
            if (input != null) {
                try {
                    int date = Integer.parseInt(input);
                    if (date < 1 || date > 31) {
                        JOptionPane.showMessageDialog(modal, "Please enter a valid date between 1 and 31.", "Error", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(modal, "Checking availability for date: " + date);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(modal, "Please enter a valid number.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            */
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
    
    public static void showReservedRoomsByDate(JFrame parent, Hotel chosenHotel, int day) {
        StringBuilder title = new StringBuilder("Reserved Rooms on Day " + day);
        ArrayList<Room> rooms = chosenHotel.getReservedRoomsByDay(day);
        StringBuilder message;
        
        if (rooms.size() == 0) {
            message = new StringBuilder("There are no rooms reserved on day " + day + ".\n");
        }
        else {
            message = new StringBuilder("These rooms are reserved on day " + day + ".\n");
            for (Room room : rooms) {
                message.append(room.getName()).append(" ");
            }
        }
        
        JOptionPane.showMessageDialog(parent, message.toString(), title.toString(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showReservedRoomsByRoom(JFrame parent, Room room, int day) {
        StringBuilder title = new StringBuilder("Reservations on Day " + day);
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
        
        JOptionPane.showMessageDialog(parent, message.toString(), title.toString(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showReservationDetails(JFrame parent, Reservation reservation, float roomMultiplier) {
        StringBuilder title = new StringBuilder("Reservation by " + reservation.getCustomerName());
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
        
        JOptionPane.showMessageDialog(parent, message.toString(), title.toString(), JOptionPane.INFORMATION_MESSAGE);
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
