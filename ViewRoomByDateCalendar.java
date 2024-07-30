package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

/**
 * GUI Panel that shows a calendar of days displaying
 * how many rooms are available on each specific day.
 */
public class ViewRoomByDateCalendar extends JPanel {
    private JButton backButton;

    private JPanel panelNorth = new JPanel();
    private int fullWidth = 1050;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    private JPanel[] weeks = new JPanel[5];
    private JButton[][] calendar = new JButton[5][7];

    private MVC_Controller controller;

    /**
     * Constructor for the panel that shows how many available rooms
     * there are in a specific hotel per day. Uses a different function
     * to add listeners to each day button.
     * @param hotel     the hotel chosen to have the number of rooms available shown per day
     */
    public ViewRoomByDateCalendar (Hotel hotel) {
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
        JLabel labelManageHotels = new JLabel("Select Day to View Reserved Rooms", JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new GridLayout(5, 1));

        for (int i = 0; i < 5; i++) {
            // System.out.println("hi");
            weeks[i] = new JPanel(new GridLayout(1, 7));
            for (int j = 0; j < 7; j++) {
                if (i == 4 && j > 2) {
                    JPanel whiteSpace = new JPanel();
                    weeks[i].add(whiteSpace);
                }
                else {
                    int roomsReserved = hotel.getReservedRoomsByDay(i * 7 + j + 1).size();
                    calendar[i][j] = new JButton("<html>" + (i * 7 + j + 1) + "<br>(" + (hotel.getAllRooms().size() - roomsReserved) + " Rooms<br>" +  "Available)</html>");
                    calendar[i][j].setFont(new Font(new JButton().getFont().getFamily(), Font.PLAIN, 10));

                    if (roomsReserved != 0) {
                        calendar[i][j].setBackground(Color.decode("#9DF69C"));
                    }

                    weeks[i].add(calendar[i][j]);
                }
            }
            weeks[i].setBorder(new EmptyBorder(5, 5, 5, 30));
            calendarPanel.add(weeks[i]);
        }

        addCalendarListener(hotel);

        add(calendarPanel, BorderLayout.CENTER);
    }

    /**
     * Adds a listener to each day button to command the controller
     * to show the rooms reserved on a specific day in a hotel's calendar.
     * @param hotel     hotel chosen to be viewed
     */
    public void addCalendarListener(Hotel hotel) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    calendar[i][j].addActionListener(e -> {
                        controller.viewReservedRoomsByDate(hotel, day);
                    });
                }
            }
        }
    }

    /**
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
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
