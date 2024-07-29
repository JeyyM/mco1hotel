package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
/*
*
*
*
* This meant to copy the calendar but show buttons like
*  | 25 (1.0x) for each one |
*
*
* */
public class ViewReservationCalendarByRoom extends JPanel {
    private Room room;
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
    private int fullWidth = 1050;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private JPanel mainPanel;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    private JPanel[] weeks = new JPanel[5];
    private JButton[][] calendar = new JButton[5][7];

    private String name, discountCode;
    private float cost;
    private Hotel hotel;

    private MVC_Controller controller;

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

    //public ReserveSpecificHotelPanel(Hotel hotel, )

    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    public void addCalendarListener(Room room) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j;
                    calendar[i][j].addActionListener(e -> {
                        controller.showReservationsByRoomAndDate(room, day);
                    });
                }
            }
        }
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
