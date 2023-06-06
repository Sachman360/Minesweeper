import java.util.*;
public class Minesweeper {
    // Colored text!
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    Random random = new Random();

    Square[][] minesweeper;
    int rows;
    int columns;
    int mines;
    int flags;
    String message;
    boolean gameOver;
    enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
    public Minesweeper(Difficulty difficulty) {
        switch(difficulty) {
            case EASY:
                rows = 8;
                columns = 10;
                mines = 10;
            break;
            case MEDIUM:
                rows = 14;
                columns = 18;
                mines = 40;
            break;
            case HARD:
                rows = 20;
                columns = 24;
                mines = 99;
            break;
        }
        minesweeper = new Square[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                minesweeper[i][j] = new Square();
            }
        }
        fillMines();
        fillNumbers();
        message = "";
        flags = mines;
        gameOver = false;
    }
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public void print() {
        System.out.print("  ");
        for(int j = 0; j < columns; j++) {
            if(j < 10) {
                System.out.print("  " + j);
            } else {
                System.out.print(" " + j);
            }
        }
        System.out.println();
        for(int i = 0; i < rows; i++) {
            if(i < 10) {
                System.out.print(i + "  ");
            } else {
                System.out.print(i + " ");
            }
            for(int j = 0; j < columns; j++) {
                System.out.print(minesweeper[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Flags: " + ANSI_RED + flags + ANSI_RESET);
        System.out.println(message);
        System.out.println();
    }
    public void uncoverAll() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                minesweeper[i][j].uncover();
            }
        }
    }
    public void unflagAll() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                minesweeper[i][j].setFlagged(false);
            }
        }
    }
    public void fillMines() {
        int m = totalMines();
        while (m < mines) {
            int r = random.nextInt(rows - 1);
            int c = random.nextInt(columns - 1);
            if(!minesweeper[r][c].isMine()) {
                minesweeper[r][c].setMine(true);
                m++;
            }
        }
    }
    public void fillNumbers() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(!minesweeper[i][j].isMine()) {
                    minesweeper[i][j].setNumber(countMines(i, j));
                }
            }
        }
    }
    public int countMines(int r, int c) {
        int[] bounds = getBounds(r, c);
        int rl = bounds[0];
        int ru = bounds[1];
        int cl = bounds[2];
        int cu = bounds[3];
        int m = 0;
        for(int i = rl; i <= ru; i++) {
            for(int j = cl; j <= cu; j++) {
                if(!(i == r && j == c)) {
                    if(minesweeper[i][j].isMine()) {
                        m++;
                    }
                }
            }
        }
        return m;
    }
    public int totalMines() {
        int m = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(minesweeper[i][j].isMine()) {
                    m++;
                }
            }
        }
        return m;
    }
    public int[] getBounds(int r, int c) {
        int rl = r - 1;
        int ru = r + 1;
        int cl = c - 1;
        int cu = c + 1;
        if(rl < 0) {
            rl = 0;
        }
        if(ru > rows - 1) {
            ru = rows - 1;
        }
        if(cl < 0) {
            cl = 0;
        }
        if(cu > columns - 1) {
            cu = columns - 1;
        }
        return new int[]{rl, ru, cl, cu};
    }
    public void click(int r, int c) {
        if(gameOver) {
            return;
        }
        if(isUntouched()) {
            firstClickZero(r, c);
        }
        if(r < 0 || r >= rows || c < 0 || c >= columns) {
            return;
        }
        Square q = minesweeper[r][c];
        if(q.isMine() && !gameOver) {
            gameOver();
            gameOver = true;
        } else if(q.getNumber() > 0) {
            q.uncover();
        } else {
            q.uncover();
            ArrayList<Integer> zeroRows = new ArrayList<Integer>();
            ArrayList<Integer> zeroColumns = new ArrayList<Integer>();
            zeroRows.add(r);
            zeroColumns.add(c);
            for(int k = 0; k < zeroRows.size(); k++) {
                int[] bounds = getBounds(zeroRows.get(k), zeroColumns.get(k));
                int rl = bounds[0];
                int ru = bounds[1];
                int cl = bounds[2];
                int cu = bounds[3];
                for(int i = rl; i <= ru; i++) {
                    for(int j = cl; j <= cu; j++) {
                        if(!(i == r && j == c)) {
                            if(minesweeper[i][j].getNumber() == 0 && minesweeper[i][j].isCovered()) {
                                zeroRows.add(i);
                                zeroColumns.add(j);
                            }
                            minesweeper[i][j].uncover();
                        }
                    }
                }
            }
        }
        if(countCovered() == mines && !gameOver) {
            win();
            gameOver = true;
        }
    }
    public void gameOver() {
        uncoverAll();
        message = ANSI_RED + "GAME OVER" + ANSI_RESET;
    }
    public void win() {
        unflagAll();
        message = ANSI_GREEN + "YOU WIN" + ANSI_RESET;
    }
    public String readSquare(int r, int c) {
        return minesweeper[r][c].toString();
    }
    public void toggleFlag(int r, int c) {
        if(gameOver) {
            return;
        }
        if(minesweeper[r][c].isFlagged()) {
            minesweeper[r][c].setFlagged(false);
            flags++;
        } else {
            minesweeper[r][c].setFlagged(true);
            flags--;
        }
    }
    public boolean isUntouched() {
        int uncovered = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(!minesweeper[i][j].isCovered()) {
                    uncovered++;
                }
            }
        }
        return uncovered == 0;
    }
    public void firstClick(int r, int c) {
        while(minesweeper[r][c].isMine()) {
            minesweeper[r][c].setMine(false);
            fillMines();
            fillNumbers();
        }
    }
    public void firstClickZero(int r, int c) {
        while(minesweeper[r][c].getNumber() != 0) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    minesweeper[i][j].setMine(false);
                }
            }
            fillMines();
            fillNumbers();
        }
    }
    public int countCovered() {
        int covered = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(minesweeper[i][j].isCovered()) {
                    covered++;
                }
            }
        }
        return covered;
    }
}
