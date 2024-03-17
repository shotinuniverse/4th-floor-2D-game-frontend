package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.entities.AnimationEntity;
import javafx.scene.shape.Rectangle;

import java.util.*;

/**
 * Класс для анимирования объектов
 */
public class Animation {

    /**
     * Массив сущностей объекта БД, представляющие таблицу animations
     */
    public ArrayList<AnimationEntity> animations = new ArrayList<>();
    public ArrayList<Integer[]> beginFramesAnimations = new ArrayList<>();
    /**
     * Хранит текущий номер анимации
     */
    public int counterFramesAnimation;
    public int lastState;

    /**
     * Начинает анимацию по определенному состоянию
     * @param hitBoxes <code>List<Rectangle></code> хит-боксы объекта
     * @param state <code>int</code> номер текущего состояния. Принимает следующие значения:
     *              1) 1 - состояние покоя;
     *              2) 2 - движение без прыжка;
     *              3) 3 - прыжок без движения;
     *              4) 4 - прыжок в движении
    */
    public void animate(List<Rectangle> hitBoxes, int state) {

        int currentFrame = Game.getCurrentFrame();

        int numberOfAnimations = beginFramesAnimations.size() == 0 ? 0 : beginFramesAnimations.get(0).length;

        if (counterFramesAnimation == numberOfAnimations) {
            counterFramesAnimation = 0;
            beginFramesAnimations.clear();
        }

        if (counterFramesAnimation == 0) {
            for (AnimationEntity animation : animations) {
                int stateFromEntity = animation.getState();
                if (state == stateFromEntity) {
                    int index = animation.getOwnerId();
                    int frame = currentFrame + (animation.getFrameNumber() - 1);
                    frame = frame > Game.framesPerSecond ? frame - Game.framesPerSecond : frame;
                    Integer[] values = new Integer[4];
                    values[0] = index;
                    values[1] = frame;
                    values[2] = animation.getId();
                    values[3] = stateFromEntity;

                    beginFramesAnimations.add(values);
                }
            }
        }

        boolean madeAnimate = animateHitBoxes(currentFrame, hitBoxes);

        if (madeAnimate)
            counterFramesAnimation += 1;
    }

    /**
     * Выполняет поиск анимаций для хит-боксов для текущего кадра и вызывает отрисовку в случае нахождения соответствия
     * @param currentFrame <code>int</code> номер текущего кадра
     * @param hitBoxes <code>List<Rectangle></code> хит-боксы объекта в виде массива прямоугольников
     * @return <code>boolean</code> анимация найдена и отрисовка выполнена
     */
    private boolean animateHitBoxes(int currentFrame, List<Rectangle> hitBoxes) {
        boolean madeAnimate = false;

        for (Integer[] animationInfo: beginFramesAnimations) {
            if (animationInfo[1] != currentFrame)
                continue;
            Rectangle rectangle = hitBoxes.get(animationInfo[0] - 1);
            for (AnimationEntity animation : animations) {
                if (animation.getId() == animationInfo[2]) {
                    animateBasedOnAction(animation, rectangle);
                    madeAnimate = true;
                }
            }
        }

        return madeAnimate;
    }

    /**
     * Выполняет отрисовку анимации по заданному действию (в БД), посредством движения хит-боксов
     * @param animation <code>AnimationEntity</code> сущность объекта БД, представляющая строку таблицы animations
     * @param rectangle <code>Rectangle</code> хит-бокс объекта
     */
    private void animateBasedOnAction(AnimationEntity animation, Rectangle rectangle) {
        String actionName = animation.getAction();

        if (actionName.equals("right shift")) {
            double xPos = rectangle.getX() + animation.getValue();
            double yPos = rectangle.getY();

            rectangle.setX(xPos);
            rectangle.setY(yPos);
        } else if (actionName.equals("left shift")) {
            double xPos = rectangle.getX() - animation.getValue();
            double yPos = rectangle.getY();

            rectangle.setX(xPos);
            rectangle.setY(yPos);
        } else if (actionName.equals("rotate clockwise")) {
            rectangle.setRotate(-animation.getValue());
        } else if (actionName.equals("rotate counter-clockwise")) {
            rectangle.setRotate(animation.getValue());
        }
    }

    /**
     * Откатывает инициализированные анимации
     */
    public void rollbackAnimate() {
        counterFramesAnimation = 0;
        beginFramesAnimations.clear();
    }
}
