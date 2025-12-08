package Ex3;

import static java.lang.IO.print;
import static term.term.*;

public class JAVA_06_03 {
    // ==================== MODEL ====================
    public static class TPoint {
        public int x, y;
    }

    public static class TSection {
        public TPoint start, end;
    }

    public static class TSectionPath {
        public TSection[] sections = new TSection[200];
        public int count = 0;
        public char pathChar;
        public int color;
    }

    public static class MovingString {
        public String text;
        public TSectionPath path;
        public double prevPosition;
        public double position;
        public int direction;
        public double speed;
        public int color;
        public boolean isBouncing;
        public int prevIndexStart = -1;
    }

    public static class AppModel {
        public TSectionPath path1, path2, path3, path4;
        public MovingString strBeReady1, strBeReady2;
        public MovingString strWelcome, strPress;
        public MovingString strSpiral;
        public MovingString strPathmania;
        public boolean isRunning = true;
    }

    public static class AppController {
        private AppModel model;
        private final int TERMINAL_WIDTH = 120;
        private final int TERMINAL_HEIGHT = 30;
    }

    // Factories
    public static TPoint point(int x, int y) {
        TPoint point = new TPoint();
        point.x = x;
        point.y = y;
        return point;
    }

    public static TSection section(TPoint start, TPoint end) {
        TSection section = new TSection();
        section.start = start;
        section.end = end;
        return section;
    }

    public static void addSection(TSectionPath path, TPoint start, TPoint end) {
        if (path.count < path.sections.length) path.sections[path.count++] = section(start, end);
    }

    public MovingString createMovingString(String text, TSectionPath path, int startIdx, int direction, int color, boolean bounces) {
        MovingString str = new MovingString();
        str.text = text;
        str.path = path;
        str.position = startIdx;
        str.direction = direction;
        str.color = color;
        str.isBouncing = bounces;
        str.speed = 5.0;
        return str;
    }

    public AppController controller(AppModel model) {
        AppController controller = new AppController();
        controller.model = model;
        return controller;
    }

    // Logic functions
    public static int GetSectionLength(TSection s) {
        return Math.max(Math.abs(s.end.x - s.start.x), Math.abs(s.end.y - s.start.y)) + 1;
    }

    public static TPoint GetSectionNthPoint(TSection s, int n) {
        int dx = Integer.compare(s.end.x, s.start.x);
        int dy = Integer.compare(s.end.y, s.start.y);
        return point(s.start.x + (n * dx), s.start.y + (n * dy));
    }

    public static int GetPathLength(TSectionPath path) {
        int totalLen = 0;
        for (int i = 0; i < path.count; i++) {
            totalLen += GetSectionLength(path.sections[i]);
            if (i > 0) {
                TSection prev = path.sections[i - 1];
                TSection curr = path.sections[i];
                if (prev.end.x == curr.start.x && prev.end.y == curr.start.y) {
                    totalLen--;
                }
            }
        }
        return totalLen;
    }

    public static TPoint GetPathNthPoint(TSectionPath path, int n) {
        if (path.count == 0) return point(0, 0);
        int totalLen = GetPathLength(path);
        if (totalLen == 0) return path.sections[0].start;

        n = n % totalLen;
        while (n < 0) n += totalLen;

        int currentPos = 0;
        for (int i = 0; i < path.count; i++) {
            TSection s = path.sections[i];
            int sectionLen = GetSectionLength(s);
            boolean connected = (i > 0 && path.sections[i - 1].end.x == s.start.x && path.sections[i - 1].end.y == s.start.y);
            int currentLen = connected ? sectionLen - 1 : sectionLen;

            if (n < currentPos + currentLen) {
                int remainder = n - currentPos;
                int localIndex = connected ? remainder + 1 : remainder;
                return GetSectionNthPoint(s, localIndex);
            }
            currentPos += currentLen;
        }
        return path.sections[path.count - 1].end;
    }

    // Path Generators
    public static TSectionPath createSquarePath(TPoint A, TPoint B, TPoint C, TPoint D, int color) {
        TSectionPath path = new TSectionPath();
        path.pathChar = '*';
        path.color = color;
        addSection(path, A, B);
        addSection(path, B, C);
        addSection(path, C, D);
        addSection(path, D, A);
        return path;
    }

