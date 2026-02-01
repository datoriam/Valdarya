package com.rpgpoo.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rpgpoo.game.battle.Batalha;
import com.rpgpoo.game.entity.Combatente;
import com.rpgpoo.game.main.Valdarya;
import java.util.List;

public class ArenaScreen extends ScreenAdapter {

    private final Valdarya game;
    private ShapeRenderer shape;
    private BitmapFont font;
    private SpriteBatch field;
    private OrthographicCamera camera;
    private Batalha batalha;

    // Construtor recebe a party da seleção, mas a batalha ainda precisa ser configurada
    public ArenaScreen(Valdarya game, List<Combatente> party) {
        this.game = game;
        // TODO: Inicializar a batalha usando os membros da 'party'
        // Atualmente a Arena não está utilizando a lista de heróis selecionados!
    }

    @Override
    public void show() {
        this.shape = new ShapeRenderer();
        this.font = new BitmapFont();
        font.getData().setScale(2.0f);
        this.field = new SpriteBatch();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // TODO: Carregar assets dinamicamente baseados nos inimigos/heróis atuais
        // Ex: if (inimigo instanceof Slime) carregar "slime.png"

        // Inicialização placeholder - SUBSTITUIR PELA LÓGICA REAL DE INÍCIO DE BATALHA
        // batalha = new Batalha(...);
        // batalha.iniciar();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        field.setProjectionMatrix(camera.combined);

        field.begin();
        // TODO: Desenhar fundo, heróis e inimigos nas posições corretas
        font.draw(field, "ARENA - COMBATE PENDENTE", 400, 400);
        field.end();

        // TODO: Capturar input para avançar turno (Espaço ou Clique)
        // if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        //     batalha.executarTurno();
        // }
    }

    @Override
    public void dispose() {
        if (field != null) field.dispose();
        if (font != null) font.dispose();
        if (shape != null) shape.dispose();
        // TODO: Dispose de texturas carregadas
    }
}
