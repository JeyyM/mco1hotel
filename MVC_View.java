package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
* Main_Menu contains the options to edit the hotels
 */
public class MVC_View extends JFrame {
    private ArrayList<Hotel> hotels;

    // Menu sizing
    private int menuSize = 450;
    private int menuHeight = 500;

    // Panel Components
    private JLabel currentHotelsCount;
    private JButton createHotelButton;
    private JButton viewHotelButton;
    private JButton manageHotelButton;
    private JButton reserveHotelButton;
    private JButton exitProgramButton;

    // Panels of other pages
    private JPanel mainPanel;
    private ManageHotelsPanel manageHotelsPanel;
    private ReserveHotelSelectPanel reservationsPanel;
    private ViewHotelsPanel viewHotelsPanel;

    //Event Listeners
    public void addCreateHotelListener(ActionListener listener) {
        createHotelButton.addActionListener(listener);
    }
    public void addViewHotelListener(ActionListener listener) {
        viewHotelButton.addActionListener(listener);
    }
    public void addManageHotelListener(ActionListener listener) {
        manageHotelButton.addActionListener(listener);
    }
    public void addReserveHotelListener(ActionListener listener) {
        reserveHotelButton.addActionListener(listener);
    }

    // Getters and Setters
    public JLabel getCurrentHotelsCount() {
        return currentHotelsCount;
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Getters and Setters for other panels
    public ViewHotelsPanel getViewHotelsPanel(){
        return viewHotelsPanel;
    }

    public void setViewHotelsPanel(ViewHotelsPanel viewHotelsPanel) {
        this.viewHotelsPanel = viewHotelsPanel;
    }

    public ManageHotelsPanel getManageHotelsPanel() {
        return this.manageHotelsPanel;
    }

    public void setManageHotelsPanel(ManageHotelsPanel manageHotelsPanel) {
        this.manageHotelsPanel = manageHotelsPanel;
    }

    public ReserveHotelSelectPanel getReservationsPanel() {
        return this.reservationsPanel;
    }

    public void setReservationsPanel(ReserveHotelSelectPanel reservationsPanel) {
        this.reservationsPanel = reservationsPanel;
    }

    // Controller setters, they are given since menus within as not having it in some cases
    // can mess with the base switchPanels
    public void setManageHotelsPanelController(MVC_Controller controller) {
        if (manageHotelsPanel != null) {
            manageHotelsPanel.setController(controller);
        }
    }

    // Set up the option buttons
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

        // FlowLayout has a .CENTER to keep content centered
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 0, 10));

        currentHotelsCount = new JLabel(String.format("Current Hotels: %d", hotels.size()));
        currentHotelsCount.setFont(new Font("Verdana", Font.BOLD, 15));
        buttonPanel.add(currentHotelsCount);

        // Add buttons to the button panel
        createHotelButton = new JButton("Create Hotel");
        viewHotelButton = new JButton("View Hotels");
        manageHotelButton = new JButton("Manage Hotel");
        reserveHotelButton = new JButton("Reserve a Hotel");
        exitProgramButton = new JButton("Exit Program");

        buttonPanel.add(createHotelButton);
        buttonPanel.add(viewHotelButton);
        buttonPanel.add(manageHotelButton);
        buttonPanel.add(reserveHotelButton);
        buttonPanel.add(exitProgramButton);

        panelCenter.add(buttonPanel);
        panel.add(panelCenter, BorderLayout.CENTER);
    }

    MVC_View(ArrayList<Hotel> hotels, HotelManager manager) {
        // Set title
        super("Hotel Manager");
        // Set all hotels
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
}
