package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * Base Template for hotel selection panels
 * */
public class ShowHotels extends JPanel {
    protected ArrayList<Hotel> hotels;
    protected JButton backButton;
    protected JPanel panelCenter;
    protected JLabel bannerLabel;
    protected JPanel panelNorth;

    // Size variables
    protected int fullWidth = 680;
    protected int backButtonFontSize = 25;
    protected int northHeight = 40;
    protected int rowHeight = 80;
    protected int buttonWidth = 120;
    protected int buttonHeight = 80;
    protected int northLabelFontSize = 20;

    protected MVC_Controller controller;

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

    protected void addHotelButtonListener(JButton hotelButton, Hotel hotel) {
        hotelButton.addActionListener(e -> controller.switchToSpecificHotelPanel(hotel));
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
                    addHotelButtonListener(hotelButton, hotel);
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

    public ShowHotels(ArrayList<Hotel> hotels, JLabel newBannerLabel) {
        this.hotels = hotels;
        this.bannerLabel = newBannerLabel;

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
