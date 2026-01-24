import com.rpgpoo.game.Arcanista;
import com.rpgpoo.game.Combatente;
/*Aqueles que desejarem testar suas subclasses antes de levá-las para a parte gráfica, sintam-se a vontade em realizar
* os testes nesse arquivo. Basta instanciar a subclasse desejada e simular uma batalha de turnos e cada método!*/
public class Teste {
    public static void main(String[] args){
        //Combatente jogador = Combatente.criarPersonagem();
        //Arcanista heroi = new Arcanista("maguin da luz");
        Arcanista inimigo = new Arcanista("maguin das trevas");

        System.out.println("Get Ready For The Next Battle!");
        try {
            Thread.sleep(1500);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        //System.out.println("Do lado da luz, ele com " + jogador.getVidaAtual() + " de vida. O imparável: " + jogador.getNome()  + "!");
        try {
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Do lado das trevas, ele com " + inimigo.getVidaAtual() + " de vida. O abominável: " + inimigo.getNome()  + "!");
        try {
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Comecem!");
        try {
            Thread.sleep(500);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        //jogador.atacar(inimigo);
        try {
            Thread.sleep(1500);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        //inimigo.atacar(jogador);
        try {
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
