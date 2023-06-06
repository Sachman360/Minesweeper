import java.io.*;
public class Square {
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

    // Square properties
    boolean covered;
    boolean flagged;
    int number;
    public Square() {
        covered = true;
        flagged = false;
        number = 0;
    }
    public void setMine(boolean mineState) {
        if(mineState) {
            number = -1;
        } else {
            number = 0;
        }
    }
    public void uncover() {
        covered = false;
    }
    public void cover() {
        covered = true;
    }
    public void setFlagged(boolean flag) {
        flagged = flag;
    }
    public void setNumber(int n) {
        if(-1 <= n && n <= 8) {
            number = n;
        } else {
            number = 0;
        }
    }
    public boolean isMine() {
        return number == -1;
    }
    public boolean isCovered() {
        return covered;
    }
    public boolean isFlagged() {
        return flagged;
    }
    public int getNumber() {
        return number;
    }
    public String toString() {
        String square = "";
        if(covered) {
           square += ANSI_GREEN + "[" + ANSI_RESET;
           if(flagged) {
               square += ANSI_RED + "F" + ANSI_RESET;
           } else {
               square += " ";
           }
           square += ANSI_GREEN + "]" + ANSI_RESET;
        } else {
            square += "[";
            switch(number) {
                case -1:
                    square += ANSI_RED + "M" + ANSI_RESET;
                break;
                case 0:
                    square += " ";
                break;
                case 1:
                    square += ANSI_BLUE + "1" + ANSI_RESET;
                break;
                case 2:
                    square += ANSI_GREEN + "2" + ANSI_RESET;
                break;
                case 3:
                    square += ANSI_RED + "3" + ANSI_RESET;
                break;
                case 4:
                    square += ANSI_PURPLE + "4" + ANSI_RESET;
                break;
                case 5:
                    square += ANSI_YELLOW + "5" + ANSI_RESET;
                break;
                case 6:
                    square += ANSI_CYAN + "6" + ANSI_RESET;
                break;
                case 7:
                    square += ANSI_BLACK + "7" + ANSI_RESET;
                break;
                case 8:
                    square += ANSI_WHITE + "8" + ANSI_RESET;
                break;
            }
            square += "]";
        }
        return square;
    }
}