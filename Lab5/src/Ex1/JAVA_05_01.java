package Ex1;

import static java.lang.IO.*;
import static term.term.*;

public class JAVA_05_01 {
    static final int TERMINAL_WIDTH = 120;
    static final int TERMINAL_HEIGHT = 30;
    static final int PADDLE_HEIGHT = 7;
    static final int PADDLE_WIDTH = 3;
    static final int PADDLE_MARGIN_X = (int) (TERMINAL_WIDTH * 0.2);

    static int leftPaddleY;
    static int rightPaddleY;
    static int leftPaddleX = PADDLE_MARGIN_X;
    static int rightPaddleX = TERMINAL_WIDTH - PADDLE_MARGIN_X - PADDLE_WIDTH + 1;

    // -1 -> up, 0 -> stop, -1 -> down
    static int leftPaddleMove = 0;
    static int rightPaddleMove = 0;

    // Ball
    static int ballX, ballY;
    static int ball_dx, ball_dy;
    static boolean isBallMoving = false;
    static int ball_step = 0;
    static final int BALL_SPEED = 2;

    // Game state
    static int scoreLeft = 0;
    static int scoreRight = 0;
    static boolean gameRunning = true;
    static String previousScore = "";

    // Initial round state
    static void resetPositions() {
        leftPaddleY = (TERMINAL_HEIGHT / 2) - (PADDLE_HEIGHT / 2);
        rightPaddleY = (TERMINAL_HEIGHT / 2) - (PADDLE_HEIGHT / 2);

        ballX = TERMINAL_WIDTH / 2;
        ballY = TERMINAL_HEIGHT / 2;

        ball_dx = 0;
        ball_dy = 0;
        isBallMoving = false;

        leftPaddleMove = 0;
        rightPaddleMove = 0;

        clrscr();
        delay(10);
        framexyc(1, 1, TERMINAL_WIDTH, TERMINAL_HEIGHT, '#');
    }

    static void handleInput() {
        leftPaddleMove = 0;
        rightPaddleMove = 0;

        if (keypressed()) {
            String key = readkeystr();

            if (key.equals("esc")) {
                gameRunning = false;
                return;
            }

            if (!isBallMoving && key.equals(" ")) {
                startBall();
            }

            if (key.equalsIgnoreCase("w")) {
                if (leftPaddleY > 2) {
                    clearPaddle(leftPaddleX, leftPaddleY);
                    leftPaddleY--;
                    leftPaddleMove = -1;
                }
            }
            if (key.equalsIgnoreCase("s")) {
                if (leftPaddleY + PADDLE_HEIGHT <= TERMINAL_HEIGHT - 1) {
                    clearPaddle(leftPaddleX, leftPaddleY);
                    leftPaddleY++;
                    leftPaddleMove = 1;
                }
            }
            if (key.equals("arrow_up")) {
                if (rightPaddleY > 2) {
                    clearPaddle(rightPaddleX, rightPaddleY);
                    rightPaddleY--;
                    rightPaddleMove = -1;
                }
            }
            if (key.equals("arrow_dn")) {
                if (rightPaddleY + PADDLE_HEIGHT <= TERMINAL_HEIGHT - 1) {
                    clearPaddle(rightPaddleX, rightPaddleY);
                    rightPaddleY++;
                    rightPaddleMove = 1;
                }
            }
        }
    }

    // Determining the first move of the ball
    static void startBall() {
        isBallMoving = true;
        ball_dx = (Math.random() > 0.5) ? 1 : -1;
        ball_dy = (int) (Math.random() * 3) - 1;
    }

    static void updatePhysics() {
        int nextX = ballX + ball_dx;
        int nextY = ballY + ball_dy;

        // Frame collision
        if (nextY <= 1 || nextY >= TERMINAL_HEIGHT) {
            ball_dy = -ball_dy;
            playSound(400, 50);
            return;
        }

        // Paddle collision
        boolean hitLeftX = (nextX >= leftPaddleX && nextX < leftPaddleX + PADDLE_WIDTH);
        boolean hitLeftY = (nextY >= leftPaddleY && nextY < leftPaddleY + PADDLE_HEIGHT);

        if (hitLeftX && hitLeftY && ball_dx < 0) {
            ball_dx = -ball_dx;

            // Vertical move dependent on the paddle's move
            if (leftPaddleMove == 1) ball_dy = 1;
            else if (leftPaddleMove == -1) ball_dy = -1;
            else ball_dy = (int) (Math.random() * 3) - 1; // ball_dy = 0 would make it kind of boring

            playSound(600, 50);
            return;
        }

        boolean hitRightX = (nextX >= rightPaddleX && nextX < rightPaddleX + PADDLE_WIDTH);
        boolean hitRightY = (nextY >= rightPaddleY && nextY < rightPaddleY + PADDLE_HEIGHT);

        if (hitRightX && hitRightY && ball_dx > 0) {
            ball_dx = -ball_dx;

            if (rightPaddleMove == 1) ball_dy = 1;
            else if (rightPaddleMove == -1) ball_dy = -1;
            else ball_dy = (int) (Math.random() * 3) - 1; // ball_dy = 0 would make it kind of boring

            playSound(600, 50);
            return;
        }

        if (nextX < leftPaddleX) {
            scoreRight++;
            playSound(200, 150);
            ball_step = 0;
            resetPositions();
            return;
        }

        if (nextX > rightPaddleX + PADDLE_WIDTH) {
            scoreLeft++;
            playSound(200, 150);
            ball_step = 0;
            resetPositions();
            return;
        }

        gotoxy(ballX, ballY);
        print(" ");

        ballX = nextX;
        ballY = nextY;
    }

    static void drawGame() {
        // Score
        String scoreStr = " " + scoreLeft + " : " + scoreRight + " ";
        if (ball_step == 0) framexyc(1, 1, TERMINAL_WIDTH, TERMINAL_HEIGHT, '#');
        if (!scoreStr.equals(previousScore)) {
            gotoxy((TERMINAL_WIDTH - scoreStr.length()) / 2, 1);
            print(scoreStr);
        }
        previousScore = scoreStr;

        // Paddles
        setfgcolor(yellow);
        drawPaddle(leftPaddleX, leftPaddleY);
        setfgcolor(ltgreen);
        drawPaddle(rightPaddleX, rightPaddleY);

        // Ball
        setfgcolor(white);
        gotoxy(ballX, ballY);
        print("*");
    }

    static void drawPaddle(int x, int y) {
        framexyc(x, y, x + PADDLE_WIDTH - 1, y + PADDLE_HEIGHT - 1, '|');
    }

    static void clearPaddle(int x, int y) {
        setfgcolor(black);
        framexyc(x, y, x + PADDLE_WIDTH - 1, y + PADDLE_HEIGHT - 1, '|');
    }

    public static void playSound(final int freq, final int durationMs) {
        new Thread(() -> {
            try {
                sound(freq, durationMs);
            } catch (Exception _) {
            }
        }).start();
    }

    void main() {
        cursor_hide();
        resetPositions();
        framexyc(1, 1, TERMINAL_WIDTH, TERMINAL_HEIGHT, '#');

        while (gameRunning) {
            drawGame();
            handleInput();

            if (isBallMoving && ball_step % BALL_SPEED == 0) {
                updatePhysics();
            }
            ball_step += 1;
            delay(20);
        }

        setfgcolor(7);
        clrscr();
        println("Game over.\nFinal score: " + scoreLeft + " : " + scoreRight);
    }
}