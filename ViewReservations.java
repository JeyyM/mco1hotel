package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI Panel that is used to show all the reservations
 * of a hotel without sorting using by room or by date.
 * Can show all the specific information of a reservation
 * such as the customer name, room name, check-in day, and
 * check-out day
 */
public class ViewReservations extends JPanel {
    private JButton backButton;
    private JPanel panelCenter;
    private JLabel bannerLabel;
    private JPanel panelNorth;
    
    private ArrayList<Room> reservedRooms;
    
    private int fullWidth = 680;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int rowHeight = 80;
    private int buttonWidth = 120;
    private int buttonHeight = 80;
    private int bannerLabelFontSize = 20;
    
    private MVC_Controller controller;
    
    /**
     * Constructor for the panel and shows the all the different
     * reservations for a chosen hotel. Uses a different function
     * to create the rows of buttons for each reservation.
     * @param hotel     hotel chosen to be viewed
     */
    public ViewReservations(Hotel hotel) {
        this.reservedRooms = hotel.getAllReservedRooms();
        this.bannerLabel = new JLabel("Select Reservation to View", JLabel.CENTER);

        setLayout(new BorderLayout());

        // Setting north panel
        panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Back Button
        backButton = new JButton("\u2190");
        backButton.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButton, BorderLayout.WEST);

        // North Label
        bannerLabel.setForeground(Color.WHITE);
        bannerLabel.setFont(new Font("Verdana", Font.BOLD, bannerLabelFontSize));
        panelNorth.add(bannerLabel, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        // Create the center panel with vertical BoxLayout so no grid row count needed
        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        initializeRows();

        JScrollPane scrollPane = new JScrollPane(panelCenter);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Function that creates the rows of buttons corresponding to
     * the all the reservations of the hotel. The buttons also show
     * specific information about the reservation in order to better
     * identify which reservation is which. Uses a different function
     * to add listeners to each reservation button.
     */
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();
        
        ArrayList<Reservation> allReservations = new ArrayList<>();
        ArrayList<Float> roomTypes = new ArrayList<>();
        
        for (Room room : reservedRooms) {
            for (Reservation reservation : room.getReservations()) {
                allReservations.add(reservation);
                roomTypes.add(room.getBaseRate());
            }
        }
        
        int totalReservations = allReservations.size();
        
        int cols = 5;
        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalReservations / cols);

        int reservationIndex = 0;
        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (reservationIndex < totalReservations) {
                    Reservation reservation = allReservations.get(reservationIndex);
                    float roomType = roomTypes.get(reservationIndex);
                    JButton reservationButton = new JButton("<html>Room " + reservation.getRoomName() + "<br>By " + reservation.getCustomerName() + "<br>Day " + reservation.getStartDay() + " to " + reservation.getEndDay() + "</html>");
                    reservationButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    reservationButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

                    addReservationButtonListener(reservationButton, reservation, roomType);
                    rowWrapper.add(reservationButton);
                    reservationIndex++;
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
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    /**
     * Adds a listener to the reservation buttons that commands the
     * controller to view the specific reservation selected
     * @param button            button of the reservation selected to be viewed
     * @param reservation       reservation that corresponds to the button clicked and is from the list of reservations in the hotel
     * @param roomType          the room multiplier of the room that can be used to determine the room type
     */
    public void addReservationButtonListener(JButton button, Reservation reservation, float roomType) {
        button.addActionListener(e -> {
            controller.switchToViewSelectedReservation(reservation, roomType);
        });
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
