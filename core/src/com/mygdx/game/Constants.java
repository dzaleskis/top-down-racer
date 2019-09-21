package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Constants {

    //used by all
    static final float metersToPixels = 17.5f;

    //car-specific
    static final float carRotationScale = 10f;
    static final float carMaxSpeed = 10;
    static final float carSpeedScale = 2.5f;

    //incoming-car specific
    static final float incomingSpeedScale = 5f;

    //first level
    static final float cameraSpeed = 5;
    static final float nextLevel = 5000;

    //Window Dimensions
    static final int screenWidth = Gdx.graphics.getWidth();
    static final int screenHeight = Gdx.graphics.getHeight();

    //filenames
    static final String level1BackgroundPath = "road2.jpg";
    static final String level2BackgroundPath = "highway.png";
    static final String carPath = "c1.png";
    static final String incomingPath = "c2.png";
    static final String coinPath = "health.png";

    //sets position at which to spawn new game objects
    static final int spawnLimit = 500;
    static final int level1RoadWidth = 340;
    static final int level1RoadStartX = 210;

}
