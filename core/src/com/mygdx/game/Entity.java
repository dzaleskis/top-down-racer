package com.mygdx.game;

public interface Entity {
    public void update(float delta);
    public void onCollision(Object other);

}
