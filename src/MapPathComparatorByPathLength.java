import java.util.Comparator;

public class MapPathComparatorByPathLength implements Comparator<MapPath> {
    @Override
    public int compare(MapPath path1, MapPath path2) {
        // Наиболее длинные пути должны быть сверху (порядок обратный!)
        int result = path2.getCount() - path1.getCount();
        if (result != 0) return result;
        // На выходе Y-координата будет инверирована (порядок обратный!)
        result = path2.getRow() - path1.getRow();
        if (result != 0) return result;
        // На выходе X-кооордината будет в прямом порядке
        result = path1.getCol() - path2.getCol();
        return result;
    }
}
