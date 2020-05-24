package org.example;

import javafx.fxml.FXML;
import org.tinylog.Logger;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void Exit() {
        Logger.info("Kilépés!");
        System.exit(0);
    }

    @FXML
    private void Options() throws IOException {
        Logger.info("Nehézség beállítás!");
        App.setRoot("options");

    }

    @FXML
    public void HighScore() throws IOException {
        Logger.info("High Score megjelenitve");
        App.setRoot("highScore");
    }


}
