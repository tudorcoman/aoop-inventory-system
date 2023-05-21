package ro.unibuc.inventorysystem.gui.generic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Function;

public final class StergereWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;

    private List<Object> entities;
    private Function<Integer, Void> deleteFunction;

    public StergereWindow(List<Object> entities, Function<Integer, Void> deleteFunction) {
        this.entities = entities;
        this.deleteFunction = deleteFunction;

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

        comboBox1.addItem("");
        for (Object entity: entities) {
            comboBox1.addItem(entity.toString().substring(0, 45) + "...");
        }

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
    }

    private void onOK() {
        // add your code here
        deleteFunction.apply(comboBox1.getSelectedIndex() - 1);
        JOptionPane.showMessageDialog(null, "Stergerea a fost executata cu succes!");
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