    public enum SpiralOrientation {HORIZONTAL, VERTICAL}

    public static TSectionPath createSpiralPath(TPoint center, int cycles, int spacing, int initialLength, int color, SpiralOrientation orientation) {
        TSectionPath path = new TSectionPath();
        path.pathChar = '+';
        path.color = color;

        TPoint current = point(center.x, center.y);
        int moveIdx; // 0:R, 1:D, 2:L, 3:U
        if (orientation == SpiralOrientation.VERTICAL) moveIdx = 0;
        else moveIdx = 1;

        int lengthOriented = initialLength;
        int length = spacing;
        int changes = 0;

        for (int i = 0; i < cycles * 4; i++) {
            TPoint next = point(current.x, current.y);

            switch (moveIdx) {
                case 0:
                    if (orientation == SpiralOrientation.HORIZONTAL) next.x += lengthOriented;
                    else next.x += length;
                    break;
                case 1:
                    if (orientation == SpiralOrientation.VERTICAL) next.y += lengthOriented;
                    else next.y += length;
                    break;
                case 2:
                    if (orientation == SpiralOrientation.HORIZONTAL) next.x -= lengthOriented;
                    else next.x -= length;
                    break;
                case 3:
                    if (orientation == SpiralOrientation.VERTICAL) next.y -= lengthOriented;
                    else next.y -= length;
                    break;
            }

            addSection(path, current, next);
            current = next;

            moveIdx = (moveIdx + 1) % 4;
            changes++;
            if (changes % 2 == 0) {
                length += spacing;
                if (orientation == SpiralOrientation.HORIZONTAL) lengthOriented += 2 * spacing;
                else lengthOriented += spacing + 1;
            }
        }
        return path;
    }

    public static TSectionPath createSnakePath(int color, int w, int h) {
        TSectionPath path = new TSectionPath();
        path.pathChar = '+';
        path.color = color;

        // Snake shape based on diagram
        TPoint p1 = point(6, 3);
        TPoint p2 = point(55, p1.y);
        TPoint p3 = point(p2.x, 9);
        TPoint p4 = point(p2.x, 21);
        TPoint p5 = point(p2.x, 24);
        TPoint p6 = point(61, p5.y);
        TPoint p7 = point(64, 21);
        TPoint p8 = point(81, 18);
        TPoint p9 = point(85, 14);
        TPoint p10 = point(p9.x, 12);
        TPoint p11 = point(81, p10.y);
        TPoint p12 = point(39, p10.y);
        TPoint p13 = point(35, p10.y);
        TPoint p14 = point(p13.x, p9.y);
        TPoint p15 = point(p13.x, p8.y);
        TPoint p16 = point(p13.x, p5.y);
        TPoint p17 = point(52, p5.y);
        TPoint p18 = point(p7.x, p5.y);
        TPoint p19 = point(p9.x, p5.y);
        TPoint p20 = point(p9.x, p5.y + 2);
        TPoint p21 = point(w - 5, p20.y);
        TPoint p22 = point(p21.x, p9.y - 2);
        TPoint p23 = point(p21.x, p22.y - 2);
        TPoint p24 = point(p21.x, p1.y);
        TPoint p25 = point(p13.x, p1.y);
        TPoint p26 = point(p25.x, p25.y + 2);
        TPoint p27 = point(60, p26.y);
        TPoint p28 = point(p27.x, p1.y);
        TPoint p29 = point(p28.x + 3, p1.y);
        TPoint p30 = point(p29.x, p27.y);
        TPoint p31 = point(p10.x, p30.y);
        TPoint p32 = point(p31.x, p31.y + 2);
        TPoint p33 = point(p20.x - 5, p20.y);
        TPoint p34 = point(p1.x, p33.y);
        TPoint p35 = point(p1.x, p16.y - 2);
        TPoint p36 = point(p1.x, p15.y);
        TPoint p37 = point(p1.x, p14.y);
        TPoint p38 = point(p1.x, p13.y + 1);

        addSection(path, p1, p2);
        addSection(path, p2, p3);
        addSection(path, p4, p5);
        addSection(path, p5, p6);
        addSection(path, p6, p7);
        addSection(path, p8, p9);
        addSection(path, p9, p10);
        addSection(path, p10, p11);
        addSection(path, p12, p13);
        addSection(path, p13, p14);
        addSection(path, p15, p16);
        addSection(path, p16, p17);
        addSection(path, p18, p19);
        addSection(path, p19, p20);
        addSection(path, p20, p21);
        addSection(path, p21, p22);
        addSection(path, p23, p24);
        addSection(path, p24, p25);
        addSection(path, p25, p26);
        addSection(path, p26, p27);
        addSection(path, p27, p28);
        addSection(path, p29, p30);
        addSection(path, p30, p31);
        addSection(path, p31, p32);
        addSection(path, p33, p34);
        addSection(path, p34, p35);
        addSection(path, p36, p37);
        addSection(path, p38, p1);
        return path;
    }

