package com.rpgpoo.game;

public class Guardiao extends Combatente {
 // Atributos específicos do Guardião
    private int vigor;
    private int vigorMaximo;
    private final int CUSTO_BLOQUEIO = 15;
    
    public Guardiao(String nome) {
        // Guardião: alta vida, dano moderado
        super(nome, 150, 8);
        this.vigorMaximo = 50;
        this.vigor = vigorMaximo;
        setMensagem(nome + " o Guardião entra na batalha!");
    }
    
    @Override
    public void atacar(Combatente alvo) {
        // Guardião ataca com arma pesada
        setMensagem(getNome() + " ataca com sua espada pesada!");
        alvo.receberDano(getDano());
        
        // Recupera vigor ao atacar
        int recuperacao = 5;
        this.vigor = Math.min(this.vigor + recuperacao, vigorMaximo);
    }
    
    @Override
    public void receberDano(int danoRecebido) {
        // LÓGICA DO BLOQUEIO: Se tiver vigor suficiente, anula dano e consome vigor
        
        if (vigor >= CUSTO_BLOQUEIO) {
            // BLOQUEIO BEM-SUCEDIDO - dano anulado
            setMensagem(getNome() + " BLOQUEIA o ataque com seu escudo!");
            this.vigor -= CUSTO_BLOQUEIO;
        } else {
            // VIGOR INSUFICIENTE - dano normal
            super.receberDano(danoRecebido);
        }
    }
    
    @Override
    protected void evoluirStats() {
        // Guardião ganha mais vida e dano por nível
        super.atualizaAtributos(2, 35);
        
        // Aumenta vigor máximo também
        this.vigorMaximo += 10;
        this.vigor = vigorMaximo; // Recupera todo vigor ao subir de nível
    }
    
    // Métodos específicos do Guardião
    public int getVigor() {
        return vigor;
    }
    
    public int getVigorMaximo() {
        return vigorMaximo;
    }
    
    public boolean podeBloquear() {
        return vigor >= CUSTO_BLOQUEIO;
    }
}
