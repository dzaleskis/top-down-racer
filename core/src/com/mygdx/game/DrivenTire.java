package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DrivenTire extends Tire{

    Tire parent;
    float speed = 0;
    final float maxSpeed = 15;
    final float speedScale = 5f;

    DrivenTire(Tire parent){
        this.parent = parent;
        this.body = parent.getBody();
    }

    public void update(){
        parent.update();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            body.setLinearDamping(0.5f);
            if (speed < maxSpeed) {
                speed += 0.3f;
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (speed > 0.01f) {
                speed = 0f;
            }
            if(speed < 0.01f && speed > (-maxSpeed)/2){
                speed -= 0.05f;
            }
        }
        else {
            speed = 0;
            /*
            if (speed > 0f) {
                speed -= 0.2f;
            }
            if (speed < 0f) {
                speed += 0.2f;
            }
            */
        }
        //find current speed in forward direction
        Vector2 velocity = body.getWorldVector(new Vector2(0,1));
        velocity.scl(speed*speedScale);
        //body.applyLinearImpulse(velocity, body.getWorldCenter(), true);
        body.applyForceToCenter(velocity, true);
    }

}
