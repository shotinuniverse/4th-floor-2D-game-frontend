package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.engine.*;
import com.shotinuniverse.fourthfloorgamefrontend.engine.Character;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Main;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Pause;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Game extends Application {

    private static Stage primaryStage;
    private static Pane root;
    private final int levelNumber;
    Character character;
    ArrayList<LevelPlatform> platformArrayList;
    CharacterAnimation characterAnimation;
    public static int framesPerSecond = 60;
    public static int currentFrame;
    public static boolean runnable;
    public static Thread gameThread;

    public Game(int levelNumber) {
        super();

        root = new Pane();
        this.levelNumber = levelNumber;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Pane getRoot() {
        return root;
    }

    @Override
    public void start(Stage primaryStage) {

        Game.primaryStage = primaryStage;
        runnable = true;

        Map<String, Object> map = LevelBuilder.createLevel(levelNumber, root);
        character = (Character) map.get("character");
        platformArrayList = (ArrayList<LevelPlatform>) map.get("platforms");
        characterAnimation = new CharacterAnimation(character);

        SessionManager.scene.setOnKeyPressed(character);
        SessionManager.scene.setOnKeyReleased(character);
        SessionManager.scene.setRoot(root);

        gameThread = new Thread(new DrawRunnable(), "game");
        gameThread.start();
    }

    public void resumeAfterPause() {
        SessionManager.scene.setRoot(root);
        currentFrame = currentFrame - 1;
        gameThread = new Thread(new DrawRunnable(), "game");
        gameThread.start();
    }

    public static int getCurrentFrame() {
        return currentFrame;
    }

    public Game getGameClass() {
        return this;
    }

    private class DrawRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (runnable) {
                    Thread.sleep(1000 / framesPerSecond);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            countFrames();
                            executeSequenceActionsFrame();
                        }
                    });
                }
            } catch (InterruptedException ex) {
                if (runnable) {
                    Pause pause = new Pause(getGameClass());
                    try {
                        pause.start(primaryStage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Main main = new Main();
                    try {
                        main.start(primaryStage);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void countFrames() {
            currentFrame += 1;
            if (currentFrame > framesPerSecond)
                currentFrame = 1;
        }

        private void executeSequenceActionsFrame() {
            characterAnimation.animateRest();
            character.characterKeyHandler();
            character.collisionHandler(platformArrayList);
            characterAnimation.animateMove();
            SessionManager.scene.setRoot(root);
        }
    }
}
