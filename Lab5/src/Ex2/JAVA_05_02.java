import static term.term.*;

static class Ball {
    public int x, y;
    public int dx, dy;
    public boolean isBallMoving;
    public int ball_step = 0;
    private final int BALL_SPEED = 4;

    public Ball(int boardWidth, int boardHeight, int dx, int dy, boolean isBallMoving) {
        this.x = boardWidth / 2;
        this.y = boardHeight / 2;
        
        this.dx = 0;
        this.dy = 0;
        this.isBallMoving = false;
    }
    
}

static class Paddle {
    private final int length;
    public int x, y;
    public int leftPaddleMove = 0;
    public int rightPaddleMove = 0;
    
    public Paddle(int length, int paddleX, int paddleY) {
        this.length = length;
        this.x = paddleX;
        this.y = paddleY;
        
    }
}

static class Frame {
    static final int TERMINAL_WIDTH = 120;
    static final int TERMINAL_HEIGHT = 30;
    static final int PADDLE_HEIGHT = 7;
    static final int PADDLE_WIDTH = 3;
    static final int PADDLE_MARGIN_X = (int) (TERMINAL_WIDTH * 0.2);
}

static class GameModel {
    static int scoreLeft = 0;
    static int scoreRight = 0;
    static boolean gameRunning = true;
    static String previousScore = "";
}

static class GameView {
    public void drawFrame(int width, int height) {
        setfgcolor(7);
        framexyc(1, 1, width, height, '#');
    }
    
    public void drawBall(Ball ball) {
        setfgcolor(7);
        framexyc(ball.x, ball.y, ball.x, ball.y, '*');
    }

    public void clearBall(Ball ball) {
        setfgcolor(0);
        framexyc(ball.x, ball.y, ball.x, ball.y, ' ');
    }

    public void drawPaddle(Paddle paddle) {
        setfgcolor(7);
        framexyc(paddle.x, paddle.y, paddle.x, paddle.y, '|');
    }

    public void clearPaddle(Paddle paddle) {
        setfgcolor(0);
        framexyc(paddle.x, paddle.y, paddle.x, paddle.y, ' ');
    }
}

static class GameController {
    private GameView view;
    private GameModel model;
    
    
    
}

void main() {
    clrscr();
    cursor_hide();
//    view.drawFrame(WIDTH, HEIGHT);
}