package ro.unibuc.inventorysystem.gui.update;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Depozit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

public class UpdateDepozitWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JComboBox comboBoxManager;
    private JTextArea textArea1;
    private JComboBox comboBox1;

    public UpdateDepozitWindow() {
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
        textArea1.setEnabled(false);
        comboBoxManager.setEnabled(false);

        final Application app = ApplicationWrapper.INSTANCE.application();
        final Map<Integer, Angajat> angajatiTable = app.getAngajatiTable();

        comboBoxManager.addItem("");
        comboBox1.addItem("");

        final List<Depozit> depozitList = app.getDepozite();
        final List<Angajat> ang = app.getAngajati();

        for (Map.Entry<Integer, Angajat> angajatEntry: angajatiTable.entrySet()) {
            final Angajat manager = angajatEntry.getValue();
            comboBoxManager.addItem(angajatEntry.getKey() + "). " + manager.getFirstName() + " " + manager.getLastName());
        }

        for(Depozit d: app.getDepozite()) {
            comboBox1.addItem(d.getNume());
        }

        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(comboBox1.getSelectedIndex() > 0) {
                    fieldNume.setEnabled(true);
                    textArea1.setEnabled(true);
                    comboBoxManager.setEnabled(true);

                    final Depozit depozit = depozitList.get(comboBox1.getSelectedIndex() - 1);
                    fieldNume.setText(depozit.getNume());
                    textArea1.setText(depozit.getAdresa());
                    comboBoxManager.setSelectedIndex(1 + ang.indexOf(depozit.getManager()));
                } else {
                    fieldNume.setText("");
                    textArea1.setText("");
                    comboBoxManager.setSelectedIndex(0);
                    fieldNume.setEnabled(false);
                    textArea1.setEnabled(false);
                    comboBoxManager.setEnabled(false);
                }
            }
        });

    }

    private void onOK() {
        // add your code here
        final int indexToUpdate = comboBox1.getSelectedIndex() - 1;
        final Application app = ApplicationWrapper.INSTANCE.application();
        final List<Depozit> depozitList = app.getDepozite();
        final List<Angajat> ang = app.getAngajati();

        if (indexToUpdate < 0 || indexToUpdate >= depozitList.size()) {
            JOptionPane.showMessageDialog(null, "Nu ati selectat un depozit!");
            return;
        }

        final Depozit toUpdate = depozitList.get(indexToUpdate);
        final String nume = fieldNume.getText();
        final String adresa = textArea1.getText();
        final int managerIndex = comboBoxManager.getSelectedIndex() - 1;
        final Angajat manager = managerIndex >= 0 ? ang.get(managerIndex) : null;

        app.updateDepozit(app.searchDepozit(toUpdate).get(), nume, adresa, app.searchAngajat(manager).get());
        JOptionPane.showMessageDialog(null, "Depozitul a fost actualizat cu succes!");
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
