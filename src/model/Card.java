package model;

import java.util.stream.IntStream;

public class Card {
    private final String suit; // seme della carta
    private final String rank; // valore della carta
    private final int value;   // valore numerico della carta

    /**
     * Costruttore della classe Card.
     *
     * @param suit  il seme della carta
     * @param rank  il valore della carta
     * @param value il valore numerico della carta
     */
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    /**
     * Restituisce il seme della carta.
     *
     * @return il seme della carta
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Restituisce il valore della carta.
     *
     * @return il valore della carta
     */
    public String getRank() {
        return rank;
    }

    /**
     * Restituisce il valore numerico della carta.
     *
     * @return il valore numerico della carta
     */
    public int getValue() {
        return value;
    }

    /**
     * Restituisce una rappresentazione sotto forma di stringa della carta.
     *
     * @return una stringa nel formato "valore-seme", dove valore è il valore della carta
     *         e seme è il seme della carta
     */
    @Override
    public String toString() {
        return rank + "-" + suit;
    }

    /**
     * Questa funzione utilizza uno stream di interi per iterare attraverso i possibili valori dell'Asso (10, 10, 10, 11)
     * e ritorna il primo valore che corrisponde al seme della carta. Se non viene trovato alcun valore corrispondente,
     * viene restituito il valore numerico della carta.
     *
     * @return il valore della carta Asso
     */
    public int getValueOfAce() {
        return IntStream.of(10, 10, 10, 11)
                .filter(val -> rank.equals("A") || rank.equals("J") || rank.equals("Q") || rank.equals("K"))
                .findFirst()
                .orElse(Integer.parseInt(rank));
    }


    /**
     * Verifica se la carta è un Asso.
     *
     * @return true se la carta è un Asso, false altrimenti
     */
    public boolean isAce() {
        return rank.equals("A");
    }

    /**
     * Restituisce il percorso dell'immagine della carta.
     *
     * @return il percorso dell'immagine della carta, nel formato "cards/valore-seme.png"
     */
    public String getImagePath() {
        return "cards/" + toString() + ".png";
    }
}
