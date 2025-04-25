package ra.edu;

import ra.edu.business.config.ConnectionDB;
import ra.edu.presentation.DisplayMenu;

public class MainApplication {
    public static void main(String[] args) {
        ConnectionDB.testConnection();
        DisplayMenu menu = new DisplayMenu();
        menu.displayMenu();
    }
}