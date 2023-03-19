import java.util.Comparator;

public class MapPathComparatorByPath implements Comparator<MapPath> {
    @Override
    public int compare(MapPath path1, MapPath path2) {
        return path1.getPath().compareTo(path2.getPath());
    }
}
