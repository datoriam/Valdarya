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
    private final boolean zerouJogo; // Nova variável para saber se é o fim

    private final List<Combatente> party;
    private final int andarAtual;

    private SpriteBatch batch;
    private Texture imgFundo;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Rectangle boundsBotaoPrincipal; // Proximo Andar ou Tentar de Novo
    private Rectangle boundsVoltarMenu;     // Voltar ao Titulo

    private GlyphLayout layoutBotaoPrincipal;
    private GlyphLayout layoutVoltar;
    private String textoBotao1;

    public GameOverScreen(Valdarya game, boolean venceu, List<Combatente> party, int andarAtual) {
        this.game = game;
        this.venceu = venceu;
        this.party = party;
        this.andarAtual = andarAtual;

        // Verifica se é a vitória final (Andar 25)
        this.zerouJogo = venceu && andarAtual >= 25;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        batch = new SpriteBatch();

        // --- CARREGAMENTO DA IMAGEM ---
        String nomeArquivo;
        if (zerouJogo) {
            nomeArquivo = "imgespecial.png"; // Imagem final
        } else if (venceu) {
            nomeArquivo = "vitoria.png";
        } else {
            nomeArquivo = "gameover.png";
        }

        try {
            if (Gdx.files.internal(nomeArquivo).exists()) {
                imgFundo = new Texture(nomeArquivo);
            } else {
                // Fallback se não tiver a imagem especial, usa a padrão
                if (zerouJogo && Gdx.files.internal("vitoria.png").exists()) {
                    imgFundo = new Texture("vitoria.png");
                } else if (Gdx.files.internal("gameover.png").exists()) {
                    imgFundo = new Texture("gameover.png");
                }
            }
        } catch (Exception e) {
            // Se der erro, fica sem fundo (preto)
        }

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

        // --- LÓGICA DOS BOTÕES ---

        if (zerouJogo) {
            // Se zerou, só tem opção de voltar pro menu (não tem proximo andar)
            textoBotao1 = ""; // Vazio
        } else if (venceu) {
            textoBotao1 = "PROXIMO ANDAR (" + (andarAtual + 1) + ")";
        } else {
            textoBotao1 = "TENTAR DE NOVO";
        }

        layoutBotaoPrincipal = new GlyphLayout(font, textoBotao1);
        layoutVoltar = new GlyphLayout(font, "VOLTAR AO TITULO");

        float yBotoes = 80;

        if (zerouJogo) {
            // Se zerou, centraliza o botão de voltar
            float xCentral = (1280 - layoutVoltar.width) / 2;
            boundsVoltarMenu = new Rectangle(xCentral, yBotoes, layoutVoltar.width, layoutVoltar.height);
            // Botão principal fica fora da tela
            boundsBotaoPrincipal = new Rectangle(-1000, 0, 0, 0);
        } else {
            // Layout normal (dois botões)
            float gap = 150;
            float larguraTotal = layoutBotaoPrincipal.width + gap + layoutVoltar.width;
            float xInicial = (1280 - larguraTotal) / 2;

            boundsBotaoPrincipal = new Rectangle(xInicial, yBotoes, layoutBotaoPrincipal.width, layoutBotaoPrincipal.height);
            boundsVoltarMenu = new Rectangle(xInicial + layoutBotaoPrincipal.width + gap, yBotoes, layoutVoltar.width, layoutVoltar.height);
        }
    }

    @Override
    public void render(float delta) {
        // Fundo Verde escuro se venceu, Preto se perdeu, Dourado se zerou
        if (zerouJogo) Gdx.gl.glClearColor(0.2f, 0.2f, 0, 1);
        else if (venceu) Gdx.gl.glClearColor(0, 0.2f, 0, 1);
        else Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (imgFundo != null) batch.draw(imgFundo, 0, 0, 1280, 720);

        // --- TEXTOS ---

        // Se zerou e tem a imagem especial, talvez não queira desenhar texto por cima
        // Mas vou deixar o texto caso a imagem não tenha
        String msgResultado;
        if (zerouJogo) {
            font.setColor(Color.GOLD);
            msgResultado = "LENDARIO! TORRE CONQUISTADA!";
        } else if (venceu) {
            font.setColor(Color.GREEN);
            msgResultado = "ANDAR " + andarAtual + " CONCLUIDO";
        } else {
            font.setColor(Color.RED);
            msgResultado = "DERROTA...";
        }

        GlyphLayout layoutResultado = new GlyphLayout(font, msgResultado);
        // Desenha o texto de resultado um pouco mais alto
        font.draw(batch, msgResultado, (1280 - layoutResultado.width) / 2, 650);

        // --- DESENHA BOTÕES ---

        // Botão Principal (Próximo Andar / Tentar de Novo) - Só desenha se NÃO zerou
        if (!zerouJogo) {
            font.setColor(isMouseOver(boundsBotaoPrincipal) ? Color.YELLOW : Color.WHITE);
            font.draw(batch, textoBotao1, boundsBotaoPrincipal.x, boundsBotaoPrincipal.y + layoutBotaoPrincipal.height);
        }

        // Botão Voltar (Sempre aparece)
        font.setColor(isMouseOver(boundsVoltarMenu) ? Color.YELLOW : Color.WHITE);
        font.draw(batch, "VOLTAR AO TITULO", boundsVoltarMenu.x, boundsVoltarMenu.y + layoutVoltar.height);

        batch.end();

        // --- INPUT ---
        if (Gdx.input.justTouched()) {
            if (!zerouJogo && isMouseOver(boundsBotaoPrincipal)) {
                if (venceu) {
                    // Avança
                    game.setScreen(new ArenaScreen(game, party, andarAtual + 1));
                } else {
                    // Reinicia seleção
                    game.setScreen(new SelecaoScreen(game));
                }
                dispose();
            } else if (isMouseOver(boundsVoltarMenu)) {
                // Volta para o Menu Principal
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
