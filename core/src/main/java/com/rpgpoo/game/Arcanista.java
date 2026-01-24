package com.rpgpoo.game;

public class Arcanista extends Combatente {

    private int manaAtual;
    private int manaMaxima;
    private static final int custoFeitico = 20; //custo inicial, pode haver mudança no valor do custo
    private static final int recuperaMeditacao = 10;

    private static final int multiplicadorMagia = 2;

    public Arcanista (String nome) {

        super(nome, 80,  8);

        this.manaAtual = 100;
        this.manaMaxima = 100;
    }

    public int getMana(){
        return this.manaAtual;
    }

    private void meditar(Combatente alvo) {
        this.manaAtual += recuperaMeditacao;

        if (this.manaAtual > this.manaMaxima){
            this.manaAtual = this.manaMaxima;
        }
        alvo.receberDano(this.getDano());

        setMensagem(this.getNome() + " está sem mana ele vai atacar fisicamente enquanto está nesse estado");

    }

    private void lancarFeitico(Combatente alvo){
        this.manaAtual -= custoFeitico;
        int danoMagico = this.getDano() * multiplicadorMagia;
        setMensagem(this.getNome() + " está lançando o feitiço! Dano causado: " + getDano() + "\n A mana atual de " + getNome() + " é " + this.manaAtual);
        //para aplicar o sleep
        //corrigi pra ação de efeito mágico funcionar depois de atacar
        if(this.getNivel() >= 5) {
            alvo.receberDano(danoMagico);
            alvo.aplicarSono();
        } else if (this.getNivel() >= 6) {
            alvo.receberDano(danoMagico);
            alvo.queimarInimigo();
        } else {

            alvo.receberDano(danoMagico);
        }
    }

    @Override
    public void atacar(Combatente alvo) {

        if(this.manaMaxima >= custoFeitico) {
            lancarFeitico(alvo);

        } else {
            meditar(alvo);
        }
    }

    @Override
    protected void evoluirStats() {  //método de evolução do Arcanista, depois analisar para um melhor balanceamento

        super.atualizaAtributos(5, 10);

        this.manaMaxima += 20;
        this.manaAtual = this.manaMaxima;

        setMensagem(this.getNome() + " aumentou de nível! Mana Máxima = " + this.manaMaxima);
    }



}
