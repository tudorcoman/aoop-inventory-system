package ro.unibuc.inventorysystem.application;

import ro.unibuc.inventorysystem.application.model.Application;

import java.lang.reflect.Proxy;

public enum ApplicationWrapper {
    INSTANCE;

    private final Application a;
    private final ApplicationProxy proxy;

    ApplicationWrapper() {
        final Application app = new ApplicationImpl();
        this.proxy = new ApplicationProxy(app);
        this.a = (Application) Proxy.newProxyInstance(ApplicationImpl.class.getClassLoader(), new Class<?>[] { Application.class }, proxy);
    }

    public Application application() {
        return a;
    }
}
