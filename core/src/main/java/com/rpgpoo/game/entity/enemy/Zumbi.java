package com.rpgpoo.game.entity.enemy;

import com.rpgpoo.game.entity.Combatente;

public class Zumbi extends Combatente {
    public Zumbi(String nome) {

        super(nome, 70, 8);
    }

    @Override
    protected void evoluirStats() {

        atualizaAtributos(1, 8);
        recuperarVidaTotal();
    }
}
