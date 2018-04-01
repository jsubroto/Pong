/**
 * 
 * Copyright (c) 2018 Jaimes Subroto
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Jaimes Subroto
 */
public class Pong extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private static final String NAME = "Pong";
    private static final int HEIGHT = 130;
    private static final int WIDTH = 160;
    private static final int SCALE = 4;
    protected static int ballSpeed = 6;
    private static final int CHARSPEED = 4;
    private static final int ballSize = SCALE * 5;
    private boolean multiplayer; // Two Players or play against computer
    private static boolean pause = false; // Pauses the game
    private static int freeze = 0;
    
    private static int x;
    private static int y;
    private static int xBall;
    private static int yBall;
    private static int yy;
    private static int yy1;
    private static int yy2;
    private static int yyz;
    
    private static int playerScore;
    private static int botScore;
    private static int winner;
    
    Pong() {
        x = WIDTH * SCALE / 2;
        y = HEIGHT * SCALE /2;
        xBall = ballSpeed;
        yBall = ballSpeed;
        yy = HEIGHT * SCALE / 3;
        yy1 = 0;
        yy2 = 0;
        yyz = HEIGHT * SCALE / 3;
        freeze = 0;
        playerScore = 0;
        botScore = 0;
        winner = 0;
        pause = false;
        addKeyListener(new MyKeyListener());
        addMouseListener(new MyMouseListener());
        setFocusable(true);
    }
    
    private static STATE State = STATE.MENU;
    
    private enum STATE {
        MENU, HELP, GAME, PAUSE, END
    };
    
    public int getPlayerScore() {
        return playerScore;
    }
    
    public int getBotScore() {
        return botScore;
    }
    
    public void moveBall() {
        x = x + xBall;
        y = y + yBall;
        if (y < 0 || y > getHeight() - ballSize) yBall = -yBall;
        // if (x < 0 || x > getWidth() - ballSize) xBall = -xBall;
    }
    
    private static boolean bot;
    
    public void moveChar() {
        if (bot) {
            if (yy + yBall > - SCALE + 1 && yy + yBall < getHeight() - (SCALE * 25 - 3)) // Computer
            if (yy < y - SCALE * 12) yy += yBall;
        } else {
            yy += yy1;
            if (yy < 0) yy = 0; // Top border
            if (yy > getHeight() - SCALE * 25) yy = (getHeight() - SCALE * 25); // Bottom border
        }
    }
    
    public void moveBot() {
        if (multiplayer) {
            if (yyz + yy2 > - SCALE && yyz + yy2 < getHeight() - (SCALE * 25 - 4)) yyz += yy2; // Player 2 UP/DOWN
        } else {
            if (yyz + yBall > - SCALE + 1 && yyz + yBall < getHeight() - (SCALE * 25 - 3)) // Computer
            if (yyz < y - SCALE * 12) yyz += yBall;
        }
    }
    
    private static int mx = 0, my = 0, ny = 30, nBall = 3, xxBall = 3, yyball = 3;
    
    public void moveMenu() {
        if (mx < 0 || mx > getWidth() - ballSize)  xxBall = -xxBall;
        if (my < 30 || my > getHeight() - ballSize + 30) yyball = -yyball;
        mx = mx + xxBall;
        my = my + yyball;
        if (ny < 30 || ny > getHeight() - 60) nBall = -nBall;
        ny = ny + nBall;
    }
    
    public Rectangle getBallBounds() {
        return new Rectangle(x, y, ballSize, ballSize);
    }
    
    public Rectangle getPlayerBounds() {
        return new Rectangle(10, yy, SCALE * 2, SCALE * 25);
    }
    
    public Rectangle getBotBounds() {
        return new Rectangle(getWidth() - 20, yyz, SCALE * 2 + 10, SCALE * 25);
    }
    
    private int collision() {
        if (getPlayerBounds().intersects(getBallBounds())) return -1;
        if (getBotBounds().intersects(getBallBounds())) return 1;
        return 0;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        switch(State) {
            case MENU:
                moveMenu();
                g2d.fillOval(180, 130, 44, 44);
                g2d.fillOval(410, 130, 44, 44);
                g2d.fillOval(mx, my + 30, 30, 30);
                g2d.fillOval(0, ny, 30, 30);
                g2d.fillOval(getWidth() - 30, -ny + getHeight() - 30, 30, 30);
                g2d.fillOval(-mx + getWidth() - 30, my + 390, 30, 30);
                // g2d.fillOval(mx, ny, 30, 30); // Crazy one
                for (int i = 0; i < 630; i += 20) {
                    g2d.fillOval(i, 2, 12, 12);
                    g2d.fillOval(i, 17, 12, 12);
                    g2d.fillOval(i, 419, 12, 12);
                    g2d.fillOval(i, 434, 12, 12);
                }
                for (int i = 160; i < 480; i += 20) { // x Borders
                    g2d.fillOval(i, 110, 12, 12);
                    g2d.fillOval(i, 345, 12, 12);
                }
                for (int i = 110; i < 350; i += 20) { // y Borders
                    g2d.fillOval(160, i, 12, 12);
                    g2d.fillOval(460, i, 12, 12);
                }
                g2d.setFont(new Font("Verdana", Font.BOLD, 50));
                g2d.drawString("PONG", getWidth() / 3 + 26, 170);
                g2d.drawRect(getWidth() / 3 - 14, 190, 240, 40); // Single player box
                g2d.drawRect(getWidth() / 3 - 4, 240, 220, 40); // Multiplayer box
                g2d.drawRect(getWidth() / 3, 290, 100, 40); // Help box
                g2d.drawRect(getWidth() / 3 + 112, 290, 100, 40); // Quit box
                g2d.setFont(new Font("Verdana", Font.BOLD, 30));
                g2d.drawString("Single Player", getWidth() / 3 - 4, 220);
                g2d.drawString("Multi Player", getWidth() / 3 + 6, 270);
                g2d.drawString("Help", getWidth() / 3 + 14, 320);
                g2d.drawString("Quit", getWidth() / 3 + 126, 320);
                g2d.setFont(new Font("Consolas", Font.ITALIC, 15));
                g2d.drawString("Built by Jaimes Subroto", 288, 370);
                break;
            case HELP:
                g2d.setFont(new Font("Verdana", Font.BOLD, 40));
                g2d.drawString("Controls:", 10, 40);
                g2d.drawString("Objectives:", 10, 190);
                g2d.setFont(new Font("Verdana", Font.BOLD, 20));
                g2d.drawString("Player 1: W/S", 40, 60);
                g2d.drawString("Player 2: Up Arrow/Down Arrow:", 40, 85);
                g2d.drawString("Pause: P", 40, 110);
                g2d.drawString("Exit to Main Menu: Escape key", 40, 135);
                g2d.drawString("Defend your goal post by blocking the ball from", 40, 220);
                g2d.drawString("passing through. The first to reach 10 points wins.", 40, 245);
                g2d.setFont(new Font("Consolas", Font.PLAIN, 16));
                g2d.drawString("Pong (marketed as PONG) is one of the earliest arcade video games", 10, 285);
                g2d.drawString("and the very first sports arcade video game. It is a tennis sports", 10, 310);
                g2d.drawString("game featuring simple two-dimensional graphics. The game was", 10, 335);
                g2d.drawString("originally manufactured by Atari which released it in 1972.", 10, 360);
                g2d.setFont(new Font("Consolas", Font.ITALIC, 20));
                g2d.drawString("Project completed on January 4th, 2016.", 10, 400);
                break;
            case GAME:
                g2d.fillOval(x, y, ballSize, ballSize);
                g2d.fillRect(10, yy, SCALE * 2, SCALE * 25);
                g2d.fillRect(getWidth() - 20, yyz, SCALE * 2, SCALE * 25);
                for (int i = 0; i < getHeight() / (SCALE * 5) + 1; i++) {
                    g2d.fillRect(getWidth() / 2, i * SCALE * 5, SCALE, SCALE * 4);
                }
                // g2d.drawOval(175, 100, 280, 280);

                if (collision() == -1) {
                    x = 10 + SCALE * 2;
                    xBall = -xBall;
                    /*
                    int n = JOptionPane.showConfirmDialog(this,
                        "Thanks for playing!",
                        "Game Over",
                        JOptionPane.CLOSED_OPTION);
                    System.exit(0);
                    */
                } else if (collision() == 1) {
                    x = getWidth() - (31 + SCALE * 2);
                    xBall = -xBall;
                }

                g2d.setFont(new Font("Verdana", Font.BOLD, 50));
                g2d.drawString(String.valueOf(getPlayerScore()), WIDTH * SCALE / 4, 50);
                g2d.drawString(String.valueOf(getBotScore()), WIDTH * SCALE - SCALE * 40, 50);

                if (x < -ballSize) {
                    botScore++;
                    x = WIDTH * SCALE / 2;
                    y = HEIGHT * SCALE /2;
                } else if (x > getWidth() + ballSize) {
                    playerScore++;
                    x = WIDTH * SCALE / 2;
                    y = HEIGHT * SCALE /2;
                }

                if (playerScore == 10) {
                    winner = -1;
                    State = STATE.END;
                } else if (botScore == 10) {
                    winner = 1;
                    State = STATE.END;
                }
                break;
            case PAUSE:
                g2d.fillOval(x, y, ballSize, ballSize);
                g2d.fillRect(10, yy, SCALE * 2, SCALE * 25);
                g2d.fillRect(getWidth() - 20, yyz, SCALE * 2, SCALE * 25);
                for (int i = 0; i < getHeight() / (SCALE * 5) + 1; i++) {
                    if (i == 9 || i == 10 || i == 11) {
                        continue;
                    }
                    g2d.fillRect(getWidth() / 2, i * SCALE * 5, SCALE, SCALE * 4);
                }
                g2d.setFont(new Font("Verdana", Font.BOLD, 50));
                g2d.drawString(String.valueOf(getPlayerScore()), WIDTH * SCALE / 4, 50);
                g2d.drawString(String.valueOf(getBotScore()), WIDTH * SCALE - SCALE * 40, 50);
                g2d.drawString("PAUSED", 200, getHeight() / 2);
                break;
            case END:
                if (winner == -1) {
                    x = y = yy = 0;
                    g2d.setFont(new Font("Verdana", Font.BOLD, 50));
                    g2d.drawString("PLAYER WIN!", 100, 100);
                } else if (winner == 1) {
                    x = y = yy = 0;
                    g2d.setFont(new Font("Verdana", Font.BOLD, 50));
                    if (multiplayer) {
                        g2d.drawString("PLAYER 2 WIN!", 100, 100);
                    } else {
                        g2d.drawString("BOT WIN!", 150, 100);
                    }
                }
                break;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        Pong pong = new Pong();
        JFrame frame = new JFrame(NAME);
        frame.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        frame.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pong);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        while(true) {
            pong.repaint();
            if (State == STATE.GAME && !pause) {
                pong.moveBall();
                pong.moveChar();
                pong.moveBot();
            }
            Thread.sleep(10);
        }
        
    }

    private static class MyKeyListener implements KeyListener {

        public MyKeyListener() {
        }

        @Override
        public void keyTyped(KeyEvent ke) {
        }

        @Override
        public void keyPressed(KeyEvent ke) {
            // System.out.println("keyPressed="+KeyEvent.getKeyText(ke.getKeyCode()));
            if (State == STATE.GAME || State == STATE.PAUSE) {
                if (ke.getKeyCode() == KeyEvent.VK_W) {
                    yy1 = -CHARSPEED;
                }
                if (ke.getKeyCode() == KeyEvent.VK_S) {
                    yy1 = CHARSPEED;
                }
                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    yy2 = -CHARSPEED;
                }
                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    yy2 = CHARSPEED;
                }
                if (ke.getKeyCode() == KeyEvent.VK_P) {
                    System.out.println("Pause int: " + freeze + pause);
                    freeze++;
                    if (freeze%2 != 0) {
                        State = STATE.PAUSE;
                        pause = true;
                    } else if (freeze%2 == 0) {
                        State = STATE.GAME;
                        pause = false;
                    }
                }
            }
            if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                State = STATE.MENU;
                Pong pong = new Pong();
            }
            if (State == STATE.MENU) {
                if (ke.getKeyCode() == KeyEvent.VK_B) { // Bot vs Bot
                    bot = true;
                    State = STATE.GAME;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_W || ke.getKeyCode() == KeyEvent.VK_S) {
                yy1 = 0;
            }
            if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN) {
                yy2 = 0;
            }
        }
    }
    
    public class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {
            int x = me.getX();
            int y = me.getY();
            if (x > getWidth() / 3 - 14 && x < getWidth() / 3 + 226 && y > 190 && y < 230) { // Single Player button x: 197-437, y: 190-230;
                multiplayer = false;
                State = STATE.GAME; 
            }
            if (x > getWidth() / 3 - 4 && x < getWidth() / 3 + 216 && y > 240 && y < 280) { // Multi Player button x: 207-427, y: 240-280;
                multiplayer = true;
                State = STATE.GAME;
            }
            if (x > getWidth() / 3 && x < getWidth() / 3 + 100 && y > 290 && y < 330) { // Help button x: 211-311, y: 290-330;
                State = STATE.HELP;
            }
            if (x > getWidth() / 3 + 112 && x < getWidth() / 3 + 212 && y > 290 && y < 330) { // Quit button x: 323-423, y: 290-330;
                System.exit(0);
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }
        
    }
    
}