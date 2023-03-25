package ro.unibuc.inventorysystem.core;

import java.util.Objects;
import java.util.Optional;

public class Angajat extends Persoana {
    private Optional<Angajat> manager;

    public Angajat() {
        super();
        this.manager = Optional.empty();
    }

    public Angajat(String firstName, String lastName, long cnp, String phone, String email) {
        super(firstName, lastName, cnp, phone, email);
        this.manager = Optional.empty();
    }

    public Angajat(String firstName, String lastName, long cnp, String phone, String email, Optional<Angajat> manager) {
        super(firstName, lastName, cnp, phone, email);
        this.manager = manager;
    }

    public Optional<Angajat> getManager() {
        return manager;
    }

    public void setManager(Angajat manager) {
        this.manager = Optional.of(manager);
    }

    @Override
    public String toString() {
        final String basicInfo = "Angajat " + super.toString();
        String managerInfo = "";
        if (manager.isPresent())
            managerInfo = ", Manager " + manager.get().getFirstName() + " " + manager.get().getLastName();
        return basicInfo + managerInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Angajat angajat = (Angajat) o;
        return Objects.equals(manager, angajat.manager);
    }
}
