package com.rpgpoo.game.entity;

import com.rpgpoo.game.entity.Combatente;

public class Arcanista extends Combatente {

    private int manaAtual;
    private int manaMaxima;

    private static final int CUSTO_FEITICO = 20;
    private static final int RECUPERA_MEDITACAO = 15; // Aumentado para não travar muito o dps
    private static final int MULTIPLICADOR_MAGIA = 2;

    public Arcanista(String nome) {
        // Vida base menor (80), Dano base ok (8) para compensar o multiplicador
        super(nome, 80, 8);
        this.manaMaxima = 100;
        this.manaAtual = 100;
    }

    private void meditar(Combatente alvo) {
        this.manaAtual += RECUPERA_MEDITACAO;
        if (this.manaAtual > this.manaMaxima) this.manaAtual = this.manaMaxima;

        // Ataque físico fraco enquanto recupera mana
        alvo.receberDano(this.getDano());
        
        // Seta a mensagem para o log da Batalha.java
        this.setMensagem(this.getNome() + " meditou (+Mana) e deu tapa físico em " + alvo.getNome());
    }

    private void lancarFeitico(Combatente alvo) {
        this.manaAtual -= CUSTO_FEITICO;
        int danoMagico = this.getDano() * MULTIPLICADOR_MAGIA;
        alvo.receberDano(danoMagico);
        
        this.setMensagem(this.getNome() + " lançou FEITIÇO em " + alvo.getNome() + " (" + danoMagico + " dano)!");
    }

    @Override
    public void atacar(Combatente alvo) {
        // Correção lógica: checar manaAtual e não manaMaxima
        if (this.manaAtual >= CUSTO_FEITICO) {
            lancarFeitico(alvo);
        } else {
            meditar(alvo);
        }
    }

    @Override
    protected void evoluirStats() {
        // Evolução focada em Inteligência (Dano e Mana)
        // super.atualizaAtributos(Dano, Vida)
        super.atualizaAtributos(4, 15); 

        this.manaMaxima += 25;
        this.manaAtual = this.manaMaxima;

        // Mensagem interna para controle de console
        System.out.println(this.getNome() + " subiu de nível! Mana agora: " + this.manaMaxima);
    }
}