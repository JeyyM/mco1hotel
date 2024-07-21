package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MVC_View extends JFrame {
    private HotelManager manager;
    private ArrayList<Hotel> hotels;
    private JButton createHotelButton;
    private JButton manageHotelButton;
    private JLabel currentHotelsCount;

    private int menuSize = 450;
    private int menuHeight = 500;

    private JPanel mainPanel;
    private ManageHotelsPanel manageHotelsPanel;

    public void addCreateHotelListener(ActionListener listener) {
        createHotelButton.addActionListener(listener);
    }

    public void addManageHotelListener(ActionListener listener) {
        manageHotelButton.addActionListener(listener);
    }

    MVC_View(ArrayList<Hotel> hotels, HotelManager manager) {
        super("Hotel Manager");
        this.manager = manager;
        this.hotels = hotels;

        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());
        initializeContent(mainPanel);
        add(mainPanel, BorderLayout.CENTER);

        setSize(menuSize, menuHeight);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Getters and Setters
    public JLabel getCurrentHotelsCount() {
        return currentHotelsCount;
    }

    // Get Panels for Switching
    public ManageHotelsPanel getManageHotelsPanel() {
        return manageHotelsPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setManageHotelsPanel(ManageHotelsPanel manageHotelsPanel) {
        this.manageHotelsPanel = manageHotelsPanel;
    }

    // To create menu
    public void initializeContent(JPanel panel) {
        // Create the north label
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(400, 40));

        JLabel labelHotelManager = new JLabel("Hotel Management System");
        labelHotelManager.setForeground(Color.WHITE);
        labelHotelManager.setFont(new Font("Verdana", Font.BOLD, 20));

        panelNorth.add(labelHotelManager);
        panel.add(panelNorth, BorderLayout.NORTH);

        // FlowLayout has a .CENTER
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 0, 10));

        currentHotelsCount = new JLabel(String.format("Current Hotels: %d", hotels.size()));
        currentHotelsCount.setFont(new Font("Verdana", Font.BOLD, 15));
        buttonPanel.add(currentHotelsCount);

        // Add buttons to the button panel
        createHotelButton = new JButton("Create Hotel");
        JButton button2 = new JButton("View Hotels");
        manageHotelButton = new JButton("Manage Hotel");
        JButton button4 = new JButton("Reserve a Hotel");
        JButton button5 = new JButton("Exit Program");

        buttonPanel.add(createHotelButton);
        buttonPanel.add(button2);
        buttonPanel.add(manageHotelButton);
        buttonPanel.add(button4);
        buttonPanel.add(button5);

        panelCenter.add(buttonPanel);
        panel.add(panelCenter, BorderLayout.CENTER);
    }
}
