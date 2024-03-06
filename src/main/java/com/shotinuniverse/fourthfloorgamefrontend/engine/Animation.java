package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.entities.AnimationEntity;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.AnimationRepository;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

public class Animation {

    public ArrayList<AnimationEntity> animations = new ArrayList<>();
    public final Map<Integer, Integer> mapBeginFrameAnimations = new HashMap<>();
    public int countFramesAnimationRest;
    public final List<Integer> objectIdForAnimationRest = new ArrayList<>();

    public void animateRest(List<Rectangle> hitBoxes) {

        int currentFrame = Game.getCurrentFrame();

        if (mapBeginFrameAnimations.size() > 0 && !mapBeginFrameAnimations.containsKey(currentFrame))
            return;

        if (countFramesAnimationRest == 0) {
            for (AnimationEntity animation : animations) {
                if (animation.getInMove() == 0) {
                    mapBeginFrameAnimations.put(currentFrame + (animation.getFrameNumber() - 1), animation.getId());
                    if (!objectIdForAnimationRest.contains(animation.getOwnerId()))
                        objectIdForAnimationRest.add(animation.getOwnerId());
                }
            }
        } else {
            if (countFramesAnimationRest == mapBeginFrameAnimations.size()) {
                countFramesAnimationRest = 0;
                mapBeginFrameAnimations.clear();
                objectIdForAnimationRest.clear();
            }
        }

        for (int objectId: objectIdForAnimationRest) {
            Rectangle rectangle = hitBoxes.get(objectId - 1);
            int animationId = mapBeginFrameAnimations.get(currentFrame);
            for (AnimationEntity animation : animations) {
                if (animation.getId() == animationId) {
                    if (animation.getAction().equals("right shift")) {
                        double xPos = rectangle.getX() + animation.getValue();
                        rectangle.setX(xPos);
                    } else if (animation.getAction().equals("left shift")) {
                        double xPos = rectangle.getX() - animation.getValue();
                        rectangle.setX(xPos);
                    }
                }
            }
        }

        countFramesAnimationRest += 1;
    }

    public void rollbackAnimate() {
        countFramesAnimationRest = 0;
        mapBeginFrameAnimations.clear();
        objectIdForAnimationRest.clear();
    }
}
