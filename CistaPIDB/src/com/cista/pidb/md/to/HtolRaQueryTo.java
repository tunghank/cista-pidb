package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class HtolRaQueryTo extends BaseQueryTo {
    private String prodName;
    private String projName;
    private String projCodeWVersion;
    private String raTestItem;
    private String raTestResult;
    private String owner;
    
    public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getRaTestResult() {
		return raTestResult;
	}
	public void setRaTestResult(String raTestResult) {
		this.raTestResult = raTestResult;
	}
	public String getRaTestItem() {
        return raTestItem;
    }
    public void setRaTestItem(String raTestItem) {
        this.raTestItem = raTestItem;
    }
    
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
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
}
