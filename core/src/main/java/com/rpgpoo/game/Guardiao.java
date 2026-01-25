package com.rpgpoo.game;
public class Guardiao extends Combatente {
 // ATRIBUTOS ESPECIAIS DO GUARDIÃO
    private int vigor;
    private int vigorMaximo;
    private final int CUSTO_BLOQUEIO = 15;
    
    public Guardiao(String nome) {
        // Segundo especificação: Guardiões são robustos (vida alta)
        super(nome, 150, 8); // 150 PV, 8 de dano base
        this.vigorMaximo = 50;
        this.vigor = vigorMaximo;
    }
    
    // IMPLEMENTAÇÃO DO ATAQUE DO GUARDIÃO
    @Override
    public void atacar(Combatente alvo) {
        // Guardiões causam dano físico constante
        int dano = this.getDano();
        alvo.receberDano(dano);
        
        // Recupera um pouco de vigor ao atacar (mecânica especial)
        this.vigor = Math.min(this.vigor + 5, this.vigorMaximo);
    }
    
    // SOBRESCRITA DO MÉTODO receberDano PARA IMPLEMENTAR BLOQUEIO
    @Override
    public void receberDano(int dano) {
        // LÓGICA DE NEGÓCIO DO GUARDIÃO:
        // Se Vigor atual for suficiente, o dano recebido é anulado (reduzido a 0)
        // e o Vigor é consumido. Caso contrário, o dano é subtraído normalmente
        
        if (this.vigor >= CUSTO_BLOQUEIO) {
            // BLOQUEIO BEM-SUCEDIDO - Dano anulado, consome vigor
            this.vigor -= CUSTO_BLOQUEIO;
        } else {
            // BLOQUEIO FALHOU - Aplica dano normal
            super.receberDano(dano);
        }
    }
    
    // IMPLEMENTAÇÃO DA EVOLUÇÃO POR NÍVEL
    @Override
    protected void evoluirStats() {
        // Guardiões ganham mais vida e um pouco de dano por nível
        super.atualizaAtributos(2, 35); // +2 dano, +35 vida por nível
        
        // Vigor máximo também aumenta com o nível
        this.vigorMaximo += 10;
        this.vigor = this.vigorMaximo; // Recupera todo o vigor ao subir de nível
    }
    
    // MÉTODOS ESPECÍFICOS DO GUARDIÃO
    
    public int getVigor() {
        return vigor;
    }
    
    public int getVigorMaximo() {
        return vigorMaximo;
    }
    
    public int getCustoBloqueio() {
        return CUSTO_BLOQUEIO;
    }
    
    // Verifica se pode bloquear no momento
    public boolean podeBloquear() {
        return this.vigor >= CUSTO_BLOQUEIO;
    }
    
    // Método para restaurar vigor (útil entre batalhas)
    public void restaurarVigor() {
        this.vigor = this.vigorMaximo;
    }
    
    // Método para recuperar vigor parcialmente
    public void recuperarVigor(int quantidade) {
        this.vigor = Math.min(this.vigor + quantidade, this.vigorMaximo);
    }
    
    // Método para usar vigor ativamente (para habilidades futuras)
    public boolean usarVigor(int quantidade) {
        if (this.vigor >= quantidade) {
            this.vigor -= quantidade;
            return true;
        }
        return false;
    }
    
    // Representação textual para debug/logs
    @Override
    public String toString() {
        return "Guardiao{" +
               "nome='" + this.getNome() + '\'' +
               ", PV=" + this.getPontosVida() + "/" + this.getPontosVidaMaximos() +
               ", nivel=" + this.getNivel() +
               ", dano=" + this.getDano() +
               ", vigor=" + vigor + "/" + vigorMaximo +
               ", podeBloquear=" + podeBloquear() +
               '}';
    }
}

