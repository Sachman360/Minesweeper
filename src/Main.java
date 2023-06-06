import java.util.*;

public class Main {
    public static void main(String[] args) {

        Minesweeper test = new Minesweeper(Minesweeper.Difficulty.HARD);
        MinesweeperSolver solver = new MinesweeperSolver();
        solver.solve(test);

        System.exit(0);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please select difficulty (E, M, H): ");
        String diff = scanner.nextLine();
        Minesweeper minesweeper;
        switch(diff.toUpperCase()){
            case "E":
                minesweeper = new Minesweeper(Minesweeper.Difficulty.EASY);
            break;
            case "M":
                minesweeper = new Minesweeper(Minesweeper.Difficulty.MEDIUM);
            break;
            case "H":
                minesweeper = new Minesweeper(Minesweeper.Difficulty.HARD);
            break;
            default:
                minesweeper = new Minesweeper(Minesweeper.Difficulty.MEDIUM);
                System.out.println("Default MEDIUM chosen");
        }
        System.out.println();
        minesweeper.print();
    }
}