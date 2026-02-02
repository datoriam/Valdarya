package com.rpgpoo.game.entity;

public class Guardiao extends Combatente {
    private int vigor = 50;
    private final int CUSTO_BLOQUEIO = 15;

    public Guardiao(String nome) {
        // Base: 100 vida (antes era 120), 10 dano
        super(nome, 100, 10);
    }

    @Override
    public void atacar(Combatente alvo) {
        limparMensagem();
        setMensagem(getNome() + " ataca com martelo pesado!");
        alvo.receberDano(getDano());

        vigor = Math.min(vigor + 5, 50);
        setMensagem("Recuperou 5 de vigor [" + vigor + "/50]");
    }

    @Override
    public void receberDano(int danoRecebido) {
        limparMensagem();

        if (vigor >= CUSTO_BLOQUEIO) {
            // BLOQUEIO
            setMensagem(getNome() + " BLOQUEIA com escudo!");
            setMensagem("Dano anulado (" + danoRecebido + " -> 0)");

            vigor -= CUSTO_BLOQUEIO;
        } else {
            // SEM VIGOR
            setMensagem(getNome() + " sem vigor para bloquear!");
            super.receberDano(danoRecebido);
        }
    }

    @Override
    protected void evoluirStats() {
        // NERF: Antes ganhava 30 de vida, agora ganha 12
        // Dano subia 3, agora sobe 2
        super.atualizaAtributos(2, 12);

        vigor = 50;
        setMensagem("Vigor restaurado!");
    }

    @Override
    public boolean processaStatus() {
        boolean podeAtacar = super.processaStatus();

        if (podeAtacar) {
            int recuperacao = 3;
            int vigorAntes = vigor;
            vigor = Math.min(vigor + recuperacao, 50);

            if (vigorAntes < 50 && vigor > vigorAntes) {
                setMensagem("Passiva: Recuperou " + (vigor - vigorAntes) + " vigor");
            }
        }
        return podeAtacar;
    }

    public int getVigor() { return vigor; }
}
