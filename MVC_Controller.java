package mco1;

import javax.swing.*;
import java.awt.*;

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

    /*
    For making warning pop-ups in hotel creation
    */
    public void showCreateHotelDialog() {
        // the this:: allows you to send functions to be ran
        Modals.showCreateHotelDialog(view, model, this::updateHotelCount, this::updateHotels);
    }

    public void showRenameHotelDialog(Hotel chosenHotel){
        // the updaters are so it changes on the current panel and the hotel selection list
        Modals.showRenameHotelDialog(view, model, chosenHotel, this::updateSpecificHotelPanel, this::updateHotels);
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
        view.getManageHotelsPanel().setController(this); // Ensure the controller is set
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
        view.getContentPane().removeAll();
        view.add(specificHotelPanel, BorderLayout.CENTER);
        view.setSize(manageSpecificHotelPanelWidth, manageSpecificHotelPanelHeight);
        view.setResizable(false);
        view.revalidate();
        view.repaint();
    }

    public void updateSpecificHotelPanel() {
        if (specificHotelPanel != null) {
            specificHotelPanel.updateHotelInfo();
        }
    }
}
