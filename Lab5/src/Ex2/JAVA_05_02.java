import static java.lang.IO.print;
import static java.lang.IO.println;
import static term.term.*;

static class Ball {
    public static int x, y;
    public static int dx = 0, dy = 0;
    public static boolean isBallMoving = false;
    public static int step = 0;
    private static final int speed = 4;

    public Ball(int boardWidth, int boardHeight) {
        x = boardWidth / 2;
        y = boardHeight / 2;
    }

    public static void startBall() {
        isBallMoving = true;
        dx = (Math.random() > 0.5) ? 1 : -1;
        dy = (int) (Math.random() * 3) - 1;
    }
}

static class Paddle {
    final int width, height;
    public int x, y;
    public int moveDirection = 0;

    public Paddle(int width, int height, String side) {
        this.width = width;
        this.height = height;
        if (side.equals("LEFT")) {
            this.x = Frame.PADDLE_MARGIN_X;
        } else {
            this.x = Frame.TERMINAL_WIDTH - Frame.PADDLE_MARGIN_X - width + 1;
        }
        this.y = (Frame.TERMINAL_HEIGHT / 2) - (height / 2);
    }
}

static class Frame {
    static final int TERMINAL_WIDTH = 120;
    static final int TERMINAL_HEIGHT = 30;
    static final int PADDLE_MARGIN_X = (int) (TERMINAL_WIDTH * 0.2);
}

static class GameModel {
    static int scoreLeft = 0;
    static int scoreRight = 0;
    static String previousScore = "";

    Paddle PaddleLeft = new Paddle(3, 7, "LEFT");
    Paddle PaddleRight = new Paddle(3, 7, "RIGHT");

    void handleInput() {
        if (keypressed()) {
            String key = readkeystr();

            if (key.equals("esc") || key.equals("q")) {
                GameController.gameRunning = false;
                return;
            }

            if (!Ball.isBallMoving && key.equals(" ")) {
                Ball.startBall();
            }

            if (key.equalsIgnoreCase("w")) {
                if (PaddleLeft.y > 2) {
                    GameView.clearPaddle(PaddleLeft);
                    PaddleLeft.y--;
                    PaddleLeft.moveDirection = -1;
                }
            }
            if (key.equalsIgnoreCase("s")) {
                if (PaddleLeft.y + PaddleLeft.height <= Frame.TERMINAL_HEIGHT - 1) {
                    GameView.clearPaddle(PaddleLeft);
                    PaddleLeft.y++;
                    PaddleLeft.moveDirection = 1;
                }
            }
            if (key.equals("arrow_up")) {
                if (PaddleRight.y > 2) {
                    GameView.clearPaddle(PaddleRight);
                    PaddleRight.y--;
                    PaddleRight.moveDirection = -1;
                }
            }
            if (key.equals("arrow_dn")) {
                if (PaddleRight.y + PaddleRight.height <= Frame.TERMINAL_HEIGHT - 1) {
                    GameView.clearPaddle(PaddleRight);
                    PaddleRight.y++;
                    PaddleRight.moveDirection = 1;
                }
            }
        }
    }

    void updatePhysics() {
        int nextX = Ball.x + Ball.dx;
        int nextY = Ball.y + Ball.dy;

        // Frame collision
        if (nextY <= 1 || nextY >= Frame.TERMINAL_HEIGHT) {
            Ball.dy = -Ball.dy;
            playSound(400, 50);
            return;
        }

        // Paddle collision
        boolean hitLeftX = (nextX >= PaddleLeft.x && nextX < PaddleLeft.x + PaddleLeft.width);
        boolean hitLeftY = (nextY >= PaddleLeft.y && nextY < PaddleLeft.y + PaddleLeft.height);

        if (hitLeftX && hitLeftY && Ball.dx < 0) {
            Ball.dx = -Ball.dx;

            // Vertical move dependent on the paddle's move
            if (PaddleLeft.moveDirection == 1) Ball.dy = 1;
            else if (PaddleLeft.moveDirection == -1) Ball.dy = -1;
            else Ball.dy = (int) (Math.random() * 3) - 1;

            playSound(600, 50);
            return;
        }

        boolean hitRightX = (nextX >= PaddleRight.x && nextX < PaddleRight.x + PaddleRight.width);
        boolean hitRightY = (nextY >= PaddleRight.y && nextY < PaddleRight.y + PaddleRight.height);

        if (hitRightX && hitRightY && Ball.dx > 0) {
            Ball.dx = -Ball.dx;

            if (PaddleRight.moveDirection == 1) Ball.dy = 1;
            else if (PaddleRight.moveDirection == -1) Ball.dy = -1;
            else Ball.dy = (int) (Math.random() * 3) - 1;

            playSound(600, 50);
            return;
        }

        if (nextX < PaddleLeft.x) {
            scoreRight++;
            playSound(200, 150);
            Ball.step = 0;
            resetPositions();
            return;
        }

        if (nextX > PaddleRight.x + PaddleRight.width) {
            scoreLeft++;
            playSound(200, 150);
            Ball.step = 0;
            resetPositions();
            return;
        }

        gotoxy(Ball.x, Ball.y);
        print(" ");

        Ball.x = nextX;
        Ball.y = nextY;
    }

