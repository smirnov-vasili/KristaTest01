import java.util.Arrays;

public class MapPath {

    private final int row;
    private final int col;

    private long[] path;
    private int pathItemCount;

    public MapPath() {
        this.row = -1;
        this.col = -1;
        path = new long[1];
        pathItemCount = -1;
    }

    public MapPath(int row, int col) {
        this.row = row;
        this.col = col;
        path = new long[0];
        pathItemCount = 0;
    }

    // Добавление шага к пути
    public void addCellValue(boolean newStep) {
        if (pathItemCount % 63 == 0){
            path = Arrays.copyOf(path, path.length + 1);
        }
        pathItemCount++;
        AppUtils.shiftLeft(path, newStep);
    }

    public long[] getPath() {
        return path;
    }

    public int getCount() {
        return pathItemCount;
    }

    public int getLength()  {
        return pathItemCount - 1;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
