package com.example.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;

public class Gui extends LightweightGuiDescription {
    public Gui(){
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300,300);

    }
}
