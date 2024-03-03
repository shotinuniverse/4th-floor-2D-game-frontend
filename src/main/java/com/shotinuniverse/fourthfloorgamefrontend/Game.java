package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.engine.Character;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class Game extends Application {

    private Group root;
    private int levelNumber;
    Character character;

    public Game(int levelNumber) {
        super();

        this.root = new Group();
        this.levelNumber = levelNumber;
    }

    @Override
    public void start(Stage primaryStage) {
        paintChar();

        root.getChildren().add(new Rectangle(0, 540, 1920, 30));

        SessionManager.scene.setOnKeyPressed(character);
        SessionManager.scene.setRoot(root);

        Thread t = new Thread(new DrawRunnable());
        t.start();
    }

    private void paintChar() {

        List<Rectangle> hitBoxes = null;
        try {
            hitBoxes = Character.getHitBoxesRectangles(levelNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Rectangle hitBox: hitBoxes)
            root.getChildren().add(hitBox);

        character = new Character(hitBoxes);
    }

    private class DrawRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(30);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            SessionManager.scene.setRoot(root);
                        }
                    });
                }
            } catch (InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }
}
