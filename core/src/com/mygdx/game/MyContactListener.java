package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;


public class MyContactListener implements ContactListener {
    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixt1 = contact.getFixtureA();
        Fixture fixt2 = contact.getFixtureB();
        Body body1 = fixt1.getBody();
        Body body2 = fixt2.getBody();

        Object obj1 = body1.getUserData();
        Object obj2 = body2.getUserData();

        if(obj1 != null && obj1 instanceof Entity){
            Entity e1 = (Entity) obj1;
            e1.onCollision(obj2);

        }
        if(obj2 != null && obj2 instanceof Entity){
            Entity e2 = (Entity) obj2;
            e2.onCollision(obj1);
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

};