package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class FtTestProgramQueryTo extends BaseQueryTo {

    private String partNum;

    private String prodCode;

    private String ftTestProgName;
    private String ftTestProgRevision;
    //Add 2007/11/12
    private String ftMaterialNum;
    private boolean multipleStage;
    private String tester;
    
    public String getTester() {
        return tester;
    }
    public void setTester(String tester) {
        this.tester = tester;
    }
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
    
    public String getFtTestProgName() {
        return ftTestProgName;
    }
    public void setFtTestProgName(String ftTestProgName) {
        this.ftTestProgName = ftTestProgName;
    }
    public String getFtTestProgRevision() {
        return ftTestProgRevision;
    }
    public void setFtTestProgRevision(String ftTestProgRevision) {
        this.ftTestProgRevision = ftTestProgRevision;
    }
    public String getPartNum() {
        return partNum;
    }
    public void setPartNum(String partNum) {
        this.partNum = partNum;
    }
    public String getProdCode() {
        return prodCode;
    }
    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

}
