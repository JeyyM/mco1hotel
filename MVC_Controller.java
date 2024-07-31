package mco1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main controller of the whole program and connects
 * all the functions and panels together.
 */
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

    /**
     * Constructor for the controller class.
     * Takes in the model and view as parameters
     * to be able to interact with both of them.
     * @param model     the model of the program
     * @param view      the view of the program
     */
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
    
    // Updaters
    // To update the shown number in current hotels
    /**
     * Function that updates the number of hotels.
     * Called when a hotel is created or deleted.
     */
    public void updateHotelCount() {
        view.getCurrentHotelsCount().setText(String.format("Current Hotels: %d", model.getHotelCount()));
    }

    // For setting the hotel count in ManageHotelsPanel so MVC
    /**
     * Updates the list of hotels for all the different panels
     */
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
    /**
     * Updates the manage hotel panel for a specific
     * hotel to refresh the updated information
     */
    public void updateSpecificHotelPanel() {
        if (specificHotelPanel != null) {
            specificHotelPanel.updateHotelInfo();
        }
    }

    // To update the current rooms when in specific
    /**
     * Updates the rooms being shown in different panels
     */
    public void updateRoomsShown() {
        if (showRooms != null) {
            showRooms.updateShowRoomInfo();
        }
    }
    
    /**
     * Updates the colors when the date multiplier of a hotel
     * gets changed.
     * @param hotel     hotel that had its date multiplier changed
     */
    public void updateDateMultiplierCalendar(Hotel hotel) {
        manageDateMultiplier.recolorDays(hotel);
        manageDateMultiplier.revalidate();
        manageDateMultiplier.repaint();
    }

    // Pop-ups
    // the this:: allows you to pass functions, but it seems parameters cant be added

    /**
     * Error message that is shown when there are no hotels for
     * a specified action chosen.
     * @param action    the action chosen, could be view, manage, or reserve
     */
    public void showNoHotels(String action){
        String message = String.format("There are currently no hotels to %s.", action);
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Calls the dialog needed for creating a hotel
     */
    public void showCreateHotelDialog() {
        Modals.showCreateHotelDialog(view, model, this::updateHotelCount, this::updateAllPanels);
    }

    /**
     * Calls the dialog needed for renaming a hotel
     * @param chosenHotel   the hotel being renamed
     */
    public void showRenameHotelDialog(Hotel chosenHotel) {
        Modals.showRenameHotelDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateAllPanels);
    }

    /**
     * Calls the dialog needed for changing the base price of a hotel
     * @param chosenHotel   the hotel with the base price being changed
     */
    public void showModifyBasePriceDialog(Hotel chosenHotel) {
        Modals.showModifyBasePriceDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateAllPanels);
    }

    // havent changed the update specific hotel
    /**
     * Shows the options for viewing a chosen hotel
     * @param chosenHotel   the hotel being viewed
     */
    public void showViewOptions(Hotel chosenHotel){
        Modals.showViewOptions(view, this, chosenHotel);
    }
    
    /**
     * Creates a panel that shows a calendar which shows the number
     * of rooms available and not per day
     * @param chosenHotel   the hotel being viewed
     */
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
    
    /**
     * Calls the dialog for viewing which rooms are reserved
     * in the chosen hotel for a specific day
     * @param chosenHotel   the hotel being viewed
     * @param day           the day chosen to be checked
     */
    public void viewReservedRoomsByDate(Hotel chosenHotel, int day) {
        Modals.showReservedRoomsByDate(view, chosenHotel, day);
    }
    
    /**
     * Creates the GUI for showing all the rooms of a
     * hotel and the number of reservations in each room.
     * @param hotel     the hotel being viewed
     */
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
    
    /**
     * Shows the reservation calendar of a room chosen to be viewed
     * @param room      the room being viewed
     */
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
    
    /**
     * Calls the dialog that shows the name of the customer who
     * reserved a specific room at a certain day chosen
     * @param room  the room being viewed
     * @param day   the day chosen in the room's reservation calendar
     */
    public void showReservationsByRoomAndDate(Room room, int day) {
        Modals.showReservedRoomsByRoom(view, room, day);
    }
    
    /**
     * Creates the GUI for viewing all the reservations in
     * a chosen hotel that displays most of the information
     * of each reservation.
     * @param hotel     the hotel being viewed
     */
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
    
    /**
     * Calls the dialog for viewing all the details
     * for a chosen reservation in the hotel
     * @param reservation   the reservation being viewed
     * @param roomType      the multiplier of the room reserved
     */
    public void switchToViewSelectedReservation(Reservation reservation, float roomType) {
        Modals.showReservationDetails(view, reservation, roomType);
    }

    /**
     * Calls the dialog needed for adding a room
     * @param chosenHotel   the hotel having rooms added to it
     */
    public void showAddRoomsDialog(Hotel chosenHotel) {
        Modals.showAddRoomsDialog(view, model, chosenHotel, this::updateAllPanels, this::updateRoomsShown);
    }

    /**
     * Calls the dialog needed for editing the rate of a room
     * @param chosenRoom    the room having its multiplier changed
     */
    public void showEditRateDialog(Room chosenRoom) {
        Modals.showEditRateDialog(view, model, chosenRoom, this::updateAllPanels, this::updateRoomsShown);
    }

    /**
     * Calls the dialog needed for removing chosen rooms in a hotel
     * @param chosenHotel   the hotel having rooms deleted
     * @param toDelete      the list of names of the rooms being deleted
     * @return              whether the deletion was confirmed or cancelled
     */
    public int showRemoveRoomsDialog(Hotel chosenHotel, ArrayList<String> toDelete) {
        int answer = 1;
        if (toDelete.size() > 0) {
            answer = Modals.showRemoveRoomsDialog(view, model, chosenHotel, toDelete, this::updateAllPanels, this::updateRoomsShown);
        }
        return answer;
    }

    /**
     * Calls the dialog needed for confirming the deletion of a hotel
     * @param hotels        the list of the all the hotels from where the hotel will be removed from if deleted
     * @param chosenHotel   the hotel being deleted
     */
    public void showDeleteHotelDialog(ArrayList<Hotel> hotels, Hotel chosenHotel) {
        int answer = 1;

        answer = Modals.showDeleteHotelDialog(view, model, hotels, chosenHotel, this::updateAllPanels);

        if (answer == 0) {
            updateHotelCount();

            if (hotels.size() > 0){
                switchToManageHotelsPanel();
            } else {
                switchToMainPanel();
            }
        }
    }

    // To switch to different panels
    /**
     * Switches the GUI back to the main panel with all the options
     */
    public void switchToMainPanel() {
        view.getContentPane().removeAll();
        view.add(view.getMainPanel(), BorderLayout.CENTER);
        view.setSize(mainPanelWidth, mainPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    /**
     * Switches the GUI to displaying the hotels for viewing
     */
    public void switchToViewHotelsPanel() {
        if (model.getHotels().isEmpty()){
            showNoHotels("View");
        } else {
            view.getContentPane().removeAll();
            if (view.getViewHotelsPanel() == null) {
                view.setViewHotelsPanel(new ViewHotelsPanel(model.getHotels()));
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

    /**
     * Switches the GUI to displaying the hotels for managing
     */
    public void switchToManageHotelsPanel() {
        if (model.getHotels().isEmpty()){
            showNoHotels("Manage");
        } else {
            view.getContentPane().removeAll();
            if (view.getManageHotelsPanel() == null) {
                view.setManageHotelsPanel(new ManageHotelsPanel(model.getHotels()));
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
    
    /**
     * Switches the GUI to managing a specific hotel
     * @param hotel     the hotel chosen to be managed
     */
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

    /**
     * Switches the GUI to show the calendar of the day
     * multipliers of the hotel to be changed
     * @param hotel     the hotel being managed
     */
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
    
    /**
     * Calls the dialog needed to change the day multiplier of a hotel
     * @param hotel     the hotel with its day multipliers being changed
     * @param day       the day selected to have the multiplier changed
     */
    public void changeDayMultiplier(Hotel hotel, int day) {
        Modals.showChangeDayMultiplierDialog(view, model, hotel, day);
        updateDateMultiplierCalendar(hotel);
    }
    
    /**
     * Switches the GUI to show the rooms of the chosen hotel to be managed
     * @param hotel     the hotel that will have its rooms shown
     */
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

    /**
     * Switches the GUI to the reservation panel
     * where the user can reserve a hotel
     */
    public void switchToReservationsPanel() {
        if (model.getHotels().size() == 0){
            showNoHotels("Reserve");
        } else {
            view.getContentPane().removeAll();
            if (view.getReservationsPanel() == null) {
                view.setReservationsPanel(new ReserveHotelSelectPanel(model.getHotels()));
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

    /**
     * Switches the GUI to show all the rooms of the hotel
     * selected to be reserved
     * @param hotel     the hotel being reserved
     * @param name      the name of the customer
     */
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

    /**
     * Switches the GUI to the reservation calendar of
     * the room chosen. Used to determine the customer's
     * check-in day
     * @param room      room being reserved
     * @param cost      base price of the hotel
     * @param name      name of the customer
     */
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

    /**
     * Switches the GUI to the reservation calendar of
     * the room chosen. Used to determine the customer's
     * check-out day
     * @param room      room being reserved
     * @param cost      base price of the hotel
     * @param name      name of the customer
     * @param startDay  check-in day of the customer
     */
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

    /**
     * Calls the dialog needed to prompt the user for a discount code
     * @param discountCodeStorage   stores all the information regarding the reservation
     */
    public void getDiscountCode(ReserveCalendar discountCodeStorage) {
        Modals.showDiscountDialog(view, discountCodeStorage);
    }
    
    /**
     * Calls the dialog needed to confirm if the reservation details are correct
     * @param room          room being reserved
     * @param cost          base cost of the hotel
     * @param name          name of the customer
     * @param startDay      check-in day of the customer
     * @param endDay        check-out day of the customer
     * @param discountCode  discount code provided by the customer
     */
    public void reserveRoomFinal(Room room, float cost, String name, int startDay, int endDay, String discountCode) {
        int answer = Modals.showReserveRoomDialog(view, model, tempHotel, room, name, cost, startDay, endDay, discountCode);

        if (answer == JOptionPane.YES_OPTION) {
            switchToMainPanel();
        }
    }
    
    /**
     * Switches the GUI to view all the reserved rooms in
     * a hotel for the purpose of removing a reservation
     * @param hotel     the hotel being managed
     */
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
    
    /**
     * Switches the GUI to view all the reservations in
     * a specific room in a hotel with the purpose
     * of removing a reservation
     * @param room      the room selected to be managed
     */
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

    /**
     * Calls the dialog needed to confirm if the reservation being removed is correct
     * @param reservation   the reservation being removed
     */
    public void removeReservationFinal(Reservation reservation) {
        int answer = Modals.showRemoveReservationDialog(view, model, tempHotel, reservation);

        if (answer == JOptionPane.YES_OPTION) {
            switchToMainPanel();
        }
    }
}
