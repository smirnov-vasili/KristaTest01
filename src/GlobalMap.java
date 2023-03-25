import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

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
            FileReader inpitFile = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(inpitFile);
            String line = bufferedReader.readLine();
            String[] lineParts = line.split(" ");
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
            FileWriter outputFile = new FileWriter(fileName);
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
    public void buildPaths() {
        long startTime = System.currentTimeMillis();
        // Начальное заполнение списка путей для каждой ячейки карты
        ArrayList<MapPath> paths = new ArrayList<>();
        for (int row = 0; row < getRowCount(); row++) {
            for (int col = 0; col < getColCount(); col++) {
                MapPath path = new MapPath(row, col);
                path.addCellValue(getCell(row, col));
                paths.add(path);
            }
        }
        MapPathComparatorByPath mapPathComparator = new MapPathComparatorByPath();
        int stepNum = 1;
        do {
            MapCoords delta = getCellEnvironment(stepNum);
            paths.sort(mapPathComparator);
            // Заглушки в начале и в конце списка путей для однородности алгоритма
            paths.add(0, new MapPath());
            paths.add(paths.size(), new MapPath());
            // Поиск неповторяющихся путей
            ArrayList<MapPath> singlePaths = new ArrayList<>();
            int i = paths.size() - 2;
            while (i > 0) {
                MapPath path = paths.get(i);
                if (Arrays.equals(path.getPath(), paths.get(i - 1).getPath())) {
                    i--;
                } else {
                    if (!Arrays.equals(path.getPath(), paths.get(i + 1).getPath())) {
                        singlePaths.add(path);
                    }
                }
                i--;
            }
            // Удаление заглушек
            paths.remove(0);
            paths.remove(paths.size() - 1);
            // Удаление законченных неповторяющихся путей
            this.paths.addAll(singlePaths);
            paths.removeAll(singlePaths);
            // Достройка шага для оставшихся путей
            paths.parallelStream()
                    .forEach(p -> p.addCellValue(getCell(p.getRow() + delta.row, p.getCol() + delta.col)));
            stepNum++;
        } while (paths.size() > 0);
        this.paths.sort(new MapPathComparatorByPathLength());
        long endTime = System.currentTimeMillis();
        System.out.printf("Вермя %d мс", endTime - startTime);
    }

    // Максимальная длина пути
    public int getMaxPathLength() {
        return paths.size() > 0 ? paths.get(0).getLength() : 0;
    }

    // Список ячеек карты с максимальной длиной пути
    public String getMaxPathCells() {
        String cells = "";
        if (paths.size() == 0) return cells;
        int i = 0;
        while (i < paths.size() && paths.get(i).getLength() == getMaxPathLength()) {
            cells = cells.concat(String.format(" (%d,%d)",
                    paths.get(i).getCol() + 1, getRowCount() - paths.get(i).getRow()));
            i++;
        }
        cells = cells.substring(1);
        return cells;
    }

    // Средняя длина пути обхода для всех ячеек
    public float getAvgPathLength() {
        if (paths.size() == 0) return 0;
        int totalLength = 0;
        for (MapPath path : paths) {
            totalLength += path.getCount() - 1;
        }
        return totalLength * 1f / paths.size();
    }
}
