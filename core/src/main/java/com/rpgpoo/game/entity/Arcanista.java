package com.rpgpoo.game.entity;

public class Arcanista extends Combatente {

    private int manaAtual;
    private int manaMaxima;
    private static final int CUSTO_FEITICO = 25; // Aumentei custo pra nao spammar
    private static final int RECUPERA_MEDITACAO = 15;
    private static final int MULTIPLICADOR_MAGIA = 2;

    public Arcanista (String nome) {
        // Vida baixa (60), Dano base alto (18)
        super(nome, 60, 18);
        this.manaAtual = 100;
        this.manaMaxima = 100;
    }

    public int getMana(){ return this.manaAtual; }

    private void meditar() {
        this.manaAtual += RECUPERA_MEDITACAO;
        if (this.manaAtual > this.manaMaxima){
            this.manaAtual = this.manaMaxima;
        }
        setMensagem(this.getNome() + " medita e recupera mana [" + manaAtual + "/" + manaMaxima + "]");
    }

    private void lancarFeitico(Combatente alvo){
        this.manaAtual -= CUSTO_FEITICO;
        int danoMagico = this.getDano() * MULTIPLICADOR_MAGIA;

        setMensagem(this.getNome() + " lança feitiço! (Mana: " + this.manaAtual + ")");

        // Aplica dano
        alvo.receberDano(danoMagico);

        // Efeitos extras por nivel
        if(this.getNivel() >= 8) {
            alvo.queimarInimigo();
        } else if (this.getNivel() >= 4) {
            // Chance de sono pra nao ficar op
            if (Math.random() > 0.5) alvo.aplicarSono();
        }
    }

    @Override
    public void atacar(Combatente alvo) {
        limparMensagem();

        // CORRECAO: checa manaAtual, nao manaMaxima
        if(this.manaAtual >= CUSTO_FEITICO) {
            lancarFeitico(alvo);
        } else {
            meditar(); // Se nao tem mana, medita (perde o turno de ataque mas recupera)
        }
    }

    @Override
    protected void evoluirStats() {
        // NERF: Mago ganha muito dano (+4) mas quase nada de vida (+3)
        // Virou "Glass Cannon" real
        super.atualizaAtributos(4, 3);

        this.manaMaxima += 10;
        this.manaAtual = this.manaMaxima;

        setMensagem("Mana Máxima aumentou para " + this.manaMaxima);
    }
}
