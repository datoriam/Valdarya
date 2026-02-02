package com.rpgpoo.game.entity.enemy;

import com.rpgpoo.game.entity.Combatente;

public class Slime extends Combatente {
    public Slime(String nome) {
        super(nome, 40, 9);
    }

    @Override
    protected void evoluirStats() {
        atualizaAtributos(1, 6);
    }
}
