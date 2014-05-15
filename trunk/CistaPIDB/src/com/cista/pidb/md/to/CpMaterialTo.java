package com.cista.pidb.md.to;

public class CpMaterialTo {
    
	private String cpMaterialNum;
	private String projectCodeWVersion;
	private String cpVariant;
	private String cpTestProgramNameList;
	private String description;
	private String remark;
	private String updateDate;
	private String createdBy;
	private String modifiedBy;
	//private String cdt;  //因ic wafer create時不可有此欄位出現
    
    public String getCpMaterialNum() {
        return cpMaterialNum;
    }
    public void setCpMaterialNum(String cpMaterialNum) {
        this.cpMaterialNum = cpMaterialNum;
    }
    public String getCpTestProgramNameList() {
        return cpTestProgramNameList;
    }
    public void setCpTestProgramNameList(String cpTestProgramNameList) {
        this.cpTestProgramNameList = cpTestProgramNameList;
    }
    public String getCpVariant() {
        return cpVariant;
    }
    public void setCpVariant(String cpVariant) {
        this.cpVariant = cpVariant;
    }
    public String getProjectCodeWVersion() {
        return projectCodeWVersion;
    }
    public void setProjectCodeWVersion(String projectCodeWVersion) {
        this.projectCodeWVersion = projectCodeWVersion;
    }
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/*
	public String getCdt() {
		return cdt;
	}
	public void setCdt(String cdt) {
		this.cdt = cdt;
	}
	*/
}
