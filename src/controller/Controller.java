package controller;

import model.Hand;
import view.GameMenu;
import view.GameGUI;
import model.Card;
import model.Deck;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Controller che gestisce la logica del gioco Blackjack.
 */
public class Controller {

    private Player player;

    /**
     * Costruttore della classe Controller.
     *
     * @param player Oggetto Player che rappresenta il giocatore.
     */
    public Controller(Player player) {
        this.player = player;
        startGame();
    }

    /**
     * Metodo per avviare una nuova partita.
     */
    public void startGame() {
        Deck deck = new Deck(4);  // Crea un nuovo mazzo con 4 mazzi di carte
        Hand dealer = new Hand();  // Mano del dealer
        Hand player1 = new Hand(); // Mano del giocatore
        Hand bot1 = new Hand();    // Mano del bot1
        Hand bot2 = new Hand();    // Mano del bot2

        Card hiddenCard = deck.drawCard(); // Estrae una carta per nasconderla
        dealer.addToHand(hiddenCard);  // Aggiunge la carta nascosta al dealer
        dealer.addToHand(deck.drawCard());  // Estrae e aggiunge una carta visibile al dealer

        // Distribuisce 2 carte a player1, bot1 e bot2
        for (int i = 0; i < 2; i++) {
            player1.addToHand(deck.drawCard());
            bot1.addToHand(deck.drawCard());
            bot2.addToHand(deck.drawCard());
        }

        // Inizializza il gioco per il giocatore e i bot
        player.initializeGame(dealer, player1, hiddenCard, deck);

        // Stampa le carte iniziali del gioco
        System.out.println("Start game player-dealer: " + player1.getHand() + dealer.getHand());
        System.out.println("Start game bot1-bot2:     " + bot1.getHand() + bot2.getHand());
    }

    /**
     * Metodo chiamato quando il giocatore decide di "colpire" (richiedere una carta aggiuntiva).
     */
    public void hitAction() {
        player.playerHit(); // Il giocatore colpisce (aggiunge una carta)
        // Se il giocatore ha superato 21 o ha esattamente 21, il turno passa al dealer e ai bot
        if (player.getPlayer1().getSum() > 21 || player.getPlayer1().getSum() == 21 ) {
            player.dealerTurn();
            Player.incrementGamesPlayed();
            System.out.println("Stay action performed"); // Stampa che è stato eseguito il comando "stay"
        }
        System.out.println("Hit action performed"); // Stampa che è stato eseguito il comando "hit"
    }

    /**
     * Metodo chiamato quando il giocatore decide di "stare" (non richiedere ulteriori carte).
     */
    public void stayAction() {
    	Player.incrementGamesPlayed();
        player.dealerTurn(); // Il dealer gioca il suo turno
        System.out.println("Stay action performed"); // Stampa che è stato eseguito il comando "stay"
    }

    /**
     * Metodo chiamato quando il giocatore decide di "riprovare" (avviare una nuova partita).
     */
    public void retryAction() {
        new GameGUI(player, this); // Crea una nuova interfaccia grafica per il gioco
        startGame(); // Avvia una nuova partita
        System.out.println("Retry action performed"); // Stampa che è stato eseguito il comando "retry"
    }

    /**
     * Metodo chiamato quando il giocatore decide di "iniziare" (avviare una nuova partita senza riprovare).
     */
    public void startAction() {
        new GameGUI(player, this); // Crea una nuova interfaccia grafica per il gioco
        startGame(); // Avvia una nuova partita
        System.out.println("Start action performed"); // Stampa che è stato eseguito il comando "start"
    }

    /**
     * Metodo per ottenere l'immagine della carta nascosta del dealer.
     *
     * @param stayButtonEnabled Flag che indica se il pulsante "stay" è abilitato.
     * @return L'immagine della carta nascosta del dealer o la carta visibile del dealer.
     */
    public Image getHiddenCardImage(boolean stayButtonEnabled) {
        if (stayButtonEnabled) {
            return new ImageIcon(getClass().getResource("Cards/BACK.png")).getImage();
        } else {
            return new ImageIcon(getClass().getResource(player.getHiddenCard().getImagePath())).getImage();
        }
    }

    /**
     * Metodo per ottenere il valore iniziale del dealer.
     *
     * @param stayButtonEnabled Flag che indica se il pulsante "stay" è abilitato.
     * @return Il valore iniziale del dealer (solo la prima carta visibile o la somma delle carte).
     */
    public String getDealerInitialValue(boolean stayButtonEnabled) {
        if (stayButtonEnabled) {
            return "Dealer: " + player.getDealer().getHand().get(1).getValue();
        } else {
            return "Dealer: " + player.getDealer().getSum();
        }
    }

