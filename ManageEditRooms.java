package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
* For adding and removing rooms as well as changing the room type
* */
public class ManageEditRooms extends JPanel {
    private MVC_Controller controller;
    private Hotel chosenHotel;
    private ArrayList<Room> rooms;
    private boolean removeMode;
    private ArrayList<String> toDelete = new ArrayList<>();

    // Panel components
    private JButton backButton;
    private JPanel panelCenter;
    private JPanel panelNorth;
    private JLabel labelManageHotels;
    private JPanel modifyButtons;
    private JButton addButton = new JButton("Add Rooms");
    private JButton removeButton = new JButton("Remove Rooms");

    // Size variables
    private int fullWidth = 680;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int rowHeight = 80;
    private int buttonWidth = 110;
    private int buttonHeight = 80;
    private int northLabelFontSize = 20;

    // Updaters
    public void updateShowRoomInfo() {
        labelManageHotels.setText("Modify Rooms (" + chosenHotel.getTotalRooms() + ")");
        initializeRows();
        revalidate();
        repaint();
    }

    public void updateRemoveButtonText() {
        if (toDelete.size() > 0) {
            removeButton.setText("Complete Deletion");
        } else {
            removeButton.setText("Cancel Removal");
        }
    }

    // Getters and  Setters
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }

    // For refreshing the hotels every enter
    public void setHotel(Hotel hotelUpdate) {
        chosenHotel = hotelUpdate;
        initializeRows();
    }

    // Remove mode Functions
    public void setRemoveMode() {
        removeMode = !removeMode;
        if (removeMode) {
            panelNorth.setBackground(Color.decode("#970606"));
            modifyButtons.setBackground(Color.decode("#970606"));
            labelManageHotels.setText("Select Rooms to Remove");
            removeButton.setText("Cancel Removal");
            addButton.setEnabled(false);
        } else {
            panelNorth.setBackground(Color.decode("#063970"));
            modifyButtons.setBackground(Color.decode("#063970"));
            labelManageHotels.setText("Modify Rooms (" + chosenHotel.getTotalRooms() + ")");
            removeButton.setText("Remove Rooms");
            addButton.setEnabled(true);

            toDelete.clear();
        }
        initializeRows();
        revalidate();
        repaint();
    }

    public void insertToDelete(JButton roomButton, Room room) {
        String roomName = room.getName();
        if (toDelete.contains(roomName)) {
            toDelete.remove(roomName);
            roomButton.setBackground(null);
        } else {
            toDelete.add(roomName);
            roomButton.setBackground(Color.RED);
        }
        updateRemoveButtonText();
    }

    public ArrayList<String> getToDelete() {
        return toDelete;
    }

    // for when the person did delete
    public void resetRemove(){
        toDelete.clear();
        setRemoveMode();
    }

    // Event Listeners
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addRemoveButtonListener(ActionListener listener) {
        removeButton.addActionListener(listener);
    }

    // There are rows that will contain the grid of room buttons
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();
        int totalRooms = chosenHotel.getTotalRooms();
        int cols = 5;

        // Rows should be rounded up to make an additional incomplete one
        int rows = (int) Math.ceil((double) totalRooms / cols);

        for (int i = 0; i < totalRooms; i++) {
            rooms.get(i).setIndex(i);
        }

        int roomIndex = 0;

        for (int i = 0; i < rows; i++) {
            JPanel rowWrapper = new JPanel(new GridLayout(1, cols, 10, 10));
            rowWrapper.setMaximumSize(new Dimension(fullWidth, rowHeight));

            // To create margins
            rowWrapper.setBorder(new EmptyBorder(5, 5, 5, 30));

            for (int j = 0; j < cols; j++) {
                if (roomIndex < totalRooms) {
                    Room room = rooms.get(roomIndex);
                    JButton roomButton = new JButton("<html>" + room.getName() + "</html>");
                    roomButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                    roomButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));

                    // For coloring based on room type
                    float baseRate = room.getBaseRate();
                    if (baseRate == 1.0f) {
                        roomButton.setBackground(null);
                    } else if (baseRate == 1.20f) {
                        roomButton.setBackground(Color.decode("#fae105"));
                    } else if (baseRate == 1.35f) {
                        roomButton.setBackground(Color.decode("#00fff2"));
                    }

                    if (removeMode) {
                        roomButton.addActionListener(e -> insertToDelete(roomButton, room));
                    } else {
                        roomButton.addActionListener(e -> {
                            if (room.getReservations().size() > 0){
                                JOptionPane.showMessageDialog(this, "There are reservations for this room, cannot edit.", "Error", JOptionPane.WARNING_MESSAGE);
                            } else {
                                if (controller != null) {
                                    controller.showEditRateDialog(room);
                                }
                            }
                        });
                    }

                    rowWrapper.add(roomButton);
                    roomIndex++;
                } else {
                    // Put an empty label since the buttons will overgrow if not
                    rowWrapper.add(new JLabel());
                }
            }
            panelCenter.add(rowWrapper);
        }

        panelCenter.revalidate();
        panelCenter.repaint();
    }

    public ManageEditRooms(Hotel hotel) {
        this.chosenHotel = hotel;
        this.rooms = hotel.getAllRooms();

        setLayout(new BorderLayout());

        // Setting north panel
        panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Back Button
        backButton = new JButton("\u2190");
        backButton.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButton, BorderLayout.WEST);

        // Edit rooms button
        modifyButtons = new JPanel();
        modifyButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        modifyButtons.setBackground(Color.decode("#063970"));
        modifyButtons.add(addButton);
        modifyButtons.add(removeButton);
        panelNorth.add(modifyButtons, BorderLayout.EAST);

        // North Label
        labelManageHotels = new JLabel("Modify Rooms (" + hotel.getTotalRooms() + ")", JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        // Create the center panel with vertical BoxLayout so no grid row count needed
        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

        initializeRows();

        JScrollPane scrollPane = new JScrollPane(panelCenter);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        removeButton.addActionListener(e -> {
            if (toDelete.size() == 0) {
                setRemoveMode();
            }
        });
    }
}
