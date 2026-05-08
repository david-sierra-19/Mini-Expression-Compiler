public class Token {

    public enum Type {
        NUMBER, PLUS, MINUS, STAR, SLASH,
        LPAREN, RPAREN, INC, DEC, EOF
    }

    public final Type   type;
    public final String value;
    public final int    position;

    public Token(Type type, String value, int position) {
        this.type     = type;
        this.value    = value;
        this.position = position;
    }

    @Override
    public String toString() {
        return value;
    }
}
