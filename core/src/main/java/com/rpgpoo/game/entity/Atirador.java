package com.rpgpoo.game.entity;

import java.util.Random;

public class Atirador extends Combatente {
    private Random dado;

    public Atirador(String nome) {
        // Vida decente e Dano alto
        super(nome, 100, 20);
        this.dado = new Random();
    }

    @Override
    protected void evoluirStats() {
        // Ganha vida e dano consistentes
        atualizaAtributos(5, 20);
        recuperarVidaTotal();
    }

    @Override
    public void atacar(Combatente alvo) {
        setMensagem("");

        int danoFinal = getDano();
        boolean critico = false;

        // 30% de chance de CRÍTICO
        if (dado.nextInt(100) < 30) {
            danoFinal *= 2; // Dobra o dano
            critico = true;
            setMensagem("HEADSHOT! " + getNome() + " acertou um ponto vital!");
        } else {
            setMensagem(getNome() + " disparou uma flecha.");
        }

        // Aplica o dano
        alvo.receberDano(danoFinal);

        // --- ROUBO DE VIDA ---
        // O Atirador recupera 30% do dano causado como vida.
        // Se der critico de 100, cura 30 de vida.
        int rouboDeVida = (int) (danoFinal * 0.30);

        // Garante pelo menos 1 de cura
        if (rouboDeVida < 1) rouboDeVida = 1;

        // Lógica de cura (não ultrapassa o máximo)
        if (getVidaAtual() + rouboDeVida >= getVidaTotal()) {
            recuperarVidaTotal();
        } else {
            receberDano(-rouboDeVida); // Dano negativo cura
        }

        // Adiciona mensagem de cura
        setMensagem("(Roubou " + rouboDeVida + " de vida)");
    }
}
