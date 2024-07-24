package model;

import java.util.Observable;
import view.GameMenu;


@SuppressWarnings("deprecation")
public class Player extends Observable {
    
    private Deck deck;       
    private Hand dealer;   
    private Hand player1;      

    private static int gamesPlayed = 0;  
    private static int gamesWon = 0;    
    private static int gamesLost = 0;    

    private Card hiddenCard; 
    private static Player instance;
    
    private Player() { }
    
    /**
     * Fornisce l'accesso all'istanza singleton della classe Player.
     *
     * @return L'istanza singleton di Player.
     */
    public static synchronized Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }
    
    /**
     * Inizializza il gioco impostando i giocatori, la carta nascosta e il mazzo.
     *
     * @param dealer Mano del dealer.
     * @param player1 Mano del giocatore umano.
     * @param bot1 Mano del bot 1.
     * @param bot2 Mano del bot 2.
     * @param hiddenCard Carta nascosta del dealer.
     * @param deck Il mazzo di carte.
     */
    public void initializeGame(Hand dealer, Hand player1, Card hiddenCard, Deck deck) {
        this.dealer = dealer;
        this.player1 = player1;
        this.hiddenCard = hiddenCard;
        this.deck = deck;
        setChanged();
        notifyObservers();
    }
    
    /**
     * Esegue l'azione di "hit" per il giocatore umano, aggiungendo una carta alla sua mano.
     */
    public void playerHit() {
        player1.addToHand(deck.drawCard());  
        if (player1.getAceCount() > 0 && player1.getSum() > 21) {
            player1.getSum();
        }

        setChanged();
        notifyObservers();
        System.out.println("Player hit: " + player1.getHand());
    }
    
    /**
     * Simula il turno del dealer, pescando carte fino a quando il valore della mano non raggiunge 17 o più.
     */
    public void dealerTurn() {
        while (dealer.getSum() < 17) {
            dealer.addToHand(deck.drawCard());
            if (dealer.getAceCount() > 0 && dealer.getSum() > 21) {
                dealer.getSum();
            }
        }
        setChanged();
        notifyObservers();
        System.out.println("Dealer turn complete: " + player1.getHand());
    }
    
    /**
     * Simula il turno del bot 1, pescando carte fino a quando il valore della mano non raggiunge 17 o più.
     */
    
    
    /**
     * Simula il turno del bot 2, pescando carte fino a quando il valore della mano non raggiunge 17 o più.
     */
    
    /**
     * Determina il vincitore del gioco in base ai valori delle mani dei giocatori e del dealer.
     *
     * @return Un messaggio che indica il risultato del gioco.
     */
    public String determineWinner() {
        int playerSum = player1.getSum();
        int dealerSum = dealer.getSum();

        boolean playerWins = playerSum <= 21 && (playerSum > dealerSum || dealerSum > 21);
        if (playerWins) {
        	incrementGamesWon();
        } else {
        	incrementGamesLost();
        }
       

        boolean tiePlayer = playerSum == dealerSum && playerSum <= 21;
        

        StringBuilder result = new StringBuilder();

        if (tiePlayer) {
            result.append(GameMenu.playerName).append(" tied with the dealer. ");
        } else if (playerWins) {
            result.append(GameMenu.playerName).append(" you Win!!. ");
        } else {
            result.append(GameMenu.playerName).append(" you Lost!!. ");
        }

        


        

        return result.toString();
    }
    
    /**
     * Restituisce la mano del dealer.
     *
     * @return La mano del dealer.
     */
    public Hand getDealer() {
        return dealer;
    }

    /**
     * Restituisce la mano del giocatore umano.
     *
     * @return La mano del giocatore umano.
     */
    public Hand getPlayer1() {
        return player1;
    }

    /**
     * Restituisce la mano del bot 1.
     *
     * @return La mano del bot 1.
     */
   

    /**
     * Restituisce la mano del bot 2.
     *
     * @return La mano del bot 2.
     */
    

    /**
     * Restituisce la carta nascosta del dealer.
     *
     * @return La carta nascosta del dealer.
     */
    public Card getHiddenCard() {
        return hiddenCard;
    }
    
    /**
     * Restituisce il numero di giochi giocati.
     *
     * @return Il numero di giochi giocati.
     */
    public static int getGamesPlayed() {
        return gamesPlayed;
    }
    
    /**
     * Restituisce il numero di giochi vinti.
     *
     * @return Il numero di giochi vinti.
     */
    public static int getGamesWon() {
        return gamesWon;
    }
    
    /**
     * Restituisce il numero di giochi persi.
     *
     * @return Il numero di giochi persi.
     */
    public static int getGamesLost() {
        return gamesLost;
    }
    
    /**
     * Incrementa il numero di giochi giocati.
     */
    public static void incrementGamesPlayed() {
        gamesPlayed++;
    }
    
    /**
     * Incrementa il numero di giochi vinti.
     */
    public static void incrementGamesWon() {
        gamesWon++;
    }
    
    /**
     * Incrementa il numero di giochi persi.
     */
    public static void incrementGamesLost() {
        gamesLost++;
    }
    
    /**
     * Resetta le statistiche del gioco.
     */
    public static void resetGame() {
        gamesPlayed = 0;
        gamesLost = 0;
        gamesWon = 0;
    }

    
}
