package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CornerLotCharQueryTo extends BaseQueryTo {
    private String prodCode;
    private String projName;
    private String fab;
    private String projOption;
    private String projCodeWVersion;
    
   
    public String getFab() {
		return fab;
	}
	public void setFab(String fab) {
		this.fab = fab;
	}
	public String getProjCodeWVersion() {
		return projCodeWVersion;
	}
	public void setProjCodeWVersion(String projCodeWVersion) {
		this.projCodeWVersion = projCodeWVersion;
	}
	public String getProjName() {
        return projName;
    }
    public void setProjName(String projName) {
        this.projName = projName;
    }
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
    public String getProjOption() {
        return projOption;
    }
    public void setProjOption(String projOption) {
        this.projOption = projOption;
    }
}
