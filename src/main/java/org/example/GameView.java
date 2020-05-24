package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.tinylog.Logger;


public class GameView {
    public Timeline clock = new Timeline();
    public static Image mine;
    public static Image flag;
    private int timeinsecond = 0;


    static {
        mine = new Image(new GameView().getClass().getResourceAsStream("mine.png"));
        flag = new Image(new GameView().getClass().getResourceAsStream("flag.png"));
    }

    public int spacing = 5;
    public Canvas img = new Canvas();
    public Label score = new Label();
    public Label timer = new Label();
    public GraphicsContext gc;
    Alert alert = new Alert(Alert.AlertType.WARNING);
    Alert alertwin = new Alert(Alert.AlertType.WARNING);
    public String nehezseg = new String();
    private GameModell game;
    private GameController controller;

    /**
     * A konstruktorba beállítjuk az alertek címét, fejléc szövegét és
     * a context szövegét majd berajzoljuk a pályát.
     *
     * @param gamecome       Gamemodell példánya
     * @param controllercome Gamecontroller példánya
     * @param img            a Canvas amire rajzolunk
     * @param score          A score Label ahova a scoret írjuk
     * @param timer          A timer label ahova az időt írjuk
     * @param nehezseg       A nehézség amit beállított
     */

    GameView(GameModell gamecome, GameController controllercome, Canvas img, Label score, Label timer, String nehezseg) {
        this.img = img;
        this.score = score;
        this.timer = timer;
        controller = controllercome;
        this.nehezseg = nehezseg;
        game = gamecome;
        gc = img.getGraphicsContext2D();
        alert.setTitle("Felugró ablak!");
        alert.setHeaderText("Vesztettél!");
        alert.setContentText("Egy bombamezőre kattintottál!");
        alertwin.setTitle("Felugró ablak!");
        alertwin.setHeaderText("Győztél!");
        alertwin.setContentText("Gratulálok megtaláltad az összes aknát!");

        gc.setFill(Color.BLACK);

        if (nehezseg.equals("könnyű")) {
            Logger.info("Pálya kirajzolása könnyű nehézségi szinthez!");
            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                    gc.fillRect(spacing + i * 50, spacing + j * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);

        } else if (nehezseg.equals("közepes")) {
            Logger.info("Pálya kirajzolása közepes nehézségi szinthez!");
            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++)
                    gc.fillRect(spacing + i * 50, spacing + j * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
        } else if (nehezseg.equals("nehéz")) {
            Logger.info("Pálya kirajzolása nehéz nehézségi szinthez!");
            for (int i = 0; i < 30; i++)
                for (int j = 0; j < 30; j++)
                    gc.fillRect(spacing + i * 50, spacing + j * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
        }

    }


    /**
     * Az x,y koordinátára berajzol egy zászlót.
     *
     * @param x sorszám
     * @param y oszlopszám
     */
    public void DrawFlag(int x, int y) {
        gc.drawImage(flag, (spacing + (x - 1) * 50) + 2, (spacing + (y - 1) * 50 + 50) + 2);
        Logger.info("Flag lehelyezése!");


    }

    /**
     * Az x,y koordinátáról felveszi a zászlót
     *
     * @param x sorszám
     * @param y oszlopszám
     */
    public void getFlag(int x, int y) {
        gc.setFill(Color.BLACK);
        Logger.info("Flag felvétele!");
        gc.fillRect(spacing + (x - 1) * 50, spacing + (y - 1) * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
    }

    public GameView() {

    }

    /**
     * Beállítja az időt, hogy hány másodperce játszik a játékos.
     *
     * @param timer1 idő
     */
    public void setTimer(int timer1) {
        timer.setText("Timer: " + Integer.toString(timer1) + ". s");
    }

    /**
     * Beállítja a pontot amennyit eddig szerzett a játékos
     *
     * @param score1 pont
     */
    public void setScore(int score1) {
        score.setText("Score: " + Integer.toString(score1));
    }

    /**
     * Berajzolja az összes aknát amennyiben vagy rákatintott egyre vagy megnyerte a játékot.
     *
     * @param i sorszám
     * @param j oszlopszám
     */
    public void DrawMines(int i, int j) {
        gc.setFill(Color.RED);
        gc.fillRect(spacing + (i - 1) * 50, spacing + (j - 1) * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
        gc.drawImage(mine, (spacing + (i - 1) * 50) + 2, (spacing + (j - 1) * 50 + 50) + 2);


    }

    /**
     * Felfordítja azt a mezőt amelyikre klikkelt.
     *
     * @param inboxX sorszám
     * @param inboxY oszlopszám
     */
    public void DrawClicked(int inboxX, int inboxY) {
        gc.setFill(Color.GRAY);
        gc.fillRect(spacing + (inboxX - 1) * 50, spacing + (inboxY - 1) * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
    }

    /**
     * Ráírja a felfordított mezőre mennyi szomszédos mezőn van akna amennyiben van ilyen.
     *
     * @param szam mennyi akna van.
     * @param x    sorszám
     * @param y    oszlopszám
     */
    public void DrawNeighbour(int szam, int x, int y) {
        gc.strokeText(String.valueOf(szam), spacing + (x - 1) * 50 + 20, spacing + (y - 1) * 50 + 50 + 20);
    }

    /**
     * Rekurzívan hívódik meg hogy felfordítsa az összes olyan mezőt amely mellett nincs akna.
     *
     * @param szam hány szomszédos mezőn van akna
     * @param x    sorszám
     * @param y    oszlopszám
     */
    public void Drawreveald(int szam, int x, int y) {
        gc.setFill(Color.GRAY);
        gc.fillRect(spacing + (x - 1) * 50, spacing + (y - 1) * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCBlue() {
        gc.setStroke(Color.BLUE);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCGreen() {
        gc.setStroke(Color.GREEN);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCRed() {
        gc.setStroke(Color.RED);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCDarkBlue() {
        gc.setStroke(Color.DARKBLUE);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCBrown() {
        gc.setStroke(Color.BROWN);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCCyan() {
        gc.setStroke(Color.CYAN);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCPurple() {
        gc.setStroke(Color.PURPLE);
    }

    /**
     * Beállítja a szöveg színet ami a mezőre kerül.
     */
    public void setGCDarkGray() {
        gc.setStroke(Color.DARKGRAY);
    }

    /**
     * Beugrik az alertwin ablak.
     */
    public void ShowAlertWin() {
        this.stopClock();
        alertwin.showAndWait();
    }

    /**
     * Újra rajzolja a pályát.
     */
    public void Restart() {
        gc.setFill(Color.BLACK);
        if (this.nehezseg.equals("könnyű")) {
            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                    gc.fillRect(spacing + i * 50, spacing + j * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
        }

        if (this.nehezseg.equals("közepes")) {
            for (int i = 0; i < 16; i++)
                for (int j = 0; j < 16; j++)
                    gc.fillRect(spacing + i * 50, spacing + j * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
        }

        if (this.nehezseg.equals("nehéz")) {
            for (int i = 0; i < 30; i++)
                for (int j = 0; j < 30; j++)
                    gc.fillRect(spacing + i * 50, spacing + j * 50 + 50, 50 - 2 * spacing, 50 - 2 * spacing);
        }
    }

    /**
     * Beugrik az alert ablak.
     */
    public void ShowAlertLose() {
        this.stopClock();
        alert.showAndWait();

        controller.restart();
    }

    /**
     * Elindítja az órát ami másodpercenként frissül.
     */
    public void startClock() {
        clock = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {

                setTimer(timeinsecond);
                timeinsecond++;

            }
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    /**
     * Leállítja az órát.
     */
    public void stopClock() {
        clock.stop();
    }

    /**
     * Viszaadja az időt.
     *
     * @return int timeinsecond
     */
    public int getTimeinsecond() {
        return timeinsecond;
    }
}
