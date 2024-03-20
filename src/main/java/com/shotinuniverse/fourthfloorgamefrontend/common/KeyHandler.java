package com.shotinuniverse.fourthfloorgamefrontend.common;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;
import java.util.*;

public class KeyHandler implements EventHandler<KeyEvent> {
    private boolean inGame;
    private final Set<KeyCode> keysUp = new HashSet<>();
    private final Set<KeyCode> keysRight = new HashSet<>();
    private final Set<KeyCode> keysDown = new HashSet<>();
    private final Set<KeyCode> keysLeft = new HashSet<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public KeyHandler (boolean inGame) {
        this.inGame = inGame;

        if (inGame)
            setControlKeysForGame();
        else
            setControlKeysForMenu();
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            activeKeys.add(event.getCode());
        } else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            activeKeys.remove(event.getCode());
        }
    }

    private void setControlKeysForGame() {
        String query = "select action_types.name as name, keys.value as value from keys as keys inner join action_types as action_types on keys.action = action_types._id";
        try {
            ArrayList<Object> arrayList = SqlQuery.getObjects(query);
            for (Object object: arrayList) {
                Map<String, Object> map = (HashMap) object;
                if (map.get("name").equals("move forward"))
                    keysRight.add(KeyCode.valueOf((String) map.get("value")));
                else if (map.get("name").equals("move back"))
                    keysLeft.add(KeyCode.valueOf((String) map.get("value")));
                else if (map.get("name").equals("jump"))
                    keysUp.add(KeyCode.valueOf((String) map.get("value")));
                else if (map.get("name").equals("sneak"))
                    keysDown.add(KeyCode.valueOf((String) map.get("value")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setControlKeysForMenu() {
        keysRight.add(KeyCode.RIGHT);
        keysUp.add(KeyCode.UP);
        keysLeft.add(KeyCode.LEFT);
        keysDown.add(KeyCode.DOWN);
    }

    public Set<KeyCode> getActiveKeys() {
        return Collections.unmodifiableSet(activeKeys);
    }

    public int getCountActiveKeys() {
        return getActiveKeys().size();
    }

    public void handleServiceKeyPressed(Set<KeyCode> keyCodes) throws RuntimeException {
        if (inGame) {
            if (keyCodes.contains(KeyCode.ESCAPE)) {
                synchronized (Thread.currentThread()) {
                    Game.gameThread.interrupt();
                }
            }
        }
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean pressedRight(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyRight: keysRight) {
            if (keyCodes.contains(keyRight))
                haveMove = true;
        }
        return haveMove;
    }

    public boolean pressedLeft(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyLeft: keysLeft) {
            if (keyCodes.contains(keyLeft))
                haveMove = true;
        }
        return haveMove;
    }

    public boolean pressedUp(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyUp: keysUp) {
            if (keyCodes.contains(keyUp))
                haveMove = true;
        }
        return haveMove;
    }

    public boolean pressedDown(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyDown: keysDown) {
            if (keyCodes.contains(keyDown))
                haveMove = true;
        }
        return haveMove;
    }

}
