package com.cista.pidb.code.to;

public class SapMasterCustomerTo {

    private String shortName;
    private String customerCode;
    private String customerGrp;
    
    public String getCustomerGrp() {
		return customerGrp;
	}
	public void setCustomerGrp(String customerGrp) {
		this.customerGrp = customerGrp;
	}
	public String getCustomerCode() {
        return customerCode;
    }
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
