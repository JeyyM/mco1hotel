package mco2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

/**
 * GUI Panel that shows the reservation calendar of
 * a chosen room when the room is selected to be viewed.
 */
public class ViewReservationCalendarByRoom extends JPanel {
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
     * Constructor that creates a calendar-like format of
     * buttons for each day. Colors the buttons where a check-in
     * or a check-out happens in orange, while colors the days where
     * there are customers reserving the room in red.
     * @param room      the selected room to be viewed
     */
    public ViewReservationCalendarByRoom (Room room) {
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
        JLabel labelManageHotels = new JLabel("Select Day to View Reservation", JLabel.CENTER);
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
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    /**
     * Adds a listener for each day button by sending the information
     * back to the controller with both the room instance and the specific day
     * @param room      the room chosen to be viewed
     */
    public void addCalendarListener(Room room) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    calendar[i][j].addActionListener(e -> {
                        controller.showReservationsByRoomAndDate(room, day);
                    });
                }
            }
        }
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
