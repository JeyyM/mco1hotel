package mco1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

/**
 * GUI Panel that shows a calendar-like list
 * of buttons that can be clicked to change the
 * multiplier of a certain day
 */
public class ManageDateMultiplier extends JPanel {
    private JButton backButton;
    
    private JPanel panelNorth = new JPanel();
    private int fullWidth = 1050;
    private int menuHeight = 500;
    private int backButtonFontSize = 25;
    private int northHeight = 40;
    private int northLabelFontSize = 20;

    private JPanel[] weeks = new JPanel[5];
    private JButton[][] calendar = new JButton[5][7];

    private MVC_Controller controller;

    /**
     * Constructor for the panel.
     * Buttons are days and have the multiplier
     * for said day shown inside parentheses.
     * @param hotel     hotel that will have its day multipliers changed
     */
    public ManageDateMultiplier (Hotel hotel) {
        setLayout(new BorderLayout());

        // Setting north panel
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBackground(Color.decode("#063970"));
        panelNorth.setPreferredSize(new Dimension(fullWidth, northHeight));

        // Back Button
        backButton = new JButton("\u2190");
        backButton.setFont(new Font(UIManager.getFont("Button.font").getName(), Font.PLAIN, backButtonFontSize));
        panelNorth.add(backButton, BorderLayout.WEST);

        // North Label
        JLabel labelManageHotels = new JLabel("Select Day to Modify Multiplier", JLabel.CENTER);
        labelManageHotels.setForeground(Color.WHITE);
        labelManageHotels.setFont(new Font("Verdana", Font.BOLD, northLabelFontSize));
        panelNorth.add(labelManageHotels, BorderLayout.CENTER);

        add(panelNorth, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new GridLayout(5, 1));

        for (int i = 0; i < 5; i++) {
            // System.out.println("hi");
            weeks[i] = new JPanel(new GridLayout(1, 7));
            for (int j = 0; j < 7; j++) {
                if (i == 4 && j > 2) {
                    JPanel whiteSpace = new JPanel();
                    weeks[i].add(whiteSpace);
                }
                else {
                    float priceMultiplier = hotel.getDayMultipliers()[i * 7 + j];
                    String buttonName = String.format("%d (%.1f)", (i * 7 + j + 1), priceMultiplier);
                    calendar[i][j] = new JButton(buttonName);

                    if (priceMultiplier != 1.0f) {
                        calendar[i][j].setBackground(Color.decode("#AFDDED"));
                    }

                    weeks[i].add(calendar[i][j]);
                }
            }
            weeks[i].setBorder(new EmptyBorder(5, 5, 5, 30));
            calendarPanel.add(weeks[i]);
        }

        addCalendarListener(hotel);

        add(calendarPanel, BorderLayout.CENTER);
    }

    /**
     * Sets the controller that the panel can call functions to
     * @param controller    the main controller of the program
     */
    public void setController(MVC_Controller controller) {
        this.controller = controller;
    }
    
    /**
     * Recolors the buttons of the calendar to a different color
     * if the multiplier is not the default.
     * Called after a multiplier is changed.
     * @param hotel     hotel that had its day multipliers changed
     */
    public void recolorDays(Hotel hotel) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    if (hotel.getDayMultipliers()[i * 7 + j] != 1.0f) {
                        calendar[i][j].setBackground(Color.decode("#AFDDED"));
                    }
                    else {
                        calendar[i][j].setBackground(new JButton().getBackground());
                    }
                    String buttonName = String.format("%d (%.1f)", (i * 7 + j + 1), hotel.getDayMultipliers()[i * 7 + j]);
                    calendar[i][j].setText(buttonName);
                }
            }
        }
        
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Adds an event listener for each day button to call
     * a controller function to change the multiplier
     * for the selected day
     * @param hotel     hotel that will have its day multipliers changed
     */
    public void addCalendarListener(Hotel hotel) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (!(i == 4 && j > 2)) {
                    final int day = i * 7 + j;
                    calendar[i][j].addActionListener(e -> {
                        controller.changeDayMultiplier(hotel, day);
                    });
                }
            }
        }
    }

    /**
     * Adds an event listener for the back button
     * at the top left of the GUI
     * @param listener      action that will happen when the back button is clicked
     */
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
