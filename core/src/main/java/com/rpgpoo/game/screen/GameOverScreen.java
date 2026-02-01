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
import com.rpgpoo.game.entity.Combatente;
import com.rpgpoo.game.main.Valdarya;

import java.util.List;

public class GameOverScreen extends ScreenAdapter {

    private final Valdarya game;
    private final boolean venceu;

    // Precisamos disso pra continuar a jornada
    private final List<Combatente> party;
    private final int andarAtual;

    private SpriteBatch batch;
    private Texture imgFundo;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Rectangle boundsBotaoPrincipal;
    private Rectangle boundsVoltarMenu;
    private GlyphLayout layoutBotaoPrincipal;
    private GlyphLayout layoutVoltar;

    // Construtor atualizado para receber o Progresso
    public GameOverScreen(Valdarya game, boolean venceu, List<Combatente> party, int andarAtual) {
        this.game = game;
        this.venceu = venceu;
        this.party = party;
        this.andarAtual = andarAtual;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        batch = new SpriteBatch();

        // Carrega imagem (Vitoria ou Derrota)
        String nomeArquivo = venceu ? "vitoria.png" : "gameover.png";
        try {
            if (Gdx.files.internal(nomeArquivo).exists()) imgFundo = new Texture(nomeArquivo);
            else if (Gdx.files.internal("gameover.png").exists()) imgFundo = new Texture("gameover.png");
        } catch (Exception e) { }

        // Fonte
        try {
            if (Gdx.files.internal("game_over.ttf").exists()) {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("game_over.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                parameter.size = 96;
                parameter.color = Color.WHITE;
                parameter.borderColor = Color.BLACK;
                parameter.borderWidth = 3;
                font = generator.generateFont(parameter);
                generator.dispose();
            } else {
                font = new BitmapFont();
                font.getData().setScale(2.0f);
            }
        } catch (Exception e) {
            font = new BitmapFont();
        }

        // --- LÓGICA DO BOTÃO ---
        String textoPrincipal;
        if (venceu) {
            textoPrincipal = "PROXIMO ANDAR (" + (andarAtual + 1) + ")";
        } else {
            textoPrincipal = "TENTAR DE NOVO";
        }

        layoutBotaoPrincipal = new GlyphLayout(font, textoPrincipal);
        layoutVoltar = new GlyphLayout(font, "VOLTAR AO INICIO");

        float yBotoes = 80;
        float gap = 150;
        float larguraTotal = layoutBotaoPrincipal.width + gap + layoutVoltar.width;
        float xInicial = (1280 - larguraTotal) / 2;

        boundsBotaoPrincipal = new Rectangle(xInicial, yBotoes, layoutBotaoPrincipal.width, layoutBotaoPrincipal.height);
        boundsVoltarMenu = new Rectangle(xInicial + layoutBotaoPrincipal.width + gap, yBotoes, layoutVoltar.width, layoutVoltar.height);
    }

    @Override
    public void render(float delta) {
        if (venceu) Gdx.gl.glClearColor(0, 0.2f, 0, 1);
        else Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (imgFundo != null) batch.draw(imgFundo, 0, 0, 1280, 720);

        // Texto de status (Sem "Vitória Gloriosa", apenas o status)
        font.setColor(venceu ? Color.GOLD : Color.RED);
        String msgResultado = venceu ? "ANDAR " + andarAtual + " CONCLUIDO" : "DERROTA...";
        GlyphLayout layoutResultado = new GlyphLayout(font, msgResultado);
        font.draw(batch, msgResultado, (1280 - layoutResultado.width) / 2, 600);

        // Botões
        String textoBotao1 = venceu ? "PROXIMO ANDAR (" + (andarAtual + 1) + ")" : "TENTAR DE NOVO";

        font.setColor(isMouseOver(boundsBotaoPrincipal) ? Color.RED : Color.WHITE);
        font.draw(batch, textoBotao1, boundsBotaoPrincipal.x, boundsBotaoPrincipal.y + layoutBotaoPrincipal.height);

        font.setColor(isMouseOver(boundsVoltarMenu) ? Color.RED : Color.WHITE);
        font.draw(batch, "VOLTAR AO INICIO", boundsVoltarMenu.x, boundsVoltarMenu.y + layoutVoltar.height);

        batch.end();

        // Input
        if (Gdx.input.justTouched()) {
            if (isMouseOver(boundsBotaoPrincipal)) {
                if (venceu) {
                    // --- AVANÇA DE FASE ---
                    // Passa o time atual e aumenta o andar
                    game.setScreen(new ArenaScreen(game, party, andarAtual + 1));
                } else {
                    // Reinicia seleção
                    game.setScreen(new SelecaoScreen(game));
                }
                dispose();
            } else if (isMouseOver(boundsVoltarMenu)) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        }
    }

    private boolean isMouseOver(Rectangle bounds) {
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPos);
        return bounds.contains(touchPos.x, touchPos.y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (imgFundo != null) imgFundo.dispose();
        if (font != null) font.dispose();
    }
}
