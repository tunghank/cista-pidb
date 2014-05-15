package com.cista.pidb.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;

/**
 * Support class for get next value of oracle sequence.
 * @author Matrix
 *
 */
public final class SequenceSupport {
    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(PIDBContext.class);

    /** LHP_USER's sequence.*/
    public static final String SEQ_USER = "PIDB_USER_SEQ";

    /** LHP_ROLE's sequence.*/
    public static final String SEQ_ROLE = "PIDB_ROLE_SEQ";

    /** LHP_USER_ROLE's sequence.*/
    public static final String SEQ_USER_ROLE = "PIDB_USER_ROLE_SEQ";

    /** LHP_FUNC's sequence.*/
    public static final String SEQ_FUNCION = "PIDB_FUNCTION_SEQ";

    /** LHP_ROLE_FUNC's sequence.*/
    public static final String SEQ_ROLE_FUNC = "PIDB_ROLE_FUNC_SEQ";
    
    /** PIDB_TRADITIONAL_PKG's sequence.*/
    public static final String SEQ_IC_WAFER = "PIDB_IC_WAFER_SEQ";
    
    /** */
    public static final String SEQ_TRAD_PKG = "PIDB_TRAD_PKG_SEQ";
    
    /** */
    public static final String SEQ_CORNER_LOT_CHAR = "PIDB_CORNER_LOT_CHAR_SEQ";
    
    /** */
    public static final String SEQ_CORNER_ESD_TEST = "PIDB_ESD_TEST_SEQ";
    
    /** */
    public static final String SEQ_IF_MATERIAL_MASTER = "PIDB_IF_MATERIAL_MASTER_SEQ";
    
    /** */
    public static final String SEQ_IF_MATERIAL_CHARACTER = "PIDB_IF_MATERIAL_CHARACTER_SEQ";
    
    /** */
    public static final String SEQ_IF_PROJECT_CODE = "PIDB_IF_PROJECT_CODE_SEQ";    
    
    /**ASL*/
    public static final String SEQ_PIDB_SOURCE_LIST = "PIDB_SOURCE_LIST_SEQ";    
   
    /**APL*/
    public static final String SEQ_PIDB_APL = "PIDB_APL_SEQ";
    
    /**ZS_SDRAM 外購料*/    
    public static final String SEQ_PIDB_SDRAM = "SEQ_PIDB_SDRAM";
    
    /**ZS_EEPROM 外購料*/    
    public static final String SEQ_PIDB_EEPROM = "SEQ_PIDB_EEPROM";
    
    /**ZS_HDCP_KEY 外購料*/    
    public static final String SEQ_PIDB_HDCP_KEY = "SEQ_PIDB_HDCP_KEY";
    
    /**
     * Default constructor.
     *
     */
    private SequenceSupport() {

    }

    /**
     * Return a sequence generator.
     * @param sequenceName sequence name.
     * @return sequence generator.
     */
    public static int nextValue(final String sequenceName) {
        OracleSequenceMaxValueIncrementer incrementer = null;
        incrementer = new OracleSequenceMaxValueIncrementer(PIDBContext.getPIDBDataSource(), sequenceName);

        if (incrementer == null) {
            LOGGER.error("No sequence find with name " + sequenceName);
            return -1;
        }

        int nextValue = incrementer.nextIntValue();
        LOGGER.debug("Return next value of " + sequenceName + " = " + nextValue);

        return nextValue;
    }
}
