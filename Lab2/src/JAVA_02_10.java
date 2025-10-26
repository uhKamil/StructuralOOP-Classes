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

static boolean spiral_end(int i, int j, int l, int x, int y) {
    if ((i + j + l) % 4 == 0) {
        return y - i < 1;
    } else if ((i + j + l) % 4 == 1) {
        return x + i > 120;
    } else if ((i + j + l) % 4 == 2) {
        return y + i > 30;
    } else if ((i + j + l) % 4 == 3) {
        return x - i < 1;
    }
    return (x > 120 || y > 30);
}


//add your procedures here
void coloured_spiral(int x, int y, char c, int s) {
    // The number of rounds is one move in each direction
    // The number of steps in each round is equal to the round's number
    // Direction is given according to the remainder: 0 - up, 1 - right, 2 - down, 3 - left
    // An additional array so we don't get an error; (int) (Math.random() * 15) can't be used (or can it?)
    int[] colors = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15};
    int l = 0; // to correct the length needed for the round (length corrector)
    boolean continueSpiral = true;
    // because of the j loop we divide the number of rounds by 2 and add 1 so not to skip the odd round
    for (int i = 1; i <= s / 2 + 1; i++) {
        for (int j = 0; j <= 1; j++) {
            if (spiral_end(i, j, l, x, y)) {
                continueSpiral = false;
                break;
            }
            if (continueSpiral) {
                for (int k = 1; k <= i; k++) {
                    gotoxy(x, y);
                    setfgcolor(colors[(int) (Math.random() * colors.length)]);
                    if ((i + j + l) % 4 == 0) {
                        y -= 1;
                    } else if ((i + j + l) % 4 == 1) {
                        x += 1;
                    } else if ((i + j + l) % 4 == 2) {
                        y += 1;
                    } else {
                        x -= 1;
                    }
                    print(c);
                    delay(15);
                    // Logging purposes
//                    setfgcolor(white);
//                    gotoxy(125, 30);
//                    print(x);
//                    gotoxy(128, 30);
//                    print(y);
//                    gotoxy(125, 31);
//                    print((i + j + l) % 4);
                }
            }
        }
        l += 1;
    }
}


void main() {
    Scanner scanner = new Scanner(System.in);
    clrscr();
    print("The colored spiral");
    print("\nSpecify the starting X position: ");
    int posX = scanner.nextInt();
    print("Specify the starting Y position: ");
    int posY = scanner.nextInt();
    print("Specify the character for the spiral: ");
    char ch = scanner.next().charAt(0);
    print("Specify the number of rounds: ");
    int rounds = scanner.nextInt();
    cursor_hide();
    clrscr();
    coloured_spiral(posX, posY, ch, rounds);
    delay(3000);
}