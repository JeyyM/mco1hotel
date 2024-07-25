package mco1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MVC_Controller {
    private MVC_Model model;
    private MVC_View view;

    private int mainPanelWidth= 450;
    private int mainPanelHeight= 500;

    private int viewHotelsPanelWidth = 450;
    private int viewHotelsPanelHeight = 500;

    private int manageHotelsPanelWidth = 680;
    private int manageHotelsPanelHeight = 500;

    private int manageSpecificHotelPanelWidth = 450;
    private int manageSpecificHotelPanelHeight = 500;

    private int reserveCalendarWidth = 680;
    private int reserveCalendarHeight = 500;

    private ManageSpecificHotelPanel specificHotelPanel;
    private ShowRooms showRooms;

    private String[] toDelete;

    private Hotel tempHotel;

    public MVC_Controller(MVC_Model model, MVC_View view) {
        this.model = model;
        this.view = view;

        view.addCreateHotelListener(e -> showCreateHotelDialog());

        view.addViewHotelListener(e -> {
            switchToViewHotelsPanel();
            updateHotels();
        });

        view.addManageHotelListener(e -> {
            switchToManageHotelsPanel();
            updateHotels();
        });

        if (view.getManageHotelsPanel() != null) {
            view.getManageHotelsPanel().setController(this);
        }

        view.addReserveHotelListener(e -> {
            switchToReservationsPanel();
            updateHotels();
        });
        if (view.getReservationsPanel() != null) {
            view.getReservationsPanel().setController(this);
        }

    }

    // Updaters
    // To update the shown number
    public void updateHotelCount() {
        view.getCurrentHotelsCount().setText(String.format("Current Hotels: %d", model.getHotelCount()));
    }

    // For setting the hotel count in ManageHotelsPanel so MVC
    public void updateHotels() {
        ManageHotelsPanel manageHotelsPanel = view.getManageHotelsPanel();
        if (manageHotelsPanel != null) {
            manageHotelsPanel.setHotels(model.getHotels());
        }

        ReservationsPanel reservationsPanel = view.getReservationsPanel();
        if (reservationsPanel != null) {
            reservationsPanel.setHotels(model.getHotels());
        }

        ViewHotelsPanel viewHotelsPanel = view.getViewHotelsPanel();
        if (viewHotelsPanel != null) {
            viewHotelsPanel.setHotels(model.getHotels());
        }
    }

    // To update current hotel in panel
    public void updateSpecificHotelPanel() {
        if (specificHotelPanel != null) {
            specificHotelPanel.updateHotelInfo();
        }
    }

    // To update the current rooms when in specific
    public void updateRoomsShown() {
        if (showRooms != null) {
            showRooms.updateShowRoomInfo();
        }
    }

    /*
    For making warning pop-ups in hotel creation
    */
    public void showCreateHotelDialog() {
        // the this:: allows you to send functions to be ran
        Modals.showCreateHotelDialog(view, model, this::updateHotelCount, this::updateHotels);
    }

    public void showRenameHotelDialog(Hotel chosenHotel) {
        // the updaters are so it changes on the current panel and the hotel selection list
        Modals.showRenameHotelDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateHotels);
    }

    public void showModifyBasePriceDialog(Hotel chosenHotel) {
        Modals.showModifyBasePriceDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateHotels);
    }

    public void showAddRoomsDialog(Hotel chosenHotel) {
        Modals.showAddRoomsDialog(view, model, chosenHotel, this::updateHotels, this::updateRoomsShown);
    }

    public void showEditRateDialog(Room chosenRoom) {
        Modals.showEditRateDialog(view, model, chosenRoom, this::updateHotels, this::updateRoomsShown);
    }

    public int showRemoveRoomsDialog(Hotel chosenHotel, ArrayList<String> toDelete) {
        int answer = 1;
        if (toDelete.size() > 0) {
            answer = Modals.showRemoveRoomsDialog(view, model, chosenHotel, toDelete, this::updateHotels, this::updateRoomsShown);
        }
        return answer;
    }

    public void showDeleteHotelDialog(ArrayList<Hotel> hotels, Hotel chosenHotel) {
        int answer = 1;

        answer = Modals.showDeleteHotelDialog(view, model, hotels, chosenHotel, this::updateHotels, this::updateHotels);

        if (answer == 0) {
            switchToManageHotelsPanel();
            updateHotelCount();
        }
    }

    // Switchers
    public void switchToMainPanel() {
        view.getContentPane().removeAll();
        view.add(view.getMainPanel(), BorderLayout.CENTER);
        view.setSize(mainPanelWidth, mainPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToViewHotelsPanel() {
        view.getContentPane().removeAll();
        if (view.getViewHotelsPanel() == null) {
            view.setViewHotelsPanel(new ViewHotelsPanel(model.getHotels(), model.getManager()));
        }
        view.getViewHotelsPanel().setController(this);
        view.getViewHotelsPanel().addBackButtonListener(e -> {
            switchToMainPanel();
        });
        JScrollPane scrollPane = new JScrollPane(view.getViewHotelsPanel());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane, BorderLayout.CENTER);
        view.setSize(viewHotelsPanelWidth, viewHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToManageHotelsPanel() {
        view.getContentPane().removeAll();
        if (view.getManageHotelsPanel() == null) {
            view.setManageHotelsPanel(new ManageHotelsPanel(model.getHotels(), model.getManager()));
        }
        view.getManageHotelsPanel().setController(this);
        view.getManageHotelsPanel().addBackButtonListener(e -> {
            switchToMainPanel();
        });
        JScrollPane scrollPane = new JScrollPane(view.getManageHotelsPanel());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane, BorderLayout.CENTER);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToSpecificHotelPanel(Hotel hotel) {
        specificHotelPanel = new ManageSpecificHotelPanel(hotel);
        specificHotelPanel.addBackButtonListener(e -> switchToManageHotelsPanel());
        specificHotelPanel.addRenameHotelButtonListener(e -> showRenameHotelDialog(hotel));
        specificHotelPanel.addModifyRoomsButtonListener(e -> switchToShowRooms(hotel));
        specificHotelPanel.addModifyBasePriceButtonListener(e -> {
            if (hotel.getTotalReservationCount() == 0){
                showModifyBasePriceDialog(hotel);
            } else {
                JOptionPane.showMessageDialog(specificHotelPanel, "There is at least one reservation, cannot change base price.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        specificHotelPanel.addRemoveReservationsButtonListener(e -> {
            if (hotel.getTotalReservationCount() == 0) {
                JOptionPane.showMessageDialog(specificHotelPanel, "There are no reservations in this hotel.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            else {
                switchToViewAllReservedRooms(hotel);
            }
        });
        specificHotelPanel.addDeleteHotelButtonListener(e -> {
            ArrayList<Hotel> hotels = model.getHotels();
            showDeleteHotelDialog(hotels, hotel);
        });

        view.getContentPane().removeAll();
        view.add(specificHotelPanel, BorderLayout.CENTER);
        view.setSize(manageSpecificHotelPanelWidth, manageSpecificHotelPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToShowRooms(Hotel hotel) {
        showRooms = new ShowRooms(hotel);
        showRooms.setController(this);
        showRooms.addBackButtonListener(e -> switchToSpecificHotelPanel(hotel));
        showRooms.addAddButtonListener(e -> showAddRoomsDialog(hotel));

        showRooms.addRemoveButtonListener(e -> {
            if (showRemoveRoomsDialog(hotel, showRooms.getToDelete()) == 0){
                showRooms.resetRemove();
            }
        });

        view.getContentPane().removeAll();
        view.add(showRooms, BorderLayout.CENTER);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToReservationsPanel() {
        view.getContentPane().removeAll();
        if (view.getReservationsPanel() == null) {
            view.setReservationsPanel(new ReservationsPanel(model.getHotels(), model.getManager()));
        }
        view.getReservationsPanel().setController(this);
        view.getReservationsPanel().addBackButtonListener(e -> {
            switchToMainPanel();
        });
        JScrollPane scrollPane = new JScrollPane(view.getReservationsPanel());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane, BorderLayout.CENTER);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToReserveSpecificRoomPanel(Hotel hotel, String name) {
        RoomReservationsPanel roomReservationsPanel = new RoomReservationsPanel(hotel, name);
        roomReservationsPanel.setController(this);
        tempHotel = hotel;
        roomReservationsPanel.addBackButtonListener(e -> switchToReservationsPanel());

        view.getContentPane().removeAll();
        JScrollPane scrollPane = new JScrollPane(roomReservationsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToReserveCalendarStart(Room room, float cost, String name) {
        ReserveCalendar reserveStart = new ReserveCalendar(room, cost, name);
        reserveStart.setController(this);
        reserveStart.addBackButtonStartListener(e -> switchToReserveSpecificRoomPanel(tempHotel, name));

        view.getContentPane().removeAll();
        view.add(reserveStart, BorderLayout.CENTER);
        view.setSize(reserveCalendarWidth, reserveCalendarHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToReserveEndPanel(Room room, float cost, String name, int startDay) {
        System.out.println("Start day : " + startDay);

        ReserveCalendar reserveEnd = new ReserveCalendar(room, cost, name, startDay);
        reserveEnd.setController(this);
        reserveEnd.addBackButtonEndListener(e -> switchToReserveCalendarStart(room, cost, name));

        view.getContentPane().removeAll();
        view.add(reserveEnd, BorderLayout.CENTER);
        view.setSize(reserveCalendarWidth, reserveCalendarHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void reserveRoomFinal(Room room, float cost, String name, int startDay, int endDay) {
        int answer = Modals.showReserveRoomDialog(view, model, tempHotel, room, name, cost, startDay, endDay);

        if (answer == JOptionPane.YES_OPTION) {
            switchToMainPanel();
        }
    }

    public void switchToViewAllReservedRooms(Hotel hotel) {
        ViewAllReservedRoomsPanel reservedRoomsPanel = new ViewAllReservedRoomsPanel(hotel);
        reservedRoomsPanel.setController(this);
        tempHotel = hotel;
        reservedRoomsPanel.addBackButtonListener(e -> switchToSpecificHotelPanel(hotel));
        
        view.getContentPane().removeAll();
        JScrollPane scrollPane = new JScrollPane(reservedRoomsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void switchToRemoveReservations(Room room) {
        RemoveReservationsPanel removeReservationsPanel = new RemoveReservationsPanel(room);
        removeReservationsPanel.setController(this);
        removeReservationsPanel.addBackButtonListener(e -> switchToViewAllReservedRooms(tempHotel));
        
        view.getContentPane().removeAll();
        JScrollPane scrollPane = new JScrollPane(removeReservationsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void removeReservationFinal(Reservation reservation) {
        int answer = Modals.showRemoveReservationDialog(view, model, tempHotel, reservation);

        if (answer == JOptionPane.YES_OPTION) {
            switchToMainPanel();
        }
    }
}
