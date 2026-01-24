package com.rpgpoo.game;

import java.util.Random;

public class Batalha {
    public String mensagemAtual;
    private int andarAtual;
    private boolean turnoChoose;
    private Combatente heroi;
    private Combatente inimigo;

    public Batalha(int andarAtual, Combatente heroi, Combatente inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;
        this.andarAtual = andarAtual;
        mensagemAtual = "";
    }

    public String getMensagemAtual(){ return this.mensagemAtual; }

    public void iniciar() {
        turnoChoose = new Random().nextBoolean();
    }

    public void executarTurno() {
        if(terminou()){
            return;
        }
        else if (turnoChoose) {
            if(heroi.processaStatus()) {
                heroi.atacar(inimigo);
                this.mensagemAtual = heroi.getMensagem();
            }
        }
        else{
            if(heroi.processaStatus()) {
                inimigo.atacar(heroi);
                this.mensagemAtual = inimigo.getMensagem();
            }
        }
        proximoTurno();//Por enquanto não terá a opção pra escolher qual dos alvos gerados o combatente pode atacar
    }

    public boolean terminou() {
        if (!inimigo.checaVida()) {
            mensagemAtual = heroi.getNome() + " venceu!";
            andarAtual++;
            return true;
        } else if (!heroi.checaVida()) {
            mensagemAtual = heroi.getNome() + " perdeu!";
            return true;
        }
        return false;
    }

    public void proximoTurno() {
        turnoChoose = !turnoChoose;
        }

}
