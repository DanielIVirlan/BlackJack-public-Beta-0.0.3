package view;

import controller.Controller;
import model.Player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe GameGUI che gestisce l'interfaccia grafica del gioco Blackjack.
 */
@SuppressWarnings({ "deprecation", "serial" })
public class GameGUI extends JFrame implements Observer {

    private static Clip cardClip;

    private int cardWidth = 120;
    private int cardHeight = 170;

    private JPanel gamePanel, buttonPanel;
    private JButton hitButton, stayButton, retryButton, backButton;

    private Controller controller;
    private Player player;

    /**
     * Costruttore della classe GameGUI.
     *
     * @param player     Oggetto Player che rappresenta il giocatore.
     * @param controller Oggetto Controller che gestisce la logica del gioco.
     */
    public GameGUI(Player player, Controller controller) {
        this.controller = controller;
        this.player = player;
        initialize(); // Inizializza l'interfaccia grafica
        setupButtons(); // Imposta i pulsanti e i loro gestori di eventi
        this.player.addObserver(this); // Aggiunge se stesso come osservatore del giocatore
    }

    /**
     * Metodo per inizializzare l'interfaccia grafica del gioco.
     */
    public void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Blackjack Game"); // Imposta il titolo della finestra
        setSize(1000, 800); // Imposta le dimensioni della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Imposta l'operazione di chiusura
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setLayout(new BorderLayout()); // Imposta il layout della finestra
        setBackground(new Color(35, 101, 51)); // Imposta il colore di sfondo
        setResizable(false); // Impedisce il ridimensionamento della finestra

