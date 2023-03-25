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
        path[0] = -1;
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
        shiftLeft(path, newStep);
    }

    private static final long LOW_BIT_MASK = 1;
    private static final long HIGH_BIT_MASK = (long) Math.pow(2, 62);

    public void shiftLeft(long[] arr, boolean newBit) {
        for (int i = 0; i < arr.length; i++) {
            boolean highBit = (arr[i] & HIGH_BIT_MASK) > 0;
            if (highBit) arr[i] &= ~HIGH_BIT_MASK;
            arr[i] = arr[i] << 1;
            if (newBit) arr[i] |= LOW_BIT_MASK;
            newBit = highBit;
        }
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
