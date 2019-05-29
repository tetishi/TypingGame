package com.company;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

// Loads in the "HardDictionary" from the "TypingGame" file when difficulty setting Hard is selected
// Reads in text file "hardWords.txt" into an ArrayList
class HardDictionary {
    public HardDictionary () {}

    public static ArrayList<String> getHardWords(String file) throws FileNotFoundException {

        Scanner input =  new Scanner(new File(file));

        ArrayList<String> words = new ArrayList<String>();
        while(input.hasNext()) {
            words.add(input.next());
        }
        input.close();
        return words;
    }
}

// Loads in the "EasyDictionary" from the "TypingGame" file when difficulty setting Easy is selected
// Reads in text file "easyWords.txt" into an ArrayList
class EasyDictionary {
    public EasyDictionary () {}

    public static ArrayList<String> getEasyWords(String file) throws FileNotFoundException {

        Scanner input =  new Scanner(new File(file));

        ArrayList<String> words = new ArrayList<String>();
        while(input.hasNext()) {
            words.add(input.next());
        }
        input.close();
        return words;
    }
}

// Loads in the "NormalDictionary" from the "TypingGame" file when difficulty setting Normal is selected
// Reads in text file "normalWords.txt" into an ArrayList
class NormalDictionary {
    public NormalDictionary() {}

    public static ArrayList<String> getWords(String file) throws FileNotFoundException {

        Scanner input =  new Scanner(new File(file));

        ArrayList<String> words = new ArrayList<String>();
        while(input.hasNext()) {
            words.add(input.next());
        }
        input.close();
        return words;
    }
}

// Used when difficulty setting Easy is selected
class Easy implements KeyListener {
    Typing typingGame;

    public Easy(Typing typingGame) {
        this.typingGame = typingGame;
    }

    public void keyPressed(KeyEvent ea) {
    }

    public void keyReleased(KeyEvent ea) {
    }

    public void keyTyped(KeyEvent ea) {
        if (!typingGame.isPlaying) {
            typingGame.easyNewGame();
        } else {
            String text = typingGame.label.getText();
            if (text.charAt(0) == ea.getKeyChar()) {
                if (text.length() > 1) {
                    typingGame.label.setForeground(Color.BLACK);
                    typingGame.label.setText(text.substring(1));
                } else {
                    typingGame.easySuccess();
                }
            } else {
                typingGame.label.setForeground(Color.RED);
            }
        }
    }
}

// Used when Normal difficulty setting is selected
class Normal implements KeyListener {
    Typing typingGame;

    public Normal(Typing typingGame) {
        this.typingGame = typingGame;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        if (!typingGame.isPlaying) {
            typingGame.normalNewGame();
        } else {
            String text = typingGame.label.getText();
            if (text.charAt(0) == e.getKeyChar()) {
                if (text.length() > 1) {
                    typingGame.label.setForeground(Color.BLACK);
                    typingGame.label.setText(text.substring(1));
                } else {
                    typingGame.normalSuccess();
                }
            } else {
                typingGame.label.setForeground(Color.RED);
            }
        }
    }
}

// Used when Hard difficulty setting is selected
class Hard implements KeyListener {
    Typing typingGame;

    public Hard(Typing typingGame) {
        this.typingGame = typingGame;
    }

    public void keyPressed(KeyEvent ha) {
    }

    public void keyReleased(KeyEvent ha) {
    }

    public void keyTyped(KeyEvent ha) {
        if (!typingGame.isPlaying) {
            typingGame.hardNewGame();
        } else {
            String text = typingGame.label.getText();
            if (text.charAt(0) == ha.getKeyChar()) {
                if (text.length() > 1) {
                    typingGame.label.setForeground(Color.BLACK);
                    typingGame.label.setText(text.substring(1));
                } else {
                    typingGame.hardSuccess();
                }
            } else {
                typingGame.label.setForeground(Color.RED);
            }
        }
    }
}

class Typing extends JFrame implements ActionListener{
    ArrayList<String> normalBank;
    ArrayList<String> easyBank;
    ArrayList<String> hardBank;
    protected JLabel label;
    private Random random;

    //private int sec = 2;

    private JLabel scoreLabel;

    // INIT_DELAY is represented in Milliseconds.
    // 2000 Milliseconds = 2 Seconds.
    private static final int INIT_DELAY = 3000;
    private static final int SPEED = 10;
    private static final int NUM_RANKING = 1;

    protected boolean isPlaying; // "Flag" to check if the game is being played; true/ false
    private int delay; // The Current Time Limit
    private int score; // Variable to keep track of User score
    private ArrayList<Integer> ranking; // Used for a future ranking system
    private long prevTime; // Variable that shows the previous time the word was displayed
    private javax.swing.Timer timer; // Variable timer used for time limit

    Typing() {}