        // Pannello principale del gioco
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g); // Disegna il contenuto del gioco nel pannello
            }
        };
        gamePanel.setLayout(new BorderLayout()); // Imposta il layout del pannello
        gamePanel.setBackground(new Color(35, 101, 51)); // Imposta il colore di sfondo del pannello

        buttonPanel = new JPanel(); // Pannello per i pulsanti
        buttonPanel.setBackground(new Color(35, 101, 51)); // Imposta il colore di sfondo del pannello dei pulsanti

        add(gamePanel, BorderLayout.CENTER); // Aggiunge il pannello del gioco al centro della finestra
        add(buttonPanel, BorderLayout.SOUTH); // Aggiunge il pannello dei pulsanti nella parte inferiore della finestra

        setVisible(true); // Rende visibile la finestra
    }

    /**
     * Metodo per creare un pulsante con le impostazioni di base.
     *
     * @param text Testo da visualizzare nel pulsante.
     * @return Il JButton creato.
     */
    public JButton createButton(String text) {
        JButton button = new JButton(text); // Crea un nuovo pulsante con il testo specificato
        button.setFocusable(false); // Disabilita il focus sul pulsante
        button.setFont(new Font("Typewriter", Font.ITALIC, 14)); // Imposta il font del testo del pulsante
        button.setBackground(new Color(91, 91, 91)); // Imposta il colore di sfondo del pulsante
        button.setForeground(Color.yellow); // Imposta il colore del testo del pulsante
        button.setPreferredSize(new Dimension(200, 35)); // Imposta le dimensioni preferite del pulsante

        return button;
    }

    /**
     * Metodo per impostare i pulsanti e i relativi gestori di eventi.
     */
    public void setupButtons() {
        hitButton = createButton("HIT"); // Crea il pulsante "HIT"
        stayButton = createButton("STAY"); // Crea il pulsante "STAY"
        retryButton = createButton("RETRY"); // Crea il pulsante "RETRY"
        backButton = createButton("BACK"); // Crea il pulsante "BACK"

        buttonPanel.add(hitButton); // Aggiunge il pulsante "HIT" al pannello dei pulsanti
        buttonPanel.add(stayButton); // Aggiunge il pulsante "STAY" al pannello dei pulsanti
        buttonPanel.add(retryButton); // Aggiunge il pulsante "RETRY" al pannello dei pulsanti
        buttonPanel.add(backButton); // Aggiunge il pulsante "BACK" al pannello dei pulsanti

        // Gestore di evento per il pulsante "BACK"
        backButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic
            dispose(); // Chiude la finestra corrente
            new GameMenu(controller); // Apre il menu principale del gioco
        });

        // Gestore di evento per il pulsante "HIT"
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic
                controller.hitAction(); // Esegue l'azione "hit" nel controller
            }
        });

        // Gestore di evento per il pulsante "STAY"
        stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic
                playCardSound("src/resources/flip.wav"); // Riproduce il suono della carta
                stayButton.setEnabled(false); // Disabilita il pulsante "STAY"
                stayButton.setForeground(Color.RED); // Imposta il colore del testo a rosso
                hitButton.setEnabled(false); // Disabilita il pulsante "HIT"
                hitButton.setForeground(Color.RED); // Imposta il colore del testo a rosso
                controller.stayAction(); // Esegue l'azione "stay" nel controller
            }
        });

        // Gestore di evento per il pulsante "RETRY"
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic
                playCardSound("src/resources/flip.wav"); // Riproduce il suono della carta
                dispose(); // Chiude la finestra corrente
                controller.retryAction(); // Esegue l'azione "retry" nel controller
            }
        });
    }

    /**
     * Metodo per disegnare le carte nel pannello di gioco.
     *
     * @param g     Oggetto Graphics per disegnare le carte.
     * @param cards Lista di oggetti CardImageData che rappresentano le carte da disegnare.
     */
    public void drawCards(Graphics g, List<Controller.CardImageData> cards) {
        for (Controller.CardImageData cardData : cards) {
            g.drawImage(cardData.getImage(), cardData.getX(), cardData.getY(), cardData.getWidth(), cardData.getHeight(), null);
        }
    }

    /**
     * Metodo per disegnare il contenuto del gioco nel pannello di gioco.
     *
     * @param g Oggetto Graphics per disegnare il contenuto.
     */
    public void drawGame(Graphics g) {
        try {
            Image hiddenCardImage = controller.getHiddenCardImage(stayButton.isEnabled()); // Ottiene l'immagine della carta nascosta o visibile del dealer
            String dealerInitialValue = controller.getDealerInitialValue(stayButton.isEnabled()); // Ottiene il valore iniziale del dealer
            List<String> playerSums = controller.getPlayerSums(); // Ottiene i punteggi dei giocatori e del dealer
            List<Controller.CardImageData> dealerCards = controller.getDealerCardsData(stayButton.isEnabled(), 100 + cardWidth - 60, 40); // Ottiene i dati delle carte del dealer
            List<Controller.CardImageData> player1Cards = controller.getPlayerCardsData(player.getPlayer1(), 120, 500, false); // Ottiene i dati delle carte del giocatore
            List<Controller.CardImageData> bot1Cards = controller.getPlayerCardsData(player.getBot1(), 670, 40, false); // Ottiene i dati delle carte del bot1
            List<Controller.CardImageData> bot2Cards = controller.getPlayerCardsData(player.getBot2(), 670, 500, false); // Ottiene i dati delle carte del bot2

            g.setFont(new Font("Arial", Font.PLAIN, 20)); // Imposta il font per il testo
            g.setColor(Color.black); // Imposta il colore del testo

            // Disegna l'immagine della carta nascosta del dealer
            g.drawImage(hiddenCardImage, 120, 40, cardWidth, cardHeight, null);
            // Disegna i punteggi dei giocatori e del dealer
            g.drawString(playerSums.get(0), 120, 485);
            g.drawString(playerSums.get(1), 670, 245);
            g.drawString(playerSums.get(2), 670, 485);
            g.drawString(dealerInitialValue, 120, 245);

            // Disegna le carte del dealer, del giocatore e dei bot
            drawCards(g, dealerCards);
            drawCards(g, player1Cards);
            drawCards(g, bot1Cards);
            drawCards(g, bot2Cards);

            // Se il gioco è terminato (pulsante "STAY" disabilitato), disegna il messaggio del vincitore
            if (!stayButton.isEnabled()) {
                String winnerMessage = controller.getWinnerMessage();
                g.drawString(winnerMessage, 170, 350);
                g.drawString(playerSums.get(1), 670, 245);
                g.drawString(playerSums.get(2), 670, 485);
                g.drawString(playerSums.get(3), 120, 245);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo dell'interfaccia Observer per aggiornare l'interfaccia grafica in base agli eventi osservati.
     *
     * @param o   Oggetto osservabile che notifica l'evento (in questo caso, Player).
     * @param arg Argomento opzionale passato dall'oggetto osservabile.
     */
    public void update(Observable o, Object arg) {
        if (o instanceof Player) {
            Player player = (Player) o;
            // Se il giocatore supera 21 o raggiunge 21, disabilita i pulsanti "HIT" e "STAY"
            if (player.getPlayer1().getSum() > 21 || player.getPlayer1().getSum() == 21) {
                hitButton.setEnabled(false);
                hitButton.setForeground(Color.RED);
                stayButton.setEnabled(false);
                stayButton.setForeground(Color.RED);
            }
            repaint(); // Ridisegna l'interfaccia grafica
        }
    }

    /**
     * Metodo statico per riprodurre un suono quando viene effettuata un'azione con le carte.
     *
     * @param audioFilePath Percorso del file audio da riprodurre.
     */
    public static void playCardSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            cardClip = AudioSystem.getClip();
            cardClip.open(audioInputStream);
            cardClip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per riprodurre un suono quando viene cliccato un pulsante.
     *
     * @param audioFilePath Percorso del file audio da riprodurre.
     */
    public void playButtonClickSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
