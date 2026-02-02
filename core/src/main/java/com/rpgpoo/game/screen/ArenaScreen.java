package com.rpgpoo.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.rpgpoo.game.battle.Batalha;
import com.rpgpoo.game.entity.Combatente;
import com.rpgpoo.game.entity.Guardiao;
import com.rpgpoo.game.entity.Arcanista;
import com.rpgpoo.game.entity.Atirador;
import com.rpgpoo.game.entity.enemy.Slime;
import com.rpgpoo.game.main.Valdarya;

import java.util.List;

public class ArenaScreen extends ScreenAdapter {

    private final Valdarya game;
    private final List<Combatente> party;
    private final int andarAtual;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font; // fonte do log
    private BitmapFont fontUi; // fonte da ui

    private Batalha batalha;

    // texturas
    private Texture imgFundo;
    private Texture imgGuardiao;
    private Texture imgArcanista;
    private Texture imgAtirador;

    // inimigos
    private Texture imgInimigoSlime;
    private Texture imgInimigoZumbi;

    private Texture imgSeta;
    private Texture imgSetaAlvo;

    private Animation<TextureRegion> animacaoSlime;
    private Animation<TextureRegion> animacaoZumbi;
    private float tempoAnimacao;

    private float tempoPisca;

    // posicoes fixas
    private int[] heroPositionsX = {150, 300, 450};
    private int heroPositionY = 250;
    private int[] enemyPositionsX = {900, 1000, 1100};
    private int enemyPositionY = 250;

    // hitboxes pra clicar
    private Rectangle[] heroRects;
    private Rectangle[] enemyRects;

