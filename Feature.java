package mco1;

import javax.swing.*;
import java.util.ArrayList;

public class Feature {
    private String featureName;
    private ArrayList<ImageIcon> hotelImages;

    public Feature(String name) {
        this.featureName = name;
        this.hotelImages = new ArrayList<>();
    }

    public void setName(String newName) {
        featureName = newName;
    }

    public String getName() {
        return featureName;
    }

    public void setHotelImages(ArrayList<ImageIcon> images) {
        this.hotelImages = images;
    }

    public ArrayList<ImageIcon> getHotelImages() {
        return hotelImages;
    }
}