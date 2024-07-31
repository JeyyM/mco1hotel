package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Main panel of the system that the user
 * will interact with. Also stores the different
 * specific panels used for viewing, managing,
 * and reserving the different hotels in the system.
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

    /**
     * Constructor for the view class.
     * Takes in the list of hotels in the
     * system as a copy of the one in the model.
     * @param hotels    the list of hotels in the system
     */
    public MVC_View(ArrayList<Hotel> hotels) {
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
    
    // Set up the option buttons
    /**
     * Creates the buttons and sets up their layout in the main menu panel
     * @param panel     the main menu panel
     */
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
    
    //Event Listeners
    /**
     * Adds an event listener for the create hotel button
     * @param listener      action that will happen when the create hotel button is clicked
     */
    public void addCreateHotelListener(ActionListener listener) {
        createHotelButton.addActionListener(listener);
    }
    
    /**
     * Adds an event listener for the view hotel button
     * @param listener      action that will happen when the view hotel button is clicked
     */
    public void addViewHotelListener(ActionListener listener) {
        viewHotelButton.addActionListener(listener);
    }
    
    /**
     * Adds an event listener for the manage hotel button
     * @param listener      action that will happen when the manage hotel button is clicked
     */
    public void addManageHotelListener(ActionListener listener) {
        manageHotelButton.addActionListener(listener);
    }
    
    /**
     * Adds an event listener for the reserve hotel button
     * @param listener      action that will happen when the reserve hotel button is clicked
     */
    public void addReserveHotelListener(ActionListener listener) {
        reserveHotelButton.addActionListener(listener);
    }

    // Getters and Setters
    /**
     * Returns the JLabel that contains the number of hotels in the system
     * @return  the label that has the number of the hotels in the system
     */
    public JLabel getCurrentHotelsCount() {
        return currentHotelsCount;
    }
    
    /**
     * Returns the main menu panel
     * @return  the main menu panel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Getters and Setters for other panels
    /**
     * Returns the panel created for viewing the hotels
     * @return  the panel that shows all the hotels for the purpose of viewing their information
     */
    public ViewHotelsPanel getViewHotelsPanel(){
        return viewHotelsPanel;
    }

    /**
     * Sets the local copy of the view hotels panel to the most updated one
     * @param viewHotelsPanel   the most updated version of the view hotels panel
     */
    public void setViewHotelsPanel(ViewHotelsPanel viewHotelsPanel) {
        this.viewHotelsPanel = viewHotelsPanel;
    }

    /**
     * Returns the panel created for managing the hotels
     * @return  the panel that shows all the hotels for the purpose of managing their properties
     */
    public ManageHotelsPanel getManageHotelsPanel() {
        return this.manageHotelsPanel;
    }

    /**
     * Sets the local copy of the manage hotels panel to the most updated one
     * @param manageHotelsPanel   the most updated version of the manage hotels panel
     */
    public void setManageHotelsPanel(ManageHotelsPanel manageHotelsPanel) {
        this.manageHotelsPanel = manageHotelsPanel;
    }

    /**
     * Returns the panel created for reserving the hotels
     * @return  the panel that shows all the hotels for the purpose of creating a reservation
     */
    public ReserveHotelSelectPanel getReservationsPanel() {
        return this.reservationsPanel;
    }

    /**
     * Sets the local copy of the reserve hotels panel to the most updated one
     * @param reservationsPanel     the most updated version of the reserve hotels panel
     */
    public void setReservationsPanel(ReserveHotelSelectPanel reservationsPanel) {
        this.reservationsPanel = reservationsPanel;
    }

    // Controller setters, they are given since menus within as not having it in some cases
    // can mess with the base switchPanels
    /**
     * Sets the controller for the manage hotels panel
     * @param controller    the main controller of the system
     */
    public void setManageHotelsPanelController(MVC_Controller controller) {
        if (manageHotelsPanel != null) {
            manageHotelsPanel.setController(controller);
        }
    }
}
