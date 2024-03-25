package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.entities.AnimationEntity;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.AnimationRepository;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MobAnimation extends Animation implements AnimationInt {

    private final Mob mob;
    private final List<Rectangle> primalHitBoxes = new ArrayList<>();

    public MobAnimation(Mob mob) {
        this.mob = mob;
        //animations = AnimationRepository.getAnimationsForCharacter(1);
    }

    @Override
    public void animateRest() {

    }

    @Override
    public void animateMove() {

    }

    @Override
    public void saveStateHitBoxesBeforeAnimate() {
        if (counterFramesAnimation == 0) {
            this.primalHitBoxes.clear();
            for (Rectangle hitBox: mob.hitBoxes){
                Rectangle temp = new Rectangle(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
                temp.setRotate(hitBox.getRotate());
                this.primalHitBoxes.add(temp);
            }
        }
    }

    @Override
    public void rollbackAnimate() {
        for (Integer[] animationInfo: beginFramesAnimations) {
            List<String> actions = new ArrayList<>();
            for (AnimationEntity animation : animations) {
                if (animation.getId() == animationInfo[2]) {
                    actions.add(animation.getAction());
                }
            }

            Rectangle base = mob.hitBoxes.get(animationInfo[0] - 1);
            Rectangle primal = primalHitBoxes.get(animationInfo[0] - 1);
            if(actions.contains("right shift") || actions.contains("left shift")) {
                base.setX(primal.getX());
                base.setY(primal.getY());
            } else if (actions.contains("rotate clockwise") || actions.contains("rotate counter-clockwise")) {
                base.setRotate(primal.getRotate());
            }
        }

        clearAnimations();
    }
}
