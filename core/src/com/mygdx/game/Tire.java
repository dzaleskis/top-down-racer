package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Tire {
    Body body;
    final float METERS_TO_PIXELS = 17.5f;
    Image img;

    Tire(){

    }

    Tire(World world, Stage stage, float pos_x, float pos_y){

        img = new Image(new Texture("tire.png"));
        img.setPosition(pos_x, pos_y);
        stage.addActor(img);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(img.getX()/METERS_TO_PIXELS, img.getY()/METERS_TO_PIXELS);

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(0.25f, 0.6f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        //fixtureDef.filter.maskBits = 0; // filters collisions with car object
        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setAngularDamping(0.2f);
        shape.dispose();
    }

    public void update(){

        updateFriction();

    }

    public void updateFriction(){

        float maxLateralImpulse = 0.8f;
        Vector2 impulse = getLateralVelocity(body).scl(-body.getMass());
        if (impulse.len() > maxLateralImpulse){
            impulse.scl(maxLateralImpulse / impulse.len());
        }
        body.applyLinearImpulse( impulse, body.getWorldCenter(), true);
        body.applyAngularImpulse( 0.1f * body.getInertia() * (-body.getAngularVelocity()), true );

        Vector2 currentForwardNormal = getForwardVelocity(body);
        float currentForwardSpeed = currentForwardNormal.nor().len();
        float dragForceMagnitude = -2 * currentForwardSpeed;
        body.applyForce(currentForwardNormal.scl(dragForceMagnitude), body.getWorldCenter(), true );

        img.setPosition(body.getPosition().x * METERS_TO_PIXELS, body.getPosition().y * METERS_TO_PIXELS );
        img.setRotation(body.getAngle());

    }

    public static Vector2 getLateralVelocity(Body body) {
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(1,0));
        float a = currentRightNormal.dot(body.getLinearVelocity());
        return currentRightNormal.scl(a);
    }

    public static Vector2 getForwardVelocity(Body body) {
        Vector2 currentRightNormal = body.getWorldVector(new Vector2(0,1));
        float a = currentRightNormal.dot(body.getLinearVelocity());
        return currentRightNormal.scl(a);
    }
    public Body getBody(){
        return this.body;
    }
}

