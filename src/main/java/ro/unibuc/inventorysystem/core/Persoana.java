package ro.unibuc.inventorysystem.core;

import ro.unibuc.inventorysystem.infra.Utilities;

import java.util.Objects;

public class Persoana {
    private String firstName;
    private String lastName;
    private final long cnp;
    private final String phone;

    private final String email;

    public Persoana() {
        this.firstName = "";
        this.lastName = "";
        this.phone = "";
        this.email = "";
        this.cnp = 0;
    }

    public Persoana(String firstName, String lastName, long cnp, String phone, String email) {
        if (Utilities.containsNonAlphaChars(firstName) || Utilities.containsNonAlphaChars(lastName))
            throw new IllegalArgumentException("Numele si prenumele unei persoane pot contine doar litere.");
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnp = cnp;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getCnp() {
        return cnp;
    }

    @Override
    public String toString() {
        return "Persoana " + firstName + " " + lastName + ", CNP " + cnp + ", Telefon " + phone + ", Email " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persoana persoana = (Persoana) o;
        return cnp == persoana.cnp && Objects.equals(firstName, persoana.firstName)
                && Objects.equals(lastName, persoana.lastName) && Objects.equals(email, persoana.email)
                && Objects.equals(phone, persoana.phone);
    }
}
