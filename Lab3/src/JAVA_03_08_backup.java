//For best results run this code in at least 120x30 terminal window

import static java.lang.IO.*;  //including package IO to be able to use simple print()
import static term.term.*;     //include package term (clrscr, gotoxy, setfgcolor, etc., were moved there for clarity)

//add your procedures and functions here

void draw_vert_line_c(int x, int y1, int y2, char c, int color) {
    if (y1 > y2) {
        int temp = y1;
        y1 = y2;
        y2 = temp;
    }
    setfgcolor(color);
    for (int i = y1; i <= y2; i++) {
        gotoxy(x, i);
        print(c);
    }
}

void draw_horiz_line_c(int x1, int x2, int y, char c, int color) {
    if (x1 > x2) {
        int temp = x1;
        x1 = x2;
        x2 = temp;
    }
    setfgcolor(color);
    for (int i = x1; i <= x2; i++) {
        gotoxy(i, y);
        print(c);
    }
}

void draw_frame_c(int x1, int x2, int y1, int y2, char c, int color) {
    draw_vert_line_c(x1, y1, y2, c, color);
    draw_horiz_line_c(x1, x2, y1, c, color);
    draw_vert_line_c(x2, y1, y2, c, color);
    draw_horiz_line_c(x2, x1, y2, c, color);
}

int guard_pos_x(int posx) {
    if (posx > 1 + 1 && posx < 120 - 1) {
        return 1;
    }
    return 0;
}

int guard_pos_y(int posy) {
    if (posy > 1 + 1 && posy < 30 - 1) {
        return 1;
    }
    return 0;
}

int getRndNum(int min, int max) {
    // e.g., min = 3, max = 118 -> min (3) + 118-3+1 = 116.
    // but 116 will never be achievable because of (int), so it'll be
    // rounded down to 115. thus the max retrievable number is 3+115 = 118, which is what we want.
    // this is why max-min+1 must be used
    return min + (int) (Math.random() * (max-min+1));
}

void square_control_with_ghosts(int posx, int posy, int ball_color) {
    // Randomised square coordinates
    int posX1 = getRndNum(3, 118);
    int posY1 = getRndNum(3, 28);
    int posX2 = getRndNum(3, 118);
    int posY2 = getRndNum(3, 28);
    
    char c = '#';

    draw_frame_c(posX1 - 1, posX1 + 1, posY1 - 1, posY1 + 1, c, cyan);
    draw_frame_c(posX2 - 1, posX2 + 1, posY2 - 1, posY2 + 1, c, lime);
    // to overwrite the ghost if it stands on the main square
    draw_frame_c(posx - 1, posx + 1, posy - 1, posy + 1, '*', ball_color);
    delay(100);
    draw_frame_c(posX1 - 1, posX1 + 1, posY1 - 1, posY1 + 1, ' ', 15);
    draw_frame_c(posX2 - 1, posX2 + 1, posY2 - 1, posY2 + 1, ' ', 15);
    // same here
    draw_frame_c(posx - 1, posx + 1, posy - 1, posy + 1, '*', ball_color);
}


void square_control(int posx, int posy, int ball_color) {
    while (true) {
        square_control_with_ghosts(posx, posy, ball_color);
        if (keypressed()) {
            String keystr = readkeystr();

            int last_posx = posx;
            int last_posy = posy;

            if (keystr.equals("arrow_up")) {
                if (guard_pos_y(posy - 1) == 1) {
                    posy -= 1;
                }
            }
            ;
            if (keystr.equals("arrow_lt")) {
                if (guard_pos_x(posx - 1) == 1) {
                    posx -= 1;
                }
            }
            ;
            if (keystr.equals("arrow_dn")) {
                if (guard_pos_y(posy + 1) == 1) {
                    posy += 1;
                }
            }
            ;
            if (keystr.equals("arrow_rt")) {
                if (guard_pos_x(posx + 1) == 1) {
                    posx += 1;
                }
            }
            if (keystr.equals("c")) {
                if (ball_color == 2) {
                    ball_color = 4;
                } else if (ball_color == 4) {
                    ball_color = 9;
                } else {
                    ball_color = 2;
                }
            }
            ;
            if (keystr.equals("q")) {
                break;
            }
            ;
            // Refresh moving objects
            draw_frame_c(last_posx - 1, last_posx + 1, last_posy - 1, last_posy + 1, ' ', ball_color);
            draw_frame_c(posx - 1, posx + 1, posy - 1, posy + 1, '*', ball_color);
        }
        delay(20); //additional delay to limit the frequency of the event processing loop
    }
}

void main() {
    // Preparation of the scene
    cursor_hide();
    clrscr();
    setfgcolor(white);
    framexy(1, 1, 120, 30);
    gotoxy(20, 30);
    write("  Use arrow keys to move the square. Use 'c' to change the square's colour. Use 'q' to quit  ");
    gotoxy(60 - 6, 1);
    write("  Exercise 8  ");
    setbgcolor(black);
    cursor_hide();
    int posx = 65;
    int posy = 15;
    int ball_color = ltgreen;

    // Initial position
    gotoxy(posx, posy);
    setfgcolor(ball_color);
    draw_frame_c(posx - 1, posx + 1, posy - 1, posy + 1, '*', ball_color);

    square_control(posx, posy, ball_color);
    // Clear the terminal before exit
    clrscr();
    gotoxy(1, 1);
}