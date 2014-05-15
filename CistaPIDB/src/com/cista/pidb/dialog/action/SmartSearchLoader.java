package com.cista.pidb.dialog.action;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SmartSearchLoader {
    private static final Log LOGGER = LogFactory.getLog(SmartSearchLoader.class);

    private static final String SMART_SEARCH_CONFIG_FILE = "SmartSearch.xml";

    private Map<String, SmartSearch> smartsearches;

    private static SmartSearchLoader _instance;

    /**
     * Get lov according to name.
     * @param name String
     * @return LovDefine
     */
    public static synchronized SmartSearch getSmartSearch(final String name) {
         //if (_instance == null) {
        // Always rebuild Lov config, when debug
             _instance = buildSmartSearch();
         //}
        return _instance.smartsearches.get(name);
    }

    /**
     * Add a lov according to ld.
     * @param ld LovDefine
     */
    @SuppressWarnings("unused")
    public void addSmartSearch(final SmartSearch ld) {
        if (smartsearches == null) {
            smartsearches = new Hashtable<String, SmartSearch>();
        }
        smartsearches.put(ld.getName(), ld);
    }

    /**
     * Build lov.
     * @return LovConfig
     */
    private static SmartSearchLoader buildSmartSearch() {
        SmartSearchLoader config = null;
        try {
            InputStream inStream = null;
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            // load the file as Stream
            inStream = classLoader.getResourceAsStream(SMART_SEARCH_CONFIG_FILE);
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.addObjectCreate("SmartSearches", SmartSearchLoader.class);
            digester.addObjectCreate("SmartSearches/SmartSearch", SmartSearch.class);
            digester.addSetProperties("SmartSearches/SmartSearch");
            digester.addBeanPropertySetter("SmartSearches/SmartSearch/name", "name");
            digester.addBeanPropertySetter("SmartSearches/SmartSearch/title", "title");
            digester.addBeanPropertySetter("SmartSearches/SmartSearch/table", "table");
            digester.addBeanPropertySetter("SmartSearches/SmartSearch/keyColumn", "keyColumn");
            digester.addBeanPropertySetter("SmartSearches/SmartSearch/orderBy", "orderBy");
            digester.addBeanPropertySetter("SmartSearches/SmartSearch/mode", "mode");
            digester.addCallMethod("SmartSearches/SmartSearch/columns/column",
                    "addColumn", 0);
            digester.addSetNext("SmartSearches/SmartSearch", "addSmartSearch");

            config = (SmartSearchLoader) digester.parse(inStream);
        } catch (Exception e) {
            LOGGER.warn("Error while parsing smart search config file: "
                    + SMART_SEARCH_CONFIG_FILE, e);
        }

        return config;
    }
}
