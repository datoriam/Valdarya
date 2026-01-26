package com.rpgpoo.game; // [cite: 64]

/**
 * Implementa a evolução para Atirador das Trevas com bônus de 20% de dano.
 */
public class Atirador extends Combatente { // [cite: 19, 66]

    public Atirador(String nome) {
        // Inicialização com valores balanceados: 85 de Vida e 18 de Dano [cite: 25, 31, 78]
        super(nome, 85, 18);
    }

    @Override
    public void atacar(Combatente alvo) { // [cite: 15, 36, 88, 89]
        System.out.println(this.getNome() + " dispara um projétil envolto em sombras!"); // [cite: 90]
        
        // Uso de getters para ler status e método receberDano do alvo [cite: 35, 44, 92]
        alvo.receberDano(this.getDano());
        
        // Efeito Especial: Atordoar o alvo utilizando setDormindo [cite: 125, 126]
        alvo.setDormindo(true);
        System.out.println("O alvo foi paralisado pela energia das trevas!");
    }

    @Override
    protected void evoluirStats() { // [cite: 16, 54, 107, 108]
        // Lógica de Atirador das Trevas: Aumento de 20% no dano atual
        int bonusDanoTrevas = (int) (this.getDano() * 0.20);
        int ganhoVidaBase = 12;

        //  Uso obrigatório do super.atualizaAtributos [cite: 13, 50, 58, 111]
        super.atualizaAtributos(bonusDanoTrevas, ganhoVidaBase);
        
        System.out.println("O " + this.getNome() + " ascendeu para Atirador das Trevas!"); // [cite: 112]
        System.out.println("Seu dano aumentou drasticamente com o poder sombrio!");
        
        // Feedback visual para níveis específicos [cite: 113, 114]
        if (this.getNivel() >= 5) {
            System.out.println("Domínio total das trevas alcançado!"); // [cite: 115]
        }
    }
}
