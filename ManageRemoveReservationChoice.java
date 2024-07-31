package mco2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI Panel that shows all the reservations
 * and the information for each reservation
 * from a specific room. Clicking these
 * reservations would lead to the deletion
 * of the selected reservation.
 */
public class ManageRemoveReservationChoice extends JPanel {
    private ArrayList<Reservation> reservations;
    private MVC_Controller controller;

    // Panel components
    private JButton backButton;
    private JPanel panelCenter;

    // Size variables
    private int fullWidth = 680;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int rowHeight = 80;
    private int buttonWidth = 120;
    private int buttonHeight = 80;
    private int northLabelFontSize = 20;

    /**
     * Constructor for the panel that shows the different
     * reservations for a chosen room in order to delete
     * a selected reservation.
     * @param room      room chosen to delete the reservations for
     */
    public ManageRemoveReservationChoice(Room room) {
        this.reservations = room.getReservations();

        setLayout(new BorderLayout());

        // Setting north panel
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Back Button
        backButton = new JButton("\u2190");
        backButton.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButton, BorderLayout.WEST);

        // North Label
        JLabel labelManageHotels = new JLabel("Select Reservation to Remove", JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        // Create the center panel with vertical BoxLayout so no grid row count needed
        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        initializeRows();

        JScrollPane scrollPane = new JScrollPane(panelCenter);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Getters and Setters
    /**
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }

    /**
     * Sets the rows of the panel to be buttons, with each button
     * having a corresponding reservation that can be clicked
     * to be deleted.
     */
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();

        int totalReservations = reservations.size();
        int cols = 5;
        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalReservations / cols);

        for (int i = 0; i < totalReservations ; i++) {
            reservations.get(i).setIndex(i);
        }

        int roomIndex = 0;
        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (roomIndex < totalReservations) {
                    Reservation reservation = reservations.get(roomIndex);
                    JButton reservationButton = new JButton("<html>By " + reservation.getCustomerName() + "<br>" + "Days " + reservation.getStartDay() + " to " + reservation.getEndDay() + "<br>Total " + reservation.getCost() + "</html>");
                    reservationButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    reservationButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
                    reservationButton.addActionListener(e -> {
                        controller.removeReservationFinal(reservation);
                    });
                    rowWrapper.add(reservationButton);
                    roomIndex++;
                } else {
                    // Put an empty label since the buttons will overgrow if not
                    rowWrapper.add(new JLabel());
                }
            }

            panelCenter.add(rowWrapper);
        }

        panelCenter.revalidate();
        panelCenter.repaint();
    }
    
    /**
     * Adds an event listener for the back button
     * at the top left of the GUI
     * @param listener      action that will happen when the back button is clicked
     */
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
