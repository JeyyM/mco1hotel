package mco1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageModifyRooms extends ShowRooms {
    private boolean removeMode;
    private ArrayList<String> toDelete = new ArrayList<>();

    private JPanel modifyButtons;
    private JButton addButton = new JButton("Add Rooms");
    private JButton removeButton = new JButton("Remove Rooms");
    private Hotel hotel;

    // Updaters
    public void updateShowRoomInfo() {
        bannerLabel.setText("Modify Rooms (" + hotel.getTotalRooms() + ")");
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

    // Remove mode Functions
    public void setRemoveMode() {
        removeMode = !removeMode;
        if (removeMode) {
            panelNorth.setBackground(Color.decode("#970606"));
            modifyButtons.setBackground(Color.decode("#970606"));
            bannerLabel.setText("Select Rooms to Remove");
            removeButton.setText("Cancel Removal");
            addButton.setEnabled(false);
        } else {
            panelNorth.setBackground(Color.decode("#063970"));
            modifyButtons.setBackground(Color.decode("#063970"));
            bannerLabel.setText("Modify Rooms (" + hotel.getTotalRooms() + ")");
            removeButton.setText("Remove Rooms");
            addButton.setEnabled(true);

            toDelete.clear();
        }
        initializeRows();
        revalidate();
        repaint();
    }

    public void editToDelete(JButton roomButton, Room room) {
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
    public void resetRemove() {
        toDelete.clear();
        setRemoveMode();
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addRemoveButtonListener(ActionListener listener) {
        removeButton.addActionListener(listener);
    }

    // have to override since there are many different functionalities
    @Override
    public void initializeRows() {
        // Panel is cleared to it can reset everything
        panelCenter.removeAll();
        int totalRooms = rooms.size();
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
                        roomButton.addActionListener(e -> editToDelete(roomButton, room));
                    } else {
                        roomButton.addActionListener(e -> {
                            if (room.getReservations().size() > 0) {
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

    public ManageModifyRooms(Hotel hotel) {
        super(hotel, hotel.getName(), new JLabel("Modify Rooms (" + hotel.getTotalRooms() + ")", JLabel.CENTER));
        this.rooms = hotel.getAllRooms();
        this.hotel = hotel;

        // Edit rooms button
        modifyButtons = new JPanel();
        modifyButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        modifyButtons.setBackground(Color.decode("#063970"));
        modifyButtons.add(addButton);
        modifyButtons.add(removeButton);
        panelNorth.add(modifyButtons, BorderLayout.EAST);

        removeButton.addActionListener(e -> {
            if (toDelete.size() == 0) {
                setRemoveMode();
            }
        });

        add(panelNorth, BorderLayout.NORTH);

        initializeRows();
    }
}
