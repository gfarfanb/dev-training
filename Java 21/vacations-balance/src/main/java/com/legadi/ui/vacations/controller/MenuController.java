package com.legadi.ui.vacations.controller;

import org.springframework.stereotype.Component;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

@Component
public class MenuController {

    @FXML
    private MenuItem fileMenu;

    @FXML
    private MenuItem quitMenu;

    private final HostServices hostServices;

    public MenuController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void initialize () {
        System.out.print(fileMenu);
        System.out.print(quitMenu);
    }
}
