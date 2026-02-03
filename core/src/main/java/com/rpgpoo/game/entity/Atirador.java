package com.rpgpoo.game.entity;

import com.rpgpoo.game.entity.Combatente;

public class Atirador extends Combatente {

    public Atirador(String nome) {
        super(nome, 85, 18);
    }

    private int calcularDanoCritico(int danoBase) {
        // Simulação de dado de 20 lados (D20)
        int dado = (int) (Math.random() * 20) + 1;

        // Se tirar 18, 19 ou 20, causa 50% de dano extra (Crítico)
        if (dado >= 18) {
            return (int) (dadoBase * 1.5);
        }
        return danoBase;
    }

    @Override
    public void atacar(Combatente alvo) {
        int danoFinal = calcularDanoCritico(this.getDano());
        alvo.receberDano(danoFinal);
        
        // Aplica o status de sono/paralisia
        alvo.setDormindo(true);

        if (danoFinal > this.getDano()) {
            this.setMensagem(this.getNome() + " ACERTO CRÍTICO! Dano das sombras: " + danoFinal);
        } else {
            this.setMensagem(this.getNome() + " disparou projétil de sombras em " + alvo.getNome());
        }
    }

    @Override
    protected void evoluirStats() {
        int bonusDanoTrevas = (int) (this.getDano() * 0.20);
        int ganhoVidaBase = 12;

        super.atualizaAtributos(bonusDanoTrevas, ganhoVidaBase);
        
        System.out.println("Ascensão: " + this.getNome() + " agora é um Atirador das Trevas!");
    }
}
