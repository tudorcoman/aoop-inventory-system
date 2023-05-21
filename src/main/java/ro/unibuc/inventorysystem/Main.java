package ro.unibuc.inventorysystem;

import ro.unibuc.inventorysystem.gui.generic.EntityWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        EntityWindow gui = new EntityWindow();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(300, 300);
        gui.setVisible(true);
        gui.pack();
        gui.setTitle("Inventory System");
    }
}
