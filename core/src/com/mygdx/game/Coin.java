package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Coin implements Entity{

    private Image img;
    private Rect rect;
    private Body body;


    Coin(Stage stage, World world, float posY){
        img = new Image(new Texture(Constants.coinPath));
        float posX = (float) Math.random() * Constants.screenWidth;
        rect = new Rect(posX, posY, (int) img.getWidth(), (int) img.getHeight());
        img.setPosition(rect.x, rect.y);
        stage.addActor(img);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(rect.x/Constants.metersToPixels, rect.y/Constants.metersToPixels);

        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(rect.w/Constants.metersToPixels, rect.h/Constants.metersToPixels );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3f;
        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setAngularDamping(0.3f);
        shape.dispose();

        body.setUserData((Object) this);
    }
    @Override
    public void update(float delta){

        img.setPosition((body.getPosition().x * Constants.metersToPixels) - rect.w/2, (body.getPosition().y * Constants.metersToPixels) - rect.h/2);
        img.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

    }
    @Override
    public void onCollision(Object other){
        img.scaleBy(1.5f);
    }
}
