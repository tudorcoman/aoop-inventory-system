package ro.unibuc.inventorysystem;

import ro.unibuc.inventorysystem.gui.EntityWindow;
import ro.unibuc.inventorysystem.gui.OperationWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        Meniu.INSTANCE.runMeniu();
        //OperationWindow gui = new OperationWindow();
        EntityWindow gui = new EntityWindow();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(300, 300);
        gui.setVisible(true);
        gui.pack();
        gui.setTitle("Inventory System");
    }
}
