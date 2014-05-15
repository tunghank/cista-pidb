package com.cista.pidb.core;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * <p>Title: CharSequenceSeeder.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Matrix
 * @version 1.0
 */
public final class RandomDataSeeder {
    private static final int DEFAULT_LENGTH = 8;
    private static final boolean START_WITH_ALPHA = true;
    private static final Random R = new Random();
    private static final String ABC = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String MIX = "abcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * Default constructor.
     *
     */
    private RandomDataSeeder() {
    }

    /**
     * Seed method for capital alpha string.
     * @return random string like "DEHCONSU", default length = 8.
     */
    public static String seedStringABC() {
        return seedStringABC(DEFAULT_LENGTH);
    }

    /**
     * Seed method for capital alpha string with special length.
     * @param length return string length
     * @return random string like "POEKDCZQELDCC" as specified length.
     */
    public static String seedStringABC(final int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += ABC.charAt(R.nextInt(ABC.length()));
        }
        return result;
    }

    /**
     * Seed method for digit string.
     * @return random string like "39510392", default length = 8.
     */
    public static String seedString123() {
        return seedString123(DEFAULT_LENGTH);
    }

    /**
     * Seed method for digit string with special length.
     * @param length return string length
     * @return random string like "397501338503" as specified length.
     */
    public static String seedString123(final int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += DIGITS.charAt(R.nextInt(DIGITS.length()));
        }
        return result;
    }

    /**
     * Seed method for mixed string.
     * @return radom string with digits and alphas.
     */
    public static String seedStringMix() {
        return seedStringMix(DEFAULT_LENGTH, START_WITH_ALPHA);
    }

    /**
     * Seed method for mixed string with special length.
     * @param length return string length
     * @return radom string with digits and alphas.
     */
    public static String seedStringMix(final int length) {
        return seedStringMix(length, START_WITH_ALPHA);
    }

    /**
     * Seed method for mixed string always start with alpha.
     * @param startWithAlpha whether start with alpha
     * @return radom string with digits and alphas.
     */
    public static String seedStringMix(final boolean startWithAlpha) {
        return seedStringMix(DEFAULT_LENGTH, startWithAlpha);
    }

    /**
     * Seed method for mixed string always start with alpha and with special length.
     * @param length return string length
     * @param startWithAlpha whether start with alpha
     * @return radom string with digits and alphas.
     */
    public static String seedStringMix(final int length, final boolean startWithAlpha) {
        String result = "";
        for (int i = 0; i < length; i++) {
            int r = R.nextInt(MIX.length());
            if (i == 0 && startWithAlpha && r >= ABC.length()) {
                i--;
                continue;
            }
            result += MIX.charAt(r);
        }
        return result;
    }

    /**
     * Seed a random boolean value.
     * @return true or false, it's random.
     */
    public static boolean seedBoolean() {
        return R.nextBoolean();
    }

    /**
     * Seed a random value from given dictionary.
     * @param dictionary source dictionary.
     * @return one of dictionary.
     */
    public static Object seedFrom(final Object[] dictionary) {
        return dictionary[R.nextInt(dictionary.length)];
    }

    /**
     * Seed a random int value.
     * @return int value
     */
    public static int seedInt() {
        return R.nextInt();
    }

    /**
     * Seed an int value from give range.
     * The bounder are included.
     * @param from min value
     * @param to max value
     * @return int value >= from and <= to
     */
    public static int seedIntBetween(final int from, final int to) {
        if (to < from) {
            return -1;
        }

        int diff = to - from + 1;

        return R.nextInt(diff) + from;
    }

    /**
     * Seed a double value from give range.
     * The bounder are included.
     * @param from min value
     * @param to max value
     * @param dec the decimal digital numbers
     * @return double value >= from and <= to
     */
    public static double seedNumberBetween(final double from, final double to, final int dec) {
        if (to < from) {
            return -1;
        }

        double zoom = Math.pow(10, dec);
        int iFrom = (int) (from * zoom);
        int iTo = (int) (to * zoom);

        return seedIntBetween(iFrom, iTo) / zoom;
    }

    /**
     * Seed a long value.
     * @return long value
     */
    public static long seedLong() {
        return R.nextLong();
    }

    /**
     * Seed random date between 1900/01/01 00:00:00 to 2099/12/31 23:59:59.
     * @return random date.
     */
    public static Date seedDate() {
        Calendar c = Calendar.getInstance();
        int year = R.nextInt(200) + 1900;
        c.set(Calendar.YEAR, year);
        int month = R.nextInt(12);
        c.set(Calendar.MONTH, month);
        int day = R.nextInt(c.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);
        c.set(Calendar.DATE, day);
        int hour = R.nextInt(24);
        c.set(Calendar.HOUR, hour);
        int minute = R.nextInt(60);
        c.set(Calendar.MINUTE, minute);
        int second = R.nextInt(60);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }

    /**
     * Seed random date between give from and to date.
     * @param from begin date
     * @param to end date
     * @return random date.
     */
    public static Date seedDateBetween(final Date from, final Date to) {
        Calendar cFrom = Calendar.getInstance();
        cFrom.setTime(from);
        Calendar cTo = Calendar.getInstance();
        cTo.setTime(to);

        int diff = (int) ((cTo.getTimeInMillis() - cFrom.getTimeInMillis()) / 1000) + 1;

        cFrom.add(Calendar.SECOND, R.nextInt(diff));

        return cFrom.getTime();
    }
}
