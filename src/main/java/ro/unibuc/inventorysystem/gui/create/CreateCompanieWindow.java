package ro.unibuc.inventorysystem.gui.create;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Persoana;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateCompanieWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldNume;
    private JTextField fieldCUI;
    private JTextArea textArea1;
    private JButton adaugaButton;
    private JComboBox comboBox1;

    private Persoana person;

    public CreateCompanieWindow(String selectedEntity) {
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

        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreatePersoanaWindow dialog = new CreatePersoanaWindow();
                dialog.pack();
                dialog.setVisible(true);
                person = dialog.getPerson();
            }
        });

        comboBox1.addItem("Furnizor");
        comboBox1.addItem("Client");
        comboBox1.setSelectedItem(selectedEntity);
        comboBox1.setEnabled(false);

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

        if (person == null) {
            JOptionPane.showMessageDialog(this, "Trebuie sa adaugati o persoana de contact!");
            return;
        }

        if (nume.isEmpty() || cui.isEmpty() || adresa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Trebuie sa completati toate campurile!");
            return;
        }

        final long cuiLong = Long.parseLong(cui);
        final Companie c = new Companie(nume, cuiLong, adresa, person);
        final String tip = comboBox1.getSelectedItem().toString();

        if ("Furnizor".equals(tip)) {
            ApplicationWrapper.INSTANCE.application().adaugaFurnizor(c);
        } else {
            ApplicationWrapper.INSTANCE.application().adaugaClient(c);
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
