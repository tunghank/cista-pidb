package com.cista.pidb.md.to;

import java.util.Date;

public class EsdTestTo {
    private String prodCode;
    private String projCodeWVersion;
    private String idEsdTesting;
    private String esdRaTestRptDoc;
    private String esdRptVersion;
    private String remark;
    private String priority;
    private String testingHouse;
    private String testToFail;
    private boolean testHbm;
    private boolean testMm;
    private boolean testCdm;
    private boolean testLu;
    private String testHbmEa;
    private String testMmEa;
    private String testCdmEa;
    private String testLuEa;
    private String lotId;
    private Date esdFinishDate;
    private String resultsHbm;
    private String resultsMm;
    private String resultsCdm;
    private String resultsLu;
    private String resultsHbmPf;
    private String resultsMmPf;
    private String resultsCdmPf;
    private String resultsLuPf;
    private String faRequest;
    private Date sampleReceiveDate;
    private String assignTo;
    private String assignEmail;
    private String createdBy;
    private String modifiedBy;
    private String owner;
    
    public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
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

    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEsdRaTestRptDoc() {
        return esdRaTestRptDoc;
    }
    public void setEsdRaTestRptDoc(String esdRaTestRptDoc) {
        this.esdRaTestRptDoc = esdRaTestRptDoc;
    }
    public String getEsdRptVersion() {
        return esdRptVersion;
    }
    public void setEsdRptVersion(String esdRptVersion) {
        this.esdRptVersion = esdRptVersion;
    }
    public Date getEsdFinishDate() {
        return esdFinishDate;
    }
    public void setEsdFinishDate(Date esdFinishDate) {
        this.esdFinishDate = esdFinishDate;
    }
    public String getFaRequest() {
        return faRequest;
    }
    public void setFaRequest(String faRequest) {
        this.faRequest = faRequest;
    }
    public Date getSampleReceiveDate() {
        return sampleReceiveDate;
    }
    public void setSampleReceiveDate(Date sampleReceiveDate) {
        this.sampleReceiveDate = sampleReceiveDate;
    }

    public String getIdEsdTesting() {
        return idEsdTesting;
    }
    public void setIdEsdTesting(String idEsdTesting) {
        this.idEsdTesting = idEsdTesting;
    }
    public String getLotId() {
        return lotId;
    }
    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
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
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getResultsCdm() {
        return resultsCdm;
    }
    public void setResultsCdm(String resultsCdm) {
        this.resultsCdm = resultsCdm;
    }
    public String getResultsHbm() {
        return resultsHbm;
    }
    public void setResultsHbm(String resultsHbm) {
        this.resultsHbm = resultsHbm;
    }
    public String getResultsLu() {
        return resultsLu;
    }
    public void setResultsLu(String resultsLu) {
        this.resultsLu = resultsLu;
    }
    public String getResultsMm() {
        return resultsMm;
    }
    public void setResultsMm(String resultsMm) {
        this.resultsMm = resultsMm;
    }

    public String getResultsLuPf() {
        return resultsLuPf;
    }
    public void setResultsLuPf(String resultsLuPf) {
        this.resultsLuPf = resultsLuPf;
    }  
    public String getResultsCdmPf() {
        return resultsCdmPf;
    }
    public void setResultsCdmPf(String resultsCdmPf) {
        this.resultsCdmPf = resultsCdmPf;
    }  
    public String getResultsMmPf() {
        return resultsMmPf;
    }
    public void setResultsMmPf(String resultsMmPf) {
        this.resultsMmPf = resultsMmPf;
    }    
    public String getResultsHbmPf() {
        return resultsHbmPf;
    }
    public void setResultsHbmPf(String resultsHbmPf) {
        this.resultsHbmPf = resultsHbmPf;
    }
    
    public String getTestCdmEa() {
        return testCdmEa;
    }
    public void setTestCdmEa(String testCdmEa) {
        this.testCdmEa = testCdmEa;
    }
    public String getTestHbmEa() {
        return testHbmEa;
    }
    public void setTestHbmEa(String testHbmEa) {
        this.testHbmEa = testHbmEa;
    }
    public String getTestingHouse() {
        return testingHouse;
    }
    public void setTestingHouse(String testingHouse) {
        this.testingHouse = testingHouse;
    }
    public String getTestLuEa() {
        return testLuEa;
    }
    public void setTestLuEa(String testLuEa) {
        this.testLuEa = testLuEa;
    }
    public String getTestMmEa() {
        return testMmEa;
    }
    public void setTestMmEa(String testMmEa) {
        this.testMmEa = testMmEa;
    }

    public void setTestLu(boolean testLu) {
        this.testLu = testLu;
    }
    public boolean getTestLu() {
        return testLu;
    }
    
    public void setTestCdm(boolean testCdm) {
        this.testCdm = testCdm;
    }
    public boolean getTestCdm() {
        return testCdm;
    }
    
    public void setTestMm(boolean testMm) {
        this.testMm = testMm;
    }
    public boolean getTestMm() {
        return testMm;
    }
    
    public void setTestHbm(boolean testHbm) {
        this.testHbm = testHbm;
    }
    public boolean getTestHbm() {
        return testHbm;
    }
    
    public String getTestToFail() {
        return testToFail;
    }
    public void setTestToFail(String testToFail) {
        this.testToFail = testToFail;
    }

}
