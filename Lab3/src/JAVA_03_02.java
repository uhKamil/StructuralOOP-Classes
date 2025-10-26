import static java.lang.IO.*;  //including package to be able to use simple print()

//move the cursor position to column x, row y
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
int tree_width(int n) {
    if (n <= 4) {
        return 1;
    }
    return n-3;
    // e.g., 20 (max * in a row) -> height=21 -> n-3=18
}

int stars_in_line(int i) {
    return i;
}

String tree_line(int i, String c) {
    return c.repeat(Math.max(0, i));
}

void draw_tree_branches(int x, int y, int n) {
    setfgcolor(brown);
    gotoxy((x-1) + 2, (y-1) + (n-1) + 1);
    print(tree_line(tree_width(n), "|"));
}

void draw_ctree(int x, int y, int n) {
    int rows = n - 1; // the height of the tree is the number of leaves' rows plus the row for the trunk, so we have to subtract one row
    // Trunk
    draw_tree_branches(x, y, n);
    // The green part
    setfgcolor(green);
    int m = 0;
    if (rows == 1) {
        gotoxy((x-1) + 2, (y-1) + 1);
        print('*');
    } else {
        for (int i = rows; i >= 1; i--) {
            gotoxy((x-1) + 1 + m / 2, (y-1) + i);
            print(tree_line(stars_in_line(i), "*"));
            m += 1;
        }
    }
}

void main() {
    Scanner scanner = new Scanner(System.in);
    int height; int posX; int posY;
    print("Enter the height of the christmas tree: ");
    height = scanner.nextInt();
    print("Specify the X position of the top-left corner of the tree: ");
    posX = scanner.nextInt();    
    print("Specify the Y position of the top-left corner of the tree: ");
    posY = scanner.nextInt();
    clrscr();
    cursor_hide();
    setbgcolor(white);
    draw_ctree(posX, posY, height);
    delay(15000);
}