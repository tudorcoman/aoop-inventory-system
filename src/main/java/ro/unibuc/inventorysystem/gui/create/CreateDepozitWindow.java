package ro.unibuc.inventorysystem.gui.create;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.core.Angajat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class CreateDepozitWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JComboBox comboBoxManager;
    private JTextArea textArea1;

    public CreateDepozitWindow() {
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

        final Map<Integer, Angajat> angajatiTable = ApplicationWrapper.INSTANCE.application().getAngajatiTable();
        for (Map.Entry<Integer, Angajat> angajatEntry: angajatiTable.entrySet()) {
            final Angajat manager = angajatEntry.getValue();
            comboBoxManager.addItem(angajatEntry.getKey() + "). " + manager.getFirstName() + " " + manager.getLastName());
        }
    }

    private void onOK() {
        // add your code here
        final String nume = fieldNume.getText();
        final String adresa = textArea1.getText();
        final String manager = (String) comboBoxManager.getSelectedItem();
        final Integer managerId = Integer.parseInt(manager.split("\\)\\.")[0]);

        if (nume.isEmpty() || adresa.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Toate campurile sunt obligatorii!");
        } else {
            ApplicationWrapper.INSTANCE.application().adaugaDepozit(nume, adresa, managerId);
            JOptionPane.showMessageDialog(null, "Depozitul a fost creat cu succes!");
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
