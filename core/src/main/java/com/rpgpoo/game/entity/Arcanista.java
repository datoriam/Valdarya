package com.rpgpoo.game.entity;

public class Arcanista extends Combatente {
    public Arcanista(String nome) {
        // Vida menor, mas o maior dano do jogo
        super(nome, 80, 25);
    }

    @Override
    protected void evoluirStats() {
        // Foca totalmente em explodir os inimigos
        atualizaAtributos(8, 15);
        recuperarVidaTotal();
    }

    @Override
    public void atacar(Combatente alvo) {
        setMensagem("");
        setMensagem(getNome() + " drena a energia do inimigo!");

        // Dano Mágico: 20% mais forte que o base
        int danoMagico = (int) (getDano() * 1.2);

        alvo.receberDano(danoMagico);

        // --- DRENO MÁGICO ---
        // Recupera 25% do dano mágico causado
        int curaMagica = (int) (danoMagico * 0.25);

        if (getVidaAtual() + curaMagica >= getVidaTotal()) {
            recuperarVidaTotal();
        } else {
            receberDano(-curaMagica); // Dano negativo cura
        }

        setMensagem("(Absorveu " + curaMagica + " HP)");
    }
}
