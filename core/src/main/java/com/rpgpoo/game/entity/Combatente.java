package com.rpgpoo.game.entity;

public abstract class Combatente {
    private String mensagem;
    private String nome;
    private int vidaTotal;
    private int vidaAtual;
    private int nivel;
    private int dano;
    private int xp;

    // Status básicos - Implementação de efeitos (veneno, sono) deve ser feita na Batalha ou aqui
    private boolean dormindo = false;
    private boolean queimando = false;
    private boolean envenenado = false;

    public Combatente (String nome, int vidaTotal, int danoBase) {
        this.nome = nome;
        this.vidaTotal = vidaTotal;
        this.vidaAtual = vidaTotal;
        this.nivel = 5; // Nível inicial para testes
        this.dano = danoBase;
        this.xp = 0;
        this.mensagem = "";
    }

    public void atacar(Combatente alvo){
        setMensagem("");
        String mensagemAtaque = getNome() + " atacou e causou " + getDano() + " de dano";
        setMensagem(mensagemAtaque);
        alvo.receberDano(getDano());
    }

    // Método abstrato forçando subclasses a implementarem sua própria evolução
    protected abstract void evoluirStats();

    // Gets e Sets básicos...
    public void setMensagem(String msg){
        if (this.mensagem == null || this.mensagem.isEmpty()) this.mensagem = msg;
        else this.mensagem += "\n" + msg;
    }
    public void limparMensagem() { this.mensagem = ""; }
    public String getMensagem() { return mensagem; }

    public String getNome(){return nome;}
    public int getDano(){return dano;}
    public int getNivel(){return nivel;}
    public int getVidaAtual(){return vidaAtual;}
    public int getVidaTotal(){return vidaTotal;}

    public boolean checaVida(){ return vidaAtual > 0; }

    public void receberDano(int danoRecebido){
        vidaAtual -= danoRecebido;
        if (vidaAtual < 0) vidaAtual = 0;
        setMensagem(this.nome + " recebeu " + danoRecebido + " de dano!");
    }

    public void ganharXP(int quantidade){
        this.xp += quantidade;
        if(xp >= 100){
            this.xp -= 100;
            subirNivel();
        }
    }

    // Tornado público para uso externo se necessário, mas idealmente protegido
    public void atualizaAtributos(int aumentaDano, int aumentaVida){
        this.dano += aumentaDano;
        this.vidaTotal += aumentaVida;
        this.vidaAtual = this.vidaTotal;
    }

    // Métodos de status - A lógica de DANO por status (ex: queimadura tira vida) deve ser chamada no turno
    public void aplicarSono() { this.dormindo = true; setMensagem("Dormindo..."); }
    public void queimarInimigo() { this.queimando = true; setMensagem("Queimando..."); }
    public void envenenar() { this.envenenado = true; setMensagem("Envenenado..."); }

    public boolean processaStatus() {
        // TODO: Implementar lógica de dano periódico (DoT) e impedimento de ação (Sono)
        // Atualmente retorna sempre true (pode atacar)
        return true;
    }

    public void subirNivel(){
        nivel++;
        vidaAtual = vidaTotal;
        setMensagem("Level Up! " + this.nivel);
        evoluirStats();
    }

    public void setDormindo(boolean dormindo) { this.dormindo = dormindo; }
    public void setQueimando(boolean queimando) { this.queimando = queimando; }
    public void setEnvenenado(boolean envenenado) { this.envenenado = envenenado; }
}
