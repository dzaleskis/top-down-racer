package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class SteeredTire extends Tire{

    Tire parent;
    float rotation = 0;
    final float maxRotation = 60;
    final float rotationScale = 1.5f;

    SteeredTire(Tire parent){
        this.parent = parent;
        this.body = parent.getBody();
    }

    public void update(){
        parent.update();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(rotation < maxRotation){
                rotation += 2;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(rotation > -maxRotation){
                rotation -= 2;
            }
        }
        else{
            rotation = 0;
        }
        body.applyTorque(rotation*rotationScale, true);
    }

}