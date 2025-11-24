import static term.term.*;

static class Ball {
    private int x, y;
    private int dx, dy;

    public Ball(int boardWidth, int boardHeight) {
        this.x = boardWidth / 2;
        this.y = boardHeight / 2;
    }
    
}

static class Paddle {
    private int length;
    
    public Paddle() {
        
    }
}

static class GameModel {
    
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