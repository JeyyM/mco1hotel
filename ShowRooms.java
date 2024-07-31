package mco2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI Panel that shows the rooms of a chosen hotel
 * and is also used as a basis for other panels that
 * need to show the rooms of a hotel.
 */
public class ShowRooms extends JPanel {
    protected ArrayList<Room> rooms;
    protected JButton backButton;
    protected JPanel panelCenter;
    protected JLabel bannerLabel;
    JPanel panelNorth;

    // Size variables
    protected int fullWidth = 680;
    protected int backButtonFontSize = 25;
    protected int northHeight = 40;
    protected int rowHeight = 80;
    protected int buttonWidth = 120;
    protected int buttonHeight = 80;
    protected int bannerLabelFontSize = 20;

    protected String name;
    protected float cost;
    
    protected MVC_Controller controller;

    /**
     * Constructor for the GUI panel that shows all the rooms
     * of a chosen hotel as buttons and shows the hotel name.
     * Uses a different function to create the row of buttons.
     * at the top. Used for selecting a room to reserve.
     * @param hotel             hotel chosen to have its rooms displayed
     * @param name              name of the hotel
     * @param newBannerLabel    JLabel that has the hotel's name
     */
    public ShowRooms(Hotel hotel, String name, JLabel newBannerLabel) {
        this.rooms = hotel.getAllRooms();
        this.cost = hotel.getBasePrice();
        this.name = name;
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
    
    // There are rows that will contain the grid of buttons
    /**
     * Method that creates the row of buttons which are linked
     * to each room that a hotel has. Uses a different function
     * to add a listener to each button.
     */
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

                    addRoomButtonListener(roomButton, room);
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
    
    /**
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }

    /**
     * Adds a listener to the room buttons that commands the
     * controller to switch to the reservation calendar panel
     * of the selected room.
     * @param roomButton    button of the room selected to be reserved
     * @param room          room that corresponds to the button clicked and is from the list of rooms in the hotel
     */
    public void addRoomButtonListener(JButton roomButton, Room room) {
        roomButton.addActionListener(e -> {
            controller.switchToReserveCalendarStart(room, cost, name);
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
