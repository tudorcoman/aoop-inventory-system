package ro.unibuc.inventorysystem.gui.generic;

import ro.unibuc.inventorysystem.application.ApplicationWrapper;
import ro.unibuc.inventorysystem.application.model.Application;
import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Produs;
import ro.unibuc.inventorysystem.gui.GuiWindow;
import ro.unibuc.inventorysystem.gui.create.CreateAngajatWindow;
import ro.unibuc.inventorysystem.gui.create.CreateCompanieWindow;
import ro.unibuc.inventorysystem.gui.create.CreateDepozitWindow;
import ro.unibuc.inventorysystem.gui.create.CreateProdusWindow;
import ro.unibuc.inventorysystem.gui.create.CreateTranzactieWindow;
import ro.unibuc.inventorysystem.gui.update.UpdateAngajatWindow;
import ro.unibuc.inventorysystem.gui.update.UpdateCompanieWindow;
import ro.unibuc.inventorysystem.gui.update.UpdateDepozitWindow;
import ro.unibuc.inventorysystem.gui.update.UpdateProdusWindow;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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

    private Function<Integer, Void> getDeleteFunction() {
        final List<Object> entities = getEntities();
        switch (selectedEntity) {
            case "Angajat" -> {
                return (id) -> {
                    final Angajat angajat = (Angajat) entities.get(id);
                    final Integer idAngajat = ApplicationWrapper.INSTANCE.application().searchAngajat(angajat).get();
                    ApplicationWrapper.INSTANCE.application().stergeAngajat(idAngajat);
                    return null;
                };
            }
            case "Depozit" -> {
                return (id) -> {
                    final Depozit depozit = (Depozit) entities.get(id);
                    final Integer idDepozit = ApplicationWrapper.INSTANCE.application().searchDepozit(depozit).get();
                    ApplicationWrapper.INSTANCE.application().stergeDepozit(idDepozit);
                    return null;
                };
            }

            case "Furnizor" -> {
                return (id) -> {
                    final Companie furnizor = (Companie) entities.get(id);
                    final Integer idFurnizor = ApplicationWrapper.INSTANCE.application().searchFurnizor(furnizor).get();
                    ApplicationWrapper.INSTANCE.application().stergeFurnizor(idFurnizor);
                    return null;
                };
            }

            case "Client" -> {
                return (id) -> {
                    final Companie client = (Companie) entities.get(id);
                    final Integer idClient = ApplicationWrapper.INSTANCE.application().searchClient(client).get();
                    ApplicationWrapper.INSTANCE.application().stergeClient(idClient);
                    return null;
                };
            }

            case "Produs" -> {
                return (id) -> {
                    final Produs produs = (Produs) entities.get(id);
                    final Integer idProdus = ApplicationWrapper.INSTANCE.application().searchProdus(produs).get();
                    ApplicationWrapper.INSTANCE.application().stergeProdus(idProdus);
                    return null;
                };
            }

            default -> {
                return null;
            }
        }
    }

    private List<String> columnNames() {
        return switch(selectedEntity) {
            case "Angajat" -> Arrays.asList("FirstName", "LastName", "Cnp", "Phone", "Email", "ManagerName");
            case "Depozit" -> Arrays.asList("Nume", "Adresa", "ManagerName");
            case "Furnizor", "Client" -> Arrays.asList("Nume", "Cui", "Adresa", "NumePersoanaContact");
            case "Tranzactie" -> Arrays.asList("Id", "Timestamp", "Tip", "NumeProdus", "Quantity", "NumeDepozit", "NumePartener");
            case "Produs" -> Arrays.asList("Nume", "Categorie", "PretCumparare", "PretVanzare");
            default -> null;
        };
    }

    private void eventListeners() {
        adaugareButton.addActionListener(e -> {
            if (e.getSource() == adaugareButton) {
                JDialog dialog = switch(selectedEntity) {
                    case "Angajat" -> new CreateAngajatWindow();
                    case "Depozit" -> new CreateDepozitWindow();
                    case "Produs" -> new CreateProdusWindow();
                    case "Furnizor", "Client" -> new CreateCompanieWindow(selectedEntity);
                    case "Tranzactie" -> new CreateTranzactieWindow();
                    default -> null;
                };

                dialog.pack();
                dialog.setVisible(true);
            }
        });

        actualizareButton.addActionListener(e -> {
            if (e.getSource() == actualizareButton) {
                JDialog dialog = switch(selectedEntity) {
                    case "Angajat" -> new UpdateAngajatWindow();
                    case "Depozit" -> new UpdateDepozitWindow();
                    case "Produs" -> new UpdateProdusWindow();
                    case "Furnizor", "Client" -> new UpdateCompanieWindow(selectedEntity);
                    default -> null;
                };

                dialog.pack();
                dialog.setVisible(true);
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

        stergereButton.addActionListener(e -> {
            if (e.getSource() == stergereButton) {
                StergereWindow dialog = new StergereWindow(getEntities(), getDeleteFunction());
                dialog.pack();
                dialog.setSize(480, 240);
                dialog.setVisible(true);
            }
        });

        afisareButton.addActionListener(e -> {
            if (e.getSource() == afisareButton) {
                AfisareWindow dialog = new AfisareWindow(getEntities(), columnNames());
                dialog.pack();
                dialog.setSize(480, 240);
                dialog.setVisible(true);
            }
        });
    }
}
