import static java.lang.IO.*;  //including package to be able to use simple print()

//move cursor position to column x, row y
void gotoxy(int x, int y) {
    String GOTO_XY = "\u001b[%d;%dH";
    print(String.format(GOTO_XY, y, x));
}

//clear the terminal window
void clrscr() {
    String CLEAR_SCREEN = "\u001b[2J";
    print(String.format(CLEAR_SCREEN));
}

void cursor_hide() {
    String HIDE_CURSOR = "\u001b[?25l";
    print(String.format(HIDE_CURSOR));
}

void cursor_show() {
    String SHOW_CURSOR = "\u001b[?25h";
    print(String.format(SHOW_CURSOR));
}

void delay(int msec) {
    try {
        Thread.sleep(msec);
    } catch (InterruptedException e) {}
}

//constants with identifiers of basic colors
//see: https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit
final int black = 0;
final int brown = 1;
final int green = 2;
final int yellow = 3;
final int blue = 4;
final int magenta = 5;
final int cyan = 6;
final int ltgrey = 7;
final int grey = 8;
final int red = 9;
final int ltgreen = 10;
final int ltyellow = 11;
final int ltblue = 12;
final int white = 15;

//set text foreground color
void setfgcolor(int n) {
    String SET_FG_COLOR = "\u001b[38;5;%dm";
    print(String.format(SET_FG_COLOR,n));
}

//set text background color
void setbgcolor(int n) {
    String SET_BG_COLOR = "\u001b[48;5;%dm";
    print(String.format(SET_BG_COLOR,n));
}

//add your procedures here
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


void crazy_squares() {
    // Initial square coordinates
    int[ ] posX1 = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
    int[ ] posY1 = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
    int[ ] posX2 = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
    int[ ] posY2 = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
    int[ ] posX3 = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
    int[ ] posY3 = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75};
    for (int i = 0; i <= 14; i++) {
        posX1[i] = 5 + (int) (Math.random() * 80);
        posX2[i] = 5 + (int) (Math.random() * 80);
        posX3[i] = 5 + (int) (Math.random() * 80);
        posY1[i] = 5 + (int) (Math.random() * 20);
        posY2[i] = 5 + (int) (Math.random() * 20);
        posY3[i] = 5 + (int) (Math.random() * 20);
    }
    char c = '*';
    while (true) {
        for (int i = 0; i <= 14; i++) {
            draw_frame_c(posX1[i] - 1, posX1[i] + 1, posY1[i] - 1, posY1[i] + 1, c, 2);
            draw_frame_c(posX2[i] - 1, posX2[i] + 1, posY2[i] - 1, posY2[i] + 1, c, 4);
            draw_frame_c(posX3[i] - 1, posX3[i] + 1, posY3[i] - 1, posY3[i] + 1, c, 9);
            delay(200);
            draw_frame_c(posX1[i] - 1, posX1[i] + 1, posY1[i] - 1, posY1[i] + 1, ' ', 15);
            draw_frame_c(posX2[i] - 1, posX2[i] + 1, posY2[i] - 1, posY2[i] + 1, ' ', 15);
            draw_frame_c(posX3[i] - 1, posX3[i] + 1, posY3[i] - 1, posY3[i] + 1, ' ', 15);
        }
    }
}


void main() {
    clrscr();
    print("\nPlanned squares");
    delay(1500);
    cursor_hide();
    clrscr();
    crazy_squares();
}