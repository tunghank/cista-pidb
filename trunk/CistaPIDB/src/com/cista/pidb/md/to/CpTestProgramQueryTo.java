package com.cista.pidb.md.to;

import com.cista.pidb.core.BaseQueryTo;

public class CpTestProgramQueryTo extends BaseQueryTo {
    private String prodCode;

    private String prodName;

    private String projName;

    private String fab;
    
    private String fabDescr;

    private String projOption;

    private String maskName;

    private String projCodeWVersion;

    private String cpTestProgName;

    private String cpTestProgRevision;

    //Add 2007/11/12
    private String cpMaterialNum;
    private boolean multipleStage;
    private String tester;
    private String materialDesc;
    private String vendorCode;
    private String materialNum;
    
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
    public String getCpMaterialNum() {
        return cpMaterialNum;
    }
    public void setCpMaterialNum(String cpMaterialNum) {
        this.cpMaterialNum = cpMaterialNum;
    }
    
    public String getCpTestProgName() {
        return cpTestProgName;
    }

    public void setCpTestProgName(String cpTestProgName) {
        this.cpTestProgName = cpTestProgName;
    }

    public String getCpTestProgRevision() {
        return cpTestProgRevision;
    }

    public void setCpTestProgRevision(String cpTestProgRevision) {
        this.cpTestProgRevision = cpTestProgRevision;
    }

    public String getFab() {
        return fab;
    }

    public void setFab(String fab) {
        this.fab = fab;
    }

    public String getMaskName() {
        return maskName;
    }

    public void setMaskName(String maskName) {
        this.maskName = maskName;
    }

    public String getProjOption() {
        return projOption;
    }

    public void setProjOption(String option) {
        this.projOption = option;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
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

	public String getFabDescr() {
		return fabDescr;
	}

	public void setFabDescr(String fabDescr) {
		this.fabDescr = fabDescr;
	}
	public String getMaterialNum() {
		return materialNum;
	}
	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}
}
