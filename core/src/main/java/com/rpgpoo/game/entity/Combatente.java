package com.rpgpoo.game.entity;

public abstract class Combatente {
    // Mantive a trava do nível 25
    protected static final int NIVEL_MAXIMO = 25;

    private String mensagem;
    private String nome;
    private int vidaTotal;
    private int vidaAtual;
    private int nivel;
    private int dano;
    private int xp;

    // Status
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
        String textoAtaque = this.nome + " atacou!";
        setMensagem(textoAtaque);
        alvo.receberDano(this.dano);
    }

    public void receberDano(int danoRecebido) {
        // Simples: Vida - Dano
        this.vidaAtual -= danoRecebido;
        if (this.vidaAtual < 0) this.vidaAtual = 0;
        setMensagem(this.nome + " tomou " + danoRecebido + " de dano!");
    }

    // Mantive o método utilitário pois a Batalha usa
    public void recuperarVidaTotal() {
        this.vidaAtual = this.vidaTotal;
    }

    protected abstract void evoluirStats();

    // Curva de XP padrão (Linear ou levemente progressiva)
    public int getXpNecessario() {
        // ANTES: 100 + (nivel * 50)
        // AGORA: 80 + (nivel * 25) -> Upa quase 2x mais rápido
        return 80 + (this.nivel * 25);
    }

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

    public void ganharXP(int quantidade) {
        if (this.nivel >= NIVEL_MAXIMO) {
            setMensagem("Nivel Maximo!");
            return;
        }

        this.xp += quantidade;
        setMensagem("Ganhou " + quantidade + " XP.");

        while (this.xp >= getXpNecessario()) {
            this.xp -= getXpNecessario();
            subirNivel();
            if (this.nivel >= NIVEL_MAXIMO) {
                this.xp = 0;
                setMensagem("NIVEL MAXIMO ALCANCADO!");
                break;
            }
        }
    }

    public void subirNivel() {
        subirNivel(true);
    }

    public void subirNivel(boolean mostrarMensagem) {
        this.nivel++;
        this.vidaAtual = this.vidaTotal; // Cura ao upar

        if (mostrarMensagem) {
            setMensagem("GLORIOSO! " + this.nome + " alcancou nivel " + this.nivel + "!");
        }
        evoluirStats();
    }

    public void atualizaAtributos(int aumentaDano, int aumentaVida) {
        this.dano += aumentaDano;
        this.vidaTotal += aumentaVida;
    }

    // Getters e Setters e Status
    public void aplicarSono() { this.dormindo = true; setMensagem(this.nome + " dormiu!"); }
    public void queimarInimigo() { this.queimando = true; setMensagem(this.nome + " pegou fogo!"); }
    public void envenenar() { this.envenenado = true; setMensagem(this.nome + " envenenado!"); }

    public void setMensagem(String msg) {
        if (this.mensagem == null || this.mensagem.isEmpty()) this.mensagem = msg;
        else this.mensagem += "\n" + msg;
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
