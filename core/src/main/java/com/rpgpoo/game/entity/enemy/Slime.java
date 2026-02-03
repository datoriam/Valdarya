package com.rpgpoo.game.entity.enemy;

import com.rpgpoo.game.entity.Combatente;

public class Slime extends Combatente {

    public Slime(String nome) {
        // Slime: Vida baixa (50), Dano médio (10)
        super(nome, 50, 10);
    }

    @Override
    public void atacar(Combatente alvo) {
        alvo.receberDano(this.getDano());

        // Efeito: O Slime gosmento tem 15% de chance de deixar o herói dormindo/lento
        if (Math.random() > 0.85) {
            alvo.setDormindo(true);
            this.setMensagem(this.getNome() + " lançou gosma paralisante em " + alvo.getNome() + "!");
        } else {
            this.setMensagem(this.getNome() + " deu uma investida viscosa em " + alvo.getNome());
        }
    }

    @Override
    protected void evoluirStats() {
        // Scaling suave para não quebrar o jogo no andar 25
        super.atualizaAtributos(2, 10);
    }
}
