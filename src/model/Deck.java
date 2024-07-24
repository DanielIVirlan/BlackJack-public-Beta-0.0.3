package model;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Deck {
    private Stack<Card> cards; // mazzo di carte rappresentato come uno stack

    /**
     * Costruttore della classe Deck. Costruisce un mazzo di carte mescolato.
     *
     * @param numDecks il numero di mazzi da usare per creare il mazzo
     */
    public Deck(int numDecks) {
        cards = new Stack<>();
        buildDeck(numDecks); // costruisce il mazzo con il numero specificato di mazzi
        Collections.shuffle(cards); // mescola il mazzo di carte
    }

    /**
     * Costruisce il mazzo di carte utilizzando il numero specificato di mazzi.
     *
     * @param numDecks il numero di mazzi da usare per creare il mazzo
     */
    public void buildDeck(int numDecks) {
        // Array di semi delle carte
        String[] suits = {"H", "D", "C", "S"};
        // Array di ranghi delle carte
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        // Array di valori corrispondenti ai ranghi delle carte
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        // Genera una lista di nuove carte per il numero specificato di mazzi
        List<Card> newCards = IntStream.range(0, numDecks) // genera un range di numeri per il numero di mazzi
                .boxed() // converte gli int in Integer per operazioni di stream
                .flatMap(n -> IntStream.range(0, suits.length) // per ogni mazzo, genera carte per ogni seme
                        .boxed()
                        .flatMap(i -> IntStream.range(0, ranks.length)
                                .mapToObj(j -> new Card(suits[i], ranks[j], values[j]))
                        )
                )
                .collect(Collectors.toList()); // raccoglie tutte le carte in una lista

        cards.addAll(newCards); // aggiunge tutte le nuove carte al mazzo
    }

    /**
     * Pesca una carta dal mazzo.
     *
     * @return la carta pescata dal mazzo
     */
    public Card drawCard() {
        return cards.pop(); // estrae e restituisce la carta in cima allo stack (mazzo)
    }
}
