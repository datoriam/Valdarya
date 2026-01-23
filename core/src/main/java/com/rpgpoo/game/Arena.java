package com.rpgpoo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import sun.awt.windows.ThemeReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import java.lang.String;

public class Arena extends ApplicationAdapter {

    // parte do campo e da câmera
    //private ShapeRenderer shape;
    private Combatente jogador;
    private Combatente npc;
    private Batalha batalha;
    private SpriteBatch field;
    private Texture imgHeroi;
    private Texture imgInimigo;
    private Texture imgFundo;
    private TextureRegion areaSlime;
    private Animation<TextureRegion> animacaoSlime;
    private float tempoAnimacao;
    private OrthographicCamera camera;

    private List<Combatente> timeA;
    private List<Combatente> timeB;

    private Random random; //gera aleatoriedade

    public Arena () {
        this.timeA = new ArrayList<>();
        this.timeB = new ArrayList<>();
        this.random = new Random();
    }

    public void adicionarCombatente(Combatente c, String time) {
        if (time.equalsIgnoreCase("A")){ //Corrigi o erro do timeA e time B na hora de gerar o case
            timeA.add (c);
        } else if (time.equalsIgnoreCase("B")) {
            timeB.add (c);
        } else {
            System.out.println(" Time inválido! Use A ou B! ");
        }
    }

    @Override
    public void create(){
        this.imgHeroi = new Texture("mago.png");
        this.jogador = new Arcanista("Maguin");
        this.imgInimigo = new Texture("slime.png");
        TextureRegion[][] tmp = TextureRegion.split(imgInimigo, imgInimigo.getWidth() / 4, imgInimigo.getHeight());
        animacaoSlime = new Animation<>(0.2f, tmp[0]);
        TextureRegion areaSlime = new TextureRegion(imgInimigo);
        areaSlime.flip(true, false);
        this.npc = new Slime();
        this.field = new SpriteBatch();
        this.imgFundo = new Texture("fundobatalha.png");
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        this.batalha = new Batalha(0,jogador,npc);
        this.batalha.iniciar();
    }

    @Override
    public void render(){
        tempoAnimacao += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        field.setProjectionMatrix(camera.combined);

        field.begin();
        field.draw(imgFundo, 0, 0, 1280, 720);
        field.draw(imgHeroi, 50, 160, 350, 350);
        field.draw(animacaoSlime.getKeyFrame(tempoAnimacao, true), 1000, 160, 152, 138);
        field.end();
    }
}
