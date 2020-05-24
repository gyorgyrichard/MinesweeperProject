package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;


public class HighScoreController implements Initializable {

    ArrayList<PlayerScore> highScoreList;
    @FXML
    private TableView<PlayerScore> highScore;

    @FXML
    public void back() throws IOException {
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        ObservableList<PlayerScore> players = FXCollections.observableArrayList();

        readHighScoreToJSON();
        Collections.sort(highScoreList);
        for (PlayerScore ps: highScoreList) {
            players.add(ps);
        }

        highScore.setItems(players);
    }

    public void readHighScoreToJSON() {
        try {
            highScoreList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(new File("HighScore.json")));
            String s = "";
            try {
                while (true) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    s += line;
                }
            } catch (IOException er) {
                Logger.info("IOException -> " + er.getMessage() + " Caused " + er.getCause());
            }
            JSONObject obj = new JSONObject(s);
            JSONArray arr = obj.getJSONArray("highScores");
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).getString("name");
                int score = arr.getJSONObject(i).getInt("score");
                int time = arr.getJSONObject(i).getInt("time");
                highScoreList.add(new PlayerScore(name, score, time));
            }
        }catch (Exception e)
        {

        }
    }

    @FXML
    private TableColumn<PlayerScore, String> name;
    @FXML
    private TableColumn<PlayerScore, String> score;
    @FXML
    private TableColumn<PlayerScore, String> time;

}
