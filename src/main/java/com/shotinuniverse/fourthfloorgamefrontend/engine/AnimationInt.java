package com.shotinuniverse.fourthfloorgamefrontend.engine;

public interface AnimationInt {
    void animateRest();
    void animateMove();
    void saveStateHitBoxesBeforeAnimate();
    void rollbackAnimate();
}
