package com.rpgpoo.game;

public class Slime extends Combatente{

    public Slime() {
        super("Slime", 50, 2);
    }

    @Override
    public void atacar(Combatente alvo){
        alvo.receberDano(10);
    }

    protected void evoluirStats() {

    }
}
