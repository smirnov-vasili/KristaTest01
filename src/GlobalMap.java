import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class GlobalMap {

    // Поле карты
    private boolean[][] cells;
    // Пути обхода для каждой точки карты
    private ArrayList<MapPath> paths;

    public GlobalMap() {
        initField(1, 1);
    }

    // Инициализация поля карты
    public void initField(int rows, int cols) {
        cells = new boolean[rows][cols];
        paths = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                paths.add(new MapPath(row, col));
            }
        }
    }

    // Чтение ячейки карты
    public boolean getCell(int row, int col) {
        if (row >= 0 && row < getRowCount() && col >= 0 && col < getColCount())
            return cells[row][col];
        else
            return false;
    }

    // Запись в ячейку карты
    public void setCell(int row, int col, Boolean value) {
        if (row >= 0 && row < cells.length &&
                col >= 0 && col < cells[0].length) {
            cells[row][col] = value;
        }
    }

    // Поиск относительных смещений по строкам и столбцам для заданного шага пути
    public MapCoords getCellEnvironment(int num) {
        MapCoords coords = new MapCoords();
        int step = 1;
        while (true) {
            coords.row -= step;
            num -= Math.abs(step);
            if (num <= 0) {
                coords.row -= step > 0 ? num : -num;
                return coords;
            }
            coords.col += step;
            num -= Math.abs(step);
            if (num <= 0) {
                coords.col -= step > 0 ? -num : num;
                return coords;
            }
            step = -(step > 0 ? step + 1 : step - 1);
        }
    }

    public int getRowCount() {
        return cells.length;
    }

    public int getColCount() {
        return cells.length > 0 ? cells[0].length : 0;
    }

    public void loadFromFile(String fileName) {
        try {
            FileReader inpitFile = new FileReader(fileName, Charset.forName("Cp1251"));
            BufferedReader bufferedReader = new BufferedReader(inpitFile);
            String line = bufferedReader.readLine();
            String[] lineParts = line.split("\s+");
            int cols = Integer.parseInt(lineParts[0]);
            int rows = Integer.parseInt(lineParts[1]);
            initField(rows, cols);
            int row = 0;
            while ((line = bufferedReader.readLine()) != null && row < rows) {
                line = line.toLowerCase();
                for (int col = 0; col < line.length() && col < cols; col++) {
                    setCell(row, col, line.charAt(col) == 'x');
                }
                row++;
            }
            bufferedReader.close();
            inpitFile.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            initField(1, 1);
        }
    }

    public void saveToFile(String fileName) {
        try {
            FileWriter outputFile = new FileWriter(fileName, Charset.forName("Cp1251"));
            outputFile.write(String.format("%.3f", getAvgPathLength()));
            outputFile.write("\r\n");
            outputFile.write(String.valueOf(getMaxPathLength()));
            outputFile.write("\r\n");
            outputFile.write(getMaxPathCells());
            outputFile.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Построение путей обхода для каждой из ячеек поля карты
    public void boildPaths() {
        ArrayList<MapPath> builtPaths = new ArrayList<>();
        MapPathComparatorByPath mapPathComparator = new MapPathComparatorByPath();
        int num = 0;
        do {
            MapCoords coords = getCellEnvironment(num);
            int deltaRow = coords.row;
            int deltaCol = coords.col;
            paths.sort(mapPathComparator);
            // Заглушки в начале и в конце списка путей для однородности алгоритма
            paths.add(0, new MapPath());
            paths.add(paths.size(), new MapPath());
            int i = paths.size() - 2;
            while (i > 0) {
                MapPath path = paths.get(i);
                if (testPath(i - 1, path.getPath())) {
                    path.addCellValue(getCell(path.getRow() + deltaRow, path.getCol() + deltaCol));
                    i--;
                    paths.get(i).addCellValue(getCell(paths.get(i).getRow() + deltaRow, paths.get(i).getCol() + deltaCol));
                } else {
                    if (testPath(i + 1, path.getPath())) {
                        path.addCellValue(getCell(path.getRow() + deltaRow, path.getCol() + deltaCol));
                    } else {
                        builtPaths.add(path);
                        paths.remove(path);
                    }
                }
                i--;
            }
            // Удаление заглушек
            paths.remove(0);
            paths.remove(paths.size() - 1);
            num++;
        } while (paths.size() > 0);
        paths = builtPaths;
        paths.sort(new MapPathComparatorByPathLength());
    }

    // Тестирование заданного элемента списка путей на "совпадение"
    private boolean testPath(int index, String pathStr) {
        return paths.get(index).getPath().startsWith(pathStr);
    }

    // Максимальная длина пути
    public int getMaxPathLength() {
        return paths.get(0).getLength();
    }

    // Список ячеек карты с максимальной длиной пути
    public String getMaxPathCells() {
        String cells = "";
        int i = 0;
        while (i < paths.size() && paths.get(i).getLength() == getMaxPathLength()){
            cells = cells.concat(String.format(" (%d,%d)",
                    paths.get(i).getCol() + 1, getRowCount() - paths.get(i).getRow()));
            i++;
        }
        cells = cells.substring(1);
        return cells;
    }

    // Средняя длина пути обхода для всех ячеек
    public float getAvgPathLength() {
        int totalLength = 0;
        for (MapPath path : paths) {
            totalLength += path.getLength();
        }
        return totalLength * 1f / paths.size();
    }
}
