package mco2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

/**
 * GUI Panel for the reservation calendar of a specific room.
 * Contains a constructor for selecting the start of a
 * reservation and one for selecting the end of a reservation.
 * Also commands the controller to get the discount code from the user.
 */
public class ReserveCalendar extends JPanel {
    private Room room;
    private JButton backButtonStart, backButtonEnd;

    // Size variables
    private int fullWidth = 1050;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    private JPanel[] weeks = new JPanel[5];
    private JButton[][] calendar = new JButton[5][7];
    private String name, discountCode;
    private float cost;

    private MVC_Controller controller;

    /**
     * Constructor for the Reserve Calendar GUI for selecting
     * the check-in day for a specific room. Shows the check-in
     * and check-out days of other customers booking the same room
     * in color orange, while their actual stays in color red.
     * @param room      room chosen to be booked
     * @param cost      base price of the hotel
     * @param name      name of the customer booking the reservation
     */
    public ReserveCalendar(Room room, float cost, String name) {
        this.room = room;
        this.name = name;
        this.cost = cost;

        setLayout(new BorderLayout());

        // Setting north panel
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Back Button
        backButtonStart = new JButton("\u2190");
        backButtonStart.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButtonStart, BorderLayout.WEST);

        // North Label
        JLabel labelManageHotels = new JLabel("Select Start of Reservation", JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new GridLayout(5, 1));

        for (int i = 0; i < 5; i++) {
            weeks[i] = new JPanel(new GridLayout(1, 7));
            for (int j = 0; j < 7; j++) {
                if (i == 4 && j > 2) {
                    JPanel whiteSpace = new JPanel();
                    weeks[i].add(whiteSpace);
                }
                else {
                    int availabilityStatus = room.checkDayAvailability(i * 7 + j + 1);
                    calendar[i][j] = new JButton("" + (i * 7 + j + 1));

                    if (availabilityStatus == 0) {
                        calendar[i][j].setBackground(Color.ORANGE);
                    } else if (availabilityStatus == -1) {
                        calendar[i][j].setBackground(Color.RED);
                    }

                    weeks[i].add(calendar[i][j]);
                }
            }
            weeks[i].setBorder(new EmptyBorder(5, 5, 5, 30));
            calendarPanel.add(weeks[i]);
        }

        addCalendarListener(room);

        add(calendarPanel, BorderLayout.CENTER);
    }
    
    /**
     * Overloaded constructor for the Reserve Calendar GUI for
     * selecting the check-out day for a specific room after
     * the check-in day has been selected. Still shows the check-in
     * and check-out days of other customers booking the same room
     * in color orange, while their actual stays in color red.
     * Also highlights the check-in of the current reservation in green.
     * @param room      room chosen to be booked
     * @param cost      base price of the hotel
     * @param name      name of the customer booking the reservation
     * @param startDay  the day of the check-in for the current reservation
     */
    public ReserveCalendar(Room room, float cost, String name, int startDay) {
        this.room = room;
        this.name = name;
        this.cost = cost;

        setLayout(new BorderLayout());

        // Setting north panel
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Back Button
        backButtonEnd = new JButton("Undo");
        panelNorth.add(backButtonEnd, BorderLayout.WEST);

        // North Label
        JLabel labelManageHotels = new JLabel("Select End of Reservation", JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new GridLayout(5, 1));

        for (int i = 0; i < 5; i++) {
            weeks[i] = new JPanel(new GridLayout(1, 7));
            for (int j = 0; j < 7; j++) {
                if (i == 4 && j > 2) {
                    JPanel whiteSpace = new JPanel();
                    weeks[i].add(whiteSpace);
                }
                else {
                    if ((i * 7 + j + 1) == startDay) {
                        calendar[i][j] = new JButton("" + (i * 7 + j + 1) + "");
                        calendar[i][j].setBackground(Color.GREEN);
                    }
                    else {
                        calendar[i][j] = new JButton("" + (i * 7 + j + 1));
                        int availabilityStatus = room.checkDayAvailability(i * 7 + j + 1);
                        if (availabilityStatus == 0) {
                            calendar[i][j].setBackground(Color.ORANGE);
                        } else if (availabilityStatus == -1) {
                            calendar[i][j].setBackground(Color.RED);
                        }
                    }
                    weeks[i].add(calendar[i][j]);
                }
            }
            weeks[i].setBorder(new EmptyBorder(5, 5, 5, 30));
            calendarPanel.add(weeks[i]);
        }

        addCalendarListener(room, startDay);

        add(calendarPanel, BorderLayout.CENTER);
    }
    
    // Getters and Setters
    /**
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    /**
     * Setter for the discount code used by the controller
     * @param discountCode      the discount code the user provided
     */
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    // Event listeners
    /**
     * Adds a listener for each day button for selecting
     * the check-in day of the reservation. When a button
     * is clicked, commands the controller to let the user
     * select their check-out day, unless the check-in day
     * is invalid, in which case it throws an error.
     * @param room      room that the customer is trying to reserve
     */
    public void addCalendarListener(Room room) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    int availabilityStatus = room.checkDayAvailability(day);
                    int check;
                    if (day < 31) {
                        check = room.checkDayAvailability(day + 1);
                    }
                    else {
                        check = -1;
                    }
                    calendar[i][j].addActionListener(e -> {
                        if (day == 31){
                            JOptionPane.showMessageDialog(this, "Cannot check in at 31st day.", "Error", JOptionPane.WARNING_MESSAGE);
                        } else if (availabilityStatus == -1 || check == -1) {
                            JOptionPane.showMessageDialog(this, "Day is occupied, please pick another.", "Error", JOptionPane.WARNING_MESSAGE);
                        } else {
                            controller.switchToReserveEndPanel(room, cost, name, day);
                        }
                    });
                }
            }
        }
    }
    
    /**
     * Overloaded method that adds a listener for each day
     * button for selecting the check-out day of the
     * reservation. When a button is clicked, commands the
     * controller to get a discount code from the user
     * before finalizing the reservation, unless the check-out
     * day is invalid, in which case it throws an error.
     * @param room      room that the customer is trying to reserve
     * @param startDay  the check-in day the customer selected
     */
    public void addCalendarListener(Room room, int startDay) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    calendar[i][j].addActionListener(e -> {
                        if (startDay >= day){
                            JOptionPane.showMessageDialog(this, "The end day must be more than the starting day", "Error", JOptionPane.WARNING_MESSAGE);
                        } else if (room.checkDayAvailability2(startDay, day) == -1){
                            JOptionPane.showMessageDialog(this, "There are prior reservations in the range chosen.", "Error", JOptionPane.WARNING_MESSAGE);
                        } else {
                            controller.getDiscountCode(this);
                            controller.reserveRoomFinal(room, cost, name, startDay, day, discountCode);
                        }
                    });
                }
            }
        }
    }

    /**
     * Adds an event listener for the back button
     * at the top left of the GUI when selecting
     * a check-in day
     * @param listener      action that will happen when the back button is clicked
     */
    public void addBackButtonStartListener(ActionListener listener) {
        backButtonStart.addActionListener(listener);
    }

    /**
     * Adds an event listener for the back button
     * at the top left of the GUI when selecting
     * a check-out day
     * @param listener      action that will happen when the back button is clicked
     */
    public void addBackButtonEndListener(ActionListener listener) {
        backButtonEnd.addActionListener(listener);
    }
}
