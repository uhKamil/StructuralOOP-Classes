/* Backup before converting the loop into a procedure */
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
    if (posx > 1+1 && posx < 120-1) {
        return 1;
    }
    return 0;
}

int guard_pos_y(int posy) {
    if (posy > 1+1 && posy < 30-1) {
        return 1;
    }
    return 0;
}

void main() {
    //The example code showing basic non-blocking processing of the keyboard
    //Non-blocking means we do not need to wait if nothing is being pressed
    //Change this code to present your solutions.

    //1. Preparation of the scene
    cursor_hide();
    clrscr();
    setfgcolor(white);
    framexy(1, 1, 120, 30);
    gotoxy(20, 30);
    write("   Use arrow keys to move the square. Use 'c' to change the square's colour. Use 'q' to quit   ");
    gotoxy(60-6, 1);
    write("  Exercise 7  ");
    
    setbgcolor(black);
    
    cursor_hide();
    int posx = 65;
    int posy = 15;
    int last_posx; int last_posy;
    int ball_color = ltgreen;
    int time_color = red;

    gotoxy(posx, posy);
    setfgcolor(ball_color);
    draw_frame_c(posx-1, posx+1, posy-1, posy+1, '*', ball_color);

    long time_start = System.nanoTime();

    //2. main loop for listening and processing keyboard events
    while (true) {
        if (keypressed()) {              //check if any key was pressed
            String keystr = readkeystr(); //something was pressed so read what key it was

            last_posx = posx;
            last_posy = posy;
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
                }
                else if (ball_color == 4) {
                    ball_color = 9;
                    }
                else {
                    ball_color = 2;
                }
            }
            ;
            if (keystr.equals("q")) {
                break;
            }
            ;

            //3. refresh moving objects
            draw_frame_c(last_posx-1, last_posx+1, last_posy-1, last_posy+1, ' ', ball_color);
            draw_frame_c(posx-1, posx+1, posy-1, posy+1, '*', ball_color);
        }

        long time_now = System.nanoTime();   //measure and display passed seconds
        gotoxy(50, 4);
        setfgcolor(white);
        print("Seconds passed: ");
        setfgcolor(time_color);
        print(((time_now - time_start) / 1000000000));  //nanoseconds to seconds

        delay(20);        //additional delay to limit the frequency of the event processing loop
    }
    //5. clear the terminal before exit
    clrscr();
    gotoxy(1, 1);
}