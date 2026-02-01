package com.rpgpoo.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpgpoo.game.entity.Arcanista;
import com.rpgpoo.game.entity.Atirador;
import com.rpgpoo.game.entity.Combatente;
import com.rpgpoo.game.entity.Guardiao;
import com.rpgpoo.game.main.Valdarya;

import java.util.ArrayList;
import java.util.List;

public class SelecaoScreen extends ScreenAdapter {

    private final Valdarya game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    // Lista do time que vai pra Arena
    private List<Combatente> party;

    // Assets Visuais
    private Texture imgFundo;
    private Texture imgMoldura;
    private Texture imgAvatarGuardiao;
    private Texture imgAvatarArcanista;
    private Texture imgAvatarAtirador;

    private BitmapFont font;

    // Áreas de clique (Hitboxes)
    private Rectangle rectGuardiao;
    private Rectangle rectArcanista;
    private Rectangle rectAtirador;
    private Rectangle rectBotaoIniciar;
    private Rectangle rectBotaoLimpar;

    // Cor para quando passar o mouse em cima
    private static final Color COR_HOVER = Color.PURPLE;

    public SelecaoScreen(Valdarya game) {
        this.game = game;
        this.party = new ArrayList<>();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        batch = new SpriteBatch();

        // --- CARREGAMENTO DE ASSETS ---
        try {
            if (Gdx.files.internal("fundoselecao.png").exists())
                imgFundo = new Texture("fundoselecao.png");
            else
                imgFundo = new Texture("fundobatalha.png");

            if (Gdx.files.internal("moldura.png").exists())
                imgMoldura = new Texture("moldura.png");

            imgAvatarGuardiao = carregarTexturaSegura("avatar_guardiao.png", "guardiao.png");
            imgAvatarArcanista = carregarTexturaSegura("avatar_arcanista.png", "mago.png");
            imgAvatarAtirador = carregarTexturaSegura("avatar_atirador.png", "arqueiro.png");

        } catch (Exception e) {
            System.out.println("Erro assets: " + e.getMessage());
        }

        // --- FONTE ---
        configurarFonte();

        // --- POSIÇÕES NA TELA (Proporção 1080x1470) ---

        // Ajustei o Y para 320 para centralizar verticalmente a nova altura
        float yAvatares = 320;

        float larguraCard = 150;

        // Cálculo automático da altura para manter a proporção da sua imagem
        // (1470 / 1080) = 1.36 de fator de altura
        float alturaCard = larguraCard * (1470f / 1080f); // ≈ 204 pixels

        // Posições X
        float xGuardiao = 300;
        float xArcanista = 565;
        float xAtirador = 830;

        // Cria os retângulos com a proporção exata da sua imagem
        rectGuardiao = new Rectangle(xGuardiao, yAvatares, larguraCard, alturaCard);
        rectArcanista = new Rectangle(xArcanista, yAvatares, larguraCard, alturaCard);
        rectAtirador = new Rectangle(xAtirador, yAvatares, larguraCard, alturaCard);

        rectBotaoIniciar = new Rectangle(540, 50, 200, 60);
        rectBotaoLimpar = new Rectangle(540, 120, 200, 40);
    }

    // Auxiliar para tentar carregar imagem principal ou fallback
    private Texture carregarTexturaSegura(String principal, String fallback) {
        if (Gdx.files.internal(principal).exists()) {
            return new Texture(principal);
        } else if (Gdx.files.internal(fallback).exists()) {
            return new Texture(fallback);
        } else {
            return new Texture("guardiao.png"); // Último recurso pra não travar
        }
    }

