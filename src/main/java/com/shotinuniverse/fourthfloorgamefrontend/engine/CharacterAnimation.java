package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.repositories.AnimationRepository;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class CharacterAnimation extends Animation{

    private Character character;
    private List<Rectangle> primalHitBoxes;

    public CharacterAnimation (Character character) {
        this.character = character;
        this.primalHitBoxes = character.hitBoxes;
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

        animateRest(character.hitBoxes);
    }

    public void rollbackCharacterAnimate() {
        Set<KeyCode> keyCodes = character.getActiveKeys();
        if ((!character.onGround || keyCodes.size() != 0 || character.numberFrameEndJump != 0) && countFramesAnimationRest != 0) {
            for (int objectId: objectIdForAnimationRest) {
                character.hitBoxes.set(objectId - 1, primalHitBoxes.get(objectId - 1));
            }
            rollbackAnimate();
        }
    }
}
