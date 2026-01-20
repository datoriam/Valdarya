package com.rpgpoo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;

public class RpgLogics extends ApplicationAdapter {
    int x = 50;
    int y = 50;
    int xSpeed = 5;
    int ySpeed;
    int size;
    ShapeRenderer shape;

    @Override
    public void create () {
        shape = new ShapeRenderer();
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        x += 5;
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle(x, y, 50);
        shape.end();
        x += xSpeed;
        if (x > Gdx.graphics.getWidth()) {
            xSpeed = -5;
        }
        if (x < 0){
            xSpeed = 5;
        }
    }
}
