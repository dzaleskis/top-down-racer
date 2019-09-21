package com.mygdx.game;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import java.util.ArrayList;


public class GameScreen2 implements Screen {

    private Stage stage;
    private OrthographicCamera camera;
    private Game game;
    private World world;
    private Car car;
    private RayHandler rayHandler;

    private Image road1;
    private Image road2;
    private Body leftBarrier;
    private Body rightBarrier;

    private ArrayList<Entity> entities;
    private float carPosY;
    private boolean addedNewCoin = false;

    public GameScreen2(Game aGame) {
        try{
            game = aGame;
            stage = new Stage(new ScreenViewport());
            camera = (OrthographicCamera) stage.getViewport().getCamera();
            camera.zoom = 1f;
            world = new World(new Vector2(0, 0f), true);

            road1 = new Image(new Texture(Constants.level2BackgroundPath));
            road1.setPosition(0,0);
            road2 = new Image(new Texture(Constants.level2BackgroundPath));
            road2.setPosition(0, Constants.screenHeight);
            stage.addActor(road1);
            stage.addActor(road2);

            car = new Car(world, stage);
            entities = new ArrayList<Entity>();
            entities.add(car);

            rayHandler = new RayHandler(world);
            rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 1f);
            rayHandler.setBlurNum(3);
            PointLight pl = new PointLight(rayHandler, 128, Color.MAGENTA, 10,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
            PointLight pl2 = new PointLight(rayHandler, 128, Color.LIME, 10,0,0);
            rayHandler.setShadows(true);
            pl.setStaticLight(true);
            pl.setSoft(true);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            bodyDef.position.set(0, 0);
            // Create a body in the world using our definition
            leftBarrier = world.createBody(bodyDef);
            bodyDef.position.set((Constants.screenWidth + 60)/Constants.metersToPixels, 0);
            // Create a body in the world using our definition
            rightBarrier = world.createBody(bodyDef);

            // Now define the dimensions of the physics shape
            PolygonShape shape = new PolygonShape();

            shape.setAsBox(1, 1000f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            Fixture leftFixture = leftBarrier.createFixture(fixtureDef);
            Fixture rightFixture = rightBarrier.createFixture(fixtureDef);
            shape.dispose();


        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen2","show");
        Gdx.input.setInputProcessor(stage);
        world.setContactListener(new MyContactListener());
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        moveCamera(delta);
        update(delta);
        stage.act();
        stage.draw();
        rayHandler.setCombinedMatrix(stage.getCamera().combined,0,0,1,1);
        rayHandler.updateAndRender();
    }

    private void moveCamera(float delta){
        float carX = car.getX();
        float carY = car.getY();
        camera.position.y += delta * Constants.cameraSpeed * (carY - camera.position.y);
        camera.update();
    }

    private void update(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 3, 5);
        updateBackground();
        carPosY = car.getY();
        if(carPosY % 1000 > Constants.spawnLimit){
            if(!addedNewCoin){
                entities.add(new Coin(stage, world,carPosY + Constants.spawnLimit));
                addedNewCoin = true;
            }
        }
        else{
            addedNewCoin = false;
        }

        //inc.forEach(incoming -> incoming.update());
        for(Entity e : entities){
            e.update(delta);
        }

    }

    private void updateBackground(){

        if(camera.position.y > road1.getY() + Constants.screenHeight + Constants.screenHeight/2){
            road1.setPosition(0,road2.getY()+Constants.screenHeight);
        }
        if(camera.position.y > road2.getY() + Constants.screenHeight + Constants.screenHeight/2){
            road2.setPosition(0,road1.getY()+Constants.screenHeight);
        }
        if(camera.position.y < road1.getY() + Constants.screenHeight/2){
            road2.setPosition(0,road1.getY()-Constants.screenHeight);
        }
        if(camera.position.y < road2.getY() + Constants.screenHeight/2){
            road1.setPosition(0,road2.getY()-Constants.screenHeight);
        }

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        stage.dispose();
        world.dispose();
        rayHandler.dispose();
    }

}
