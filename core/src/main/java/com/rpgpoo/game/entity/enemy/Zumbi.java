package com.rpgpoo.game.entity.enemy;

import com.rpgpoo.game.entity.Combatente;

public class Zumbi extends Combatente {

    public Zumbi(String nome) {
        // Zumbi: Vida alta (90), Dano base (12)
        super(nome, 90, 12);
    }

    @Override
    public void atacar(Combatente alvo) {
        // Mecânica de Fome: O Zumbi tem chance de dar um "Crítico de Mordida"
        int danoFinal = this.getDano();
        if (Math.random() > 0.80) {
            danoFinal *= 1.4;
            this.setMensagem(this.getNome() + " deu uma MORDIDA CRÍTICA em " + alvo.getNome() + "!");
        } else {
            this.setMensagem(this.getNome() + " golpeou " + alvo.getNome() + " com força bruta!");
        }

        alvo.receberDano(danoFinal);
    }

    @Override
    protected void evoluirStats() {
        // Zumbi foca mais em HP para servir de "esponja de dano"
        super.atualizaAtributos(3, 18);
    }
}
