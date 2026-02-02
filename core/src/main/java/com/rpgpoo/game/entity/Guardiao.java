package com.rpgpoo.game.entity;

public class Guardiao extends Combatente {
    public Guardiao(String nome) {
        // Vida alta para tankar
        super(nome, 150, 15);
    }

    @Override
    protected void evoluirStats() {
        // Ganha bastante vida por nível
        atualizaAtributos(3, 35);
        recuperarVidaTotal();
    }

    @Override
    public void atacar(Combatente alvo) {
        setMensagem("");
        setMensagem(getNome() + " esmaga com o escudo!");

        // 1. Causa dano no inimigo
        alvo.receberDano(getDano());

        // 2. LÓGICA DE CURA ESCALÁVEL (PERCENTUAL)
        // Agora ele cura 10% da Vida Máxima dele + 5 fixo
        int cura = (int) (getVidaTotal() * 0.10) + 5;

        // Se a vida atual + cura passar do total, enche tudo.
        // Senão, cura o valor calculado.
        if (getVidaAtual() + cura >= getVidaTotal()) {
            recuperarVidaTotal();
            setMensagem("(Vida recuperada totalmente!)");
        } else {
            // Truque matemático: Dano negativo = Cura
            receberDano(-cura);
            setMensagem("(Recuperou " + cura + " HP)");
        }
    }
}
