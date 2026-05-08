import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        runInteractive();
    }

    private static void runInteractive() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Mini Expression Compiler");
        System.out.println("Type an expression and press Enter. Type 'quit' to exit.\n");

        while (true) {
            System.out.print("Expression: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("quit") || line.equals("exit")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            processExpression(line);
            System.out.println();
        }
        scanner.close();
    }

    public static void processExpression(String input) {
        System.out.println("Expression: " + input);
        System.out.println("-".repeat(40));

        // Lexical analysis
        List<Token> tokens;
        try {
            tokens = new Lexer(input).tokenize();
        } catch (RuntimeException e) {
            System.out.println("Lexer Error: " + e.getMessage());
            return;
        }

        // Print token stream (omit the synthetic EOF token)
        System.out.print("Tokens: [");
        for (int i = 0; i < tokens.size() - 1; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(tokens.get(i).value);
        }
        System.out.println("]");

        // Parsing
        Parser parser = new Parser(tokens);
        boolean simplePostOp = parser.isSimplePostOp();

        ASTNode root;
        try {
            root = parser.parse();
            System.out.println("Parse: Success");
        } catch (RuntimeException e) {
            System.out.println("Parse: Failure — " + e.getMessage());
            return;
        }

        // Tree display (suppressed for bare x++ / x--)
        if (!simplePostOp) {
            TreePrinter.print(root);
        }

        // Evaluation
        try {
            double result = Evaluator.evaluate(root);
            System.out.println("Evaluation Result: " + formatResult(result));
        } catch (RuntimeException e) {
            System.out.println("Evaluation Error: " + e.getMessage());
        }
    }

    private static String formatResult(double v) {
        if (!Double.isInfinite(v) && !Double.isNaN(v) && v == Math.floor(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}

