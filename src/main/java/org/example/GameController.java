package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class GameController implements Initializable {


    String nehezseg = new String();
    String username = new String();
    public int mx = -1000;
    public int my = -1000;
    public int szam = 0;
    public GraphicsContext gc;
    private int timeinsecond = 0;
    private static GameModell game;
    private static GameView gameView;
    @FXML
    public Canvas img;
    @FXML
    public Label score;
    @FXML
    public Label timer;


    public GameController() {
    }

    public GameController(String nehezseg, String username) throws IOException {
        this.nehezseg = nehezseg;
        this.username = username;
        game = new GameModell(username, this.nehezseg);
        game.setSpacing(5);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = img.getGraphicsContext2D();
        gameView = new GameView(game, this, img, score, timer,
                game.getnehezseg());
        img.setOnMouseClicked(mouseEvent -> {
            switch (mouseEvent.getButton()) {
                case PRIMARY:
                    handlePrimaryClick(mouseEvent.getX(), mouseEvent.getY());
                    break;
                case SECONDARY:
                    handleSecondaryClick(mouseEvent.getX(), mouseEvent.getY());
                    break;
            }
        });
    }

    /**
     * Ez a metódus figyeli a jobb klikket. És ha egy olyan mezőre klikkelt ami nincs felfordítva
     * valamint nincs rajta már zászló akkor lerak egy zászlót. Ha van rajta akkor leveszi azt.
     *
     * @param x koordináta
     * @param y koordináta
     */
    public void handleSecondaryClick(double x, double y) {
        mx = (int) x;
        my = (int) y;
        if (game.inboxX(mx, my) > -1 && game.inboxY(mx, my) > -1) {
            if (game.getFlagged(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1) == false
                    && game.getRevealed(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1) == false) {
                gameView.DrawFlag(game.inboxX(mx, my), game.inboxY(mx, my));
                game.setFlagged(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1, true);
            } else if (game.getRevealed(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1) == false) {
                gameView.getFlag(game.inboxX(mx, my), game.inboxY(mx, my));
                game.setFlagged(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1, false);
            }
        }
    }

    /**
     * Ez a metódus fordítja fel a mezőket, ha akna van rajta akkor kirajzolja az összes helyre az aknákat és
     * egy üzenetet dob fel, hogy kikaptunk majd ha kilépünk az üzenetből elölről kezdődik az egész.
     * Ha viszont nem lépünk aknára akkor a legvégén egy gratulációs üzenetet dob fel és ha kilépünk
     * belőle akkor szintén előlről kezdődik.
     *
     * @param x mező sorszáma
     * @param y mező oszlopszáma
     */
    public void handlePrimaryClick(double x, double y) {
        mx = (int) x;
        my = (int) y;

        if (game.getTimecounter() == 0) {
            gameView.startClock();
            Logger.info("Óra elindult!");
        }
        game.setTimecounter(1);
        if (game.inboxX(mx, my) > -1 && game.inboxY(mx, my) > -1) {
            if (game.getBomb(game.inboxX(mx, my), game.inboxY(mx, my))) {
                for (int i = 1; i <= 10; i++)
                    for (int j = 1; j <= 10; j++)
                        if (game.getBomb(i, j)) {
                            gameView.DrawMines(i, j);
                        }
                gameView.ShowAlertLose();
                Logger.info("Aknára klikkelt!");
                gameView.stopClock();
                timeinsecond = gameView.getTimeinsecond();

                try {
                    game.writeHighScoreToJSON(new PlayerScore(game.username, game.getScore(), timeinsecond));
                } catch (Exception e) {
                    Logger.info(e.getMessage());
                }

            } else {
                if (game.getFlagged(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1) == false &&
                        game.getRevealed(game.inboxX(mx, my) - 1, game.inboxY(mx, my) - 1) == false) {

                    neighbour(game.inboxX(mx, my), game.inboxY(mx, my));
                    gameView.setScore(game.getScore());

                    for (int i = 1; i <= 10; i++)
                        for (int j = 1; j <= 10; j++) {
                            if (game.getRevealed1(i, j) == true) {
                                game.updateRevealedcounter(1);
                            }
                        }

                    if (game.getRevealedcounter() > 100) {
                        gameView.ShowAlertWin();
                        timeinsecond = gameView.getTimeinsecond();

                        try {
                            game.writeHighScoreToJSON(new PlayerScore(game.username, game.getScore(), timeinsecond));
                        } catch (Exception e) {
                            Logger.info(e.getMessage());
                        }

                        restart();
                    }
                    game.setRevealedcounter(1);
                }
            }
        }
    }

    /**
     * Ez a metódus elsőre lekéri mennyi akna van közvetlenül a mező mellett.
     * Ha ez megvolt és van legalább egy akna mellette akkor a mezőt felfordítja
     * és ráírja hány akna van közvetlenül mellette. Ha a szám 0 akkor rekurzívan
     * minden szomszédos mezőre meghívodik és felfordítja őket amíg nem talál olyan mezőt
     * ami mellett van legalább 1 akna.
     *
     * @param x a mező sorszáma
     * @param y a mező oszlopszáma
     * @return void
     */
    public void neighbour(int x, int y) {

        szam = game.neighbournumber(x, y);
        if (szam > 0) {
            switch (szam) {
                case 1:
                    gameView.setGCBlue();
                    break;
                case 2:
                    gameView.setGCGreen();
                    break;
                case 3:
                    gameView.setGCRed();
                    break;
                case 4:
                    gameView.setGCDarkBlue();
                    break;
                case 5:
                    gameView.setGCBrown();
                    break;
                case 6:
                    gameView.setGCCyan();
                    break;
                case 7:
                    gameView.setGCPurple();
                    break;
                case 8:
                    gameView.setGCDarkGray();
                    break;
            }
            if (game.getRevealed(x - 1, y - 1) == false) {
                gameView.Drawreveald(szam, x, y);
                gameView.DrawNeighbour(szam, x, y);
                game.setRevealedvalue(x - 1, y - 1, true);
                game.setRevealed1value(x, y, true);
                Logger.info(x + " " + y + " Mező felfordítása!");
                game.updateScorevalue(10);
            }
        } else {
            while (szam == 0 && game.getRevealed(x - 1, y - 1) == false) {

                game.setRevealedvalue(x - 1, y - 1, true);
                game.setRevealed1value(x, y, true);
                Logger.info(x + " " + y + " Mező felfordítása!");
                gameView.DrawClicked(x, y);
                game.updateScorevalue(10);

                if (x == 1 && y == 10) {
                    neighbour(x, y - 1);
                    neighbour(x + 1, y - 1);
                    neighbour(x + 1, y);
                    break;
                } else if (x == 10 && y == 1) {
                    neighbour(x, y + 1);
                    neighbour(x - 1, y + 1);
                    neighbour(x - 1, y);
                    break;
                } else if (x == 1 && y == 1) {
                    neighbour(x, y + 1);
                    neighbour(x + 1, y + 1);
                    neighbour(x + 1, y);
                    break;
                } else if (x == 10 && y == 10) {
                    neighbour(x, y - 1);
                    neighbour(x - 1, y - 1);
                    neighbour(x - 1, y);
                    break;

                } else if (x == 10 && y != 1) {
                    neighbour(x, y - 1);
                    neighbour(x, y + 1);
                    neighbour(x - 1, y - 1);
                    neighbour(x - 1, y);
                    neighbour(x - 1, y + 1);
                    break;
                } else if (x != 1 && y == 10) {
                    neighbour(x, y - 1);
                    neighbour(x - 1, y - 1);
                    neighbour(x - 1, y);
                    neighbour(x + 1, y - 1);
                    neighbour(x + 1, y);
                    break;
                } else if (x == 1 && y != 10) {
                    neighbour(x, y - 1);
                    neighbour(x, y + 1);
                    neighbour(x + 1, y - 1);
                    neighbour(x + 1, y);
                    neighbour(x + 1, y + 1);
                    break;
                } else if (y == 1 && x != 10) {
                    neighbour(x + 1, y);
                    neighbour(x - 1, y);
                    neighbour(x + 1, y + 1);
                    neighbour(x - 1, y + 1);
                    neighbour(x, y + 1);
                    break;
                } else if (y > 1 && x < 10 && y < 10 && x > 1) {
                    neighbour(x - 1, y - 1);
                    neighbour(x - 1, y);
                    neighbour(x - 1, y + 1);
                    neighbour(x, y - 1);
                    neighbour(x, y + 1);
                    neighbour(x + 1, y - 1);
                    neighbour(x + 1, y);
                    neighbour(x + 1, y + 1);
                    break;
                } else {
                    break;
                }

            }
        }
    }

    /**
     * Ez a metódus újrakezdi a játékot , újragenerálja az aknákat és minden mást alaphelyzetbe állít.
     */
    public void restart() {
        game.setRevealedcounter(1);
        if (game.getCounter() > 0) {
            game.set_to_zero();
        }
        timeinsecond = gameView.getTimeinsecond();
        gameView.Restart();
        game.BombGenerate(game.getnehezseg());
        game.howmuchneighbour();
        game.setScore(0);
        gameView.setScore(0);
        gameView.setTimer(0);
        game.timer = 1;
        game.setTimecounter(0);
        game.updateStop();
        if (game.getStop() > 0) {
            gameView.stopClock();

            game.setStop(0);
        }
    }

    public void restart(MouseEvent mouseEvent) throws IOException {
        restart();
    }

    @FXML
    public void menu(MouseEvent mouseEvent) throws IOException {
        Logger.info("Vissza a menübe.");
        App.setRoot("primary");
    }
}

