package com.rpgpoo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 
public class Arena extends ApplicationAdapter {
    
    // parte do campo e da câmera
    private ShapeRenderer shape;
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
        if (time.equalsIgnoreCase("A")){
            time.add A(c);
        } else if (time.equalsIgnoreCase("B")) {
            time.add B(c);
        } else {
            System.out.println(" Time inválido! Use A ou B! ");
        }
    }
}
