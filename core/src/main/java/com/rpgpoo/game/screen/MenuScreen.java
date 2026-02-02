package com.rpgpoo.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.rpgpoo.game.main.Valdarya;

public class MenuScreen extends ScreenAdapter {

    private final Valdarya game;
    private OrthographicCamera camera;
    private Texture imgTitleScreen;
    private Rectangle rectBotaoNovoJogo;
    private BitmapFont customFont;
    private boolean isHovering = false;

    public MenuScreen(Valdarya game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("game_over.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = Color.WHITE;
        customFont = generator.generateFont(parameter);
        generator.dispose();

        try {
            if (Gdx.files.internal("titlescreen.png").exists()) {
                imgTitleScreen = new Texture("titlescreen.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.rectBotaoNovoJogo = new Rectangle(490, 150, 300, 80);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        isHovering = rectBotaoNovoJogo.contains(mousePos.x, mousePos.y);

        game.batch.begin();

        if (imgTitleScreen != null) {
            game.batch.draw(imgTitleScreen, 0, 0, 1280, 720);
        }

        if (isHovering) {
            customFont.setColor(Color.YELLOW);
        } else {
            customFont.setColor(Color.WHITE);
        }

        customFont.draw(game.batch, "NOVO JOGO", rectBotaoNovoJogo.x, rectBotaoNovoJogo.y + 60);

        game.batch.end();

        if (Gdx.input.justTouched() && isHovering) {
            game.setScreen(new SelecaoScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1280, 720);
    }

    @Override
    public void dispose() {
        if (imgTitleScreen != null) imgTitleScreen.dispose();
        if (customFont != null) customFont.dispose();
    }
}