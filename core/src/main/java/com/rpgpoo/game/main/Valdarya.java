package com.rpgpoo.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rpgpoo.game.screen.MenuScreen;

public class Valdarya extends Game {

    // Recursos compartilhados para economizar memória
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // Inicia o jogo pelo Menu (fluxo normal)
        // RESPONSAVEL: DESENVOLVA A CLASSE MenuScreen.java
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Apenas desenha a tela atual, sem lógica extra
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
