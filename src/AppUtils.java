public class AppUtils {

    private static final long LOW_BIT_MASK = 1;
    private static final long HIGH_BIT_MASK = (long)Math.pow(2, 62);

    public static void shiftLeft(long[] arr, boolean newBit) {
        for (int i = 0; i < arr.length; i++) {
            boolean highBit = (arr[i] & AppUtils.HIGH_BIT_MASK) > 0;
            if (highBit) {
                arr[i] = arr[i] & ~HIGH_BIT_MASK;
            }
            arr[i] = arr[i] << 1;
            if (newBit) arr[i] |= LOW_BIT_MASK;
            newBit = highBit;
        }
    }

    public static void shiftRight(long[] arr, int cnt) {
        while (cnt > 0) {
            boolean oldBit = false;
            for (int i = arr.length - 1; i >= 0; i--) {
                boolean lowBit = (arr[i] & 1) > 0;
                arr[i] = arr[i] >> 1;
                if (oldBit) {
                    arr[i] |= HIGH_BIT_MASK;
                }
                oldBit = lowBit;
            }
            cnt--;
        }
    }
}
