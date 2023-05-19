package ro.unibuc.inventorysystem.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public final class EntityWindow extends GuiWindow {
    private JButton angajatButton;
    private JButton depozitButton;
    private JButton furnizorButton;
    private JButton clientButton;
    private JButton tranzactieButton;
    private JButton produsButton;

    private JPanel panel;

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    public EntityWindow() {
        angajatButton.addActionListener(e -> {
            if (e.getSource() == angajatButton) {
                OperationWindow ow = new OperationWindow("Angajat");
                //ow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ow.setSize(300, 300);
                ow.setVisible(true);
                ow.pack();
                ow.setTitle("Selectare operatiune");
            }
        });

        depozitButton.addActionListener(e -> {
            if (e.getSource() == depozitButton) {
                OperationWindow ow = new OperationWindow("Depozit");
                //ow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ow.setSize(300, 300);
                ow.setVisible(true);
                ow.pack();
                ow.setTitle("Selectare operatiune");
            }
        });

        furnizorButton.addActionListener(e -> {
            if (e.getSource() == furnizorButton) {
                OperationWindow ow = new OperationWindow("Companie");
                //ow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ow.setSize(300, 300);
                ow.setVisible(true);
                ow.pack();
                ow.setTitle("Selectare operatiune");
            }
        });

        clientButton.addActionListener(e -> {
            if (e.getSource() == clientButton) {
                OperationWindow ow = new OperationWindow("Companie");
                //ow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ow.setSize(300, 300);
                ow.setVisible(true);
                ow.pack();
                ow.setTitle("Selectare operatiune");
            }
        });

        tranzactieButton.addActionListener(e -> {
            if (e.getSource() == tranzactieButton) {
                OperationWindow ow = new OperationWindow("Tranzactie", Collections.singletonList("actualizareButton"));
                //ow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ow.setSize(300, 300);
                ow.setVisible(true);
                ow.pack();
                ow.setTitle("Selectare operatiune");
            }
        });

        produsButton.addActionListener(e -> {
            if (e.getSource() == produsButton) {
                OperationWindow ow = new OperationWindow("Produs");
                //ow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ow.setSize(300, 300);
                ow.setVisible(true);
                ow.pack();
                ow.setTitle("Selectare operatiune");
            }
        });


        initializeWindow();
    }
}
