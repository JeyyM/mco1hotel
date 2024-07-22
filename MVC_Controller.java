package mco1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MVC_Controller {
    private MVC_Model model;
    private MVC_View view;

    private int mainPanelWidth= 450;
    private int mainPanelHeight= 500;

    private int manageHotelsPanelWidth = 680;
    private int manageHotelsPanelHeight = 500;

    private int manageSpecificHotelPanelWidth = 450;
    private int manageSpecificHotelPanelHeight = 500;

    private ManageSpecificHotelPanel specificHotelPanel;
    private ShowRooms showRooms;

    private String[] toDelete;

    public MVC_Controller(MVC_Model model, MVC_View view) {
        this.model = model;
        this.view = view;

        view.addCreateHotelListener(e -> showCreateHotelDialog());
        view.addManageHotelListener(e -> {
            switchToManageHotelsPanel();
            updateHotels();
        });

        if (view.getManageHotelsPanel() != null) {
            view.getManageHotelsPanel().setController(this);
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

    public int showRemoveRoomsDialog(Hotel chosenHotel, ArrayList<String> toDelete) {
        int answer = 1;
        if (toDelete.size() > 0) {
            answer = Modals.showRemoveRoomsDialog(view, model, chosenHotel, toDelete, this::updateHotels, this::updateRoomsShown);
        }

        return answer;
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
        specificHotelPanel.addModifyBasePriceButtonListener(e -> showModifyBasePriceDialog(hotel));
        view.getContentPane().removeAll();
        view.add(specificHotelPanel, BorderLayout.CENTER);
        view.setSize(manageSpecificHotelPanelWidth, manageSpecificHotelPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void switchToShowRooms(Hotel hotel) {
        showRooms = new ShowRooms(hotel);
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
}