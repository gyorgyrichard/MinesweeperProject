package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.tinylog.Logger;

import java.io.IOException;


public class SecondaryController extends OptionsModell {

    @FXML
    private TextField username;
    private SecondaryModell secondaryModell;

    public SecondaryController() {
        this.secondaryModell = new SecondaryModell();
    }

    @FXML
    public void startgame() throws IOException {
        String nickname = username.getText();
        secondaryModell.setUsername(nickname);
        new GameController(getNehezseg(), nickname);
        switch (getNehezseg()) {
            case "könnyű":
                App.setRoot("easy");
                break;
            case "közepes":
                App.setRoot("medium");
                break;
            case "nehéz":
                App.setRoot("hard");
                break;
        }
        Logger.info("Játék indítása!");
    }
}