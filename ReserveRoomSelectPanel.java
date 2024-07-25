package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This is the panel for the selection of a room to reserve
 * */
public class ReserveRoomSelectPanel extends JPanel {
    private ArrayList<Room> rooms;
    private HotelManager manager;
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
    
    private String name;
    private float cost;

    private MVC_Controller controller;

    public ReserveRoomSelectPanel(Hotel hotel, String name) {
        this.rooms = hotel.getAllRooms();
        this.cost = hotel.getBasePrice();
        this.name = name;
        
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
        JLabel labelManageHotels = new JLabel("Select Room to Reserve", JLabel.CENTER);
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

        int totalRooms = rooms.size();
        int cols = 5;
        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalRooms / cols);

        for (int i = 0; i < totalRooms; i++) {
            rooms.get(i).setIndex(i);
        }

        int roomIndex = 0;
        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));
            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (roomIndex < totalRooms) {
                    Room room = rooms.get(roomIndex);
                    JButton roomButton = new JButton("<html>" + room.getName() + "</html>");
                    roomButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    roomButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

                    float baseRate = room.getBaseRate();
                    if (baseRate == 1.0f) {
                        roomButton.setBackground(null);
                    } else if (baseRate == 1.20f) {
                        roomButton.setBackground(Color.decode("#fae105"));
                    } else if (baseRate == 1.35f) {
                        roomButton.setBackground(Color.decode("#00fff2"));
                    }

                    roomButton.addActionListener(e -> {
                        controller.switchToReserveCalendarStart(room, cost, name);
                    });
                    rowWrapper.add(roomButton);
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
}
