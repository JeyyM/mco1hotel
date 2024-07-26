package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This is the panel for the selection of a hotel to manage
 * */
public class ShowHotels extends JPanel {
    private ArrayList<Hotel> hotels;
    private HotelManager manager;
    private JButton backButton;
    private JPanel panelCenter;
    JLabel bannerLabel;

    // Size variables
    private int fullWidth = 680;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int rowHeight = 80;
    private int buttonWidth = 120;
    private int buttonHeight = 80;
    private int northLabelFontSize = 20;

    private MVC_Controller controller;

    // getters and setters
    // For refreshing the hotels every enter
    public void setHotels(ArrayList<Hotel> hotels) {
        this.hotels = hotels;
        initializeRows();
    }

    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }

    // Event listeners
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // There are rows that will contain the grid of buttons
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();

        int totalHotels = hotels.size();
        int cols = 5;
        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalHotels / cols);

        for (int i = 0; i < totalHotels; i++) {
            hotels.get(i).setIndex(i);
        }

        int hotelIndex = 0;
        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (hotelIndex < totalHotels) {
                    Hotel hotel = hotels.get(hotelIndex);
                    JButton hotelButton = new JButton("<html>" + hotel.getName() + "<br>" + hotel.getTotalRooms() + " rooms" + "<br>" + hotel.getTotalReservationCount() + " reservations</html>");
                    hotelButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    hotelButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
                    hotelButton.addActionListener(e -> {
                        controller.switchToSpecificHotelPanel(hotel);
                    });
                    rowWrapper.add(hotelButton);
                    hotelIndex++;
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

    public ShowHotels(ArrayList<Hotel> hotels, HotelManager manager, JLabel newBannerLabel) {
        this.hotels = hotels;
        this.manager = manager;
        this.bannerLabel = newBannerLabel;

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
        JLabel bannerLabel = new JLabel("Select Hotel to Manage", JLabel.CENTER);
        bannerLabel.setForeground(Color.WHITE);
        bannerLabel.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(bannerLabel, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        // Create the center panel with vertical BoxLayout so no grid row count needed
        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        initializeRows();

        JScrollPane scrollPane = new JScrollPane(panelCenter);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }
}
