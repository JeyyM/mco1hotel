package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManageSpecificHotelPanel extends JPanel {
    private Hotel hotel;
    private JLabel roomCountLabel;
    private JLabel basePriceLabel;
    private JButton renameHotelButton;
    private JButton addRoomsButton;
    private JButton removeRoomsButton;
    private JButton modifyBasePriceButton;
    private JButton removeReservationsButton;
    private JButton deleteHotelButton;
    private JButton backButton;

    private JPanel panelNorth = new JPanel();
    private int fullWidth = 450;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private JPanel mainPanel;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    ManageSpecificHotelPanel(Hotel hotel) {
        super();
        this.hotel = hotel;
        setLayout(new BorderLayout());

        panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        backButton = new JButton("\u2190");
        backButton.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButton, BorderLayout.WEST);

        JLabel labelManageHotels = new JLabel("Managing " + hotel.getName(), JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        mainPanel = new JPanel(new BorderLayout());
        initializeContent(mainPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void initializeContent(JPanel panel) {
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1, 0, 10));

        roomCountLabel = new JLabel(String.format("Number of Rooms: %d", hotel.getTotalRooms()));
        roomCountLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        buttonPanel.add(roomCountLabel);

        basePriceLabel = new JLabel(String.format("Base Price: %.2f", hotel.getBasePrice()));
        basePriceLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        buttonPanel.add(basePriceLabel);

        // Add buttons to the button panel
        renameHotelButton = new JButton("Rename Hotel");
        addRoomsButton = new JButton("Add Rooms");
        removeRoomsButton = new JButton("Remove Rooms");
        modifyBasePriceButton = new JButton("Modify Base Price");
        removeReservationsButton = new JButton("Remove Reservations");
        deleteHotelButton = new JButton("Delete Hotel");

        buttonPanel.add(renameHotelButton);
        buttonPanel.add(addRoomsButton);
        buttonPanel.add(removeRoomsButton);
        buttonPanel.add(modifyBasePriceButton);
        buttonPanel.add(removeReservationsButton);
        buttonPanel.add(deleteHotelButton);

        panelCenter.add(buttonPanel);
        panel.add(panelCenter, BorderLayout.CENTER);
    }
}
