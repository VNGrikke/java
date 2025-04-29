package ra.edu;

import ra.edu.business.config.ConnectionDB;
import ra.edu.presentation.DisplayMenu;

public class MainApplication {
    public static void main(String[] args) {
        ConnectionDB.testConnection();
        DisplayMenu displayMenu = new DisplayMenu();
        displayMenu.displayMenu();
    }
}