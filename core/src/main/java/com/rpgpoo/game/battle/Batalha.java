package com.rpgpoo.game.battle;

import com.rpgpoo.game.entity.Combatente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Batalha {
    public String mensagemAtual;
    private int andarAtual;
    private boolean turnoChoose;
    private Combatente heroi;
    private Combatente inimigo;
    private StringBuilder logTurno; // Para acumular mensagens durante o turno
    private List<Combatente> timeA;
    private List<Combatente> timeB;

    private Random random; //gera aleatoriedade

    public Batalha () {
        this.timeA = new ArrayList<>();
        this.timeB = new ArrayList<>();
        this.random = new Random();
    }

    public Batalha(int andarAtual, Combatente heroi, Combatente inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;
        this.andarAtual = andarAtual;
        mensagemAtual = "";
        this.logTurno = new StringBuilder();
    }

    public String getMensagemAtual(){ return this.mensagemAtual; }

    public void iniciar() {
        turnoChoose = new Random().nextBoolean();
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

    public void executarTurno() {
        logTurno.setLength(0); // Limpa o log do turno anterior

        if(terminou()){
            return;
        }

        if (turnoChoose) {
            // TURNO DO HERÓI
            logTurno.append("TURNO DO HERÓI: ").append(heroi.getNome()).append("\n");

            if(heroi.processaStatus()) {
                // Limpa mensagens antigas do herói
                heroi.setMensagem("");

                logTurno.append(heroi.getNome()).append(" ataca ").append(inimigo.getNome()).append("!\n").append(" Heroi causou ").append(heroi.getDano());
                heroi.atacar(inimigo);

                // Adiciona mensagens DURANTE o ataque
                if (!heroi.getMensagem().isEmpty()) {
                    logTurno.append(heroi.getMensagem()).append("\n");
                }
            }

            // Verifica se inimigo foi derrotado DURANTE o ataque
            if (!inimigo.checaVida()) {
                logTurno.append("💀 ").append(inimigo.getNome()).append(" foi derrotado!\n");
            }
        }
        else {
            // TURNO DO INIMIGO
            logTurno.append("TURNO DO INIMIGO: ").append(inimigo.getNome()).append("\n");

            if(heroi.processaStatus()) {
                // Limpa mensagens antigas do inimigo
                inimigo.setMensagem("");

                logTurno.append(inimigo.getNome()).append(" ataca ").append(heroi.getNome()).append("!\n").append(" Inimigo causou ").append(inimigo.getDano());
                inimigo.atacar(heroi);

                // Adiciona mensagens DURANTE o ataque (incluindo bloqueio do Guardião)
                if (!inimigo.getMensagem().isEmpty()) {
                    logTurno.append(inimigo.getMensagem()).append("\n");
                }

                // Se o herói é Guardião, também mostra suas mensagens de bloqueio
                if (!heroi.getMensagem().isEmpty()) {
                    logTurno.append(heroi.getMensagem()).append("\n");
                }
            }

            // Verifica se herói foi derrotado DURANTE o ataque
            if (!heroi.checaVida()) {
                logTurno.append("💀 ").append(heroi.getNome()).append(" foi derrotado!\n");
            }
        }

        // Atualiza mensagem atual com TODO o log do turno
        mensagemAtual = logTurno.toString();
        proximoTurno();
    }

    public boolean terminou() {
        if (!inimigo.checaVida()) {
            // Mensagem de vitória
            mensagemAtual = heroi.getNome() + " VENCEU! Andar " + andarAtual + " completo!";
            // Chamando o método de xp
            // Nicolas: Chamada do método de XP que o Davi implementou no Combatente.java
            // Isso vai disparar automaticamente o subirNivel() e o seu evoluirStats() no Atirador.
            heroi.ganharXP(50); 
            
            return true;
        } else if (!heroi.checaVida()) {
            mensagemAtual = heroi.getNome() + " PERDEU! Fim da jornada...";
            return true;
        }
        return false;
    }

    // Método para obter status atual dos combatentes
    public String getStatusCombatentes() {
        return heroi.getNome() + ": " + heroi.getVidaAtual() + "/" + heroi.getVidaTotal() + " PV" +
               "\n" + inimigo.getNome() + ": " + inimigo.getVidaAtual() + "/" + inimigo.getVidaTotal() + " PV";
    }
}
