package com.rpgpoo.game.battle;

import com.rpgpoo.game.entity.Combatente;
import com.rpgpoo.game.entity.enemy.Slime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Batalha {
    /** -Nicolas
 * Método auxiliar para controle de Game Over.
 * Verifica se todos os heróis do time A estão mortos.
 * 
 * Regra:
 * - Se existir pelo menos um herói vivo, o jogo continua
 * - Game **/
    private boolean timeATodoMorto() {
    for (Combatente c : timeA) {
        if (c.checaVida()) { // ainda está vivo
            return false;
        }
    }
    return true;
}
    public String mensagemAtual;
    private int andarAtual;
    private boolean turnoChoose; // true = Vez do Heroi, false = Vez do Inimigo
    private Combatente heroi;
    private Combatente inimigo;

    // Mantive o log global apenas para persistência, mas usaremos o local no turno conforme sua alteração
    private StringBuilder logTurno;

    private List<Combatente> timeA;
    private List<Combatente> timeB;

    private Random random;

    public Batalha() {
        this.timeA = new ArrayList<>();
        this.timeB = new ArrayList<>();
        this.random = new Random();
        this.logTurno = new StringBuilder();
    }

    public Batalha(int andarAtual, Combatente heroi, Combatente inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;
        this.andarAtual = andarAtual;
        this.mensagemAtual = "";
        this.logTurno = new StringBuilder();
    }

    // TODO: Implementar lógica de troca (Deixei vazio pra você integrar com a UI, Nicolas)
    public void trocarHeroi(Combatente novoHeroi) {
        // Implementação pendente...
    }

    public void gerarTimeInimigo(int andarAtual) {
        // Limpa inimigos anteriores
        this.timeB.clear();

        // Lógica de Escalonamento (Sua implementação)
        // TODO: Testar se não fica muito difícil no andar 10+
        int quantidadeInimigos = 1 + (andarAtual / 5);

        for (int i = 0; i < quantidadeInimigos; i++) {
            Slime inimigo = new Slime("Slime Viscoso");
            this.timeB.add(inimigo);
        }

        this.mensagemAtual = "Andar " + andarAtual + ": " + quantidadeInimigos + " inimigos apareceram!";
    }

    public String getMensagemAtual(){ return this.mensagemAtual; }

    public void iniciar() {
        turnoChoose = new Random().nextBoolean();
    }

    public void adicionarCombatente(Combatente c, String time) {
        if (time.equalsIgnoreCase("A")){
            timeA.add(c);
        } else if (time.equalsIgnoreCase("B")) {
            timeB.add(c);
        }
    }

    // Método auxiliar para alternar turnos
    private void proximoTurno() {
        this.turnoChoose = !this.turnoChoose;
    }

    public void executarTurno() {
        logTurno.setLength(0); // Limpa o log global

        // TODO: Validar se esse StringBuilder local está capturando todos os eventos de status
        StringBuilder log = new StringBuilder();
        log.append("Início da Rodada \n");

        if(terminou()){
            return;
        }

        if (turnoChoose) {
            // --- TURNO DO HERÓI ---
            log.append("TURNO DO HERÓI: ").append(heroi.getNome()).append("\n");

            if(heroi.processaStatus()) {
                heroi.setMensagem(""); // Limpa buffer do combatente

                heroi.atacar(inimigo);

                log.append(heroi.getNome()).append(" ataca ").append(inimigo.getNome()).append("!\n");

                // Concatena mensagens de efeito (crítico, erro, etc)
                if (!heroi.getMensagem().isEmpty()) {
                    log.append(heroi.getMensagem()).append("\n");
                }
            }

            if (!inimigo.checaVida()) {
                // Removido o emoji aqui
                log.append(inimigo.getNome()).append(" foi derrotado!\n");
            }
        }
        else {
            // --- TURNO DO INIMIGO ---
            log.append("TURNO DO INIMIGO: ").append(inimigo.getNome()).append("\n");

            if(inimigo.processaStatus()) {
                inimigo.setMensagem("");

                inimigo.atacar(heroi);

                log.append(inimigo.getNome()).append(" ataca ").append(heroi.getNome()).append("!\n");

                if (!inimigo.getMensagem().isEmpty()) {
                    log.append(inimigo.getMensagem()).append("\n");
                }
                // Se o herói defendeu (Guardião), pega a msg dele
                if (!heroi.getMensagem().isEmpty()) {
                    log.append(heroi.getMensagem()).append("\n");
                }
            }

            if (!heroi.checaVida()) {
                // Removido o emoji aqui também
                log.append(heroi.getNome()).append(" foi derrotado!\n");
            }
        }

        // TODO: Verifiquei que você adicionou essa chamada. Certifique-se que não pula turno duplo.
        proximoTurno();

        // Atualiza a mensagem da tela com o log local (Sua alteração mantida)
        this.mensagemAtual = log.toString();
    }

   public boolean terminou() {

    // Vitória continua igual (por enquanto 1 inimigo)
    if (!inimigo.checaVida()) {
        mensagemAtual = "VITÓRIA! " + inimigo.getNome() + " caiu.";
        return true;
    }

    // Se o herói atual morreu
    if (!heroi.checaVida()) {

        // Se TODO o time morreu → Game Over
        if (timeATodoMorto()) {
            mensagemAtual = "DERROTA... Todos os heróis foram derrotados.";
            return true;
        }

        // Ainda há heróis vivos → troca obrigatória
        mensagemAtual = heroi.getNome() + " caiu! Escolha outro herói para continuar.";
        return false; // IMPORTANTE: não termina o jogo
    }

    return false;
}
