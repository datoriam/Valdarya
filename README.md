# ⚔️ Projeto Valdarya: RPG Tático em Java

O **Valdarya** é um jogo de RPG de turno desenvolvido como projeto prático para consolidar os conceitos das Unidades I e II da disciplina de Programação Orientada a Objetos, sob orientação do **Prof. André Yoshiaki**.

---

## 🛠️ Tecnologias e Infraestrutura
Diferente das abordagens convencionais com Java Swing, o projeto utiliza a **LibGDX**, uma framework profissional para desenvolvimento de jogos.
* **Render Loop:** Processamento gráfico em tempo real a 60 FPS.
* **Gerenciamento de Memória:** Uso do método `dispose()` para limpeza de texturas e recursos da GPU.
* **Arquitetura Visual:** Implementação de `OrthographicCamera` para garantir a proporção 1280x720 e transições suaves entre `MenuScreen`, `SelecaoScreen` e `ArenaScreen`.

---

## 🧬 Pilares de POO Aplicados (Unidade I)

O projeto foi construído sob uma arquitetura modular utilizando os fundamentos de POO:

### 1. Abstração e Herança
A base do sistema reside na classe abstrata **`Combatente`**. Ela define o contrato de estado (HP, Nível, Dano) e comportamento para todas as entidades.
* **Subclasses de Heróis:** `Guardiao`, `Arcanista` e `Atirador`.
* **Subclasses de Inimigos:** `Slime` e `Zumbi`.

### 2. Polimorfismo de Sobrescrita (Late Binding)
O método `atacar(Combatente alvo)` é o coração do polimorfismo no projeto. Embora declarado na superclasse, cada subclasse redefine seu comportamento de forma única:

| Subclasse     | Especialização do Método `atacar()`                                             |
|:--------------|:--------------------------------------------------------------------------------|
| **Guardião**  | Causa dano físico e recupera **Vigor** para alimentar sua mecânica de bloqueio. |
| **Arcanista** | Consome **Mana** para disparar feitiços com alto multiplicador de dano.         |
| **Atirador**  | Dispara projéteis sombrios com chance de aplicar status de **Sono** no alvo.    |



[Image of Polymorphism in Object-Oriented Programming diagram]


### 3. Encapsulamento
Todos os atributos sensíveis são declarados como `private`. A integridade dos dados é garantida por métodos de acesso e modificação (Getters/Setters) e métodos de lógica de negócio, como o `receberDano(int dano)`, que valida a defesa antes de alterar o estado do HP.

---

## 📚 Gerenciamento de Dados com Collections (Unidade II)

A manipulação de hordas e times foi implementada utilizando o framework de **Java Collections**.

* **Listas Dinâmicas:** Utilizamos `ArrayList<Combatente>` para gerenciar o `timeA` (Aliados) e `timeB` (Inimigos). Isso permitiu que o jogo escalasse de 1 até 3 inimigos simultâneos conforme o andar.
* **Ataque em Área:** Implementamos lógica de iteração (loops) sobre as coleções para que habilidades específicas atinjam múltiplos objetos da lista simultaneamente.
* **Fluxo de Auto-Target:** Ao detectar a derrota do personagem ativo, o sistema varre a `Collection` em busca do próximo objeto vivo, garantindo que o loop de batalha não seja interrompido por exceções de referência nula.



---

## 👥 Equipe e Contribuições

Para uma organização profissional, o projeto foi dividido em módulos de responsabilidade:

* **Daví Antonio Martins Ribeiro @datoriam (Infraestrutura):** Implementação da LibGDX, gerenciamento de telas, câmeras e ciclo de vida dos recursos gráficos.
* **Nícolas dos Santos Oliveira @ncztv36-star (Lógica e Balanceamento):** Estruturação da hierarquia de classes, implementação do polimorfismo nas subclasses e balanceamento de atributos.
* **Amós Andrade Nunes @amosandradenunes1-ux (UX e Fluxo):** Gerenciamento do log de batalha, design da interface (Arena) e lógica de transição de turnos.
* **Juan Victor Santana dos Santos @Juan80758 (Collections):** Implementação das listas dinâmicas, mecânicas de hordas e ataques em área sobre as coleções.

---

## 🎮 Conclusão
O **Valdarya** demonstra como a união de uma engine de alto desempenho com uma arquitetura sólida em POO permite criar sistemas complexos, escaláveis e de fácil manutenção, cumprindo todos os requisitos acadêmicos propostos.
