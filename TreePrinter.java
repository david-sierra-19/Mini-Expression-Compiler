import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

public class TreePrinter {

    private static int inOrderCounter;

    private static void assignPositions(ASTNode node, int depth,
                                        Map<ASTNode, int[]> pos) {
        if (node == null) return;
        assignPositions(node.left,  depth + 1, pos);
        pos.put(node, new int[]{ depth * 2, inOrderCounter++ * 2 });
        assignPositions(node.right, depth + 1, pos);
    }

    public static void print(ASTNode root) {
        if (root == null) return;

        inOrderCounter = 0;
        Map<ASTNode, int[]> positions = new IdentityHashMap<>();
        assignPositions(root, 0, positions);

        // Grid dimensions
        int maxRow = 0, maxCol = 0;
        for (int[] p : positions.values()) {
            if (p[0] > maxRow) maxRow = p[0];
            if (p[1] > maxCol) maxCol = p[1];
        }
        int rows = maxRow + 1;
        int cols = maxCol + 5; // extra room for multi-digit labels at the right edge

        char[][] grid = new char[rows][cols];
        for (char[] row : grid) Arrays.fill(row, ' ');

        // Place labels and connectors
        for (Map.Entry<ASTNode, int[]> entry : positions.entrySet()) {
            ASTNode node    = entry.getKey();
            int     nodeRow = entry.getValue()[0];
            int     nodeCol = entry.getValue()[1];

            // Centre multi-character labels on nodeCol; clamp so nothing goes off-screen
            String label    = node.value;
            int    startCol = Math.max(0, nodeCol - label.length() / 2);
            for (int i = 0; i < label.length(); i++) {
                int c = startCol + i;
                if (c >= 0 && c < cols) grid[nodeRow][c] = label.charAt(i);
            }

            int connRow = nodeRow + 1;
            if (connRow < rows) {
                if (node.left != null) {
                    int childCol = positions.get(node.left)[1];
                    drawConnector(grid, connRow, nodeCol, childCol, true);
                }
                if (node.right != null) {
                    int childCol = positions.get(node.right)[1];
                    drawConnector(grid, connRow, nodeCol, childCol, false);
                }
            }
        }

        System.out.println("Parse Tree:");
        for (char[] row : grid) {
            System.out.println(new String(row).stripTrailing());
        }
    }

    private static void drawConnector(char[][] grid, int connRow,
                                      int parentCol, int childCol, boolean isLeft) {
        if (connRow >= grid.length) return;
        int cols = grid[connRow].length;

        if (isLeft) {
            int slashPos = parentCol - 1;
            if (slashPos >= 0 && slashPos < cols) grid[connRow][slashPos] = '/';
            for (int c = childCol + 1; c < slashPos; c++) {
                if (c >= 0 && c < cols) grid[connRow][c] = '_';
            }
        } else {
            int bsPos = parentCol + 1;
            if (bsPos >= 0 && bsPos < cols) grid[connRow][bsPos] = '\\';
            for (int c = bsPos + 1; c < childCol; c++) {
                if (c >= 0 && c < cols) grid[connRow][c] = '_';
            }
        }
    }
}
