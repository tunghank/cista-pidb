package com.cista.pidb.md.erp;

public class ReleaseERPException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * ReleaseERPException.
     */
    public ReleaseERPException() {
        super();
    }

    /**
     * ReleaseERPException.
     * @param errStr error string
     */
    public ReleaseERPException(final String errStr) {
        super(errStr);
    }

}