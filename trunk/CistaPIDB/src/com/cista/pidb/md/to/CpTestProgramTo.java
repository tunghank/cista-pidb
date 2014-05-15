package com.cista.pidb.md.to;

import java.util.Date;

public class CpTestProgramTo {
    private String projCode;
    private String prodCode;
    private String prodName;
    private String projCodeWVersion;
    private String cpTestProgName;
    private String cpTestProgRevision;
    private Date cpTestProgReleaseDate;
    private Double cpCpuTime;
    private Double cpIndexTime;
    private String contactDieQty;
    private String tester;
    private String testerConfig;
    private String firstCpTestHouse;
    private String remark;
    private String assignTo;
    private String assignEmail;
    private String createdBy;
    private String modifiedBy;
    //Add 2007/11/12
    private String cpMaterialNum;
    private boolean multipleStage;
    private String materialDesc;
    private String vendorCode;
    private String materialNum;
    
    public String getMaterialNum() {
		return materialNum;
	}
	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getMaterialDesc() {
		return materialDesc;
	}
	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}
	public boolean getMultipleStage() {
        return multipleStage;
    }
    public void setMultipleStage(boolean multipleStage) {
        this.multipleStage = multipleStage;
    }
    
    public String getCpMaterialNum() {
        return cpMaterialNum;
    }
    public void setCpMaterialNum(String cpMaterialNum) {
        this.cpMaterialNum = cpMaterialNum;
    }
    
    public String getAssignEmail() {
        return assignEmail;
    }
    public void setAssignEmail(String assignEmail) {
        this.assignEmail = assignEmail;
    }
    public String getAssignTo() {
        return assignTo;
    }
    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }
    public String getContactDieQty() {
        return contactDieQty;
    }
    public void setContactDieQty(String contactDieQty) {
        this.contactDieQty = contactDieQty;
    }
    public Double getCpCpuTime() {
        return cpCpuTime;
    }
    public void setCpCpuTime(Double cpCpuTime) {
        this.cpCpuTime = cpCpuTime;
    }
    public Double getCpIndexTime() {
        return cpIndexTime;
    }
    public void setCpIndexTime(Double cpIndexTime) {
        this.cpIndexTime = cpIndexTime;
    }
    public String getCpTestProgName() {
        return cpTestProgName;
    }
    public void setCpTestProgName(String cpTestProgName) {
        this.cpTestProgName = cpTestProgName;
    }
    public Date getCpTestProgReleaseDate() {
        return cpTestProgReleaseDate;
    }
    public void setCpTestProgReleaseDate(Date cpTestProgReleaseDate) {
        this.cpTestProgReleaseDate = cpTestProgReleaseDate;
    }
    public String getCpTestProgRevision() {
        return cpTestProgRevision;
    }
    public void setCpTestProgRevision(String cpTestProgRevision) {
        this.cpTestProgRevision = cpTestProgRevision;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getFirstCpTestHouse() {
        return firstCpTestHouse;
    }
    public void setFirstCpTestHouse(String firstCpTestHouse) {
        this.firstCpTestHouse = firstCpTestHouse;
    }
    public String getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public String getProdCode() {
        return prodCode;
    }
    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
    public String getProjCodeWVersion() {
        return projCodeWVersion;
    }
    public void setProjCodeWVersion(String projCodeWVersion) {
        this.projCodeWVersion = projCodeWVersion;
    }
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
    public String getProjCode() {
        return projCode;
    }
    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getTester() {
        return tester;
    }
    public void setTester(String tester) {
        this.tester = tester;
    }
    public String getTesterConfig() {
        return testerConfig;
    }
    public void setTesterConfig(String testerConfig) {
        this.testerConfig = testerConfig;
    }
}
