package com.david.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    //ShapeRenderer shapeRenderer;

    Texture[] birds, topTubes, bottomTubes;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    Circle birdCircle;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

    int gameState = 0;
    float gravity = 1;
    float gap = 450;
    float maxTubeOffset;
    Random randomGenerator;

    float tubeVelocity = 4;
    int numberOfTubes = 4;

    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;

    int points = 0;
    BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        //shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        bottomTubes = new Texture[1];
        topTubes = new Texture[1];
        bottomTubes[0] = new Texture("bottomtube.png");
        topTubes[0] = new Texture("toptube.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTubes[0].getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }
    }

    @Override
    public void render() {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState != 0) {


            if (Gdx.input.justTouched()) {
                velocity = -20;
            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -topTubes[0].getWidth()) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                } else {
                    tubeX[i] += numberOfTubes - tubeVelocity;
                }

                tubeX[i] = tubeX[i] - tubeVelocity;

                batch.draw(topTubes[0], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTubes[0], tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTubes[0].getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTubes[0].getWidth(), topTubes[0].getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTubes[0].getHeight() + tubeOffset[i], bottomTubes[0].getWidth(), bottomTubes[0].getHeight());

                if (tubeX[i] == Gdx.graphics.getWidth() / 2) {
                    points++;
                    Gdx.app.log("Point", points + "");
                }
            }

            if (birdY > 0 || velocity < 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            }

        } else {

            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

        font.draw(batch, String.valueOf(points), 100, 200);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);



        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < numberOfTubes; i++) {

            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTubes[0].getWidth(), topTubes[0].getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTubes[0].getHeight()+ tubeOffset[i], bottomTubes[0].getWidth(), bottomTubes[0].getHeight());

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                Gdx.app.log("Collision", "Yes");
            }


        }
        //shapeRenderer.end();
    }

    @Override
    public void dispose() {

    }
}
