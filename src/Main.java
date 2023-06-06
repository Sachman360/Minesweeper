import java.util.*;

public class Main {
    public static void main(String[] args) {

        Minesweeper test = new Minesweeper(Minesweeper.Difficulty.EASY);
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
        while(!minesweeper.gameOver) {
            System.out.print("Enter \"C\" or \"F\" followed by a series of numbers (in pairs): ");
            String input = scanner.nextLine();
            int[][] action = interpretInput(input);
            boolean click = false;
            boolean flag = false;
            switch(action[0][0]) {
                case 1:
                    click = true;
                break;
                case 2:
                    flag = true;
                break;
            }
            for(int i = 1; i < action[0].length; i++) {
                if(click) {
                    minesweeper.click(action[0][i], action[1][i]);
                } else if(flag) {
                    minesweeper.toggleFlag(action[0][i], action[1][i]);
                } else {
                    System.out.println("Please try again");
                }
            }
            if(click || flag) {
                minesweeper.print();
            }
        }
    }
    public static int[][] interpretInput(String s) {
        int[][] output;
        ArrayList<Integer> rows = new ArrayList<Integer>();
        rows.add(0);
        ArrayList<Integer> columns = new ArrayList<Integer>();
        columns.add(0);
        boolean click = false;
        boolean flag = false;
        boolean row = true;
        int count = 0;
        for(int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
            if(c.toUpperCase().equals("C") && !flag) {
                click = true;
            } else if(c.toUpperCase().equals("F") && !click) {
                flag = true;
            }
            if(Character.isDigit(s.charAt(i))) {
                if(row) {
                    rows.add(Integer.parseInt(c));
                    row = false;
                } else {
                    count++;
                    columns.add(Integer.parseInt(c));
                    row = true;
                }
            }
        }
        output = new int[2][count + 1];
        if(click) {
            output[0][0] = 1;
        } else if(flag) {
            output[0][0] = 2;
        } else {
            output[0][0] = 0;
        }
        for(int i = 1; i < count + 1; i++) {
            output[0][i] = rows.get(i);
            output[1][i] = columns.get(i);
        }
        return output;
    }
}