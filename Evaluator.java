public class Evaluator {

    public static double evaluate(ASTNode node) {
        if (node == null) throw new RuntimeException("Null AST node");

        switch (node.value) {
            case "+":
                return evaluate(node.left) + evaluate(node.right);

            case "-":
                // Unary minus: left == null
                if (node.left == null) return -evaluate(node.right);
                return evaluate(node.left) - evaluate(node.right);

            case "*":
                return evaluate(node.left) * evaluate(node.right);

            case "/": {
                double divisor = evaluate(node.right);
                if (divisor == 0) throw new RuntimeException("Division by zero");
                return evaluate(node.left) / divisor;
            }

            case "++":
                return evaluate(node.left) + 1;   // post-increment

            case "--":
                return evaluate(node.left) - 1;   // post-decrement

            default:
                try {
                    return Double.parseDouble(node.value);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Unknown node value: " + node.value);
                }
        }
    }
}

