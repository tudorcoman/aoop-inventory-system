package ro.unibuc.inventorysystem.gui.create;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Produs;
import ro.unibuc.inventorysystem.core.TipTranzactie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreateTranzactieWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCantitate;
    private JComboBox companyOptions;
    private JComboBox tranzactieType;
    private JTextField dateField;
    private JComboBox depozitType;
    private JComboBox produsOptions;

    public CreateTranzactieWindow() {
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

        fieldCantitate.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if(!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_PERIOD)) {
                    e.consume();
                }
            }
        });

        final Application app = ApplicationWrapper.INSTANCE.application();
        final List<Produs> produse = app.getProduse();
        final List<Depozit> depozite = app.getDepozite();
        final List<Companie> furnizori = app.getFurnizori();
        final List<Companie> clienti = app.getClienti();

        produsOptions.addItem("");
        depozitType.addItem("");
        companyOptions.addItem("");

        for (Produs p: produse)
            produsOptions.addItem(p.getNume());

        for (Depozit d: depozite)
            depozitType.addItem(d.getNume());

        for (Companie c: furnizori)
            companyOptions.addItem(c.getNume());

        tranzactieType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (tranzactieType.getSelectedIndex() == 0) {
                    companyOptions.removeAllItems();
                    companyOptions.addItem("");
                    for (Companie c: furnizori)
                        companyOptions.addItem(c.getNume());
                } else {
                    companyOptions.removeAllItems();
                    companyOptions.addItem("");
                    for (Companie c: clienti)
                        companyOptions.addItem(c.getNume());
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        final String cantitate = fieldCantitate.getText();
        final String dataStr = dateField.getText();
        final int produsIndex = produsOptions.getSelectedIndex();
        final int depozitIndex = depozitType.getSelectedIndex();
        final int companyIndex = companyOptions.getSelectedIndex();
        final TipTranzactie tip = (tranzactieType.getSelectedIndex() == 0) ? TipTranzactie.IN : TipTranzactie.OUT;

        if (cantitate.isEmpty() || dataStr.isEmpty() || produsIndex == 0 || depozitIndex == 0 || companyIndex == 0) {
            JOptionPane.showMessageDialog(null, "Toate campurile sunt obligatorii!");
            return;
        }

        final Application app = ApplicationWrapper.INSTANCE.application();
        final List<Produs> produse = app.getProduse();
        final List<Depozit> depozite = app.getDepozite();
        final List<Companie> companii = (tip == TipTranzactie.IN) ? app.getFurnizori() : app.getClienti();

        final Produs produs = produse.get(produsIndex - 1);
        final Depozit depozit = depozite.get(depozitIndex - 1);
        final Companie companie = companii.get(companyIndex - 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date data;
        try {
            data = dateFormat.parse(dataStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Data nu este valida!");
            return;
        }

        Double cantitateDouble;

        try {
            cantitateDouble = Double.parseDouble(cantitate);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantitatea nu este valida!");
            return;
        }

        final int idProdus = app.searchProdus(produs).get();
        final int idDepozit = app.searchDepozit(depozit).get();

        if (tip == TipTranzactie.IN) {
            final int idFurnizor = app.searchFurnizor(companie).get();
            app.adaugaIntrare(idProdus, idFurnizor, cantitateDouble, data, idDepozit);
        } else {
            final int idClient = app.searchClient(companie).get();
            app.adaugaIesire(idProdus, idClient, cantitateDouble, data, idDepozit);
        }

        JOptionPane.showMessageDialog(null, "Tranzactie adaugata cu succes!");
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        CreateTranzactieWindow dialog = new CreateTranzactieWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
