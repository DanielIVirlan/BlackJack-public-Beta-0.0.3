package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import controller.Controller;
import model.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe GameMenu che gestisce il menu principale del gioco Blackjack.
 */
public class GameMenu extends JFrame {
public static Clip cardClip;
public static Clip backgroundClip;

    public static String avatarFileName;
    public static String playerName;

    private JPanel mainPanel;
    private JLabel menuPanel;
    private JPanel statsPanel;

    private JButton startButton;
    private JButton statisticsButton;
    private JButton exitButton;
    private JButton backStatsButton;

    private JLabel countLabel;
    private JLabel levelLabel;

    private JProgressBar progressBar;
    private JToggleButton musicToggle;

    
    private static JLabel avatarLabel;

    private Controller controller;
    private static final String FILE_PATH = "users.csv";

    /**
     * Costruttore della classe GameMenu.
     *
     * @param controller Oggetto Controller che gestisce la logica del gioco.
     */
    public GameMenu(Controller controller) {
        this.controller = controller;
        playSound("src/resources/music.wav"); // Avvia la riproduzione della musica di sottofondo

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initializeUI(); // Inizializza l'interfaccia grafica del menu
    }

    /**
     * Metodo per inizializzare l'interfaccia grafica del menu principale.
     */
    public void initializeUI() {
        setTitle("BlackJack"); // Imposta il titolo della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Imposta l'operazione di chiusura
        setSize(1000, 800); // Imposta le dimensioni della finestra
        setResizable(false); // Impedisce il ridimensionamento della finestra

        mainPanel = new JPanel(new CardLayout()); // Crea il pannello principale con layout a card
        add(mainPanel); // Aggiunge il pannello principale alla finestra

        setupMenuPanel(); // Imposta il pannello del menu
        setupStatsPanel(); // Imposta il pannello delle statistiche

        mainPanel.add(menuPanel, "Menu"); // Aggiunge il pannello del menu al pannello principale
        mainPanel.add(statsPanel, "Stats"); // Aggiunge il pannello delle statistiche al pannello principale

        setLocationRelativeTo(null); // Centra la finestra sullo schermo
        setVisible(true); // Rende visibile la finestra
    }

    /**
     * Metodo per impostare il pannello del menu principale.
     */
    public void setupMenuPanel() {
        ImageIcon img = new ImageIcon("src/resources/sfondoMenu.png"); // Carica l'immagine di sfondo
        img = resizeImageIcon(img, 1000, 800); // Ridimensiona l'immagine allargandola

        menuPanel = new JLabel(img); // Crea un JLabel con l'immagine di sfondo
        menuPanel.setLayout(new GridBagLayout()); // Imposta il layout del pannello come gridbaglayout

        GridBagConstraints gbc = new GridBagConstraints(); // Crea un oggetto GridBagConstraints per posizionare i componenti
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 0, 5, 0); // Imposta il margine intorno ai componenti

        startButton = createButton("START GAME", Color.GREEN); // Crea il pulsante "START GAME" con sfondo verde
        statisticsButton = createButton("STATISTICS", Color.YELLOW); // Crea il pulsante "STATISTICS" con sfondo giallo
        exitButton = createButton("EXIT", Color.RED); // Crea il pulsante "EXIT" con sfondo rosso
        musicToggle = createToggleButton("MUSIC OFF", Color.BLUE); // Crea il toggle button per la musica