    private void updatePhysics(AppModel model, double dt) {
        updateString(model.strBeReady1, dt);
        updateString(model.strBeReady2, dt);
        updateString(model.strWelcome, dt);
        updateString(model.strPress, dt);
        updateString(model.strSpiral, dt);
        updateString(model.strPathmania, dt);
        checkBounce(model.strWelcome, model.strPress, GetPathLength(model.path1));
    }

    private void updateString(MovingString ms, double dt) {
        ms.prevPosition = ms.position;

        if (ms.speed < 20.0) {
            ms.speed += dt * 2.0;
        }

        ms.position += ms.direction * ms.speed * dt;

        int pathLen = GetPathLength(ms.path);
        int textLen = ms.text.length();

        if (ms.isBouncing) {
            if (ms.direction == 1 && ms.position + textLen >= pathLen) {
                ms.position = pathLen - textLen;
                reverseDirection(ms);
            } else if (ms.direction == -1 && ms.position - textLen + 1 <= 0) {
                ms.position = textLen - 1;
                reverseDirection(ms);
            }
        } else {
            if (ms.position < 0) ms.position += pathLen;
            if (ms.position >= pathLen) ms.position -= pathLen;
        }
    }

    private void checkBounce(MovingString s1, MovingString s2, int pathLen) {
        int head1 = (int) s1.position;
        if (s1.direction == 1) head1 += s1.text.length() - 1;
        else head1 -= s1.text.length() - 1;

        int head2 = (int) s2.position;
        if (s2.direction == 1) head2 += s2.text.length() - 1;
        else head2 -= s2.text.length() - 1;

        head1 = (head1 % pathLen + pathLen) % pathLen;
        head2 = (head2 % pathLen + pathLen) % pathLen;

        int dist = Math.abs(head1 - head2);
        if (dist > pathLen / 2) dist = pathLen - dist;

        if (dist < 2.25 && s1.direction != s2.direction) {
            reverseDirection(s1);
            reverseDirection(s2);
            s1.position += s1.direction;
            s2.position += s2.direction;
        }
    }

    private void reverseDirection(MovingString ms) {
        ms.direction *= -1;
        ms.speed = 5.0;

        if (ms.direction == -1) ms.position = ms.position + ms.text.length();
        else ms.position = ms.position - ms.text.length();
    }

    // ==================== VIEW ====================
    public void drawPathFull(TSectionPath path) {
        setfgcolor(path.color);
        for (int i = 0; i < path.count; i++) {
            TSection s = path.sections[i];
            int len = GetSectionLength(s);
            for (int j = 0; j < len; j++) {
                TPoint p = GetSectionNthPoint(s, j);
                gotoxy(p.x, p.y);
                print(path.pathChar);
            }
        }
    }

    public void clearString(MovingString ms) {
        if (ms.prevIndexStart == -1) return;

        setfgcolor(ms.path.color);

        int totalLen = GetPathLength(ms.path);
        int idx = ms.prevIndexStart;

        for (int i = 0; i < ms.text.length(); i++) {
            int normIdx = idx % totalLen;
            if (normIdx < 0) normIdx += totalLen;

            if (ms.isBouncing || !(idx < 0 || idx >= totalLen)) {
                TPoint p = GetPathNthPoint(ms.path, normIdx);
                gotoxy(p.x, p.y);
                print(ms.path.pathChar);
            }

            if (ms.direction == 1) idx++;
            else idx--;
        }
    }

