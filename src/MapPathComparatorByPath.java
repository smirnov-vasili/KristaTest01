import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapPathComparatorByPath implements Comparator<MapPath> {
    @Override
    public int compare(MapPath path1, MapPath path2) {
        long[] p1 = path1.getPath();
        long[] p2 = path2.getPath();
        if (path1.isEquals(path2)) {
            return 0;
        } else {
            //return Arrays.compare(p1, p2);
            int i = p1.length - 1;
            while (p1[i] == p2[i]) {
                i--;
            }
            return p1[i] - p2[i] > 0 ? 1 : -1;
        }
    }
}
