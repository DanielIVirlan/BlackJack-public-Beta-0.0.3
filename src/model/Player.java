package model;

import java.util.Observable;
import view.GameMenu;

@SuppressWarnings("deprecation")
public class Player extends Observable {
    
    private Deck deck;       
    private Hand dealer;   
    private Hand player1;  
    private Hand bot1;     
    private Hand bot2;     

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
    public void initializeGame(Hand dealer, Hand player1, Hand bot1, Hand bot2, Card hiddenCard, Deck deck) {
        this.dealer = dealer;
        this.player1 = player1;
        this.bot1 = bot1;
        this.bot2 = bot2;
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
    public void bot1Turn() {
        while (bot1.getSum() < 17) {
            bot1.addToHand(deck.drawCard());

            if (bot1.getAceCount() > 0 && bot1.getSum() > 21) {
                bot1.getSum();
            }
        }
        setChanged();
        notifyObservers();
        System.out.println("Bot1 turn complete: " + bot1.getHand());
    }
    
    /**
     * Simula il turno del bot 2, pescando carte fino a quando il valore della mano non raggiunge 17 o più.
     */
    public void bot2Turn() {
        while (bot2.getSum() < 17) {
            bot2.addToHand(deck.drawCard());

            if (bot2.getAceCount() > 0 && bot2.getSum() > 21) {
                bot2.getSum();
            }
        }
        setChanged();
        notifyObservers();
        System.out.println("Bot2 turn complete: " + bot2.getHand());
    }
    
    /**
     * Determina il vincitore del gioco in base ai valori delle mani dei giocatori e del dealer.
     *
     * @return Un messaggio che indica il risultato del gioco.
     */
    public String determineWinner() {
        int playerSum = player1.getSum();
        int bot1Sum = bot1.getSum();
        int bot2Sum = bot2.getSum();
        int dealerSum = dealer.getSum();

        boolean playerWins = playerSum <= 21 && (playerSum > dealerSum || dealerSum > 21);
        if (playerWins) {
        	incrementGamesWon();
        } else {
        	incrementGamesLost();
        }
        boolean bot1Wins = bot1Sum <= 21 && (bot1Sum > dealerSum || dealerSum > 21);
        boolean bot2Wins = bot2Sum <= 21 && (bot2Sum > dealerSum || dealerSum > 21);

        boolean tiePlayer = playerSum == dealerSum && playerSum <= 21;
        boolean tieBot1 = bot1Sum == dealerSum && bot1Sum <= 21;
        boolean tieBot2 = bot2Sum == dealerSum && bot2Sum <= 21;

        StringBuilder result = new StringBuilder();

        if (tiePlayer) {
            result.append(GameMenu.playerName).append(" ha pareggiato con il dealer. ");
        } else if (playerWins) {
            result.append(GameMenu.playerName).append(" vince. ");
        } else {
            result.append(GameMenu.playerName).append(" perde. ");
        }

        if (tieBot1) {
            result.append("Bot 1 ha pareggiato con il dealer. ");
        } else if (bot1Wins) {
            result.append("Bot 1 vince. ");
        } else {
            result.append("Bot 1 perde. ");
        }

        if (tieBot2) {
            result.append("Bot 2 ha pareggiato con il dealer. ");
        } else if (bot2Wins) {
            result.append("Bot 2 vince. ");
        } else {
            result.append("Bot 2 perde. ");
        }

        boolean dealerWins = !tiePlayer && !tieBot1 && !tieBot2 &&
                !playerWins && !bot1Wins && !bot2Wins;

        if (dealerWins) {
            result.append("Il dealer vince.");
        } else {
            if (!tiePlayer && !playerWins && !tieBot1 && !bot1Wins && !tieBot2 && !bot2Wins) {
                result.append("Il dealer ha pareggiato con tutti i giocatori.");
            } else {
                result.append("Il dealer perde.");
            }
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
    public Hand getBot1() {
        return bot1;
    }

    /**
     * Restituisce la mano del bot 2.
     *
     * @return La mano del bot 2.
     */
    public Hand getBot2() {
        return bot2;
    }

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