    private void configurarFonte() {
        try {
            // Procura na raiz assets conforme solicitado
            if (Gdx.files.internal("game_over.ttf").exists()) {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("game_over.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.size = 64;
                parameter.color = Color.WHITE;
                parameter.borderColor = Color.BLACK;
                parameter.borderWidth = 2;
                font = generator.generateFont(parameter);
                generator.dispose();
            } else {
                font = new BitmapFont();
                font.getData().setScale(2);
            }
        } catch (Exception e) {
            font = new BitmapFont();
            font.getData().setScale(2);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Pega posição do mouse para o efeito hover
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        batch.begin();

        if (imgFundo != null) batch.draw(imgFundo, 0, 0, 1280, 720);

        // Título
        font.setColor(Color.GOLD);
        drawCentralizado("ESCOLHA SEUS HEROIS", 650);

        // --- GUARDIÃO ---
        batch.draw(imgAvatarGuardiao, rectGuardiao.x, rectGuardiao.y, rectGuardiao.width, rectGuardiao.height);
        if (imgMoldura != null) batch.draw(imgMoldura, rectGuardiao.x - 5, rectGuardiao.y - 5, rectGuardiao.width + 10, rectGuardiao.height + 10);

        // Hover Roxo
        font.setColor(rectGuardiao.contains(mousePos.x, mousePos.y) ? COR_HOVER : Color.CYAN);
        font.draw(batch, "GUARDIAO", rectGuardiao.x + 10, rectGuardiao.y - 10);

        // --- ARCANISTA ---
        batch.draw(imgAvatarArcanista, rectArcanista.x, rectArcanista.y, rectArcanista.width, rectArcanista.height);
        if (imgMoldura != null) batch.draw(imgMoldura, rectArcanista.x - 5, rectArcanista.y - 5, rectArcanista.width + 10, rectArcanista.height + 10);

        font.setColor(rectArcanista.contains(mousePos.x, mousePos.y) ? COR_HOVER : Color.PURPLE);
        font.draw(batch, "ARCANISTA", rectArcanista.x + 10, rectArcanista.y - 10);

        // --- ATIRADOR ---
        batch.draw(imgAvatarAtirador, rectAtirador.x, rectAtirador.y, rectAtirador.width, rectAtirador.height);
        if (imgMoldura != null) batch.draw(imgMoldura, rectAtirador.x - 5, rectAtirador.y - 5, rectAtirador.width + 10, rectAtirador.height + 10);

        font.setColor(rectAtirador.contains(mousePos.x, mousePos.y) ? COR_HOVER : Color.GREEN);
        font.draw(batch, "ATIRADOR", rectAtirador.x + 20, rectAtirador.y - 10);

        // Lista do Time
        font.setColor(Color.WHITE);
        font.draw(batch, "Time Atual (" + party.size() + "/3):", 50, 600);
        for (int i = 0; i < party.size(); i++) {
            font.draw(batch, "- " + party.get(i).getNome(), 50, 550 - (i * 40));
        }

        // Botões
        if (!party.isEmpty()) {
            font.setColor(rectBotaoIniciar.contains(mousePos.x, mousePos.y) ? COR_HOVER : Color.YELLOW);
            font.draw(batch, "[ INICIAR BATALHA ]", rectBotaoIniciar.x - 20, rectBotaoIniciar.y + 40);
        }

        font.setColor(rectBotaoLimpar.contains(mousePos.x, mousePos.y) ? COR_HOVER : Color.RED);
        font.draw(batch, "[ LIMPAR TIME ]", rectBotaoLimpar.x, rectBotaoLimpar.y + 30);

        batch.end();

        if (Gdx.input.justTouched()) {
            tratarClique(mousePos);
        }
    }

    private void tratarClique(Vector3 toque) {
        // Limite de 3 membros (para UI) mas flexível
        if (party.size() < 3) {
            if (rectGuardiao.contains(toque.x, toque.y)) {
                party.add(new Guardiao("Guardiao " + (party.size() + 1)));
            } else if (rectArcanista.contains(toque.x, toque.y)) {
                party.add(new Arcanista("Mago " + (party.size() + 1)));
            } else if (rectAtirador.contains(toque.x, toque.y)) {
                party.add(new Atirador("Atirador " + (party.size() + 1)));
            }
        }

        if (rectBotaoLimpar.contains(toque.x, toque.y)) {
            party.clear();
        }

        if (rectBotaoIniciar.contains(toque.x, toque.y) && !party.isEmpty()) {
            // CORREÇÃO: Inicia no Andar 1 explicitamente
            game.setScreen(new ArenaScreen(game, party, 1));
            dispose();
        }
    }

    private void drawCentralizado(String texto, float y) {
        GlyphLayout layout = new GlyphLayout(font, texto);
        float x = (1280 - layout.width) / 2;
        font.draw(batch, texto, x, y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (imgFundo != null) imgFundo.dispose();
        if (imgMoldura != null) imgMoldura.dispose();
        if (imgAvatarGuardiao != null) imgAvatarGuardiao.dispose();
        if (imgAvatarArcanista != null) imgAvatarArcanista.dispose();
        if (imgAvatarAtirador != null) imgAvatarAtirador.dispose();
        if (font != null) font.dispose();
    }
}
