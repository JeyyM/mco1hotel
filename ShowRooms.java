package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShowRooms extends JPanel {
    private Hotel chosenHotel;
    private ArrayList<Room> rooms;

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

    private boolean removeMode;
    private ArrayList<String> toDelete = new ArrayList<>();

    public ShowRooms(Hotel hotel) {
        chosenHotel = hotel;
        rooms = hotel.getAllRooms();

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

    public void updateShowRoomInfo() {
        labelManageHotels.setText("Modify Rooms (" + chosenHotel.getTotalRooms() + ")");
        initializeRows();
        revalidate();
        repaint();
    }

    // For refreshing the hotels every enter
    public void setHotel(Hotel hotelUpdate) {
        chosenHotel = hotelUpdate;
        initializeRows();
    }

    // for when the person did delete
    public void resetRemove(){
        toDelete.clear();
        setRemoveMode();
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addRemoveButtonListener(ActionListener listener) {
        removeButton.addActionListener(listener);
    }

    // Go to remove mode
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

    public void updateRemoveButtonText() {
        if (toDelete.size() > 0) {
            removeButton.setText("Complete Deletion");
        } else {
            removeButton.setText("Cancel Removal");
        }
    }

    public ArrayList<String> getToDelete() {
        return toDelete;
    }

    // There are rows that will contain the grid of buttons
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
                    if (removeMode) {
                        roomButton.addActionListener(e -> insertToDelete(roomButton, room));
                    } else {
                        roomButton.addActionListener(e -> {
                            // None for now
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
}
