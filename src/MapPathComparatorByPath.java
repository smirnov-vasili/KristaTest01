import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapPathComparatorByPath implements Comparator<MapPath> {
    @Override
    public int compare(MapPath path1, MapPath path2) {
        /*return Arrays.compare(path1.getPath(), path2.getPath());*/
        long[] p1 = path1.getPath();
        long[] p2 = path2.getPath();
        int i = p1.length - 1;
        while (i >= 0 && p1[i] == p2[i]) {
            i--;
        }
        return i < 0 ? 0 : (p1[i] - p2[i] > 0 ? 1 : -1);
    }
}
