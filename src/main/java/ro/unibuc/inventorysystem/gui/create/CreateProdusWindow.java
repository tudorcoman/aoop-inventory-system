package ro.unibuc.inventorysystem.gui.create;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.core.Produs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateProdusWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldCategorie;
    private JTextField fieldPretCumparare;
    private JTextField fieldPretVanzare;

    public CreateProdusWindow() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        final KeyAdapter onlyNumbersAllowed = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if(!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_PERIOD)){
                    e.consume();
                }
            }
        };
        fieldPretCumparare.addKeyListener(onlyNumbersAllowed);
        fieldPretVanzare.addKeyListener(onlyNumbersAllowed);
    }

    private void onOK() {
        // add your code here
        final String nume = fieldNume.getText();
        final String categorie = fieldCategorie.getText();
        final String pretCumparare = fieldPretCumparare.getText();
        final String pretVanzare = fieldPretVanzare.getText();

        if (nume.isEmpty() || categorie.isEmpty() || pretCumparare.isEmpty() || pretVanzare.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Toate campurile sunt obligatorii!");
            return;
        } else {
            try {
                final double pretCumparareDouble = Double.parseDouble(pretCumparare);
                final double pretVanzareDouble = Double.parseDouble(pretVanzare);

                if (Double.parseDouble(pretCumparare) > Double.parseDouble(pretVanzare)) {
                    JOptionPane.showMessageDialog(null, "Pretul de cumparare nu poate fi mai mare decat pretul de vanzare!");
                    return;
                }

                ApplicationWrapper.INSTANCE.application().adaugaProdus(new Produs(nume, categorie, pretCumparareDouble, pretVanzareDouble));
                JOptionPane.showMessageDialog(null, "Produsul a fost creat cu succes!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Pretul de cumparare si pretul de vanzare trebuie sa fie numere!");
                return;
            }
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
