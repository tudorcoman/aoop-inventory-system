package ro.unibuc.inventorysystem.application;

import ro.unibuc.inventorysystem.application.model.Application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Objects;

public class ApplicationProxy implements InvocationHandler {

    /*
        Am ales sa folosesc un InvocationHandler de fiecare data
        cand se apeleaza o metoda din aplicatie, pentru a putea
        scrie in fisierul de audit. Astfel, nu a fost nevoie sa
        adaug in fiecare functie din aplicatie un apel catre
        functia de scriere in fisierul de audit.

        In schimb, a trebuit sa creez o interfata in care sa apara
        toate functiile din aplicatie, pentru a ma putea folosi de
        InvocationHandler. Am creat mai multe interfete micute pentru
        fiecare obiect pe care fac operatii + o interfata mare care le
        extinde pe toate acestea mai mici.
    */

    private Application a;
    public ApplicationProxy(Application a) {
        this.a = a;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object original = method.invoke(a, args);
        final String nume = method.getName();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final String line = nume + "," + timestamp + "\n";

        final URL resource = getClass().getResource("/audit.csv");
        final String filePath = URLDecoder.decode(Objects.requireNonNull(resource).getFile(), StandardCharsets.UTF_8);

        final Writer fileWriter = new BufferedWriter(new FileWriter(filePath, true));
        fileWriter.append(line);
        fileWriter.close();
        return original;
    }
}
