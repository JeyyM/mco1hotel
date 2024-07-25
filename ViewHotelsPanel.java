package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This is for showing the list of hotels through view
 * */
public class ViewHotelsPanel extends JPanel {
    private ArrayList<Hotel> hotels;
    private HotelManager manager;
    private MVC_Controller controller;

    // Panel components
    private JButton backButton;
    private JPanel panelCenter;

    // Size variables
    private int fullWidth = 450;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int rowHeight = 120;
    private int buttonWidth = 120;
    private int buttonHeight = 80;
    private int northLabelFontSize = 20;


    // Getters and Setters
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
        JLabel labelManageHotels = new JLabel("Select Hotel to View", JLabel.CENTER);
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

    // There are rows that will contain the grid of buttons
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();
        int totalHotels = hotels.size();
        int cols = 2;
        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalHotels / cols);

        for (int i = 0; i < totalHotels; i++) {
            hotels.get(i).setIndex(i);
        }

        int hotelIndex = 0;
        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(rows, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (hotelIndex < totalHotels) {
                    Hotel hotel = hotels.get(hotelIndex);
                    String buttonString = String.format("<html>%s<br>%d rooms<br>%d reservations<br>Monthly Earnings: %.2f</html>",
                            hotel.getName(),
                            hotel.getTotalRooms(),
                            hotel.getTotalReservationCount(),
                            hotel.getTotalEarnings()
                    );

                    JButton hotelButton = new JButton(buttonString);
                    hotelButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    hotelButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
                    hotelButton.addActionListener(e -> {
                        controller.showViewOptions(hotel);
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
}
