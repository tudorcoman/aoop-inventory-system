package ro.unibuc.inventorysystem.gui.create;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Angajat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public final class CreateAngajatWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldPrenume;
    private JTextField fieldCNP;
    private JTextField fieldTelefon;
    private JTextField fieldEmail;
    private JComboBox comboBoxManager;

    public CreateAngajatWindow() {
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
        comboBoxManager.addItem("-");
        for (Map.Entry<Integer, Angajat> angajatEntry: angajatiTable.entrySet()) {
            final Angajat manager = angajatEntry.getValue();
            comboBoxManager.addItem(angajatEntry.getKey() + "). " + manager.getFirstName() + " " + manager.getLastName());
        }

        fieldCNP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if(!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        final String nume = fieldNume.getText();
        final String prenume = fieldPrenume.getText();
        final String telefon = fieldTelefon.getText();
        final String email = fieldEmail.getText();
        final String manager = (String) comboBoxManager.getSelectedItem();
        final int managerId = "-".equals(manager) ? -1: Integer.parseInt(manager.split("\\)\\.")[0]);

        if (nume.isEmpty() || prenume.isEmpty() || fieldCNP.getText().isEmpty() || telefon.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Toate campurile sunt obligatorii!");
            return;
        } else {
            final long cnp = Long.parseLong(fieldCNP.getText());
            final Application a = ApplicationWrapper.INSTANCE.application();
            a.adaugaAngajat(new Angajat(prenume, nume, cnp, telefon, email, a.searchAngajat(managerId)));
            JOptionPane.showMessageDialog(null, "Angajatul a fost creat cu succes!");
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
