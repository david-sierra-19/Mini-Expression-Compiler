public class ASTNode {

    public final String  value;
    public final ASTNode left;
    public final ASTNode right;

    /** Leaf node (number literal). */
    public ASTNode(String value) {
        this(value, null, null);
    }

    public ASTNode(String value, ASTNode left, ASTNode right) {
        this.value = value;
        this.left  = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}

