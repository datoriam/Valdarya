package com.rpgpoo.game.entity;

public class Atirador extends Combatente {

    public Atirador(String nome) {
        // Balanceado: 80 vida, 14 dano
        super(nome, 80, 14);
    }

    @Override
    public void atacar(Combatente alvo) {
        limparMensagem();
        setMensagem(this.getNome() + " dispara das sombras!");

        alvo.receberDano(this.getDano());

        // Balanceamento: Chance de aplicar sono (não 100% das vezes)
        // Se nivel for alto, tem chance de critico/efeito
        if (getNivel() >= 5 && Math.random() > 0.7) {
            alvo.aplicarSono();
            setMensagem("CRITICO! O alvo apagou!");
        }
    }

    @Override
    protected void evoluirStats() {
        // NERF: Crescimento linear e controlado
        // Antes era % (exponencial), agora é fixo: +3 dano, +6 vida
        super.atualizaAtributos(3, 6);

        setMensagem(this.getNome() + " aprimorou a mira sombria!");
    }
}
