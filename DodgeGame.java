import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class DodgeGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int playerX = 250; 
    private final int playerY = 450; 
    private final int playerWidth = 50;
    private final int playerHeight = 20;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private ArrayList<Rectangle> enemies;
    private Random random;
    private int score = 0;

    private boolean gameOver = false;
    private boolean typingLyrics = false;

    // Lyrics lines
    private String[] lyrics = {
        "Nariyan ka pa ba?....",
        "Di ka na matanaw .....",
        "Kung mayro'n madaraanang ....",
        "Pasulongggg ...."
    };

    // Typing speed per line (ms per character)
    private int[] lyricSpeeds = {150, 100, 120, 200};

    // Pause between lines (ms before next line starts)
    private int[] linePauses = {2000, 2000, 2000, 2000};

    private int currentLine = 0;
    private String displayedText = "";
    private int charIndex = 0;
    private Timer lyricsTimer;

    public DodgeGame() {
        setPreferredSize(new Dimension(600, 500));
        setBackground(Color.BLACK);

        enemies = new ArrayList<>();
        random = new Random();

        timer = new Timer(20, this); 
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || typingLyrics) return;

        if (leftPressed && playerX > 0) {
            playerX -= 5;
        }
        if (rightPressed && playerX < getWidth() - playerWidth) {
            playerX += 5;
        }

        if (random.nextInt(20) == 0) {
            enemies.add(new Rectangle(random.nextInt(getWidth() - 20), 0, 20, 20));
        }

        Iterator<Rectangle> it = enemies.iterator();
        while (it.hasNext()) {
            Rectangle enemy = it.next();
            enemy.y += 5;

            if (enemy.intersects(new Rectangle(playerX, playerY, playerWidth, playerHeight))) {
                gameOver = true;
                timer.stop();
                startLyricsTyping();
            }

            if (enemy.y > getHeight()) {
                it.remove();
                score++;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            // Full black background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.PLAIN, 22));

            // Draw typed lyrics so far
            int y = getHeight() / 2 - 50;
            for (int i = 0; i < currentLine; i++) {
                g.drawString(lyrics[i], 100, y + i * 40);
            }
            g.drawString(displayedText, 100, y + currentLine * 40);

            // After all lyrics typed, show restart option
            if (!typingLyrics && currentLine >= lyrics.length) {
                g.setFont(new Font("Consolas", Font.BOLD, 18));
                g.setColor(Color.YELLOW);
                g.drawString("Press R to Restart", getWidth()/2 - 100, y + currentLine * 40 + 50);
            }

            return;
        }

        // Draw player
        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);

        // Draw enemies
        g.setColor(Color.RED);
        for (Rectangle enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);
    }

    private void startLyricsTyping() {
        typingLyrics = true;
        currentLine = 0;
        displayedText = "";
        charIndex = 0;

        lyricsTimer = new Timer(lyricSpeeds[currentLine], new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentLine < lyrics.length) {
                    if (charIndex < lyrics[currentLine].length()) {
                        // Type characters one by one
                        displayedText += lyrics[currentLine].charAt(charIndex);
                        charIndex++;
                    } else {
                        // Finished this line
                        currentLine++;
                        charIndex = 0;
                        displayedText = "";

                        if (currentLine < lyrics.length) {
                            // Stop typing, wait before starting next line
                            ((Timer) e.getSource()).stop();
                            Timer pauseTimer = new Timer(linePauses[currentLine - 1], new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ev) {
                                    // Start typing next line with its speed
                                    lyricsTimer.setDelay(lyricSpeeds[currentLine]);
                                    lyricsTimer.start();
                                    ((Timer) ev.getSource()).stop();
                                }
                            });
                            pauseTimer.setRepeats(false);
                            pauseTimer.start();
                        } else {
                            // All lines finished
                            typingLyrics = false;
                            ((Timer) e.getSource()).stop();
                        }
                    }
                }
                repaint();
            }
        });
        lyricsTimer.start();
    }

    // Key events
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;

        if (gameOver && e.getKeyCode() == KeyEvent.VK_R && !typingLyrics) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void restartGame() {
        playerX = 250;
        enemies.clear();
        score = 0;
        gameOver = false;
        typingLyrics = false;
        displayedText = "";
        currentLine = 0;
        charIndex = 0;
        timer.start();
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dodge Game");
        DodgeGame game = new DodgeGame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
