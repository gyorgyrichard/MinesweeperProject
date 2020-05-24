package org.example;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import org.tinylog.Logger;

import java.io.IOException;


public class OptionsController {
    public OptionsModell optionsModell;
    @FXML
    private ChoiceBox kivalasztas;

    public OptionsController() {
        this.optionsModell = new OptionsModell();
    }


    @FXML
    public void apply(ActionEvent actionEvent) throws IOException {
        String choiceValue = kivalasztas.getValue().toString();
        optionsModell.setNehezseg(choiceValue);
        Logger.info(choiceValue + " nehézség beállítva!");
        App.setRoot("primary");
    }


}