    /**
     * Metodo per ottenere le somme dei punteggi dei giocatori e del dealer.
     *
     * @return Una lista di stringhe con i punteggi dei giocatori e del dealer.
     */
    public List<String> getPlayerSums() {
        List<String> playerSums = new ArrayList<>();
        playerSums.add(GameMenu.playerName + ": " + player.getPlayer1().getSum());
        playerSums.add("Dealer: " + player.getDealer().getSum());
        return playerSums;
    }

    /**
     * Metodo per ottenere il messaggio del vincitore.
     *
     * @return Il messaggio che indica il risultato del gioco (chi ha vinto).
     */
    

    /**
     * Metodo per ottenere i dati delle carte del dealer per il rendering grafico.
     *
     * @param stayButtonEnabled Flag che indica se il pulsante "stay" è abilitato.
     * @param startX Posizione iniziale X per il rendering delle carte.
     * @param startY Posizione iniziale Y per il rendering delle carte.
     * @return Una lista di oggetti CardImageData che contengono le informazioni per ogni carta del dealer.
     */
    public List<CardImageData> getDealerCardsData(boolean stayButtonEnabled, int startX, int startY) {
        List<CardImageData> cardsData = new ArrayList<>();
        Hand dealer = player.getDealer();
        // Itera sulle carte del dealer, inizia da 1 per saltare la carta nascosta
        for (int i = 1; i < dealer.getHand().size(); i++) {
            Card card = dealer.getHand().get(i);
            Image cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
            int x = startX + (120 - 80) * (i - 1);
            int y = startY;
            cardsData.add(new CardImageData(cardImage, x, y, 120, 170));
        }
        return cardsData;
    }

    /**
     * Metodo per ottenere i dati delle carte di un giocatore per il rendering grafico.
     *
     * @param player Mano del giocatore di cui ottenere i dati delle carte.
     * @param startX Posizione iniziale X per il rendering delle carte.
     * @param startY Posizione iniziale Y per il rendering delle carte.
     * @param hideFirst Flag che indica se la prima carta del giocatore deve essere nascosta.
     * @return Una lista di oggetti CardImageData che contengono le informazioni per ogni carta del giocatore.
     */
    public List<CardImageData> getPlayerCardsData(Hand player, int startX, int startY, boolean hideFirst) {
        List<CardImageData> cardsData = new ArrayList<>();
        // Itera sulle carte del giocatore
        for (int i = 0; i < player.getHand().size(); i++) {
            Card card = player.getHand().get(i);
            Image cardImage;
            // Se hideFirst è true e siamo alla prima carta, uso l'immagine della carta nascosta
            if (hideFirst && i == 0) {
                cardImage = new ImageIcon(getClass().getResource("Cards/BACK.png")).getImage();
            } else {
                cardImage = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
            }
            int x = startX + (120 - 80) * i;
            int y = startY;
            cardsData.add(new CardImageData(cardImage, x, y, 120, 170));
        }
        return cardsData;
    }

    /**
     * Classe interna per rappresentare i dati di una carta per il rendering grafico.
     */
    public static class CardImageData {
        private Image image;
        private int x;
        private int y;
        private int width;
        private int height;

        /**
         * Costruttore della classe CardImageData.
         *
         * @param image Immagine della carta.
         * @param x Posizione X per il rendering dell'immagine.
         * @param y Posizione Y per il rendering dell'immagine.
         * @param width Larghezza dell'immagine della carta.
         * @param height Altezza dell'immagine della carta.
         */
        public CardImageData(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /**
         * Metodo per ottenere l'immagine della carta.
         *
         * @return L'immagine della carta.
         */
        public Image getImage() {
            return image;
        }

        /**
         * Metodo per ottenere la posizione X dell'immagine della carta.
         *
         * @return La posizione X dell'immagine della carta.
         */
        public int getX() {
            return x;
        }

        /**
         * Metodo per ottenere la posizione Y dell'immagine della carta.
         *
         * @return La posizione Y dell'immagine della carta.
         */
        public int getY() {
            return y;
        }

        /**
         * Metodo per ottenere la larghezza dell'immagine della carta.
         *
         * @return La larghezza dell'immagine della carta.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Metodo per ottenere l'altezza dell'immagine della carta.
         *
         * @return L'altezza dell'immagine della carta.
         */
        public int getHeight() {
            return height;
        }
    }

}
