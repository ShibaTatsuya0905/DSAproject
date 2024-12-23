import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int BOX_SIZE = 20;
    private static final int WIDTH = 20; // grid width in boxes  
    private static final int HEIGHT = 20; // grid height in boxes  

    private LinkedList<Point> snake;
    private Point food;
    private char direction;
    private boolean running;
    private Timer timer;

    public GamePanel() {
        snake = new LinkedList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2)); // Start with one segment at center  
        direction = 'U'; // Start moving up  
        running = false;

        setBackground(Color.WHITE);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH * BOX_SIZE, HEIGHT * BOX_SIZE));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                }
            }
        });
    }

    public void startGame() {
        running = true;
        spawnFood();
        timer = new Timer(100, this);
        timer.start();
        repaint();
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        } while (snake.contains(new Point(x, y))); // Ensure food doesn't spawn on the snake  
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnake(g);
        drawFood(g);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * BOX_SIZE, p.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * BOX_SIZE, food.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head); // Copy the current head  

        switch (direction) {
            case 'U':
                newHead.translate(0, -1);
                break;
            case 'D':
                newHead.translate(0, 1);
                break;
            case 'L':
                newHead.translate(-1, 0);
                break;
            case 'R':
                newHead.translate(1, 0);
                break;
        }

        if (checkCollision(newHead)) {
            running = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over");
            return;
        }

        snake.addFirst(newHead);
        if (newHead.equals(food)) {
            spawnFood(); // Spawn new food if it ate  
        } else {
            snake.removeLast(); // Remove last segment if not eating  
        }
    }

    private boolean checkCollision(Point head) {
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            return true; // Collision with walls  
        }
        return snake.contains(head); // Collision with itself  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            repaint();
        }
    }
}