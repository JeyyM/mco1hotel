package mco1;
import java.util.ArrayList;

public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Hotel> hotels = new ArrayList<>();

        // hi
        MVC_View gui = new MVC_View(hotels);
        MVC_Model model = new MVC_Model(hotels);
        MVC_Controller controller = new MVC_Controller(model, gui);

        // the controller has to be passed on to be able to use the switchers
        gui.setManageHotelsPanelController(controller);
    }
}
