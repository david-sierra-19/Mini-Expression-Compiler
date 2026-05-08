import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String input;
    private int pos;

    public Lexer(String input) {
        // Strip all whitespace so the position numbers stay simple
        this.input = input.replaceAll("\\s+", "");
        this.pos   = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (Character.isDigit(c)) {
                int start = pos;
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    sb.append(input.charAt(pos++));
                }
                tokens.add(new Token(Token.Type.NUMBER, sb.toString(), start));

            } else if (c == '+') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '+') {
                    tokens.add(new Token(Token.Type.INC, "++", pos));
                    pos += 2;
                } else {
                    tokens.add(new Token(Token.Type.PLUS, "+", pos++));
                }

            } else if (c == '-') {
                if (pos + 1 < input.length() && input.charAt(pos + 1) == '-') {
                    tokens.add(new Token(Token.Type.DEC, "--", pos));
                    pos += 2;
                } else {
                    tokens.add(new Token(Token.Type.MINUS, "-", pos++));
                }

            } else if (c == '*') {
                tokens.add(new Token(Token.Type.STAR, "*", pos++));

            } else if (c == '/') {
                tokens.add(new Token(Token.Type.SLASH, "/", pos++));

            } else if (c == '(') {
                tokens.add(new Token(Token.Type.LPAREN, "(", pos++));

            } else if (c == ')') {
                tokens.add(new Token(Token.Type.RPAREN, ")", pos++));

            } else {
                throw new RuntimeException(
                        "Unexpected character '" + c + "' at position " + pos);
            }
        }

        tokens.add(new Token(Token.Type.EOF, "EOF", pos));
        return tokens;
    }
}

