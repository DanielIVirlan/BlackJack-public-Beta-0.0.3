import controller.Controller;
import model.Player;
import view.GameMenu;

/**
 * La classe principale che inizializza il gioco del Blackjack.
 */
public class JBlackJack {

    /**
     * Metodo principale per avviare il gioco del Blackjack.
     * 
     * @param args Argomenti da riga di comando (non utilizzati in questa applicazione).
     */
    public static void main(String[] args) {
        // Crea un'istanza singola di Player utilizzando il pattern Singleton
        Player player = Player.getInstance();
        
        // Crea un'istanza di Controller passando l'istanza del giocatore
        Controller controller = new Controller(player);
        
        // Crea una nuova istanza di GameMenu passando il controller
        new GameMenu(controller);
    }
}
