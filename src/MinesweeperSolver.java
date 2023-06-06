public class MinesweeperSolver {
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

    int attempts;
    int wins;
    Square[][] solverBoard;
    int row;
    int col;
    public MinesweeperSolver() {
        attempts = 0;
        wins = 0;
        row = 0;
        col = 0;
    }
    public void solve(Minesweeper minesweeper) {
        solverBoard = new Square[minesweeper.getRows()][minesweeper.getColumns()];
        // first click (middle)
        row = solverBoard.length / 2;
        col = solverBoard[0].length / 2;
        minesweeper.click(row, col);
        solverBoard[row][col] = interpret(minesweeper.readSquare(row, col));

        // Method 1: keep going down and to the right until you hit something. If covered = number, flag em all!
        /*
        while(solverBoard[row][col].getNumber() == 0 && row < minesweeper.getRows() - 1) {
            row++;
            minesweeper.click(row, col);
            solverBoard[row][col] = interpret(minesweeper.readSquare(row, col));
        }
        while(solverBoard[row][col].getNumber() == 0 && col < minesweeper.getColumns() - 1) {
            col++;
            minesweeper.click(row, col);
            solverBoard[row][col] = interpret(minesweeper.readSquare(row, col));
        }

        int[] bounds = getBounds(minesweeper, row, col);
        int rl = bounds[0];
        int ru = bounds[1];
        int cl = bounds[2];
        int cu = bounds[3];
        int covered = 0;
        for(int i = rl; i <= ru; i++) {
            for(int j = cl; j <= cu; j++) {
                if(interpret(minesweeper.readSquare(i, j)).isCovered() && !(i == row && j == col)) {
                    covered++;
                    solverBoard[i][j] = new Square();
                    solverBoard[i][j].cover();
                }
            }
        }
        if(solverBoard[row][col].getNumber() == covered) {
            for(int i = rl; i <= ru; i++) {
                for(int j = cl; j <= cu; j++) {
                    if(interpret(minesweeper.readSquare(i, j)).isCovered() && !(i == row && j == col)) {
                        minesweeper.flag(i, j);
                        solverBoard[i][j].toggleFlag();
                    }
                }
            }
        }
        */


        updateBoard(minesweeper);


        // Method 2: Same, but for all the squares!
        // Repeat until exhausted :)
        int runs = 1;
        int failsafe = 0;
        while(runs > 0 && failsafe < (minesweeper.getRows() * minesweeper.getColumns())) {
            failsafe++;
            runs = 0;
            for(int r = 0; r < solverBoard.length; r++) {
                for(int c = 0; c < solverBoard[0].length; c++) {
                    row = r;
                    col = c;
                    //System.out.println("Checking (" + row + ", " + col + ")");
                    int[] bounds = getBounds(minesweeper, row, col);
                    int rl = bounds[0];
                    int ru = bounds[1];
                    int cl = bounds[2];
                    int cu = bounds[3];
                    int covered = 0;
                    for(int i = rl; i <= ru; i++) {
                        for(int j = cl; j <= cu; j++) {
                            if(interpret(minesweeper.readSquare(i, j)).isCovered() && !(i == row && j == col)) {
                                covered++;
                                solverBoard[i][j] = new Square();
                                solverBoard[i][j].cover();
                            }
                        }
                    }
                    //System.out.print("Covered: " + covered + ", Number: " + solverBoard[row][col].getNumber() + ", Flagging in progress: ");
                    if(solverBoard[row][col].getNumber() == covered) {

                        for(int i = rl; i <= ru; i++) {
                            for(int j = cl; j <= cu; j++) {
                                if(interpret(minesweeper.readSquare(i, j)).isCovered() && !(i == row && j == col)) {
                                    if(!interpret(minesweeper.readSquare(i, j)).isFlagged()) {
                                        //System.out.print(" flag");
                                        minesweeper.toggleFlag(i, j);
                                        solverBoard[i][j].setFlagged(true);
                                        runs++;
                                    }
                                }
                            }
                        }
                    }
                    //System.out.println();
                    updateBoard(minesweeper);
                }
            }

            // And we continue by then clicking all the squares we can :)
            for(int r = 0; r < solverBoard.length; r++) {
                for(int c = 0; c < solverBoard[0].length; c++) {
                    row = r;
                    col = c;
                    //System.out.println("Checking (" + row + ", " + col + ")");
                    int[] bounds = getBounds(minesweeper, row, col);
                    int rl = bounds[0];
                    int ru = bounds[1];
                    int cl = bounds[2];
                    int cu = bounds[3];
                    int flagged = 0;
                    for(int i = rl; i <= ru; i++) {
                        for(int j = cl; j <= cu; j++) {
                            if(interpret(minesweeper.readSquare(i, j)).isFlagged() && !(i == row && j == col)) {
                                flagged++;
                            }
                        }
                    }
                    //System.out.println("Flagged: " + flagged + ", Number: " + solverBoard[row][col].getNumber());
                    if(solverBoard[row][col].getNumber() == flagged && flagged > 0) {

                        for(int i = rl; i <= ru; i++) {
                            for(int j = cl; j <= cu; j++) {
                                if(interpret(minesweeper.readSquare(i, j)).isCovered() && !interpret(minesweeper.readSquare(i, j)).isFlagged() && !(i == row && j == col)) {
                                    minesweeper.click(i, j);
                                    runs++;
                                }
                            }
                        }
                    }
                    updateBoard(minesweeper);
                }
            }
        }
        minesweeper.print();
    }
    public Square interpret(String s) {
        Square q = new Square();
        switch(s) {
            case ANSI_GREEN + "[" + ANSI_RESET + " " + ANSI_GREEN + "]" + ANSI_RESET:
                q.cover();
            break;
            case ANSI_GREEN + "[" + ANSI_RESET + ANSI_RED + "F" + ANSI_RESET + ANSI_GREEN + "]" + ANSI_RESET:
                q.cover();
                q.setFlagged(true);
            break;
            case "[ ]":
                q.uncover();
                q.setNumber(0);
            break;
            case "[" + ANSI_RED + "M" + ANSI_RESET + "]":
                q.uncover();
                q.setMine(true);
            break;
            case "[" + ANSI_BLUE + "1" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(1);
            break;
            case "[" + ANSI_GREEN + "2" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(2);
            break;
            case "[" + ANSI_RED + "3" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(3);
            break;
            case "[" + ANSI_PURPLE + "4" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(4);
            break;
            case "[" + ANSI_YELLOW + "5" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(5);
            break;
            case "[" + ANSI_CYAN + "6" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(6);
            break;
            case "[" + ANSI_BLACK + "7" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(7);
            break;
            case "[" + ANSI_WHITE + "8" + ANSI_RESET + "]":
                q.uncover();
                q.setNumber(8);
            break;
            default:
                System.out.println("just a moment");
        }
        return q;
    }
    public int[] getBounds(Minesweeper ms, int r, int c) {
        int rl = r - 1;
        int ru = r + 1;
        int cl = c - 1;
        int cu = c + 1;
        if(rl < 0) {
            rl = 0;
        }
        if(ru > ms.getRows() - 1) {
            ru = ms.getRows() - 1;
        }
        if(cl < 0) {
            cl = 0;
        }
        if(cu > ms.getColumns() - 1) {
            cu = ms.getColumns() - 1;
        }
        return new int[]{rl, ru, cl, cu};
    }
    public void updateBoard(Minesweeper ms) {
        for(int i = 0; i < ms.getRows(); i++) {
            for(int j = 0; j < ms.getColumns(); j++) {
                solverBoard[i][j] = interpret(ms.readSquare(i, j));
            }
        }
    }
}
