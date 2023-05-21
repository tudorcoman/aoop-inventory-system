package ro.unibuc.inventorysystem.gui.update;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Persoana;
import ro.unibuc.inventorysystem.gui.create.CreatePersoanaWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;

public class UpdateCompanieWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldCUI;
    private JTextArea textArea1;
    private JButton schimbaButton;
    private JComboBox company;
    private JComboBox relationship;

    private Optional<Persoana> person;

    private final List<Companie> companii;

    public UpdateCompanieWindow(String selectedEntity) {
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
        fieldCUI.setEnabled(false);
        textArea1.setEnabled(false);
        schimbaButton.setEnabled(false);

        person = Optional.empty();
        schimbaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreatePersoanaWindow dialog = new CreatePersoanaWindow();
                dialog.pack();
                dialog.setVisible(true);
                person = Optional.of(dialog.getPerson());
            }
        });

        relationship.setEnabled(false);
        relationship.addItem("Furnizor");
        relationship.addItem("Client");
        relationship.setSelectedItem(selectedEntity);


        final Application app = ApplicationWrapper.INSTANCE.application();
        company.addItem("");
        companii = ("Furnizor".equals(selectedEntity)) ? app.getFurnizori() : app.getClienti();
        companii.forEach(c -> company.addItem(c.getNume()));

        company.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                final String nume = (String) e.getItem();
                final Optional<Companie> companie = companii.stream().filter(c -> c.getNume().equals(nume)).findFirst();
                companie.ifPresentOrElse(c -> {
                    fieldNume.setText(c.getNume());
                    fieldCUI.setText(String.valueOf(c.getCui()));
                    textArea1.setText(c.getAdresa());
                    schimbaButton.setEnabled(true);
//                    relationship.setEnabled(true);
                    fieldNume.setEnabled(true);
                    fieldCUI.setEnabled(true);
                    textArea1.setEnabled(true);
                }, () -> {
                    fieldNume.setText("");
                    fieldCUI.setText("");
                    textArea1.setText("");
                    schimbaButton.setEnabled(false);
//                    relationship.setEnabled(false);
                    fieldNume.setEnabled(false);
                    fieldCUI.setEnabled(false);
                });
            }
        });

        fieldCUI.addKeyListener(new KeyAdapter() {
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
        final String cui = fieldCUI.getText();
        final String adresa = textArea1.getText();
        final String rel = (String) relationship.getSelectedItem();
        final Companie companie = companii.stream().filter(c -> c.getNume().equals(company.getSelectedItem())).findFirst().get();
        final Application app = ApplicationWrapper.INSTANCE.application();
        final Companie nou = new Companie(nume, Long.parseLong(cui), adresa, person.orElse(companie.getPersoanaContact()));

        if (nume.isEmpty() || cui.isEmpty() || adresa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trebuie sa completati toate campurile!");
            return;
        }

        if (rel.equals("Furnizor")) {
            app.actualizeazaFurnizor(app.searchFurnizor(companie).get(), nou);
        } else {
            app.actualizeazaClient(app.searchClient(companie).get(), nou);
        }
        JOptionPane.showMessageDialog(null, "Partenerul a fost actualizat cu succes!");

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
