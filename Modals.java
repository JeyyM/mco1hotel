package mco1;

import javax.swing.*;
import java.awt.*;

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

}
