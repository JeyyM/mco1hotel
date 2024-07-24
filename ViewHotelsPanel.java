package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This is the panel for the selection of a hotel to manage
 * */
public class ViewHotelsPanel extends JPanel {
    private ArrayList<Hotel> hotels;
    private HotelManager manager;
    private JButton backButton;
    private JPanel panelCenter;

    // Size variables
    private int fullWidth = 400;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int rowHeight = 80;
    private int buttonWidth = 120;
    private int buttonHeight = 80;
    private int northLabelFontSize = 20;

    private MVC_Controller controller;

    public ViewHotelsPanel(ArrayList<Hotel> hotels, HotelManager manager) {
        this.hotels = hotels;
        this.manager = manager;

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
        JLabel labelManageHotels = new JLabel("View Hotels", JLabel.CENTER);
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

    // For refreshing the hotels every enter
    public void setHotels(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
        initializeRows();
    }

    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // There are rows that will contain the grid of buttons
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();

        int totalHotels = hotels.size();

        for (int i = 0; i < totalHotels; i++) {
            hotels.get(i).setIndex(i);
        }

        int hotelIndex = 0;
        for (int i = 0; i < totalHotels; i++) {
            Hotel hotel = hotels.get(hotelIndex);
            ArrayList<Room> bookedRooms = new ArrayList<>();

            for (Room room : hotel.getAllRooms()) {
                if (room.getReservations().size() > 0) {
                    bookedRooms.add(room);
                }
            }

            JPanel rowWrapper = new JPanel(new BorderLayout());
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            JButton hotelButton = new JButton("<html>" + hotel.getName() + "<br>" + (hotel.getTotalRooms() - bookedRooms.size()) + "/" + hotel.getTotalRooms() + " rooms fully available</html>");
            hotelButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            hotelButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
            hotelButton.addActionListener(e -> {
                controller.switchToSpecificHotelPanel(hotel);
            });
            rowWrapper.add(hotelButton, BorderLayout.WEST);

            JPanel hotelData = new JPanel(new GridLayout(2, 1, 10, 10));
            hotelData.add(new JLabel("There are " + hotel.getTotalReservationCount() + " reservations"));
            hotelData.add(new JLabel("Monthly earnings: "));

            rowWrapper.add(hotelData, BorderLayout.CENTER);

            panelCenter.add(rowWrapper);
            hotelIndex++;
        }

        panelCenter.revalidate();
        panelCenter.repaint();
    }
}
