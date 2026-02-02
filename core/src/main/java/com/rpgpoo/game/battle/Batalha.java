package com.rpgpoo.game.battle;

import com.rpgpoo.game.entity.Combatente;
import com.rpgpoo.game.entity.enemy.Slime;
import com.rpgpoo.game.entity.enemy.Zumbi;

import java.util.ArrayList;
import java.util.List;

public class Batalha {
    public String mensagemAtual;
    private int andarAtual;
    private boolean turnoChoose;
    private Combatente heroi;
    private Combatente inimigo;

    private StringBuilder logTurno;

    private List<Combatente> timeA;
    private List<Combatente> timeB;

    public Batalha(int andarAtual, Combatente heroi, Combatente inimigo) {
        this.timeA = new ArrayList<>();
        this.timeB = new ArrayList<>();
        this.logTurno = new StringBuilder();

        this.heroi = heroi;
        this.inimigo = inimigo;
        this.andarAtual = andarAtual;
        this.mensagemAtual = "Batalha iniciada! Selecione seu heroi.";
        this.turnoChoose = true;
    }

    public List<Combatente> getTimeA() { return timeA; }
    public List<Combatente> getTimeB() { return timeB; }

    public boolean isTurnoHeroi() { return this.turnoChoose; }

    // -Nicolas: Verifica se geral do time A ja era
    private boolean timeATodoMorto() {
        if (timeA.isEmpty()) return true;
        for (Combatente c : timeA) {
            if (c.checaVida()) return false;
        }
        return true;
    }

    // -Nicolas: Varre o time pra botar o proximo vivo no combate
    private void trocarFocoHeroi() {
        for (Combatente c : timeA) {
            if (c.checaVida()) {
                this.heroi = c;
                return;
            }
        }
    }

    public void setHeroiAtual(Combatente novoHeroi) {
        if (!turnoChoose) return;
        if (novoHeroi != null && novoHeroi.checaVida()) {
            this.heroi = novoHeroi;
            this.mensagemAtual = "Selecionado: " + novoHeroi.getNome();
        }
    }

    public void selecionarHeroiPeloIndice(int index) {
        if (!turnoChoose) return;
        if (index >= 0 && index < timeA.size()) {
            Combatente c = timeA.get(index);
            if (c.checaVida()) {
                this.heroi = c;
                this.mensagemAtual = "Selecionado: " + c.getNome();
            } else {
                this.mensagemAtual = c.getNome() + " desmaiado!";
            }
        }
    }

    public Combatente getHeroiAtual() { return this.heroi; }

    public void setInimigoAtual(Combatente novoAlvo) {
        if (!turnoChoose) return;
        if (novoAlvo != null && novoAlvo.checaVida()) {
            this.inimigo = novoAlvo;
        }
    }

    public void selecionarInimigoPeloIndice(int index) {
        if (!turnoChoose) return;
        if (index >= 0 && index < timeB.size()) {
            Combatente c = timeB.get(index);
            if (c.checaVida()) setInimigoAtual(c);
        }
    }

    public Combatente getInimigoAtual() { return this.inimigo; }

    public void gerarTimeInimigo(int andar) {
        this.timeB.clear();
        int quantidade = 1;
        if (andar >= 3) quantidade = 2;
        if (andar >= 6) quantidade = 3;

        for (int i = 0; i < quantidade; i++) {
            Combatente mob;
            boolean ehBoss = false;
            if (andar >= 3 && i % 2 == 0) {
                String nome = "Zumbi " + (i+1);
                if (andar > 5) { nome = "Zumbi Raivoso " + (i+1); ehBoss = true; }
                mob = new Zumbi(nome);
            } else {
                String nome = "Slime " + (i+1);
                if (andar > 5) { nome = "Rei Slime " + (i+1); ehBoss = true; }
                mob = new Slime(nome);
            }

            for(int j = 1; j < andar; j++) mob.subirNivel(false);

            if (ehBoss) mob.atualizaAtributos(5, 50);
            this.timeB.add(mob);
        }
        if (!timeB.isEmpty()) this.inimigo = timeB.get(0);
        this.mensagemAtual = "Andar " + andar + " - Inimigos a vista!";
    }

    public String getMensagemAtual(){ return this.mensagemAtual; }

    public void adicionarCombatente(Combatente c, String time) {
        if (time.equalsIgnoreCase("A")) timeA.add(c);
        else if (time.equalsIgnoreCase("B")) timeB.add(c);
    }

    private void trocarFocoInimigo() {
        for (Combatente c : timeB) {
            if (c.checaVida()) {
                this.inimigo = c;
                return;
            }
        }
    }

    public void executarTurno() {
        logTurno.setLength(0);

        if(terminou()) return;

        if (turnoChoose) {
            // -Nicolas: Garante que o alvo ta vivo antes de bater
            if(!inimigo.checaVida()) trocarFocoInimigo();

            if(heroi.processaStatus()) {
                heroi.setMensagem("");
                heroi.atacar(inimigo);
                if (!heroi.getMensagem().isEmpty()) logTurno.append(heroi.getMensagem());
            }

            if (!inimigo.checaVida()) {
                logTurno.append("\n").append(inimigo.getNome()).append(" morreu! (+XP)");
                heroi.ganharXP(50 * andarAtual);
                trocarFocoInimigo();
            }
        }
        else {
            // -Nicolas: Turno da horda. Se alguem morrer no meio, ja puxa o proximo
            boolean alguemAtacou = false;
            for (Combatente mob : timeB) {
                // Se o heroi atual caiu pro inimigo anterior, busca o proximo vivo antes do proximo ataque
                if (!heroi.checaVida()) trocarFocoHeroi();

                if (mob.checaVida() && heroi.checaVida()) {
                    if(mob.processaStatus()) {
                        alguemAtacou = true;
                        mob.setMensagem("");
                        mob.atacar(heroi);
                        if (!mob.getMensagem().isEmpty()) logTurno.append(mob.getMensagem()).append("\n");
                    }
                }
            }
            if (!alguemAtacou) logTurno.append("Inimigos hesitaram...");
            if (!heroi.checaVida()) logTurno.append("\n").append(heroi.getNome()).append(" caiu!");
        }

        this.turnoChoose = !this.turnoChoose;
        this.mensagemAtual = logTurno.toString();
    }

    public boolean terminou() {
        boolean inimigosVivos = false;
        for(Combatente c : timeB) {
            if(c.checaVida()) inimigosVivos = true;
        }

        if (!inimigosVivos) {
            if (andarAtual >= 25) mensagemAtual = "PARABENS! TORRE CONQUISTADA!";
            else mensagemAtual = "VITORIA! (Tecle Espaco)";
            return true;
        }

        // -Nicolas: So acaba se geral do time A tiver no chao
        if (!heroi.checaVida()) {
            if (timeATodoMorto()) {
                mensagemAtual = "DERROTA TOTAL.";
                return true;
            }
            // Se ainda tem gente, a troca agora eh automatica no executarTurno ou manual por aqui
            trocarFocoHeroi();
            mensagemAtual = heroi.getNome() + " assumiu o combate!";
            return false;
        }
        return false;
    }
}