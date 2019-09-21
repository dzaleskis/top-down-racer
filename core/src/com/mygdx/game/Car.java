package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Car implements Entity{
    /*
    Tire top_left;
    Tire top_right;
    Tire bottom_left;
    Tire bottom_right;
    RevoluteJoint top_left_joint;
    RevoluteJoint top_right_joint;
    RevoluteJoint bottom_left_joint;
    RevoluteJoint bottom_right_joint;
    Vector<Tire> tires;
    */

    private Image img;
    private Body body;
    private Rect rect;

    private float speed = 0;
    private float velocity = 0;
    private float lastPosX = 0;
    private float lastPosY = 0;


    Car(World world, Stage stage){

        rect = new Rect(320, 240, 70, 35);

        img = new Image(new Texture(Constants.carPath));
        img.setPosition(rect.x, rect.y);
        stage.addActor(img);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(rect.x/Constants.metersToPixels, rect.y/Constants.metersToPixels);
        bodyDef.angle = 90 * MathUtils.degreesToRadians;
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(rect.w/Constants.metersToPixels, rect.h/Constants.metersToPixels );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setAngularDamping(0.5f);
        shape.dispose();

        lastPosX = body.getPosition().x;
        lastPosY = body.getPosition().y;

        body.setUserData((Object) this);

        /*

        float img_pos_x = img.getX();
        float img_pos_y = img.getY();
        float img_width = img.getWidth();
        float img_height = img.getHeight();

        top_left = new SteeredTire(new Tire(world, stage, img_pos_x - 7, img_pos_y + img_height - 25));
        top_right = new SteeredTire(new Tire(world, stage, img_pos_x + img_width + 2, img_pos_y + img_height - 25));
        bottom_left = new DrivenTire(new Tire(world, stage, img_pos_x - 7, img_pos_y + 10));
        bottom_right = new DrivenTire(new Tire(world, stage, img_pos_x + img_width + 2, img_pos_y + 10));

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;//with both these at zero...
        jointDef.upperAngle = 0;//...the joint will not move
        jointDef.localAnchorB.setZero();

        jointDef.bodyB = top_left.getBody();
        jointDef.localAnchorA.set(-1f, 2);
        top_left_joint = (RevoluteJoint) world.createJoint(jointDef);

        jointDef.bodyB = top_right.getBody();
        jointDef.localAnchorA.set(1,2);
        top_right_joint = (RevoluteJoint) world.createJoint(jointDef);

        jointDef.bodyB = bottom_left.getBody();
        jointDef.localAnchorA.set(-1f,-2);
        bottom_left_joint = (RevoluteJoint) world.createJoint(jointDef);

        jointDef.bodyB = bottom_right.getBody();
        jointDef.localAnchorA.set(1f,-2);
        bottom_right_joint = (RevoluteJoint) world.createJoint(jointDef);

        tires = new Vector();
        tires.add(top_left);
        tires.add(top_right);
        tires.add(bottom_left);
        tires.add(bottom_right);

        */

    }

    @Override
    public void update(float delta){

        //System.out.println(rect.x);
        //System.out.println(rect.y); 210-540

        float newPosX = body.getPosition().x;
        float newPosY = body.getPosition().y;
        double pow1 = Math.pow((double)(lastPosX - newPosX), 2);
        double pow2 = Math.pow((double)(lastPosY - newPosY), 2);
        double distance = Math.sqrt(pow1 + pow2);
        double velocity = distance/delta;
        lastPosX = newPosX;
        lastPosY = newPosY;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){

            body.applyAngularImpulse(Constants.carRotationScale * (float) Math.min(velocity, 20) , true);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            body.applyAngularImpulse(-Constants.carRotationScale * (float ) Math.min(velocity, 20) , true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            body.setLinearDamping(0.5f);
            if (speed < Constants.carMaxSpeed) {
                speed += 0.3f;
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (speed > 0.01f) {
                body.setLinearDamping(3f);
                speed -= 0.3f;
            }
            if(speed < 0.01f && speed > (-Constants.carMaxSpeed)/2){
                speed -= 0.05f;
            }
        }
        else {

            body.setLinearDamping(2f);
            if (speed > 0f) {
                speed -= 0.5f;
            }
            if (speed < 0f) {
                speed += 0.5f;
            }
        }
        Vector2 v = body.getWorldVector(new Vector2(1,0));
        v.scl(speed * Constants.carSpeedScale);
        body.applyLinearImpulse(v, body.getWorldCenter(), true);

        updateFriction();

        rect.x = body.getPosition().x * Constants.metersToPixels;
        rect.y = body.getPosition().y * Constants.metersToPixels;
        img.setPosition((body.getPosition().x * Constants.metersToPixels) - rect.w/2, (body.getPosition().y * Constants.metersToPixels) - rect.h/2);
        img.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

    }

    @Override
    public void onCollision(Object other) {



    }


    public void updateFriction(){

        float maxLateralImpulse = 2f;
        Vector2 impulse = getLateralVelocity(body).scl(-body.getMass());
        if (impulse.len() > maxLateralImpulse){
            impulse.scl(maxLateralImpulse / impulse.len());
        }
        body.applyLinearImpulse( impulse, body.getWorldCenter(), true);
        body.applyAngularImpulse( 0.25f * body.getInertia() * (-body.getAngularVelocity()), true );

        Vector2 currentForwardNormal = getForwardVelocity(body);
        float currentForwardSpeed = currentForwardNormal.nor().len();
        float dragForceMagnitude = -2 * currentForwardSpeed;
        body.applyForce(currentForwardNormal.scl(dragForceMagnitude), body.getWorldCenter(), true );

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
    public float getX(){
        return img.getX();
    }
    public float getY(){
        return img.getY();
    }
    public Rect getRect(){ return rect; }
    public Vector2 getPosition() { return body.getPosition(); }

}
