package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.entities.AnimationEntity;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.AnimationRepository;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterAnimation extends Animation{

    private final Character character;
    private final List<Rectangle> primalHitBoxes = new ArrayList<>();

    public CharacterAnimation (Character character) {
        this.character = character;
        try {
            animations = AnimationRepository.getAnimationsForCharacter(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void animateCharacterRest() {
        int state = 1;

        if (character.inMove || !character.onGround)
            return;

        if (lastState != state) {
            rollbackCharacterAnimate();
            lastState = state;
        }

        saveStateHitBoxesBeforeAnimate();

        animate(character.hitBoxes, state);
    }

    public void animateCharacterMove() {
        if (!character.inMove && character.onGround)
            return;

        int state;
        if (character.onGround) {
            state = 2;
        } else {
            if (character.inMove)
                state = 4;
            else
                state = 3;
        }

        if (lastState != state) {
            rollbackCharacterAnimate();
            lastState = state;
        }

        saveStateHitBoxesBeforeAnimate();

        animate(character.hitBoxes, state);
    }

    private void saveStateHitBoxesBeforeAnimate() {
        if (counterFramesAnimation == 0) {
            this.primalHitBoxes.clear();
            for (Rectangle hitBox: character.hitBoxes){
                Rectangle temp = new Rectangle(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
                temp.setRotate(hitBox.getRotate());
                this.primalHitBoxes.add(temp);
            }
        }
    }

    public void rollbackCharacterAnimate() {
        for (Integer[] animationInfo: beginFramesAnimations) {
            List<String> actions = new ArrayList<>();
            for (AnimationEntity animation : animations) {
                if (animation.getId() == animationInfo[2]) {
                    actions.add(animation.getAction());
                }
            }

            Rectangle base = character.hitBoxes.get(animationInfo[0] - 1);
            Rectangle primal = primalHitBoxes.get(animationInfo[0] - 1);
            if(actions.contains("right shift") || actions.contains("left shift")) {
                base.setX(primal.getX());
                base.setY(primal.getY());
            } else if (actions.contains("rotate clockwise") || actions.contains("rotate counter-clockwise")) {
                base.setRotate(primal.getRotate());
            }
        }

        rollbackAnimate();
    }
}
