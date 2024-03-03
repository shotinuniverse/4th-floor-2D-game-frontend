package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.engine.Character;
import com.shotinuniverse.fourthfloorgamefrontend.engine.LevelBuilder;
import com.shotinuniverse.fourthfloorgamefrontend.engine.LevelPlatform;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class Game extends Application {

    private Pane root;
    private int levelNumber;
    Character character;
    ArrayList<LevelPlatform> platformArrayList;
    static int framesPerSecond = 60;
    public static int currentFrame;

    public Game(int levelNumber) {
        super();

        this.root = new Pane();
        this.levelNumber = levelNumber;
    }

    @Override
    public void start(Stage primaryStage) {

        Map<String, Object> map = LevelBuilder.createLevel(levelNumber, root);
        character = (Character) map.get("character");
        platformArrayList = (ArrayList<LevelPlatform>) map.get("platfroms");

        SessionManager.scene.setOnKeyPressed(character);
        SessionManager.scene.setOnKeyReleased(character);
        SessionManager.scene.setRoot(root);

        Thread t = new Thread(new DrawRunnable());
        t.start();
    }

    public static int getCurrentFrame() {
        return currentFrame;
    }

    private class DrawRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000 / framesPerSecond);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            countFrames();

                            character.move();
                            character.collisionHandler(platformArrayList);
                            SessionManager.scene.setRoot(root);
                        }
                    });
                }
            } catch (InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }

        public void countFrames() {
            currentFrame += 1;
            if (currentFrame > framesPerSecond)
                currentFrame = 1;
        }
    }
}
