package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.repositories.AnimationRepository;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CharacterAnimation extends Animation{

    private Character character;
    private List<Rectangle> primalHitBoxes = new ArrayList<>();

    public CharacterAnimation (Character character) {
        this.character = character;
        //this.primalHitBoxes = character.hitBoxes;
        try {
            animations = AnimationRepository.getAnimationsForCharacter(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void animateCharacterRest() {
        Set<KeyCode> keyCodes = character.getActiveKeys();
        if (!(character.onGround && keyCodes.size() == 0 && character.numberFrameEndJump == 0)) {
            return;
        }

        if (counterFramesAnimationRest == 0) {
            this.primalHitBoxes.clear();
            for (Rectangle hitBox: character.hitBoxes){
                Rectangle temp = new Rectangle(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
                this.primalHitBoxes.add(temp);
            }
        }

        animateRest(character.hitBoxes);
    }

    public void rollbackCharacterAnimate() {
        Set<KeyCode> keyCodes = character.getActiveKeys();
        if ((!character.onGround || keyCodes.size() != 0 || character.numberFrameEndJump != 0) && counterFramesAnimationRest != 0) {
            for (int objectId: mapBeginFrameAnimations.keySet()) {
                Rectangle base = character.hitBoxes.get(objectId - 1);
                Rectangle primal = primalHitBoxes.get(objectId - 1);
                base.setX(primal.getX());
                base.setY(primal.getY());
            }
            rollbackAnimate();
        }
    }
}
