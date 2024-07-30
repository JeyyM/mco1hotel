package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * GUI Panel for all the functions that can be done
 * when managing a specific hotel such as modifying the rooms,
 * base price, date multiplier for each day, removing
 * reservations, and renaming or deleting the hotel.
 */
public class ManageSpecificHotelPanel extends JPanel {
    private Hotel hotel;

    // Panel Components
    private JLabel roomCountLabel;
    private JLabel basePriceLabel;
    private JButton renameHotelButton;
    private JButton modifyRoomsButton;
    private JButton modifyBasePriceButton;
    private JButton modifyDateMultiplyButton;
    private JButton removeReservationsButton;
    private JButton deleteHotelButton;
    private JButton backButton;
    private JLabel managingLabel;
    private JPanel panelNorth;

    // Size variables
    private int fullWidth = 450;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private JPanel mainPanel;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    /**
     * Constructor for the manage hotel panel.
     * This takes in a specific hotel to be managed.
     * @param hotel     selected hotel to be managed
     */
    public ManageSpecificHotelPanel(Hotel hotel) {
        this.hotel = hotel;
        setLayout(new BorderLayout());

        panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        backButton = new JButton("\u2190");
        backButton.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButton, BorderLayout.WEST);

        managingLabel = new JLabel("Managing " + hotel.getName(), JLabel.CENTER);
        managingLabel.setForeground(Color.WHITE);
        managingLabel.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(managingLabel, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        mainPanel = new JPanel(new BorderLayout());
        initializeContent(mainPanel);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the content for the main panel as the
     * constructor is tasked with creating the title
     * of the panel. Shows the high-level information
     * about the hotel, being the rooms and base price,
     * and creates the buttons needed to manage the hotel.
     * @param panel     the panel where this panel will be attached to
     */
    public void initializeContent(JPanel panel) {
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9, 1, 0, 10));

        roomCountLabel = new JLabel(String.format("Number of Rooms: %d", hotel.getTotalRooms()));
        roomCountLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        buttonPanel.add(roomCountLabel);

        basePriceLabel = new JLabel(String.format("Base Price: %.2f", hotel.getBasePrice()));
        basePriceLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        buttonPanel.add(basePriceLabel);

        // Add buttons to the button panel
        renameHotelButton = new JButton("Rename Hotel");
        modifyRoomsButton = new JButton("Modify Rooms");
        modifyBasePriceButton = new JButton("Modify Base Price");
        modifyDateMultiplyButton = new JButton("Modify Date Multiplier");
        removeReservationsButton = new JButton("Remove Reservations");
        deleteHotelButton = new JButton("Delete Hotel");

        buttonPanel.add(renameHotelButton);
        buttonPanel.add(modifyRoomsButton);
        buttonPanel.add(modifyBasePriceButton);
        buttonPanel.add(modifyDateMultiplyButton);
        buttonPanel.add(removeReservationsButton);
        buttonPanel.add(deleteHotelButton);

        panelCenter.add(buttonPanel);
        panel.add(panelCenter, BorderLayout.CENTER);
    }
    
    // Updaters
    /**
     * Updates the banner in case the hotel's name is changed,
     * updates the number of rooms in case more are added or
     * some are removed, and updates the base price in case
     * this is changed.
     */
    public void updateHotelInfo() {
        managingLabel.setText("Managing " + hotel.getName());
        roomCountLabel.setText(String.format("Number of Rooms: %d", hotel.getTotalRooms()));
        basePriceLabel.setText(String.format("Base Price: %.2f", hotel.getBasePrice()));
        revalidate();
        repaint();
    }
    
    // Event Listeners
    /**
     * Adds an event listener for the back button
     * at the top left of the GUI
     * @param listener      action that will happen when the back button is clicked
     */
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    /**
     * Adds an event listener for the rename hotel button
     * @param listener      action that will happen when the rename button is clicked
     */
    public void addRenameHotelButtonListener(ActionListener listener) {
        renameHotelButton.addActionListener(listener);
    }

    /**
     * Adds an event listener for the modify rooms button
     * @param listener      action that will happen when the modify rooms button is clicked
     */
    public void addModifyRoomsButtonListener(ActionListener listener) {
        modifyRoomsButton.addActionListener(listener);
    }

    /**
     * Adds an event listener for the modify base price button
     * @param listener      action that will happen when the modify base price button is clicked
     */
    public void addModifyBasePriceButtonListener(ActionListener listener) {
        modifyBasePriceButton.addActionListener(listener);
    }

    /**
     * Adds an event listener for the modify date multiplier button
     * @param listener      action that will happen when the modify date multiplier button is clicked
     */
    public void addDateMultiplierListener(ActionListener listener) {
        modifyDateMultiplyButton.addActionListener(listener);
    }

    /**
     * Adds an event listener for the remove reservations button
     * @param listener      action that will happen when the remove reservations button is clicked
     */
    public void addRemoveReservationsButtonListener(ActionListener listener) {
        removeReservationsButton.addActionListener(listener);
    }

    /**
     * Adds an event listener for the delete hotel button
     * @param listener      action that will happen when the delete hotel button is clicked
     */
    public void addDeleteHotelButtonListener(ActionListener listener) {
        deleteHotelButton.addActionListener(listener);
    }
}
