public class MapPath {

    private String path;
    private final int row;
    private final int col;

    public MapPath() {
        this.row = -1;
        this.col = -1;
        path = "ERROR";
    }

    public MapPath(int row, int col) {
        this.row = row;
        this.col = col;
        path = "";
    }

    // Добавление шага к пути
    public void addCellValue(boolean cellValue) {
        path = path + (cellValue ? "1" : "0");
    }

    public String getPath() {
        return path;
    }

    public int getLength() {
        return path.length() - 1;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
