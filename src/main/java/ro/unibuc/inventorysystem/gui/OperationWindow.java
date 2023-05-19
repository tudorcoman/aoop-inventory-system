package ro.unibuc.inventorysystem.gui;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public final class OperationWindow extends GuiWindow {
    private JButton adaugareButton;
    private JButton stergereButton;
    private JButton actualizareButton;
    private JButton cautareButton;
    private JButton afisareButton;

    private String selectedEntity;

    private JPanel panel;

    public OperationWindow(String selectedEntity) {
        this.selectedEntity = selectedEntity;
        initializeWindow();
        eventListeners();
    }

    public OperationWindow(String selectedEntity, List<String> disabledButtons) {
        this.selectedEntity = selectedEntity;
        for (String buttonName : disabledButtons) {
            switch (buttonName) {
                case "adaugareButton":
                    adaugareButton.setEnabled(false);
                    break;
                case "stergereButton":
                    stergereButton.setEnabled(false);
                    break;
                case "actualizareButton":
                    actualizareButton.setEnabled(false);
                    break;
                case "cautareButton":
                    cautareButton.setEnabled(false);
                    break;
                case "afisareButton":
                    afisareButton.setEnabled(false);
                    break;
            }
        }
        initializeWindow();
        eventListeners();
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    private List<Object> getEntities() {
        final Application a = ApplicationWrapper.INSTANCE.application();
        return switch (selectedEntity) {
            case "Angajat" -> Arrays.asList(a.getAngajati().toArray());
            case "Depozit" -> Arrays.asList(a.getDepozite().toArray());
            case "Furnizor" -> Arrays.asList(a.getFurnizori().toArray());
            case "Client" -> Arrays.asList(a.getClienti().toArray());
            case "Tranzactie" -> Arrays.asList(a.getTranzactii().toArray());
            case "Produs" -> Arrays.asList(a.getProduse().toArray());
            default -> null;
        };
    }

    private void eventListeners() {
        adaugareButton.addActionListener(e -> {
            if (e.getSource() == adaugareButton) {
                if ("Angajat".equals(selectedEntity)) {
                    CreateAngajatWindow dialog = new CreateAngajatWindow();
                    dialog.pack();
                    dialog.setVisible(true);
                }
            }
        });

        cautareButton.addActionListener(e -> {
            if (e.getSource() == cautareButton) {
                CautareWindow dialog = new CautareWindow(getEntities());
                dialog.pack();
                dialog.setSize(480, 240);
                dialog.setVisible(true);
            }
        });
    }
}
