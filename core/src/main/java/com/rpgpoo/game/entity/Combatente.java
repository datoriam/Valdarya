package com.rpgpoo.game.entity;

public abstract class Combatente {
    private String mensagem;
    private String nome;
    private int vidaTotal;
    private int vidaAtual;
    private int nivel;
    private int dano;
    private int xp;

    // Status de condição
    private boolean dormindo = false;
    private boolean queimando = false;
    private boolean envenenado = false;

    public Combatente (String nome, int vidaTotal, int danoBase) {
        this.nome = nome;
        this.vidaTotal = vidaTotal;
        this.vidaAtual = vidaTotal;
        this.nivel = 1;
        this.dano = danoBase;
        this.xp = 0;
        this.mensagem = "";
    }

    public void atacar(Combatente alvo) {
        setMensagem("");
        String textoAtaque = this.nome + " desferiu um golpe!";
        setMensagem(textoAtaque);
        alvo.receberDano(this.dano);
    }

    protected abstract void evoluirStats();

    public boolean processaStatus() {
        if (!checaVida()) return false;

        if (dormindo) {
            setMensagem(this.nome + " esta dormindo... Zzz...");
            dormindo = false;
            return false;
        }

        if (queimando) {
            int danoFogo = (int) Math.ceil(this.vidaTotal * 0.10);
            this.vidaAtual -= danoFogo;
            setMensagem("Fogo queima " + this.nome + "! (-" + danoFogo + ")");
        }

        if (envenenado) {
            int danoVeneno = (int) Math.ceil(this.vidaTotal * 0.05);
            this.vidaAtual -= danoVeneno;
            setMensagem(this.nome + " sofre com veneno. (-" + danoVeneno + ")");
        }

        if (this.vidaAtual <= 0) {
            this.vidaAtual = 0;
            return false;
        }
        return true;
    }

    public void receberDano(int danoRecebido) {
        this.vidaAtual -= danoRecebido;
        if (this.vidaAtual < 0) this.vidaAtual = 0;
        setMensagem(this.nome + " tomou " + danoRecebido + " de dano!");
    }

    public void ganharXP(int quantidade) {
        this.xp += quantidade;
        setMensagem("Ganhou " + quantidade + " XP.");

        while (this.xp >= 100) {
            this.xp -= 100;
            subirNivel(); // heroi sempre mostra mensagem
        }
    }

    // --- MUDANÇA AQUI: SOBRECARGA PRA ESCONDER O TEXTO ---

    // metodo padrao (pros herois)
    public void subirNivel() {
        subirNivel(true);
    }

    // metodo com opcao de silencio (pros monstros)
    public void subirNivel(boolean mostrarMensagem) {
        this.nivel++;
        this.vidaAtual = this.vidaTotal;

        if (mostrarMensagem) {
            setMensagem("GLORIOSO! " + this.nome + " alcancou nivel " + this.nivel + "!");
        }
        evoluirStats();
    }

    public void atualizaAtributos(int aumentaDano, int aumentaVida) {
        this.dano += aumentaDano;
        this.vidaTotal += aumentaVida;
        this.vidaAtual = this.vidaTotal;
    }

    public void aplicarSono() {
        this.dormindo = true;
        setMensagem(this.nome + " dormiu!");
    }

    public void queimarInimigo() {
        this.queimando = true;
        setMensagem(this.nome + " pegou fogo!");
    }

    public void envenenar() {
        this.envenenado = true;
        setMensagem(this.nome + " envenenado!");
    }

    public void setMensagem(String msg) {
        if (this.mensagem == null || this.mensagem.isEmpty()) {
            this.mensagem = msg;
        } else {
            this.mensagem += "\n" + msg;
        }
    }

    public void limparMensagem() { this.mensagem = ""; }
    public String getMensagem() { return mensagem; }

    public String getNome() { return nome; }
    public int getDano() { return dano; }
    public int getNivel() { return nivel; }
    public int getVidaAtual() { return vidaAtual; }
    public int getVidaTotal() { return vidaTotal; }
    public boolean checaVida() { return vidaAtual > 0; }

    public void setDormindo(boolean dormindo) { this.dormindo = dormindo; }
    public void setQueimando(boolean queimando) { this.queimando = queimando; }
    public void setEnvenenado(boolean envenenado) { this.envenenado = envenenado; }
}
