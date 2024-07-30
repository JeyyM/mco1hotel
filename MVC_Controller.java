package mco1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MVC_Controller {
    // Other MVC
    private MVC_Model model;
    private MVC_View view;

    // Sizes
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

    // Other panel's extensions
    private ManageSpecificHotelPanel specificHotelPanel;
    private ManageModifyRooms showRooms;
    private ManageDateMultiplier manageDateMultiplier;

    private Hotel tempHotel;

    // Updaters
    // To update the shown number in current hotels
    public void updateHotelCount() {
        view.getCurrentHotelsCount().setText(String.format("Current Hotels: %d", model.getHotelCount()));
    }

    // For setting the hotel count in ManageHotelsPanel so MVC
    public void updateAllPanels() {
        ManageHotelsPanel manageHotelsPanel = view.getManageHotelsPanel();
        if (manageHotelsPanel != null) {
            manageHotelsPanel.setHotels(model.getHotels());
        }

        ReserveHotelSelectPanel reservationsPanel = view.getReservationsPanel();
        if (reservationsPanel != null) {
            reservationsPanel.setHotels(model.getHotels());
        }

        ViewHotelsPanel viewHotelsPanel = view.getViewHotelsPanel();
        if (viewHotelsPanel != null) {
            viewHotelsPanel.setHotels(model.getHotels());
        }
    }

    // To update current hotel in specific hotel
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
    
    public void updateDateMultiplierCalendar(Hotel hotel) {
        manageDateMultiplier.recolorDays(hotel);
        manageDateMultiplier.revalidate();
        manageDateMultiplier.repaint();
    }

    // Pop-ups
    // the this:: allows you to pass functions, but it seems parameters cant be added

    public void showNoHotels(String action){
        String message = String.format("There are currently no hotels to %s.", action);
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.WARNING_MESSAGE);
    }

    public void showCreateHotelDialog() {
        Modals.showCreateHotelDialog(view, model, this::updateHotelCount, this::updateAllPanels);
    }

    public void showRenameHotelDialog(Hotel chosenHotel) {
        Modals.showRenameHotelDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateAllPanels);
    }

    public void showModifyBasePriceDialog(Hotel chosenHotel) {
        Modals.showModifyBasePriceDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateAllPanels);
    }

    // havent changed the update specific hotel
    public void showViewOptions(Hotel chosenHotel){
        Modals.showViewOptions(view, model, this, chosenHotel, this::updateSpecificHotelPanel, this::updateAllPanels);
    }
    
    public void switchToViewRoomsByDate(Hotel chosenHotel) {
        ViewRoomByDateCalendar viewRoomByDateCalendar = new ViewRoomByDateCalendar(chosenHotel);
        viewRoomByDateCalendar.setController(this);
        viewRoomByDateCalendar.addBackButtonListener(e -> switchToViewHotelsPanel());
        
        view.getContentPane().removeAll();
        view.add(viewRoomByDateCalendar, BorderLayout.CENTER);
        view.setSize(reserveCalendarWidth, reserveCalendarHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void viewReservedRoomsByDate(Hotel chosenHotel, int day) {
        Modals.showReservedRoomsByDate(view, chosenHotel, day);
    }
    
    public void selectRoomByAvailability(Hotel hotel) {
        CheckAvailabilityByRoomPanel showRooms = new CheckAvailabilityByRoomPanel(hotel);
        tempHotel = hotel;
        showRooms.setController(this);
        showRooms.addBackButtonListener(e -> switchToViewHotelsPanel());

        view.getContentPane().removeAll();
        view.add(showRooms, BorderLayout.CENTER);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void switchToViewCalendarFromRoom(Room room) {
        ViewReservationCalendarByRoom mainPanel = new ViewReservationCalendarByRoom(room);
        mainPanel.setController(this);
        mainPanel.addBackButtonListener(e -> selectRoomByAvailability(tempHotel));
        
        view.getContentPane().removeAll();
        view.add(mainPanel, BorderLayout.CENTER);
        view.setSize(reserveCalendarWidth, reserveCalendarHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void showReservationsByRoomAndDate(Room room, int day) {
        Modals.showReservedRoomsByRoom(view, room, day);
    }
    
    public void switchToViewReservations(Hotel hotel) {
        ViewReservations selectReservationsPanel = new ViewReservations(hotel);
        selectReservationsPanel.setController(this);
        tempHotel = hotel;
        selectReservationsPanel.addBackButtonListener(e -> switchToViewHotelsPanel());

        view.getContentPane().removeAll();
        JScrollPane scrollPane = new JScrollPane(selectReservationsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.add(scrollPane);
        view.setSize(manageHotelsPanelWidth, manageHotelsPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void switchToViewSelectedReservation(Reservation reservation, float roomType) {
        Modals.showReservationDetails(view, reservation, roomType);
    }


    public void showAddRoomsDialog(Hotel chosenHotel) {
        Modals.showAddRoomsDialog(view, model, chosenHotel, this::updateAllPanels, this::updateRoomsShown);
    }

    public void showEditRateDialog(Room chosenRoom) {
        Modals.showEditRateDialog(view, model, chosenRoom, this::updateAllPanels, this::updateRoomsShown);
    }

    public int showRemoveRoomsDialog(Hotel chosenHotel, ArrayList<String> toDelete) {
        int answer = 1;
        if (toDelete.size() > 0) {
            answer = Modals.showRemoveRoomsDialog(view, model, chosenHotel, toDelete, this::updateAllPanels, this::updateRoomsShown);
        }
        return answer;
    }

    public void showDeleteHotelDialog(ArrayList<Hotel> hotels, Hotel chosenHotel) {
        int answer = 1;

        answer = Modals.showDeleteHotelDialog(view, model, hotels, chosenHotel, this::updateAllPanels, this::updateAllPanels);

        if (answer == 0) {
            switchToManageHotelsPanel();
            updateHotelCount();
        }
    }

    // To switch to different panels
    public void switchToMainPanel() {
        view.getContentPane().removeAll();
        view.add(view.getMainPanel(), BorderLayout.CENTER);
        view.setSize(mainPanelWidth, mainPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToViewHotelsPanel() {
        if (model.getHotels().size() == 0){
            showNoHotels("View");
        } else {
            view.getContentPane().removeAll();
            if (view.getViewHotelsPanel() == null) {
                view.setViewHotelsPanel(new ViewHotelsPanel(model.getHotels(), model.getManager()));
            }

            // Back button
            view.getViewHotelsPanel().addBackButtonListener(e -> {
                switchToMainPanel();
            });

            view.getViewHotelsPanel().setController(this);

            JScrollPane scrollPane = new JScrollPane(view.getViewHotelsPanel());
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            view.add(scrollPane, BorderLayout.CENTER);
            view.setSize(viewHotelsPanelWidth, viewHotelsPanelHeight);
            view.setResizable(false);
            view.revalidate();
            view.repaint();
        }
    }

    public void switchToManageHotelsPanel() {
        if (model.getHotels().size() == 0){
            showNoHotels("Manage");
        } else {
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
        specificHotelPanel.addDateMultiplierListener(e -> {
            switchToDateMultiplierPanel(hotel);
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

    public void switchToDateMultiplierPanel(Hotel hotel) {
        manageDateMultiplier = new ManageDateMultiplier(hotel);
        manageDateMultiplier.setController(this);
        manageDateMultiplier.addBackButtonListener(e -> switchToSpecificHotelPanel(hotel));
        
        view.getContentPane().removeAll();
        view.add(manageDateMultiplier, BorderLayout.CENTER);
        view.setSize(reserveCalendarWidth, reserveCalendarHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }
    
    public void changeDayMultiplier(Hotel hotel, int day) {
        Modals.showChangeDayMultiplierDialog(view, model, hotel, day);
        updateDateMultiplierCalendar(hotel);
        System.out.printf("Hotel %s day %d has multiplier %.2f\n", hotel.getName(), day + 1, hotel.getDayMultipliers()[day]);
    }
    
    public void switchToShowRooms(Hotel hotel) {
        showRooms = new ManageModifyRooms(hotel);
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
        if (model.getHotels().size() == 0){
            showNoHotels("Reserve");
        } else {
            view.getContentPane().removeAll();
            if (view.getReservationsPanel() == null) {
                view.setReservationsPanel(new ReserveHotelSelectPanel(model.getHotels(), model.getManager()));
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
    }

    public void switchToReserveSpecificRoomPanel(Hotel hotel, String name) {
        ReserveRoomSelectPanel roomReservationsPanel = new ReserveRoomSelectPanel(hotel, name);
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

    public void switchToViewAllReservedRooms(Hotel hotel) {
        ManageRemoveReserveRoomChoice reservedRoomsPanel = new ManageRemoveReserveRoomChoice(hotel);
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
        ManageRemoveReservationChoice removeReservationsPanel = new ManageRemoveReservationChoice(room);
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
    
    public void getDiscountCode(ReserveCalendar discountCodeStorage) {
        Modals.showDiscountDialog(view, discountCodeStorage);
    }

    public void reserveRoomFinal(Room room, float cost, String name, int startDay, int endDay, String discountCode) {
        int answer = Modals.showReserveRoomDialog(view, model, tempHotel, room, name, cost, startDay, endDay, discountCode);

        if (answer == JOptionPane.YES_OPTION) {
            switchToMainPanel();
        }
    }


    public void removeReservationFinal(Reservation reservation) {
        int answer = Modals.showRemoveReservationDialog(view, model, tempHotel, reservation);

        if (answer == JOptionPane.YES_OPTION) {
            switchToMainPanel();
        }
    }

    public MVC_Controller(MVC_Model model, MVC_View view) {
        // Initialize MVC
        this.model = model;
        this.view = view;

        // Setting controllers on panels that need it
        if (view.getManageHotelsPanel() != null) {
            view.getManageHotelsPanel().setController(this);
        }

        if (view.getReservationsPanel() != null) {
            view.getReservationsPanel().setController(this);
        }

        // Event listeners
        view.addCreateHotelListener(e -> showCreateHotelDialog());

        view.addViewHotelListener(e -> {
            switchToViewHotelsPanel();
            updateAllPanels();
        });

        view.addManageHotelListener(e -> {
            switchToManageHotelsPanel();
            updateAllPanels();
        });

        view.addReserveHotelListener(e -> {
            switchToReservationsPanel();
            updateAllPanels();
        });
    }
}
