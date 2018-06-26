package ua.denst.music.collection.util;

public class BitRateCalculator {
    public static Short calculateBitRate(final Integer duration, final Long size) {
        final Long bitRate = size / duration / 128;

        return round(bitRate);
    }

    private static Short round(final Long bitRate) {
        if (bitRate > 300) {
            return 320;
        }
        if (bitRate > 196) {
            return 196;
        }
        return 128;
    }
}
