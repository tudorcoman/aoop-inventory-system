package ro.unibuc.inventorysystem.gui.update;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Angajat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;

public class UpdateAngajatWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldPrenume;
    private JTextField fieldCNP;
    private JTextField fieldEmail;
    private JTextField fieldTelefon;
    private JComboBox comboBoxManager;
    private JComboBox comboBox1;

    public UpdateAngajatWindow() {
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

        fieldNume.setEnabled(false);
        fieldPrenume.setEnabled(false);
        fieldCNP.setEnabled(false);
        fieldEmail.setEnabled(false);
        fieldTelefon.setEnabled(false);
        comboBoxManager.setEnabled(false);

        final Application app = ApplicationWrapper.INSTANCE.application();
        comboBox1.addItem("");
        comboBoxManager.addItem("");

        final List<Angajat> ang = app.getAngajati();
        for (Angajat a: ang) {
            comboBox1.addItem(a.getNume());
            comboBoxManager.addItem(a.getNume());
        }


        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (comboBox1.getSelectedIndex() > 0) {
                    fieldNume.setEnabled(true);
                    fieldPrenume.setEnabled(true);
                    fieldCNP.setEnabled(true);
                    fieldEmail.setEnabled(true);
                    fieldTelefon.setEnabled(true);
                    comboBoxManager.setEnabled(true);
                    comboBoxManager.removeAllItems();
                    comboBoxManager.addItem("");
                    for (Angajat a: ang) {
                        comboBoxManager.addItem(a.getNume());
                    }
                    comboBoxManager.removeItemAt(comboBox1.getSelectedIndex());

                    final Angajat angajat = ang.get(comboBox1.getSelectedIndex() - 1);
                    fieldNume.setText(angajat.getLastName());
                    fieldPrenume.setText(angajat.getFirstName());
                    fieldCNP.setText(String.valueOf(angajat.getCnp()));
                    fieldEmail.setText(angajat.getEmail());
                    fieldTelefon.setText(angajat.getPhone());

                    angajat.getManager().ifPresentOrElse(
                            manager -> comboBoxManager.setSelectedIndex(ang.indexOf(manager) + 1),
                            () -> comboBoxManager.setSelectedIndex(0)
                    );
                } else {
                    fieldNume.setText("");
                    fieldPrenume.setText("");
                    fieldCNP.setText("");
                    fieldEmail.setText("");
                    fieldTelefon.setText("");
                    comboBoxManager.setSelectedIndex(0);

                    fieldNume.setEnabled(false);
                    fieldPrenume.setEnabled(false);
                    fieldCNP.setEnabled(false);
                    fieldEmail.setEnabled(false);
                    fieldTelefon.setEnabled(false);
                    comboBoxManager.setEnabled(false);
                }
            }
        });
    }

    private void onOK() {
        final int indexToUpdate = comboBox1.getSelectedIndex() - 1;
        final Application app = ApplicationWrapper.INSTANCE.application();
        final List<Angajat> ang = app.getAngajati();
        if (indexToUpdate < 0 || indexToUpdate >= ang.size()) {
            JOptionPane.showMessageDialog(null, "Selectati un angajat!");
            return;
        }
        final Angajat toUpdate = ang.get(indexToUpdate);
        final String nume = fieldNume.getText();
        final String prenume = fieldPrenume.getText();
        final String telefon = fieldTelefon.getText();
        final String email = fieldEmail.getText();
        final String manager = (String) comboBoxManager.getSelectedItem();
        final Optional<Angajat> managerVal = "".equals(manager) ? Optional.empty(): Optional.of(ang.get(comboBoxManager.getSelectedIndex() - 1));

        if (nume.isEmpty() || prenume.isEmpty() || fieldCNP.getText().isEmpty() || telefon.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Toate campurile sunt obligatorii!");
            return;
        } else {
            final long cnp = Long.parseLong(fieldCNP.getText());
            app.actualizeazaAngajat(app.searchAngajat(toUpdate).get(), new Angajat(prenume, nume, cnp, telefon, email, managerVal));
            JOptionPane.showMessageDialog(null, "Angajatul a fost actualizat cu succes!");
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        UpdateAngajatWindow dialog = new UpdateAngajatWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
