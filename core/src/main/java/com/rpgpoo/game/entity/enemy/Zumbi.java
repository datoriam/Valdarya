package com.rpgpoo.game.entity.enemy;

import com.rpgpoo.game.entity.Combatente;

public class Zumbi extends Combatente {
    public Zumbi(String nome) {
        super(nome, 90, 12);
    }

    @Override
    protected void evoluirStats() {
        atualizaAtributos(2, 15);
    }
}
