package com.rpgpoo.game.entity.enemy;

import com.rpgpoo.game.entity.Combatente;

public class Slime extends Combatente {
    public Slime(String nome) {

        super(nome, 35, 5);
    }

    @Override
    protected void evoluirStats() {

        atualizaAtributos(1, 4);
        recuperarVidaTotal();
    }
}
