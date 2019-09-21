package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Incoming implements Entity{

    private Image img;
    private Body body;
    private Rect rect;

    Incoming(World world, Stage stage, float startPosY){

        img = new Image(new Texture(Constants.incomingPath));
        float startPosX = (float) Math.random()* Constants.level1RoadWidth;
        startPosX += Constants.level1RoadStartX;
        rect = new Rect(startPosX, startPosY, (int) img.getWidth(), (int) img.getHeight());
        img.setPosition(rect.x, rect.y);
        stage.addActor(img);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(rect.x/Constants.metersToPixels, rect.y/Constants.metersToPixels);
        bodyDef.angle = 270 * MathUtils.degreesToRadians;
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(rect.w/Constants.metersToPixels, rect.h/Constants.metersToPixels );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setAngularDamping(0.3f);
        shape.dispose();

    }
    @Override
    public void update(float delta){

        //move towards player
        Vector2 velocity = body.getWorldVector(new Vector2(1,0));
        velocity.scl(Constants.incomingSpeedScale);
        //body.applyLinearImpulse(velocity, body.getWorldCenter(), true);
        body.applyLinearImpulse(velocity, body.getWorldCenter(),true);

        img.setPosition((body.getPosition().x * Constants.metersToPixels) - rect.w/2, (body.getPosition().y * Constants.metersToPixels) - rect.h/2);
        img.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    @Override
    public void onCollision(Object other) {

    }
}