    public ArenaScreen(Valdarya game, List<Combatente> party, int andarAtual) {
        this.game = game;
        this.party = party;
        this.andarAtual = andarAtual;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // config das fontes (diminui pra caber mais texto)
        try {
            if (Gdx.files.internal("game_over.ttf").exists()) {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("game_over.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

                parameter.size = 34; // tamanho medio pro log
                parameter.color = Color.WHITE;
                parameter.borderColor = Color.BLACK;
                parameter.borderWidth = 2;
                font = generator.generateFont(parameter);

                parameter.size = 32; // tamanho pra ui
                parameter.borderWidth = 2;
                fontUi = generator.generateFont(parameter);

                generator.dispose();
            } else {
                // fallback sem a fonte ttf
                font = new BitmapFont();
                font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
                font.getData().setScale(1.8f);

                fontUi = new BitmapFont();
                fontUi.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
                fontUi.getData().setScale(1.8f);
            }
        } catch (Exception e) {
            font = new BitmapFont();
            fontUi = new BitmapFont();
            font.getData().setScale(1.5f);
        }

        // carrega assets
        imgFundo = carregarTexturaSafe("fundobatalha.png", Color.DARK_GRAY);
        imgGuardiao = carregarTexturaSafe("avatar_guardiao.png", Color.BLUE);
        imgArcanista = carregarTexturaSafe("avatar_arcanista.png", Color.PURPLE);
        imgAtirador = carregarTexturaSafe("avatar_atirador.png", Color.GREEN);

        imgInimigoZumbi = carregarTexturaSafe("zumbi.png", Color.GRAY);

        if (Gdx.files.internal("seta.png").exists()) {
            imgSeta = new Texture("seta.png");
            imgSetaAlvo = new Texture("seta.png");
        } else {
            imgSeta = criarSetaTriangular(Color.YELLOW);
            imgSetaAlvo = criarSetaTriangular(Color.RED);
        }

        // carrega animacoes
        try {
            if (Gdx.files.internal("slime.png").exists()) {
                imgInimigoSlime = new Texture("slime.png");
                TextureRegion[][] tmp = TextureRegion.split(imgInimigoSlime, imgInimigoSlime.getWidth() / 4, imgInimigoSlime.getHeight());
                animacaoSlime = new Animation<>(0.2f, tmp[0]);
            } else {
                imgInimigoSlime = criarPlaceholder(Color.RED);
                TextureRegion[][] tmp = TextureRegion.split(imgInimigoSlime, 32, 32);
                animacaoSlime = new Animation<>(0.2f, tmp[0]);
            }
        } catch (Exception e) {
            imgInimigoSlime = criarPlaceholder(Color.RED);
            TextureRegion reg = new TextureRegion(imgInimigoSlime);
            animacaoSlime = new Animation<>(1f, reg);
        }

        try {
            if (Gdx.files.internal("zumbi.png").exists()) {
                imgInimigoZumbi = new Texture("zumbi.png");
                TextureRegion[][] tmp = TextureRegion.split(imgInimigoZumbi,
                    imgInimigoZumbi.getWidth() / 5,
                    imgInimigoZumbi.getHeight() / 4);
                animacaoZumbi = new Animation<>(0.2f, tmp[0]);
            } else {
                imgInimigoZumbi = criarPlaceholder(Color.GRAY);
                animacaoZumbi = new Animation<>(1f, new TextureRegion(imgInimigoZumbi));
            }
        } catch (Exception e) {
            imgInimigoZumbi = criarPlaceholder(Color.GRAY);
            animacaoZumbi = new Animation<>(1f, new TextureRegion(imgInimigoZumbi));
        }

        // cria hitboxes
        heroRects = new Rectangle[3];
        for(int i=0; i<3; i++) {
            heroRects[i] = new Rectangle(heroPositionsX[i], heroPositionY, 150, 150);
        }

        enemyRects = new Rectangle[3];
        for(int i=0; i<3; i++) {
            int x = (i < enemyPositionsX.length) ? enemyPositionsX[i] : 900 + (i*50);
            enemyRects[i] = new Rectangle(x, enemyPositionY, 180, 180);
        }

        // inicializa batalha
        Combatente heroiInicial = party.isEmpty() ? new Guardiao("Luluzin") : party.get(0);
        Combatente inimigoInicial = new Slime("Slime Viscoso");

        this.batalha = new Batalha(andarAtual, heroiInicial, inimigoInicial);
        this.batalha.gerarTimeInimigo(andarAtual);

        for (Combatente c : party) {
            this.batalha.adicionarCombatente(c, "A");
        }
    }

    private Texture carregarTexturaSafe(String nomeArquivo, Color corPlaceholder) {
        try {
            if (Gdx.files.internal(nomeArquivo).exists()) return new Texture(nomeArquivo);
            else return criarPlaceholder(corPlaceholder);
        } catch (Exception e) { return criarPlaceholder(corPlaceholder); }
    }

    private Texture criarPlaceholder(Color cor) {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(cor);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    private Texture criarSetaTriangular(Color cor) {
        int size = 32;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(cor);
        pixmap.fillTriangle(0, 0, size-1, 0, size/2, size-1);
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    @Override
    public void render(float delta) {
        // --- INPUT DE MOUSE (so funciona no turno do heroi) ---
        if (Gdx.input.justTouched()) {
            if (batalha.isTurnoHeroi()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);

                // checa clique nos herois
                for (int i = 0; i < party.size(); i++) {
                    if (heroRects[i].contains(touchPos.x, touchPos.y)) {
                        batalha.setHeroiAtual(party.get(i));
                    }
                }

                // checa clique nos inimigos
                List<Combatente> inimigos = batalha.getTimeB();
                for (int i = 0; i < inimigos.size(); i++) {
                    if (i < enemyRects.length && inimigos.get(i).checaVida()) {
                        if (enemyRects[i].contains(touchPos.x, touchPos.y)) {
                            batalha.setInimigoAtual(inimigos.get(i));
                        }
                    }
                }
            }
        }

        // atalhos de teclado (validacao de turno feita na batalha)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) batalha.selecionarHeroiPeloIndice(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) batalha.selecionarHeroiPeloIndice(1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) batalha.selecionarHeroiPeloIndice(2);

        // passar turno ou acao
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            batalha.executarTurno();
            if (batalha.terminou()) {
                if (batalha.getMensagemAtual().contains("DERROTA")) {
                    game.setScreen(new GameOverScreen(game, false, party, andarAtual));
                }
                else if (batalha.getMensagemAtual().contains("CONQUISTADA")) { // mudou texto de zeramento
                    game.setScreen(new GameOverScreen(game, true, party, andarAtual));
                }
                else if (batalha.getMensagemAtual().contains("VITORIA")) {
                    game.setScreen(new GameOverScreen(game, true, party, andarAtual));
                }
            }
        }

        tempoAnimacao += Gdx.graphics.getDeltaTime();
        tempoPisca += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        // 1. desenha o chao e bonecos
        batch.begin();
        batch.draw(imgFundo, 0, 0, 1280, 720);

        // herois
        for (int i = 0; i < party.size(); i++) {
            Combatente c = party.get(i);
            if (c.checaVida()) {
                Texture tex = getTexturaPorClasse(c);
                float x = heroPositionsX[Math.min(i, 2)];
                float y = heroPositionY;
                batch.draw(tex, x, y, 150, 150);

                // seta amarela animada
                if (c == batalha.getHeroiAtual()) {
                    float ySeta = y + 160 + (float)Math.sin(tempoPisca * 5) * 5;
                    batch.draw(imgSeta, x + 50, ySeta, 50, 50);
                }
            }
        }

        // inimigos
        List<Combatente> inimigos = batalha.getTimeB();
        for (int i = 0; i < inimigos.size(); i++) {
            Combatente mob = inimigos.get(i);
            if (mob.checaVida()) {
                float x = (i < enemyPositionsX.length) ? enemyPositionsX[i] : 900 + (i*50);

                // desenha sprite certo (zumbi ou slime)
                if (mob.getNome().toLowerCase().contains("zumbi")) {
                    if (animacaoZumbi != null) {
                        TextureRegion frame = animacaoZumbi.getKeyFrame(tempoAnimacao, true);
                        batch.draw(frame, x, enemyPositionY, 180, 180);
                    }
                } else {
                    if (animacaoSlime != null) {
                        TextureRegion frame = animacaoSlime.getKeyFrame(tempoAnimacao, true);
                        batch.draw(frame, x, enemyPositionY, 200, 180);
                    }
                }

                // seta vermelha no alvo animada
                if (mob == batalha.getInimigoAtual()) {
                    float ySeta = enemyPositionY + 180 + (float)Math.sin(tempoPisca * 5) * 5;
                    batch.draw(imgSetaAlvo, x + 70, ySeta, 40, 40);
                }
            }
        }
        batch.end();

        // 2. desenha barras de vida
        shape.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < party.size(); i++) {
            Combatente c = party.get(i);
            if (c.checaVida()) {
                float x = heroPositionsX[Math.min(i, 2)];
                float y = heroPositionY + 160;
                drawHealthBar(x, y, c);
            }
        }

        for (int i = 0; i < inimigos.size(); i++) {
            Combatente mob = inimigos.get(i);
            if (mob.checaVida()) {
                float x = (i < enemyPositionsX.length) ? enemyPositionsX[i] : 900 + (i*50);
                float y = enemyPositionY + 180;
                drawHealthBar(x + 20, y, mob);
            }
        }

        // caixa de texto preta com borda branca
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor(0, 0, 0, 0.8f);
        shape.rect(40, 20, 1200, 180);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.WHITE);
        shape.rect(40, 20, 1200, 180);
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 3. textos finais
        batch.begin();

