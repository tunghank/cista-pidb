package com.cista.pidb.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The default name converter between java and database.
 * @author Matrix
 *
 */
public class DefaultNameConverter implements NameConverter {
    /**
     * Convert a java field name to db field name.
     * As lastLoginDate --> LAST_LOGIN_DATE
     * @param javaName a java field name.
     * @return db name.
     */
    public String java2db(final String javaName) {
        if (javaName == null) {
            return null;
        }

        List<String> words = splitByCapital(javaName);

        if (words == null || words.size() == 0) {
            return null;
        }

        String dbName = "";
        for (int i = 0; i < words.size(); i++) {
            if (i != 0) {
                dbName += "_";
            }
            dbName += words.get(i);
        }

        return dbName.toUpperCase();
    }


    /**
     * Convert a db field name to java field name.
     * As LAST_LOGIN_DATE --> lastLoginDate
     * @param dbName a db field name.
     * @return java field name.
     */
    public String db2java(final String dbName) {
        if (dbName == null) {
            return null;
        }

        List<String> words = splitByUnderline(dbName);

        if (words == null || words.size() == 0) {
            return null;
        }

        String javaName = "";
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (i == 0) {
                javaName += word.toLowerCase();
//            } else if (word.charAt(0) >= '0' && word.charAt(0) <= '9') {
//                javaName += "_" + word.toLowerCase();
            } else {
                String firstLetter = word.substring(0, 1);
                String otherLetter = word.substring(1);
                javaName += firstLetter.toUpperCase() + otherLetter.toLowerCase();
            }
        }

        return javaName;
    }

    /**
     * Split a string by capital letter.
     * @param src source string
     * @return word list
     */
    private List<String> splitByCapital(final String src) {
        if (src == null || src.length() == 0) {
            return null;
        }

        List<String> words = new ArrayList<String>();
        String lastWord = null;

        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                if (lastWord != null) {
                    words.add(lastWord);
                }
                lastWord = "";
                lastWord += c;
            } else {
                if (lastWord == null) {
                    lastWord = "";
                }
                lastWord += c;
            }
        }

        if (lastWord != null) {
            words.add(lastWord);
        }

        return words;
    }

    /**
     * Split a string by underline.
     * @param src source string
     * @return word list
     */
    private List<String> splitByUnderline(final String src) {
        if (src == null || src.length() == 0) {
            return null;
        }

        List<String> words = new ArrayList<String>();
        String[] splited = src.split("_");

        for (String s : splited) {
            if (!s.trim().equals("")) {
                words.add(s.trim());
            }
        }

        return words;
    }
}
