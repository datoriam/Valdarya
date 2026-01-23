package com.rpgpoo.game;

import java.util.Random;

public class Batalha {
    private int andarAtual;
    private boolean turnoChoose;
    private Combatente heroi;
    private Combatente inimigo;

    public Batalha(int andarAtual, Combatente heroi, Combatente inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;
        this.andarAtual = andarAtual;
    }

    public void iniciar() {
        turnoChoose = new Random().nextBoolean();
        do {executarTurno();} while (!terminou());
    }

    public void executarTurno() {
        if (turnoChoose){
            System.out.println("É a vez de " + heroi.getNome() + "!");
            heroi.atacar(inimigo);
        }
        else{
            System.out.println("É a vez de " + inimigo.getNome() + "!");
            inimigo.atacar(heroi);
        }
        proximoTurno();//Por enquanto não terá a opção pra escolher qual dos alvos gerados o combatente pode atacar
    }

    public boolean terminou() {
        if(inimigo.getVidaAtual() <= 0){
            System.out.println(heroi.getNome() + " venceu!");
            return true;
        }
        else if(heroi.getVidaAtual() <= 0){
            System.out.println(heroi.getNome() + " perdeu!");
            return true;
        } else {
            return false;
        }
    }

    public void proximoTurno() {
        turnoChoose = !turnoChoose;
        }

    public void mostrarEstado() {
        // precisa saber atributos dos combatentes (vida, mana, etc.)
    }
}