        // nomes em cima das barras
        for (int i = 0; i < party.size(); i++) {
            Combatente c = party.get(i);
            if(c.checaVida()) {
                fontUi.setColor(Color.WHITE);
                fontUi.draw(batch, c.getNome(), heroPositionsX[Math.min(i, 2)], heroPositionY + 205);
            }
        }
        for (int i = 0; i < inimigos.size(); i++) {
            Combatente m = inimigos.get(i);
            if(m.checaVida()) {
                float x = (i < enemyPositionsX.length) ? enemyPositionsX[i] : 900 + (i*50);
                fontUi.setColor(Color.RED);
                fontUi.draw(batch, m.getNome(), x + 20, enemyPositionY + 225);
            }
        }

        // log de combate
        font.setColor(Color.WHITE);
        if (batalha.getMensagemAtual() != null) {
            font.draw(batch, batalha.getMensagemAtual(), 60, 190, 1160, Align.topLeft, true);
        }

        font.setColor(Color.YELLOW);
        font.draw(batch, "ANDAR: " + andarAtual, 1100, 700);

        // dicas de turno
        fontUi.setColor(Color.LIGHT_GRAY);
        if (batalha.isTurnoHeroi()) {
            fontUi.draw(batch, "SUA VEZ: Selecione Heroi/Alvo e aperte ESPACO!", 400, 240);
        } else {
            fontUi.draw(batch, "VEZ DO INIMIGO... [ESPACO] para avancar", 400, 240);
        }

        if (tempoPisca % 1.0f > 0.5f) {
            fontUi.setColor(Color.CYAN);
            fontUi.draw(batch, "[ESPACO] Continuar >>", 950, 60);
        }

        batch.end();
    }

    private void drawHealthBar(float x, float y, Combatente c) {
        float largura = 100;
        float altura = 10;
        float porcentagem = (float)c.getVidaAtual() / (float)c.getVidaTotal();

        shape.setColor(Color.RED);
        shape.rect(x, y, largura, altura);

        shape.setColor(Color.GREEN);
        shape.rect(x, y, largura * porcentagem, altura);
    }

    private Texture getTexturaPorClasse(Combatente c) {
        if (c instanceof Guardiao) return imgGuardiao;
        if (c instanceof Arcanista) return imgArcanista;
        if (c instanceof Atirador) return imgAtirador;
        return imgGuardiao;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fontUi.dispose();
        shape.dispose();
        if (imgGuardiao != null) imgGuardiao.dispose();
        if (imgArcanista != null) imgArcanista.dispose();
        if (imgAtirador != null) imgAtirador.dispose();
        if (imgFundo != null) imgFundo.dispose();
        if (imgInimigoSlime != null) imgInimigoSlime.dispose();
        if (imgInimigoZumbi != null) imgInimigoZumbi.dispose();
        if (imgSeta != null) imgSeta.dispose();
        if (imgSetaAlvo != null) imgSetaAlvo.dispose();
    }
}
