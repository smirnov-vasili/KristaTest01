import java.util.Comparator;

public class MapPathComparatorByPath implements Comparator<MapPath> {
    @Override
    public int compare(MapPath path1, MapPath path2) {
        int i = path1.getPath().length - 1;
        while (i >= 0 && path1.getPath()[i] == path2.getPath()[i]) {
            i--;
        }
        if (i < 0) {
            return 0;
        } else {
            return path1.getPath()[i] - path2.getPath()[i] > 0 ? 1 : -1;
        }
    }
}
