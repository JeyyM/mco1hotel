package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    public void addReservationButtonListener(JButton button, Reservation reservation, float roomType) {
        button.addActionListener(e -> {
            controller.switchToViewSelectedReservation(reservation, roomType);
        });
    }
    
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
    
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
