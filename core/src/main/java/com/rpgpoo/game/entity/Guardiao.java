package com.rpgpoo.game.entity;

import com.rpgpoo.game.entity.Combatente;

public class Guardiao extends Combatente {

    private int vigor;
    private int vigorMaximo;
    private static final int CUSTO_BLOQUEIO = 15;
    private static final int RECUPERACAO_ATAQUE = 8; // Aumentado para sustentar o bloqueio

    public Guardiao(String nome) {
        // Guardião: O foco aqui é o HP altíssimo (150) e dano consistente (8)
        super(nome, 150, 8);
        this.vigorMaximo = 50;
        this.vigor = vigorMaximo;
    }

    @Override
    public void atacar(Combatente alvo) {
        // Recupera vigor ao golpear
        this.vigor = Math.min(this.vigor + RECUPERACAO_ATAQUE, vigorMaximo);
        
        // Executa o dano
        alvo.receberDano(this.getDano());
        
        // Mensagem combinada para o log não apagar a anterior
        this.setMensagem(this.getNome() + " golpeou com escudo e recuperou vigor!");
    }

    @Override
    public void receberDano(int danoRecebido) {
        // Mecânica Única: Bloqueio Ativo via Encapsulamento e Polimorfismo
        if (this.vigor >= CUSTO_BLOQUEIO) {
            this.vigor -= CUSTO_BLOQUEIO;
            // O dano não chega a ser subtraído do HP
            this.setMensagem(this.getNome() + " BLOQUEOU o impacto! (Vigor: " + this.vigor + ")");
        } else {
            // Se não tem vigor, usa a lógica padrão da superclasse
            super.receberDano(danoRecebido);
        }
    }

    @Override
    protected void evoluirStats() {
        // Evolução focada em Tanque (Muita Vida, pouco Dano)
        // super.atualizaAtributos(Dano, Vida)
        super.atualizaAtributos(2, 40); 
        
        this.vigorMaximo += 10;
        this.vigor = vigorMaximo; // Reset de vigor no Level Up

        System.out.println(this.getNome() + " elevou sua guarda! Vida máxima aumentada.");
    }

    // Getters para a ArenaScreen mostrar a barra de Vigor se quiserem
    public int getVigor() { return vigor; }
    public int getVigorMaximo() { return vigorMaximo; }
}
