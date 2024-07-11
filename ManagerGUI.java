package mco1;
import javax.swing.*;
import java.awt.*;

public class ViewJFrames extends JFrame {
    ViewJFrames(){
        super("Hotel Manager");
        setLayout(new BorderLayout());

        initializeContent();

        setSize(450, 500);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeContent(){
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        panelNorth.setBackground(Color.RED);

        JLabel labelHotelManager = new JLabel("Hotel Manager");
        labelHotelManager.setForeground(Color.WHITE);
        labelHotelManager.setFont(new Font("Verdana", Font.BOLD, 20));

        panelNorth.add(labelHotelManager);
    }
}
