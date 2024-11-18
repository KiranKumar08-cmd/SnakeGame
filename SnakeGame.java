import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    
    private final int TILE_SIZE = 20;
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);

    
    private int[] x = new int[TOTAL_TILES];
    private int[] y = new int[TOTAL_TILES];
    private int snakeLength = 3;
    private int foodX, foodY;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT) && (!right)) {
                    left = true;
                    up = false;
                    down = false;
                }
                if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                    right = true;
                    up = false;
                    down = false;
                }
                if ((key == KeyEvent.VK_UP) && (!down)) {
                    up = true;
                    right = false;
                    left = false;
                }
                if ((key == KeyEvent.VK_DOWN) && (!up)) {
                    down = true;
                    right = false;
                    left = false;
                }
            }
        });

        startGame();
    }

    private void startGame() {
        for (int i = 0; i < snakeLength; i++) {
            x[i] = 100 - i * TILE_SIZE;
            y[i] = 100;
        }
        placeFood();
        timer = new Timer(140, this);
        timer.start();
    }

    private void placeFood() {
        Random random = new Random();
        foodX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) x[0] -= TILE_SIZE;
        if (right) x[0] += TILE_SIZE;
        if (up) y[0] -= TILE_SIZE;
        if (down) y[0] += TILE_SIZE;
    }

    private void checkCollision() {
        
        for (int i = snakeLength; i > 0; i--) {
            if ((i > 3) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            snakeLength++;
            placeFood();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, TILE_SIZE, TILE_SIZE);

            for (int i = 0; i < snakeLength; i++) {
                g.setColor(i == 0 ? Color.GREEN : Color.LIGHT_GRAY);
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}