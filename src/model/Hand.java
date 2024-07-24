package model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> hand; // lista delle carte nella mano

    /**
     * Costruttore della classe Hand. Inizializza una nuova mano vuota.
     */
    public Hand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Aggiunge una carta alla mano.
     *
     * @param card la carta da aggiungere alla mano
     */
    public void addToHand(Card card) {
        hand.add(card);
    }

    /**
     * Restituisce la somma dei valori delle carte nella mano considerando la logica degli assi.
     *
     * @return la somma dei valori delle carte nella mano
     */
    public int getSum() {
        int sum = hand.stream().mapToInt(Card::getValue).sum();
        long aceCount = hand.stream().filter(Card::isAce).count();

        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }

        return sum;
    }

    /**
     * Restituisce la lista delle carte nella mano.
     *
     * @return la lista delle carte nella mano
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Restituisce il numero di Assi presenti nella mano.
     *
     * @return il numero di Assi presenti nella mano
     */
    public int getAceCount() {
        return (int) hand.stream().filter(Card::isAce).count();
    }
}
