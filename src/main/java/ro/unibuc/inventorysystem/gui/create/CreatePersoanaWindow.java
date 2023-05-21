package ro.unibuc.inventorysystem.gui.create;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Persoana;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreatePersoanaWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldPrenume;
    private JTextField fieldCNP;
    private JTextField fieldEmail;
    private JTextField fieldTelefon;

    private Persoana person;

    public CreatePersoanaWindow() {
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

        if (nume.isEmpty() || prenume.isEmpty() || fieldCNP.getText().isEmpty() || telefon.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Toate campurile sunt obligatorii!");
            return;
        } else {
            final long cnp = Long.parseLong(fieldCNP.getText());
            this.person = new Persoana(nume, prenume, cnp, telefon, email);
            JOptionPane.showMessageDialog(null, "Persoana a fost creata cu succes!");
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public Persoana getPerson() {
        return person;
    }
}
