package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManageSpecificHotelPanel extends JPanel {
    private Hotel hotel;
    private JLabel roomCountLabel;
    private JLabel basePriceLabel;
    private JButton renameHotelButton;
    private JButton modifyRoomsButton;
    private JButton modifyBasePriceButton;
    private JButton removeReservationsButton;
    private JButton deleteHotelButton;
    private JButton backButton;
    private JLabel managingLabel;

    private JPanel panelNorth = new JPanel();
    private int fullWidth = 450;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private JPanel mainPanel;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    ManageSpecificHotelPanel(Hotel hotel) {
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

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addRenameHotelButtonListener(ActionListener listener) {
        renameHotelButton.addActionListener(listener);
    }

    public void addModifyRoomsButtonListener(ActionListener listener) {
        modifyRoomsButton.addActionListener(listener);
    }

    public void addModifyBasePriceButtonListener(ActionListener listener) {
        modifyBasePriceButton.addActionListener(listener);
    }
    
    public void addRemoveReservationsButtonListener(ActionListener listener) {
        removeReservationsButton.addActionListener(listener);
    }

    public void addDeleteHotelButtonListener(ActionListener listener) {
        deleteHotelButton.addActionListener(listener);
    }

    public void updateHotelInfo() {
        managingLabel.setText("Managing " + hotel.getName());
        roomCountLabel.setText(String.format("Number of Rooms: %d", hotel.getTotalRooms()));
        basePriceLabel.setText(String.format("Base Price: %.2f", hotel.getBasePrice()));
        revalidate();
        repaint();
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
        modifyRoomsButton = new JButton("Modify Rooms");
        modifyBasePriceButton = new JButton("Modify Base Price");
        removeReservationsButton = new JButton("Remove Reservations");
        deleteHotelButton = new JButton("Delete Hotel");

        buttonPanel.add(renameHotelButton);
        buttonPanel.add(modifyRoomsButton);
        buttonPanel.add(modifyBasePriceButton);
        buttonPanel.add(removeReservationsButton);
        buttonPanel.add(deleteHotelButton);

        panelCenter.add(buttonPanel);
        panel.add(panelCenter, BorderLayout.CENTER);
    }
}
