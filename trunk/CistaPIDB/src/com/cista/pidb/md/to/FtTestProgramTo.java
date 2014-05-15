package com.cista.pidb.md.to;

import java.util.Date;

public class FtTestProgramTo {
    private String partNum;
    private String ftTestProgName;
    private String ftTestProgRevision;
    private Date ftTestProgReleaseDate;
    private Double ftCpuTime;
    private Double ftIndexTime;
    private String contactDieQty;
    private String tester;
    private String testerConfig;
    private String firstFtTestHouse;
    private String remark;
    private String assignTo;
    private String assignEmail;
    private String createdBy;
    private String modifiedBy;
    //Add 2007/11/12
    private String ftMaterialNum;
    private boolean multipleStage;

    public boolean getMultipleStage() {
        return multipleStage;
    }
    public void setMultipleStage(boolean multipleStage) {
        this.multipleStage = multipleStage;
    }
    
    public String getFtMaterialNum() {
        return ftMaterialNum;
    }
    public void setFtMaterialNum(String ftMaterialNum) {
        this.ftMaterialNum = ftMaterialNum;
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
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getFirstFtTestHouse() {
        return firstFtTestHouse;
    }
    public void setFirstFtTestHouse(String firstFtTestHouse) {
        this.firstFtTestHouse = firstFtTestHouse;
    }
    public Double getFtCpuTime() {
        return ftCpuTime;
    }
    public void setFtCpuTime(Double ftCpuTime) {
        this.ftCpuTime = ftCpuTime;
    }
    public Double getFtIndexTime() {
        return ftIndexTime;
    }
    public void setFtIndexTime(Double ftIndexTime) {
        this.ftIndexTime = ftIndexTime;
    }
    public String getFtTestProgName() {
        return ftTestProgName;
    }
    public void setFtTestProgName(String ftTestProgName) {
        this.ftTestProgName = ftTestProgName;
    }
    public Date getFtTestProgReleaseDate() {
        return ftTestProgReleaseDate;
    }
    public void setFtTestProgReleaseDate(Date ftTestProgReleaseDate) {
        this.ftTestProgReleaseDate = ftTestProgReleaseDate;
    }
    public String getFtTestProgRevision() {
        return ftTestProgRevision;
    }
    public void setFtTestProgRevision(String ftTestProgRevision) {
        this.ftTestProgRevision = ftTestProgRevision;
    }
    public String getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public String getPartNum() {
        return partNum;
    }
    public void setPartNum(String partNum) {
        this.partNum = partNum;
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
