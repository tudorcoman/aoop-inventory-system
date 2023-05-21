package ro.unibuc.inventorysystem.gui.update;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Produs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdateProdusWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldCategorie;
    private JTextField fieldPretCumparare;
    private JTextField fieldPretVanzare;
    private JComboBox comboBox1;

    public UpdateProdusWindow() {
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

        comboBox1.addItem("");
        final Application app = ApplicationWrapper.INSTANCE.application();
        app.getProduse().forEach(produs -> comboBox1.addItem(produs.getNume()));

        fieldNume.setEnabled(false);
        fieldCategorie.setEnabled(false);
        fieldPretCumparare.setEnabled(false);
        fieldPretVanzare.setEnabled(false);

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String nume = (String) e.getItem();
                if (nume != null && !nume.isEmpty()) {
                    fieldNume.setEnabled(true);
                    fieldCategorie.setEnabled(true);
                    fieldPretCumparare.setEnabled(true);
                    fieldPretVanzare.setEnabled(true);
                    app.getProduse().forEach(produs -> {
                        if (produs.getNume().equals(nume)) {
                            fieldNume.setText(produs.getNume());
                            fieldCategorie.setText(produs.getCategorie());
                            fieldPretCumparare.setText(String.valueOf(produs.getPretCumparare()));
                            fieldPretVanzare.setText(String.valueOf(produs.getPretVanzare()));
                        }
                    });
                } else {
                    fieldNume.setEnabled(false);
                    fieldCategorie.setEnabled(false);
                    fieldPretCumparare.setEnabled(false);
                    fieldPretVanzare.setEnabled(false);
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        final int indexToUpdate = comboBox1.getSelectedIndex() - 1;
        if (indexToUpdate < 0) {
            JOptionPane.showMessageDialog(null, "Selectati un produs!");
            return;
        }

        final Application app = ApplicationWrapper.INSTANCE.application();
        final int id = app.searchProdus(app.getProduse().get(indexToUpdate)).get();

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

                ApplicationWrapper.INSTANCE.application().actualizeazaProdus(id, new Produs(nume, categorie, pretCumparareDouble, pretVanzareDouble));
                JOptionPane.showMessageDialog(null, "Produsul a fost actualizat cu succes!");
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
