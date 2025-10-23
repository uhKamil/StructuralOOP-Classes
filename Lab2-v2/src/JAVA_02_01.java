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
void draw_vert_line(int x, int y1, int y2) {
    if (y1 > y2) {
        int temp = y1;
        y1 = y2;
        y2 = temp;
    }

    for (int i = y1; i <= y2; i++) {
        gotoxy(x, i);
        print("*");
    }
}

void draw_horiz_line(int x1, int x2, int y) {
    if (x1 > x2) {
        int temp = x1;
        x1 = x2;
        x2 = temp;
    }
    
    for (int i = x1; i <= x2; i++) {
        gotoxy(i, y);
        print("*");
    }
}


void main() {
    Scanner scanner = new Scanner(System.in);
    
    while (true) {
        clrscr();
        setfgcolor(white);
        int choice = -1;
        print("\nDrawing lines\n1 - Vertical line\n" +
                "2 - Horizontal line\n0 - Exit\nEnter your choice: ");
        choice = scanner.nextInt();
        delay(1000);
        clrscr();
        if (choice == 0) {
            break;
        }
        else if (choice == 1) {
             print("Enter the x coordinate: ");
             int x = scanner.nextInt();
             print("Enter the y1 coordinate: ");
             int y1 = scanner.nextInt();
             print("Enter the y2 coordinate: ");
             int y2 = scanner.nextInt();
             delay(1000);
             clrscr();
             draw_vert_line(x, y1, y2);
             delay(1000);
        }
        else if (choice == 2) {
            int x1 = scanner.nextInt();
            print("Enter the x2 coordinate: ");
            int x2 = scanner.nextInt();
            print("Enter the y coordinate: ");
            int y = scanner.nextInt();
            delay(1000);
            clrscr();
            draw_horiz_line(x1, x2, y);
            delay(1000);
        }
    }
}