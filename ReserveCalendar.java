package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

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
    private String name;
    private float cost;

    private MVC_Controller controller;

    // Getters and Setters
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }

    // Event listeners
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
                            controller.reserveRoomFinal(room, cost, name, startDay, day);
                        }
                    });
                }
            }
        }
    }

    public void addBackButtonStartListener(ActionListener listener) {
        backButtonStart.addActionListener(listener);
    }

    public void addBackButtonEndListener(ActionListener listener) {
        backButtonEnd.addActionListener(listener);
    }

    public void addCalendarListener(Room room) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    int availabilityStatus = room.checkDayAvailability(day);
                    calendar[i][j].addActionListener(e -> {
                        if (day == 31){
                            JOptionPane.showMessageDialog(this, "Cannot check in at 31st day.", "Error", JOptionPane.WARNING_MESSAGE);
                        } else if (availabilityStatus == -1) {
                            JOptionPane.showMessageDialog(this, "Day is occupied, please pick another.", "Error", JOptionPane.WARNING_MESSAGE);
                        } else {
                            controller.switchToReserveEndPanel(room, cost, name, day);
                        }
                    });
                }
            }
        }
    }

    /*
    * FOR STARTING DAY
    * */
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

    /*
    * FOR END DAY
    * */
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
}
