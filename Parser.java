import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos    = 0;
    }

    public ASTNode parse() {
        ASTNode root = parseExpression();
        Token leftover = peek();
        if (leftover.type != Token.Type.EOF) {
            throw new RuntimeException(
                    "Unexpected token '" + leftover.value +
                            "' at position " + leftover.position);
        }
        return root;
    }

    public boolean isSimplePostOp() {
        return tokens.size() == 3
                && tokens.get(0).type == Token.Type.NUMBER
                && (tokens.get(1).type == Token.Type.INC
                || tokens.get(1).type == Token.Type.DEC)
                && tokens.get(2).type == Token.Type.EOF;
    }

    private ASTNode parseExpression() {
        ASTNode left = parseTerm();

        while (peek().type == Token.Type.PLUS || peek().type == Token.Type.MINUS) {
            Token op    = consume();
            ASTNode right = parseTerm();
            left = new ASTNode(op.value, left, right);
        }
        return left;
    }

    private ASTNode parseTerm() {
        ASTNode left = parseFactor();

        while (peek().type == Token.Type.STAR || peek().type == Token.Type.SLASH) {
            Token op    = consume();
            ASTNode right = parseFactor();
            left = new ASTNode(op.value, left, right);
        }
        return left;
    }

    private ASTNode parseFactor() {
        Token t = peek();

        if (t.type == Token.Type.LPAREN) {
            consume(); // '('
            ASTNode inner = parseExpression();
            Token closing = consume();
            if (closing.type != Token.Type.RPAREN) {
                throw new RuntimeException(
                        "Expected ')' but got '" + closing.value +
                                "' at position " + closing.position);
            }
            return inner;

        } else if (t.type == Token.Type.MINUS) {
            consume(); // unary '-'
            ASTNode operand = parseFactor();
            // left=null signals unary to the evaluator
            return new ASTNode("-", null, operand);

        } else if (t.type == Token.Type.PLUS) {
            consume(); // unary '+', transparent
            return parseFactor();

        } else if (t.type == Token.Type.NUMBER) {
            consume();
            ASTNode num = new ASTNode(t.value);

            if (peek().type == Token.Type.INC) {
                consume();
                return new ASTNode("++", num, null); // left holds operand
            } else if (peek().type == Token.Type.DEC) {
                consume();
                return new ASTNode("--", num, null);
            }
            return num;

        } else if (t.type == Token.Type.EOF) {
            throw new RuntimeException(
                    "Unexpected end of expression at position " + t.position);
        } else {
            throw new RuntimeException(
                    "Unexpected token '" + t.value + "' at position " + t.position);
        }
    }

    private Token peek()    { return tokens.get(pos); }
    private Token consume() { return tokens.get(pos++); }
}