        addToPanel(menuPanel, startButton, gbc); // Aggiunge il pulsante "START GAME" al pannello del menu
        gbc.gridy++;
        addToPanel(menuPanel, statisticsButton, gbc); // Aggiunge il pulsante "STATISTICS" al pannello del menu
        gbc.gridy++;
        addToPanel(menuPanel, musicToggle, gbc); // Aggiunge il toggle button della musica al pannello del menu
        gbc.gridy++;
        addToPanel(menuPanel, exitButton, gbc); // Aggiunge il pulsante "EXIT" al pannello del menu
    }

    /**
     * Metodo per aggiungere un componente al pannello con le impostazioni di GridBagConstraints.
     *
     * @param panel     Pannello a cui aggiungere il componente.
     * @param component Componente da aggiungere.
     * @param gbc       Oggetto GridBagConstraints per il posizionamento.
     */
    public void addToPanel(JLabel panel, Component component, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.CENTER; // Imposta l'allineamento del componente al centro
        gbc.fill = GridBagConstraints.HORIZONTAL; // Imposta la dimensione orizzontale del componente
        panel.add(component, gbc); // Aggiunge il componente al pannello con le impostazioni GridBagConstraints
    }

    /**
     * Metodo per impostare il pannello delle statistiche.
     */
    public void setupStatsPanel() {
        statsPanel = new JPanel(); // Crea un nuovo pannello per le statistiche
        statsPanel.setLayout(new BorderLayout()); // Imposta il layout del pannello come borderlayout
        statsPanel.setBackground(new Color(35, 101, 51)); // Imposta il colore di sfondo del pannello

        JPanel countPanel = new JPanel(); // Crea un pannello per il conteggio
        countPanel.setOpaque(false); // Imposta la trasparenza del pannello
        countLabel = new JLabel(); // Crea una JLabel per il conteggio
        countLabel.setFont(new Font("Arial", Font.PLAIN, 25)); // Imposta il font del testo
        countLabel.setForeground(Color.WHITE); // Imposta il colore del testo a bianco
        countPanel.add(countLabel); // Aggiunge la JLabel al pannello del conteggio

        avatarLabel = new JLabel(); // Crea una JLabel per l'avatar
        countPanel.add(avatarLabel); // Aggiunge la JLabel dell'avatar al pannello del conteggio

        int level = Player.getGamesWon() / 10; // Calcola il livello basato sui giochi vinti
        int progress = (Player.getGamesWon() % 10) * 10; // Calcola il progresso del livello

        levelLabel = new JLabel("Livello: " + level); // Crea una JLabel per il livello
        levelLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Imposta il font del testo in grassetto
        levelLabel.setForeground(Color.WHITE); // Imposta il colore del testo a bianco
        countPanel.add(levelLabel); // Aggiunge la JLabel del livello al pannello del conteggio

        progressBar = new JProgressBar(); // Crea una JProgressBar per il livello
        progressBar.setMinimum(0); // Imposta il valore minimo della barra di progresso
        progressBar.setMaximum(100); // Imposta il valore massimo della barra di progresso
        progressBar.setValue(progress); // Imposta il valore corrente della barra di progresso
        progressBar.setStringPainted(true); // Abilita il testo sulla barra di progresso
        progressBar.setForeground(Color.RED); // Imposta il colore di riempimento della barra di progresso a rosso
        progressBar.setPreferredSize(new Dimension(200, 30)); // Imposta le dimensioni preferite della barra di progresso
        countPanel.add(progressBar); // Aggiunge la barra di progresso al pannello del conteggio

        statsPanel.add(countPanel, BorderLayout.CENTER); // Aggiunge il pannello del conteggio al pannello delle statistiche

        JPanel southPanel = new JPanel(); // Crea un pannello per i pulsanti nella parte inferiore
        southPanel.setOpaque(false); // Imposta la trasparenza del pannello

        backStatsButton = new JButton("BACK"); // Crea il pulsante "BACK"
        backStatsButton.setForeground(Color.RED); // Imposta il colore del testo a rosso
        backStatsButton.setFocusable(false); // Disabilita il focus sul pulsante
        backStatsButton.setFont(new Font("Typewriter", Font.ITALIC, 14)); // Imposta il font del testo del pulsante
        southPanel.add(backStatsButton); // Aggiunge il pulsante "BACK" al pannello inferiore delle statistiche
        statsPanel.add(southPanel, BorderLayout.SOUTH); // Aggiunge il pannello inferiore al pannello delle statistiche

        // Gestore di evento per il pulsante "BACK"
        backStatsButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic del pulsante
            showMenuPanel(); // Mostra nuovamente il pannello del menu principale
        });

        // Gestore di evento per il pulsante "EXIT"
        exitButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic del pulsante
            System.exit(0); // Chiude l'applicazione
        });

        // Gestore di evento per il pulsante "START GAME"
        startButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic del pulsante
            boolean loggedIn = false;
            while (!loggedIn) {
                String[] options = {"Log in", "Sign up", "Cancel"};
                int choice = JOptionPane.showOptionDialog(null, "Select an option", "User Authentication",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                switch (choice) {
                    case 0: // Log in
                        String[] loginInfo = promptUserInfo("Log in");
                        if (loginInfo != null) {
                            if (authenticate(loginInfo[0], loginInfo[1])) {
                                JOptionPane.showMessageDialog(null, "Login successful!");
                                playerName = loginInfo[0];
                                loggedIn = true;
                            } else {
                                showErrorDialog("Error: Invalid username or password.");
                            }
                            } else {
                            JOptionPane.showMessageDialog(null, "Login canceled.");
                            loggedIn = true; // Exit loop if login is canceled
                        }
                        break;

                    case 1: // Register
                        String[] registerInfo = promptUserInfo("Register");
                        if (registerInfo != null) {
                            register(registerInfo[0], registerInfo[1]);
                            showConfirmationDialog("Sign up completed.");
                                } else {
                            JOptionPane.showMessageDialog(null, "Registration canceled.");
                        }
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Operation canceled.");
                        return;
                }
            }
    
            Player.resetGame(); // Reimposta il gioco del giocatore
        
            selectAvatar(); // Mostra la finestra di selezione dell'avatar
            dispose(); // Chiude la finestra corrente
            controller.startAction(); // Avvia l'azione di inizio del gioco nel controller
        });

        // Gestore di evento per il pulsante "STATISTICS"
        statisticsButton.addActionListener(e -> {
            playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic del pulsante
            showStatsPanel(); // Mostra il pannello delle statistiche
        });

        // Gestore di evento per il toggle button della musica
        musicToggle.addActionListener(e -> {
            JToggleButton musicToggle = (JToggleButton) e.getSource(); // Ottiene il toggle button sorgente
            playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic del pulsante

            if (musicToggle.isSelected()) { // Se la musica è attualmente selezionata
                stopMusic(); // Ferma la riproduzione della musica
            } else { // Altrimenti
                playBackgroundMusic("src/resources/music.wav"); // Avvia la riproduzione della musica di sottofondo
            }
        });
    }

    /**
     * Metodo per creare un JButton personalizzato con testo e colore specificati.
     *
     * @param text  Testo da visualizzare nel pulsante.
     * @param color Colore del testo del pulsante.
     * @return JButton personalizzato creato.
     */
    public JButton createButton(String text, Color color) {
        JButton button = new JButton(text); // Crea un nuovo JButton con il testo specificato
        button.setFocusable(false); // Disabilita il focus sul pulsante
        button.setPreferredSize(new Dimension(200, 35)); // Imposta le dimensioni preferite del pulsante
        button.setBackground(new Color(91, 91, 91)); // Imposta il colore di sfondo del pulsante
        button.setFont(new Font("Typewriter", Font.ITALIC, 14)); // Imposta il font del testo del pulsante
        button.setForeground(color); // Imposta il colore del testo del pulsante
        return button; // Restituisce il pulsante personalizzato creato
    }

    /**
     * Metodo per creare un JToggleButton personalizzato con testo e colore specificati.
     *
     * @param text  Testo da visualizzare nel toggle button.
     * @param color Colore del testo del toggle button.
     * @return JToggleButton personalizzato creato.
     */
    public JToggleButton createToggleButton(String text, Color color) {
        JToggleButton toggleButton = new JToggleButton(text); // Crea un nuovo JToggleButton con il testo specificato
        toggleButton.setFocusable(false); // Disabilita il focus sul toggle button
        toggleButton.setPreferredSize(new Dimension(200, 35)); // Imposta le dimensioni preferite del toggle button
        toggleButton.setBackground(new Color(91, 91, 91)); // Imposta il colore di sfondo del toggle button
        toggleButton.setFont(new Font("Typewriter", Font.ITALIC, 14)); // Imposta il font del testo del toggle button
        toggleButton.setForeground(color); // Imposta il colore del testo del toggle button
        return toggleButton; // Restituisce il toggle button personalizzato creato
    }

    /**
     * Metodo per ridimensionare un ImageIcon specificato alle dimensioni specificate.
     *
     * @param icon   ImageIcon da ridimensionare.
     * @param width  Larghezza desiderata dell'immagine ridimensionata.
     * @param height Altezza desiderata dell'immagine ridimensionata.
     * @return ImageIcon ridimensionato.
     */
    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage(); // Ottiene l'immagine dall'ImageIcon
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Ridimensiona l'immagine in modo uniforme
        return new ImageIcon(newImage); // Restituisce un nuovo ImageIcon con l'immagine ridimensionata
    }

    /**
     * Metodo per mostrare il pannello del menu principale.
     */
    public void showMenuPanel() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout(); // Ottiene il layout a card del pannello principale
        cardLayout.show(mainPanel, "Menu"); // Mostra il pannello del menu
    }

    /**
     * Metodo per mostrare il pannello delle statistiche.
     */
    public void showStatsPanel() {
        updateAvatarLabel(); // Aggiorna l'etichetta dell'avatar
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout(); // Ottiene il layout a card del pannello principale
        cardLayout.show(mainPanel, "Stats"); // Mostra il pannello delle statistiche

        // Aggiorna il testo delle statistiche visualizzato
        countLabel.setText("<html><p>" + playerName + " has played: " + Player.getGamesPlayed() + " games</p>"
                + "<p style='margin-top: 20px;'>Games won: " + Player.getGamesWon() + "<p>Games lost: " + Player.getGamesLost() + "</html>");

        int level = Player.getGamesWon() / 10; // Calcola il livello basato sui giochi vinti
        int progress = (Player.getGamesWon() % 10) * 10; // Calcola il progresso del livello
        progressBar.setValue(progress); // Imposta il valore della barra di progresso
        levelLabel.setText("Livello: " + level); // Imposta il testo del livello
    }

    /**
     * Metodo per la selezione dell'avatar del giocatore.
     */
    public static void selectAvatar() {
        try {
            // Imposta il look and feel di Nimbus
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel panel = new JPanel(new GridLayout(2, 4, 5, 5)); // Crea un nuovo pannello con GridLayout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Imposta un bordo vuoto intorno al pannello

        // Crea i pulsanti degli avatar
        
        addAvatarButton(panel, "src/resources/av2.png", "1");
        addAvatarButton(panel, "src/resources/av1.png", "2");
        addAvatarButton(panel, "src/resources/av3.png", "3");
        addAvatarButton(panel, "src/resources/av4.png", "4");
        addAvatarButton(panel, "src/resources/av5.png", "5");
        addAvatarButton(panel, "src/resources/av6.png", "6");
        addAvatarButton(panel, "src/resources/av7.png", "7");
        addAvatarButton(panel, "src/resources/av8.png", "8");

        JDialog dialog = new JDialog((Frame) null, "Select Avatar", true); // Crea un nuovo dialogo per la selezione dell'avatar
        dialog.setContentPane(panel); // Imposta il contenuto del dialogo al pannello creato
        dialog.setSize(600, 400); // Imposta le dimensioni del dialogo
        dialog.setLocationRelativeTo(null); // Posiziona il dialogo al centro dello schermo
        dialog.setVisible(true); // Rende visibile il dialogo
    }

    private static void addAvatarButton(JPanel panel, String imagePath, String buttonText) {
        
        
        ImageIcon icon = new ImageIcon(imagePath); // Carica l'icona dell'avatar
        icon = resizeImageIcon(icon, 100, 120); // Ridimensiona l'icona dell'avatar
        JButton button = new JButton(buttonText, icon); // Crea il pulsante con l'icona dell'avatar
        button.setBackground(new Color(91, 91, 91)); // Imposta il colore di sfondo del pulsante
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // Imposta la posizione del testo verticale
        button.setHorizontalTextPosition(SwingConstants.CENTER); // Imposta la posizione del testo orizzontale
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound("src/resources/click.wav"); // Riproduce il suono del clic del pulsante
                avatarFileName = imagePath; // Imposta il nome file dell'avatar selezionato
                updateAvatarLabel(); // Aggiorna l'etichetta dell'avatar
                ((JButton) e.getSource()).getRootPane().getParent().setVisible(false); // Chiude il dialogo delle opzioni
            }
        });
        panel.add(button); // Aggiunge il pulsante al pannello
    }

    /**
     * Metodo per aggiornare l'etichetta dell'avatar con l'immagine dell'avatar selezionato.
     */
    public static void updateAvatarLabel() {
        if (avatarFileName != null) { // Se è stato selezionato un file avatar
            ImageIcon avatarIcon = new ImageIcon(avatarFileName); // Crea un nuovo ImageIcon con il file avatar
            avatarLabel.setIcon(avatarIcon); // Imposta l'icona dell'avatar nell'etichetta dell'avatar
        }
    }

    /**
     * Metodo per riprodurre un suono da un file audio specificato.
     *
     * @param audioFilePath Percorso del file audio da riprodurre.
     */
    public static void playSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath); // Crea un nuovo oggetto File con il percorso specificato
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile); // Ottiene l'input stream audio dal file
            Clip clip = AudioSystem.getClip(); // Ottiene il clip audio dal sistema
            clip.open(audioInputStream); // Apre il clip audio con l'input stream audio ottenuto
            clip.start(); // Avvia la riproduzione del clip audio
            if (audioFilePath.equals("src/resources/music.wav")) { // Se il percorso del file è quello della musica di sottofondo
                backgroundClip = clip; // Imposta il clip della musica di sottofondo
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Imposta la riproduzione continua del clip di sottofondo
            }
        } catch (Exception ex) { // Gestione delle eccezioni
            ex.printStackTrace(); // Stampa la traccia dello stack dell'eccezione
        }
    }

    /**
     * Metodo per riprodurre un suono di clic del pulsante.
     *
     * @param audioFilePath Percorso del file audio del suono del clic del pulsante.
     */
    public static void playButtonClickSound(String audioFilePath) {
        playSound(audioFilePath); // Riproduce il suono specificato del clic del pulsante utilizzando il metodo playSound
    }

    /**
     * Metodo per avviare la riproduzione della musica di sottofondo.
     *
     * @param audioFilePath Percorso del file audio della musica di sottofondo da riprodurre.
     */
    public void playBackgroundMusic(String audioFilePath) {
        if (backgroundClip == null || !backgroundClip.isRunning()) { // Se il clip della musica di sottofondo è nullo o non è in esecuzione
            playSound(audioFilePath); // Avvia la riproduzione della musica di sottofondo utilizzando il metodo playSound
        }
    }

    /**
     * Metodo per interrompere la riproduzione della musica di sottofondo.
     */
    public static void stopMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) { // Se il clip della musica di sottofondo non è nullo e è in esecuzione
            backgroundClip.stop(); // Interrompe la riproduzione del clip di sottofondo
            backgroundClip.close(); // Chiude il clip di sottofondo
            backgroundClip = null; // Resetta il clip
        }
    }

    /**
     * Metodo per richiedere all'utente di inserire il proprio nome.
     *
     * @return Il nome inserito dall'utente.
     */
    public String promptPlayerName() {
        return JOptionPane.showInputDialog("Enter your in-game name:"); // Visualizza un dialogo di input per richiedere il nome del giocatore
    }

    /**
     * Metodo per riprodurre un suono di carta.
     *
     * @param audioFilePath Percorso del file audio del suono della carta da riprodurre.
     */
    public static void playCardSound(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath); // Crea un nuovo oggetto File con il percorso specificato
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile); // Ottiene l'input stream audio dal file
            cardClip = AudioSystem.getClip(); // Ottiene il clip audio dal sistema
            cardClip.open(audioInputStream); // Apre il clip audio con l'input stream audio ottenuto
            cardClip.start(); // Avvia la riproduzione del clip audio
        } catch (Exception ex) { // Gestione delle eccezioni
            ex.printStackTrace(); // Stampa la traccia dello stack dell'eccezione
        }
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Imposta il look and feel Nimbus
        } catch (Exception e) { // Gestione delle eccezioni
            e.printStackTrace(); // Stampa la traccia dello stack dell'eccezione
        }
    }

    public static String[] promptUserInfo(String action) {

        
        JTextField nameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter your name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15)); // a spacer
        panel.add(new JLabel("Enter your password:"));
        panel.add(passwordField);

        String[] options = {action, "Cancel"};
        
        int option = JOptionPane.showOptionDialog(null, panel, "User Info",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        
        if (option == 0) { // User clicked Log in or Register
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            return new String[]{name, password};
        } else {
            return null;
        }
    }

    public static boolean authenticate(String name, String password) {
        List<String[]> users = readUsers();
        for (String[] user : users) {
            if (user[0].equals(name) && user[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void register(String name, String password) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(name + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readUsers() {
        List<String[]> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showConfirmationDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    
}
