// Kamil Wolbach (280161)
import static java.lang.IO.*;
import static term.term.*;

// Data structures
static class TSquare {
    int x, y;
    int dx, dy;
    int size;
    int color;
    boolean active;
}

static class TPlayer {
    int x, y;
    int prev_x, prev_y;
}

static class TGame {
    TSquare[] squares;
    int width;
    int height;
    int score;
    int squaresLeft;
    boolean isRunning;
    long startTime;
    int roundTime;
    int simulationSpeed;
}

static void initSquare(TSquare s, int boardWidth, int boardHeight) {
    s.size = (Math.random() < 0.5) ? 1 : 2;

    s.x = 3 + (int) (Math.random() * (boardWidth - 3 - s.size));
    s.y = 3 + (int) (Math.random() * (boardHeight - 3 - s.size));

    do {
        s.dx = (int) (Math.random() * 3) - 1;
        s.dy = (int) (Math.random() * 3) - 1;
    } while (s.dx == 0 && s.dy == 0);

    int[] palette = {1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15};
    s.color = palette[(int) (Math.random() * palette.length)];
    s.active = true;
}

static void initPlayer(TPlayer p, int startX, int startY) {
    p.x = startX;
    p.y = startY;
    p.prev_x = startX;
    p.prev_y = startY;
}

static boolean checkIntersection(int x1, int y1, int size1, int x2, int y2, int size2) {
    return x1 < x2 + size2 && x1 + size1 > x2 && y1 < y2 + size2 && y1 + size1 > y2;
}

static void updateSquares(TGame game) {
    for (int i = 0; i < game.squares.length; i++) {
        TSquare s1 = game.squares[i];
        if (!s1.active) continue;

        int nextX = s1.x + s1.dx;
        int nextY = s1.y + s1.dy;
        boolean collided = false;

        boolean hitWallX = (nextX <= 1 || nextX + s1.size >= game.width);
        boolean hitWallY = (nextY <= 1 || nextY + s1.size >= game.height + 1);

        // Square-wall collision
        if (hitWallX || hitWallY) {
            collided = true;
            if (hitWallX) {
                s1.dx = -s1.dx;
                if (s1.dx == 0) s1.dx = (Math.random() < 0.5) ? 1 : -1;
            }
            if (hitWallY) {
                s1.dy = -s1.dy;
                if (s1.dy == 0) s1.dy = (Math.random() < 0.5) ? 1 : -1;
            }
        }

        // Square-square collision
        if (!collided) {
            for (int j = i + 1; j < game.squares.length; j++) {
                TSquare s2 = game.squares[j];
                if (!s2.active) continue;

                int s2NextX = s2.x + s2.dx;
                int s2NextY = s2.y + s2.dy;

                if (checkIntersection(nextX, nextY, s1.size, s2NextX, s2NextY, s2.size)) {
                    collided = true;
                    s1.dx = -s1.dx;
                    if (s1.dx == 0) s1.dx = (Math.random() < 0.5) ? 1 : -1;
                    s1.dy = -s1.dy;
                    if (s1.dy == 0) s1.dy = (Math.random() < 0.5) ? 1 : -1;

                    s2.dx = -s2.dx;
                    if (s2.dx == 0) s2.dx = (Math.random() < 0.5) ? 1 : -1;
                    s2.dy = -s2.dy;
                    if (s2.dy == 0) s2.dy = (Math.random() < 0.5) ? 1 : -1;
                }
            }
        }
        if (!collided) {
            s1.x += s1.dx;
            s1.y += s1.dy;
        }
    }
}

static void updatePlayer(TGame game, TPlayer plr) {
    if (keypressed()) {
        String key = readkeystr();
        plr.prev_x = plr.x;
        plr.prev_y = plr.y;

        if (key.equals("q")) game.isRunning = false;
        if (key.equals("arrow_up")) plr.y--;
        else if (key.equals("arrow_dn")) plr.y++;
        else if (key.equals("arrow_lt")) plr.x--;
        else if (key.equals("arrow_rt")) plr.x++;
    }

    // Player-wall collision
    if (plr.x <= 1) plr.x = 2;
    if (plr.y <= 1) plr.y = 2;
    if (plr.x >= game.width) plr.x = game.width - 1;
    if (plr.y >= game.height) plr.y = game.height - 1;

    // Player-square collision
    for (TSquare s : game.squares) {
        if (s.active) {
            boolean hit = (plr.x >= s.x && plr.x <= s.x + s.size - 1) &&
                    (plr.y >= s.y && plr.y <= s.y + s.size - 1);

            if (hit) {
                s.active = false;
                game.squaresLeft--;
                s.color = 0;
                if (s.size == 2) game.score += 1;
                else if (s.size == 1) game.score += 4;
            }
        }
    }
}

static void drawGame(TGame game, TPlayer plr) {
    setfgcolor(0);
    gotoxy(plr.prev_x, plr.prev_y);
    print(" ");

    setfgcolor(7);
    long elapsed = (System.currentTimeMillis() - game.startTime) / 1000;
    String ui = " Time: " + (game.roundTime - elapsed) + "s | Score: " + game.score + " | Press 'q' to quit ";
    gotoxy((game.width - ui.length()) / 2, game.height);
    print(ui);

    for (TSquare s : game.squares) {
        if (s.active) {
            setfgcolor(s.color);
            framexyc(s.x, s.y, s.x + s.size - 1, s.y + s.size - 1, '*');
        }
    }

    setfgcolor(15);
    gotoxy(plr.x, plr.y);
    print("*");
}

static void eraseSquares(TGame game) {
    setfgcolor(0);
    for (TSquare s : game.squares) {
        if (s.active) {
            framexyc(s.x, s.y, s.x + s.size - 1, s.y + s.size - 1, '*');
        }
    }
}

void main() {
    TGame game = new TGame();
    game.width = 120;
    game.height = 30;
    game.squaresLeft = 10;
    game.squares = new TSquare[game.squaresLeft];
    game.simulationSpeed = 80;
    game.isRunning = true;
    game.roundTime = 120;
    game.startTime = System.currentTimeMillis();

    for (int i = 0; i < game.squares.length; i++) {
        game.squares[i] = new TSquare();
        initSquare(game.squares[i], game.width, game.height);
    }

    TPlayer player = new TPlayer();
    initPlayer(player, game.width / 2, game.height / 2);

    clrscr();
    cursor_hide();
    framexyc(1, 1, game.width, game.height, '#');

    while (game.isRunning) {
        long elapsed = (System.currentTimeMillis() - game.startTime) / 1000;
        if (elapsed >= game.roundTime) {
            game.isRunning = false;
            break;
        }
        if (game.squaresLeft == 0) {
            game.isRunning = false;
            break;
        }
        
        eraseSquares(game);

        updatePlayer(game, player);
        updateSquares(game);

        drawGame(game, player);

        System.out.flush();
        delay(game.simulationSpeed);
    }

    setfgcolor(7);
    framexyc(1, 1, game.width, game.height, '#');
    String msg = (game.squaresLeft == 0) ? "YOU WON!" : "GAME OVER";
    gotoxy((game.width - msg.length()) / 2, game.height / 2);
    print(msg);
    String score_msg = "Total score: " + game.score;
    gotoxy((game.width - score_msg.length()) / 2, game.height / 2 + 1);
    print(score_msg);

    System.out.flush();
    delay(4000);
    clrscr();
}
