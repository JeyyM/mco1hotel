package mco1;

import javax.swing.*;
import java.awt.*;

public class MVC_Controller {
    private MVC_Model model;
    private MVC_View view;

    private int mainPanelWidth= 450;
    private int mainPanelHeight= 500;

    private int manageHotelsPanelWidth = 680;
    private int manageHotelsPanelHeight = 500;

    public MVC_Controller(MVC_Model model, MVC_View view) {
        this.model = model;
        this.view = view;

        view.addCreateHotelListener(e -> showCreateHotelDialog());
        view.addManageHotelListener(e -> {
            switchToManageHotelsPanel();
            updateManageHotelsPanel();
        });
    }

    // To update the shown number
    public void updateHotelCount() {
        view.getCurrentHotelsCount().setText(String.format("Current Hotels: %d", model.getHotelCount()));
    }

    // For setting the hotel count in ManageHotelsPanel so MVC
    public void updateManageHotelsPanel() {
        ManageHotelsPanel manageHotelsPanel = view.getManageHotelsPanel();
        if (manageHotelsPanel != null) {
            manageHotelsPanel.setHotels(model.getHotels());
        }
    }

    /*
    For making warning pop-ups in hotel creation
    * */
    public void showCreateHotelDialog() {
        JDialog modal1 = new JDialog(view, "Create Hotel", true);
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
                updateHotelCount();
                modal1.dispose();
                JOptionPane.showMessageDialog(view, "Hotel successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateManageHotelsPanel(); // Call to update the ManageHotelsPanel
            } else {
                JOptionPane.showMessageDialog(modal1, "Failed to create hotel. Please try again.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        modal1.add(inputArea, BorderLayout.CENTER);
        modal1.pack();
        modal1.setLocationRelativeTo(view);
        modal1.setVisible(true);
    }

    // Switchers
    public void switchToMainPanel() {
        view.getContentPane().removeAll();
        view.add(view.getMainPanel(), BorderLayout.CENTER);
        view.setSize(mainPanelWidth, mainPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToManageHotelsPanel() {
        view.getContentPane().removeAll();
        if (view.getManageHotelsPanel() == null) {
            view.setManageHotelsPanel(new ManageHotelsPanel(model.getHotels(), model.getManager()));
        }
        view.getManageHotelsPanel().addBackButtonListener(e -> {
            switchToMainPanel();
        });
        JScrollPane scrollPane = new JScrollPane(view.getManageHotelsPanel());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane, BorderLayout.CENTER);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
}
