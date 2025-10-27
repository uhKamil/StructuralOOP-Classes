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
    } catch (InterruptedException e) {
    }
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
    print(String.format(SET_FG_COLOR, n));
}

//set text background color
void setbgcolor(int n) {
    String SET_BG_COLOR = "\u001b[48;5;%dm";
    print(String.format(SET_BG_COLOR, n));
}

// procedures
void christmas_tree(int rows) {
    // Trunk
    setfgcolor(brown);
    if (rows == 1 || rows == 2 || rows == 3 || rows == 4) {
        gotoxy(2, rows + 1);
        print('|');
    } else {
        gotoxy(rows / 2 + rows % 2, rows + 1);
        print("║");
    }
    // The green part
    setfgcolor(green);
    int m = 0;
    if (rows == 1) {
        gotoxy(2, 1);
        print('*');
    }
    else {
        for (int i = rows; i >= 1; i--) {
            for (int j = i; j >= 1; j--) {
                gotoxy(j + m / 2, i);
                print('*');
                delay(5);
            }
            m += 1;
        }
    }
}

void main() {
    Scanner scanner = new Scanner(System.in);
    int rows = 0;
    while (rows < 1 || rows > 22) {
        print("Enter the number of rows: ");
        rows = scanner.nextInt();
    }
    clrscr();
    cursor_hide();
    setbgcolor(white);
    christmas_tree(rows);
    delay(15000);
}