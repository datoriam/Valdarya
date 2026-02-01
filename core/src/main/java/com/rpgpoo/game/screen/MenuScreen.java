package com.rpgpoo.game.screen;

public class MenuScreen {

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.rpgpoo.game.main.Valdarya;

public class MenuScreen extends ScreenAdapter {

    private final Valdarya game;
    private OrthographicCamera camera;
    private Texture imgTitleScreen;
    private Rectangle rectBotaoNovoJogo;

    public MenuScreen(Valdarya game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720); 

        // Tenta carregar a imagem. Se der erro, avisa no console.
        try {
            if (Gdx.files.internal("titlescreen.png").exists()) {
                imgTitleScreen = new Texture("titlescreen.png");
            } else {
                System.out.println("ERRO: Coloque o arquivo 'titlescreen.png' na pasta assets!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- CONFIGURAÇÃO DO BOTÃO ---
        // Coordenadas passadas: X=736, Y=250
        // Defini uma largura de 300 e altura de 80 pra ficar fácil de clicar
        this.rectBotaoNovoJogo = new Rectangle(736, 250, 300, 80);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // Desenha o fundo em tela cheia (1280x720)
        if (imgTitleScreen != null) {
            game.batch.draw(imgTitleScreen, 0, 0, 1280, 720);
        } else {
            // Texto de aviso caso a imagem não exista
            game.font.draw(game.batch, "FALTA A IMAGEM titlescreen.png NA PASTA ASSETS", 300, 400);
        }

         game.font.draw(game.batch, "[ AREA DO CLIQUE ]", rectBotaoNovoJogo.x, rectBotaoNovoJogo.y + 50);

        game.batch.end();

        // --- LÓGICA DE CLIQUE ---
        if (Gdx.input.justTouched()) {
            // Pega onde o mouse clicou e converte para as coordenadas do jogo
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Se o clique foi dentro do retângulo que definimos
            if (rectBotaoNovoJogo.contains(touchPos.x, touchPos.y)) {
                System.out.println("Iniciando novo jogo...");
                game.setScreen(new SelecaoScreen(game)); // Troca para a seleção
                dispose(); // Limpa a memória do menu
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1280, 720); // Mantém a proporção se a janela mudar
    }

    @Override
    public void dispose() {
        if (imgTitleScreen != null) imgTitleScreen.dispose();
    }
}