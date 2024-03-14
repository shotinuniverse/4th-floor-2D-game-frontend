package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.entities.AnimationEntity;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class Animation {

    public ArrayList<AnimationEntity> animations = new ArrayList<>();
    public Map<Integer, Map<Integer, Integer>> mapBeginFrameAnimations = new HashMap<>();
    public int counterFramesAnimationRest;

    public void animateRest(List<Rectangle> hitBoxes) {

        int currentFrame = Game.getCurrentFrame();

        int numberOfAnimationsRest = mapBeginFrameAnimations.size() == 0 ? 0 :
                mapBeginFrameAnimations.get(mapBeginFrameAnimations.keySet().toArray()[0]).size();
        if (counterFramesAnimationRest == numberOfAnimationsRest) {
            counterFramesAnimationRest = 0;
            mapBeginFrameAnimations.clear();
        }

        if (counterFramesAnimationRest == 0) {
            for (AnimationEntity animation : animations) {
                if (animation.getInMove() == 0) {
                    int index = animation.getOwnerId();
                    int frame = currentFrame + (animation.getFrameNumber() - 1);
                    frame = frame > Game.framesPerSecond ? frame - Game.framesPerSecond : frame;
                    if (mapBeginFrameAnimations.containsKey(index)){
                        mapBeginFrameAnimations.get(index).put(frame, animation.getId());
                    } else {
                        Map<Integer, Integer> map = new HashMap<>();
                        map.put(frame, animation.getId());
                        mapBeginFrameAnimations.put(index, map);
                    }
                }
            }
        }

        boolean madeAnimate = false;
        for (Integer objectId: mapBeginFrameAnimations.keySet()) {
            Rectangle rectangle = hitBoxes.get(objectId - 1);
            Map<Integer, Integer> frameAnimation = mapBeginFrameAnimations.get(objectId);
            for (Integer frameNumber: frameAnimation.keySet()) {
                if (frameNumber != currentFrame)
                    continue;
                int animationId = frameAnimation.get(frameNumber);
                for (AnimationEntity animation : animations) {
                    if (animation.getId() == animationId) {
                        double xPos = 0;
                        double yPos = 0;
                        if (animation.getAction().equals("right shift")) {
                            xPos = rectangle.getX() + animation.getValue();
                            yPos = rectangle.getY();
                        } else if (animation.getAction().equals("left shift")) {
                            xPos = rectangle.getX() - animation.getValue();
                            yPos = rectangle.getY();
                        }

                        rectangle.setX(xPos);
                        rectangle.setY(yPos);
                        madeAnimate = true;
                    }
                }
            }
        }

        if (madeAnimate)
            counterFramesAnimationRest += 1;
    }

    public void rollbackAnimate() {
        counterFramesAnimationRest = 0;
        mapBeginFrameAnimations.clear();
    }
}
