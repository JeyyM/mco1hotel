package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ReserveCalendar extends JPanel {
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
    private int fullWidth = 450;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private JPanel mainPanel;
    private int northHeight = 40;
    private int northLabelFontSize = 20;
    
    private JPanel[] weeks = new JPanel[5];
    private JButton[][] calendar = new JButton[5][7];
    
    private String name;
    private float cost;
    
    private MVC_Controller controller;

    public ReserveCalendar(Room room, float cost, String name) {
        this.room = room;
        this.name = name;
        this.cost = cost;
        setLayout(new GridLayout(6, 1));
        
        JLabel calendarTitle = new JLabel("Select Start of Reservation");
        
        this.add(calendarTitle);
        
        for (int i = 0; i < 5; i++) {
            // System.out.println("hi");
            weeks[i] = new JPanel(new GridLayout(1, 7, 10, 10));
            for (int j = 0; j < 7; j++) {
                if (i == 4 && j > 2) {
                    JPanel whiteSpace = new JPanel();
                    weeks[i].add(whiteSpace);
                }
                else {
                    calendar[i][j] = new JButton("" + (i * 7 + j + 1));
                    weeks[i].add(calendar[i][j]);
                }
            }
            this.add(weeks[i]);
        }
        
        addCalendarListener(room);
    }
    
    public ReserveCalendar(Room room, float cost, String name, int startDay) {
        this.room = room;
        this.name = name;
        this.cost = cost;
        setLayout(new GridLayout(6, 1));
        
        JLabel calendarTitle = new JLabel("Select End of Reservation");
        
        this.add(calendarTitle);
        
        for (int i = 0; i < 5; i++) {
            // System.out.println("hi");
            weeks[i] = new JPanel(new GridLayout(1, 7));
            for (int j = 0; j < 7; j++) {
                if (i == 4 && j > 2) {
                    JPanel whiteSpace = new JPanel();
                    weeks[i].add(whiteSpace);
                }
                else {
                    if ((i * 7 + j + 1) == startDay) {
                        calendar[i][j] = new JButton(">" + (i * 7 + j + 1) + "<");
                    }
                    else {
                        calendar[i][j] = new JButton("" + (i * 7 + j + 1));
                    }
                    weeks[i].add(calendar[i][j]);
                }
            }
            this.add(weeks[i]);
        }
        
        addCalendarListener(room, startDay);
    }
    
    //public ReserveSpecificHotelPanel(Hotel hotel, )
    
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    public void addCalendarListener(Room room) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    calendar[i][j].addActionListener(e -> {
                        controller.switchToReserveEndPanel(room, cost, name, day);
                    });
                }
            }
        }
        
        /*
        ArrayList<Reservation> reservations = room.getReservations();
        
        for (Reservation reservation : reservations) {
            
        }
        */
    }
    
    public void addCalendarListener(Room room, int startDay) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j + 1;
                    calendar[i][j].addActionListener(e -> {
                        controller.reserveRoomFinal(room, cost, name, startDay, day);
                    });
                }
            }
        }
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

    public void addDeleteHotelButtonListener(ActionListener listener) {
        deleteHotelButton.addActionListener(listener);
    }
}