    public Typing(String title) throws FileNotFoundException {
        super(title);

        hardBank = HardDictionary.getHardWords("/Users/tetsuroishida/IdeaProjects/finalMy/hardWords.txt");
        easyBank = EasyDictionary.getEasyWords("/Users/tetsuroishida/IdeaProjects/finalMy/easyWords.txt");
        normalBank = NormalDictionary.getWords("/Users/tetsuroishida/IdeaProjects/finalMy/words.txt");

        Object[] options = {"Hard", "Normal", "Easy"};
        int a = JOptionPane.showOptionDialog(null, "Which mode do you want to play?",
                "Welcome", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                options[2]);
        if (a == JOptionPane.YES_OPTION){
            addKeyListener(new Hard(this));
        } else if (a == JOptionPane.NO_OPTION) {
            addKeyListener(new Normal(this));
        } else if (a == JOptionPane.CANCEL_OPTION) {
            addKeyListener(new Easy(this));
        }
        else
            System.exit(0);

        setContentPane(new JLabel(new ImageIcon("/Users/tetsuroishida/IdeaProjects/finalMy/back.png")));
        setLayout(new BorderLayout());

        random = new Random(System.currentTimeMillis());
        label = new JLabel(" ");
        label.setFont(new Font(null, Font.PLAIN, 44));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        getContentPane().add(label);

        // GUI used to diplay the Score
        scoreLabel = new JLabel();
        scoreLabel.setHorizontalAlignment(JLabel.LEFT);
        getContentPane().add(scoreLabel, BorderLayout.SOUTH);

        // Used for displaying a future ranking system
        ranking = new ArrayList<Integer>(NUM_RANKING);
        for (int i = 0; i < NUM_RANKING; i++) {
            ranking.add(0);
        }

        isPlaying = false;
        timer = new javax.swing.Timer(0, this);
        timer.setRepeats(false);
        showTitle();
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JLabel getLabel() {
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPlaying) {
            return;
        }
        if (label.getText().length() > 0) {
            gameOver();
        }
    }

    // Refresh the Score when a new game begins
    private void refreshScore() {
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        scoreLabel.setText("Score: " + score + " "); //+ "Time: " + delay);
    }

    // Starts a new game when Normal difficulty is selected
    protected void normalNewGame() {
        isPlaying = true;
        score = 0;
        delay = INIT_DELAY;
        refreshScore();
        normalNewWord();
    }

    // Starts a new game when Easy difficulty is selected
    protected void easyNewGame() {
        isPlaying = true;
        score = 0;
        delay = INIT_DELAY;
        refreshScore();
        easyNewWord();
    }

    // Starts a new game when Hard difficulty is selected
    protected void hardNewGame() {
        isPlaying = true;
        score = 0;
        delay = INIT_DELAY;
        refreshScore();
        hardNewWord();
    }

    // Used to display score when the game has ended
    private void gameOver() {
        isPlaying = false;
        timer.stop();
        ranking.add(score);
        Collections.sort(ranking);
        Collections.reverse(ranking);
        ranking.remove(NUM_RANKING);
        StringBuilder sb = new StringBuilder();
        //sb.append("Your score is ");
        //sb.append(score);
        //sb.append(" points\n\n");
        String lineSeparator = "";
        for (int i = 0, m = ranking.size(); i < m; i++) {
            sb.append(lineSeparator);
            //sb.append(i + 1);
            sb.append(" Your Score is ");
            sb.append(ranking.get(i));
            sb.append(" points");
            lineSeparator = "\n";
        }
        JOptionPane.showMessageDialog(this, sb, "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        showTitle();
    }

    // Different calculations for score and "time" depending on difficulty setting
    protected void normalSuccess() {
        long time = System.currentTimeMillis();
        //score += INIT_DELAY - (time - prevTime);
        score += 1;
        delay -= SPEED;
        delay = Math.max(delay, 0);
        normalNewWord();
        refreshScore();
    }

    protected void easySuccess() {
        long time = System.currentTimeMillis();
        score += 1;
        delay -= SPEED;
        delay = Math.max(delay, 0);
        easyNewWord();
        refreshScore();
    }

    protected void hardSuccess() {
        long time = System.currentTimeMillis();
        //score += INIT_DELAY - (time - prevTime);
        score += 1;
        delay -= SPEED;
        delay = Math.max(delay, 0);
        hardNewWord();
        refreshScore();
    }

    private void normalNewWord() {
        label.setText(normalBank.get(random.nextInt(normalBank.size())));
        timer.setInitialDelay(delay);
        timer.restart();
        prevTime = System.currentTimeMillis();
    }

    private void easyNewWord() {
        label.setText(easyBank.get(random.nextInt(easyBank.size())));
        timer.setInitialDelay(delay);
        timer.restart();
        prevTime = System.currentTimeMillis();
    }

    private void hardNewWord() {
        label.setText(hardBank.get(random.nextInt(hardBank.size())));
        timer.setInitialDelay(delay);
        timer.restart();
        prevTime = System.currentTimeMillis();
    }

    // GUI display for Game Screen
    private void showTitle() {
        label.setForeground(Color.BLACK);
        label.setText("Press any key to start");
    }
}

// Main Method
public class TypingGame extends Typing {

    public static void main(String[] args) throws IOException {
        JFrame frame = new Typing("TypingGame!");
        frame.setBounds(0, 0, 700, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}