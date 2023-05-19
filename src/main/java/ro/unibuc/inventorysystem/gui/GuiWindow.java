package ro.unibuc.inventorysystem.gui;

import javax.swing.*;
import java.awt.*;

public abstract class GuiWindow extends JFrame {
    abstract JPanel getPanel();

    protected void initializeWindow() {
        setLayout(new FlowLayout());
        setSize(300, 300);
        add(getPanel());
        validate();
    }
}