    void drawGame() {
        // Score
        String scoreStr = " " + scoreLeft + " : " + scoreRight + " ";
        if (Ball.step == 0) framexyc(1, 1, Frame.TERMINAL_WIDTH, Frame.TERMINAL_HEIGHT, '#');
        GameView.printScore(scoreStr);
        previousScore = scoreStr;

        // Paddles
        setfgcolor(yellow);
        GameView.drawPaddle(PaddleLeft);
        setfgcolor(ltgreen);
        GameView.drawPaddle(PaddleRight);

        // Ball
        setfgcolor(white);
        gotoxy(Ball.x, Ball.y);
        print("*");
    }

    void resetPositions() {
        PaddleLeft.y = (Frame.TERMINAL_HEIGHT / 2) - (PaddleLeft.height / 2);
        PaddleRight.y = (Frame.TERMINAL_HEIGHT / 2) - (PaddleRight.height / 2);

        Ball.x = Frame.TERMINAL_WIDTH / 2;
        Ball.y = Frame.TERMINAL_HEIGHT / 2;

        Ball.dx = 0;
        Ball.dy = 0;
        Ball.isBallMoving = false;

        PaddleLeft.moveDirection = 0;
        PaddleRight.moveDirection = 0;

        clrscr();
        delay(10);
        framexyc(1, 1, Frame.TERMINAL_WIDTH, Frame.TERMINAL_HEIGHT, '#');
    }

    public static void playSound(final int freq, final int durationMs) {
        new Thread(() -> {
            try {
                sound(freq, durationMs);
            } catch (Exception _) {
            }
        }).start();
    }
}

static class GameView {
    public void drawFrame(int width, int height) {
        setfgcolor(7);
        framexyc(1, 1, width, height, '#');
    }

    public static void drawPaddle(Paddle paddle) {
        framexyc(paddle.x, paddle.y, paddle.x + paddle.width - 1, paddle.y + paddle.height - 1, '|');
    }

    public static void clearPaddle(Paddle paddle) {
        setfgcolor(black);
        framexyc(paddle.x, paddle.y, paddle.x + paddle.width - 1, paddle.y + paddle.height - 1, '|');
    }

    public static void printScore(String score) {
        if (!score.equals(GameModel.previousScore)) {
            setfgcolor(7);
            gotoxy((Frame.TERMINAL_WIDTH - score.length()) / 2, 1);
            print(score);
        }
        GameModel.previousScore = score;
    }
}

static class GameController {
    private final GameModel model;
    private final GameView view;
    public static boolean gameRunning = true;

    GameController() {
        this.model = new GameModel();
        this.view = new GameView();
    }

    void run() {
        model.resetPositions();
        view.drawFrame(Frame.TERMINAL_WIDTH, Frame.TERMINAL_HEIGHT);
        while(gameRunning) {
            model.drawGame();
            model.handleInput();
        if (Ball.isBallMoving && Ball.step % Ball.speed == 0) {
            model.updatePhysics();
        }
        Ball.step += 1;
        delay(20);
    }

    setfgcolor(7);
    clrscr();
    println("Game over.\nFinal score: "+GameModel.scoreLeft +" : "+GameModel.scoreRight);
}
}

void main() {
    clrscr();
    cursor_hide();
    GameController game = new GameController();
    game.run();
}