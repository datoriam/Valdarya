package com.rpgpoo.game.entity;

public class Atirador extends Combatente {

    public Atirador(String nome) {
        // Inicialização com valores balanceados: 85 de Vida e 18 de Dano
        super(nome, 85, 18);
    }

    @Override
    public void atacar(Combatente alvo) {
        System.out.println(this.getNome() + " dispara um projétil envolto em sombras!");

        // Uso de getters para ler status e método receberDano do alvo
        alvo.receberDano(this.getDano());

        // Efeito Especial: Atordoar o alvo utilizando setDormindo
        alvo.setDormindo(true);
        System.out.println("O alvo foi paralisado pela energia das trevas!");
    }

    @Override
    protected void evoluirStats() {
        // Lógica de Atirador das Trevas: Aumento de 20% no dano atual
        int bonusDanoTrevas = (int) (this.getDano() * 0.20);
        int ganhoVidaBase = 12;

        //  Uso obrigatório do super.atualizaAtributos
        super.atualizaAtributos(bonusDanoTrevas, ganhoVidaBase);

        System.out.println("O " + this.getNome() + " ascendeu para Atirador das Trevas!");
        System.out.println("Seu dano aumentou drasticamente com o poder sombrio!");

        // Feedback visual para níveis específicos
        if (this.getNivel() >= 5) {
            System.out.println("Domínio total das trevas alcançado!");
        }
    }
}
