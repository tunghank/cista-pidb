package com.cista.pidb.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class DynamicTable {
    static Log logger = LogFactory.getFactory().getInstance(DynamicTable.class);

    public static final String rowSeparator = "\r\n";

    public static final String cellSeparator = ",\t";

    public static String[][] unpack(String src) {
        if (null == src || src.trim().equals("")) {
            return null;
        }

        src = Escape.unescape(src);

        int ri = DynamicTable.countChar(src, rowSeparator) + 0;
        int ci = (DynamicTable.countChar(src, cellSeparator) + ri) / ri;
        logger.debug("Parsing dynamic table, find " + ri + " rows, " + ci
                + " cells.");
        String[][] result = new String[ri][ci];

        String[] rows = StringUtils.delimitedListToStringArray(src, String
                .valueOf(rowSeparator));
        for (int i = 0; i < ri; i++) {
            String[] cells = StringUtils.delimitedListToStringArray(rows[i],
                    String.valueOf(cellSeparator));
            for (int j = 0; j < ci; j++) {
                String cell = "";
                if (cells != null && cells.length > 0) {
                    cell = cells[j];
                }
                result[i][j] = Escape.unescape(cell);
            }
        }

        return result;
    }

    public static String pack(final String[][] src) {
        if (src == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            if (i != 0) {
                sb.append(rowSeparator);
            }
            // sb.append(StringUtils.arrayToDelimitedString(src[i],
            // String.valueOf(cellSeparator)));
            for (int j = 0; j < src[i].length; j++) {
                if (j != 0) {
                    sb.append(cellSeparator);
                }
                String cell = src[i][j];
                if (cell == null) {
                    cell = "";
                }
                sb.append(Escape.escape(cell));
            }
        }
        return Escape.escape(sb.toString());
    }

    private static int countChar(final String s, final String c) {
        return StringUtils.countOccurrencesOf(s, c);
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        String a = "";
        String b[] = StringUtils.delimitedListToStringArray(a,
                String.valueOf(","));
        System.out.println(b[0]);
    }
}