    public void drawString(MovingString ms) {
        setfgcolor(ms.color);
        int totalLen = GetPathLength(ms.path);
        int idx = (int) ms.position;

        ms.prevIndexStart = idx;

        for (int i = 0; i < ms.text.length(); i++) {
            int normIdx = idx % totalLen;
            if (normIdx < 0) normIdx += totalLen;

            boolean draw = true;
            if (ms.isBouncing) {
                if (idx < 0 || idx >= totalLen) draw = false;
            }

            if (draw) {
                TPoint p = GetPathNthPoint(ms.path, normIdx);
                gotoxy(p.x, p.y);
                print(ms.text.charAt(i));
            }

            if (ms.direction == 1) idx++;
            else idx--;
        }
        setfgcolor(7);
    }

    private void render(AppModel model) {
        clearString(model.strBeReady1);
        clearString(model.strBeReady2);
        if ((int) model.strWelcome.prevPosition != (int) model.strWelcome.position) clearString(model.strWelcome);
        if ((int) model.strPress.prevPosition != (int) model.strPress.position) clearString(model.strPress);
        clearString(model.strSpiral);
        clearString(model.strPathmania);
        drawString(model.strBeReady1);
        drawString(model.strBeReady2);
        drawString(model.strWelcome);
        drawString(model.strPress);
        drawString(model.strSpiral);
        drawString(model.strPathmania);
    }

    // ==================== CONTROLLER ====================
    public void init(AppController controller) {
        AppModel model = controller.model;

        TPoint A = point(1, 1);
        TPoint B = point(controller.TERMINAL_WIDTH, 1);
        TPoint C = point(controller.TERMINAL_WIDTH, controller.TERMINAL_HEIGHT);
        TPoint D = point(1, controller.TERMINAL_HEIGHT);
        model.path1 = createSquarePath(A, B, C, D, white); // Outer square path
        model.path2 = createSquarePath(point(40, 10), point(80, 10), point(80, 20), point(40, 20), white); // Inner square path
        model.path3 = createSnakePath(green, controller.TERMINAL_WIDTH, controller.TERMINAL_HEIGHT); // Path (1, 2, ..., 38, 1)
        model.path4 = createSpiralPath(point(controller.TERMINAL_WIDTH / 2, controller.TERMINAL_HEIGHT / 2), 2, 2, 12, green, SpiralOrientation.HORIZONTAL); // Spiral at the center

        int topCenter = (B.x - A.x) / 2;
        int bottomCenter = (B.x - A.x) + (C.y - B.y) + (C.x - D.x) / 2;
        model.strWelcome = createMovingString(">> Welcome to our world! <<", model.path1, topCenter, 1, ltgreen, false);
        model.strPress = createMovingString(">> Press space to start <<", model.path1, bottomCenter, -1, cyan, false);
        model.strBeReady1 = createMovingString("Be ready!", model.path2, 0, 1, white, false);
        model.strBeReady2 = createMovingString("Be ready!", model.path2, GetPathLength(model.path2) / 2, 1, white, false);
        model.strPathmania = createMovingString("Pathmania mode! Pathy paths everywhere!", model.path3, 0, 1, white, false);
        model.strSpiral = createMovingString("SPIRAL SPIRAL", model.path4, 0, 1, red, true);
        
        drawPathFull(controller.model.path1);
        drawPathFull(controller.model.path2);
        drawPathFull(controller.model.path3);
        drawPathFull(controller.model.path4);
    }

    private void handleInput(AppController controller) {
        if (keypressed()) {
            String k = readkeystr();
            if (k.equals("q")) controller.model.isRunning = false;
            if (k.equals("p")) reverseDirection(controller.model.strPathmania);
        }
    }

    public void run(AppController controller) {
        init(controller);
        render(controller.model);

        long lastTime = System.currentTimeMillis();

        while (controller.model.isRunning) {
            long now = System.currentTimeMillis();
            double dt = (now - lastTime) / 1000.0;
            lastTime = now;

            handleInput(controller);
            updatePhysics(controller.model, dt);
            render(controller.model);

            delay(15);
        }
    }

    void main() {
        clrscr();
        cursor_hide();

        AppModel model = new AppModel();
        AppController controller = controller(model);
        run(controller);

        setfgcolor(7);
        clrscr();
    }
}
