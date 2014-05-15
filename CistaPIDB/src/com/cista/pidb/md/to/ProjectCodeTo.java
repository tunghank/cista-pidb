package com.cista.pidb.md.to;

import java.util.Date;

public class ProjectCodeTo {
    private String projCode;
    private String projName;
    private String projOption;
    private String funcRemark;
    private String cust;
    private Date kickOffDate;
    private String prodCode;
    private String remark;
    private String releaseTo;
    private String projectType;
    
    public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getReleaseTo() {
		return releaseTo;
	}
	public void setReleaseTo(String releaseTo) {
		this.releaseTo = releaseTo;
	}
	public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCust() {
        return cust;
    }
    public void setCust(String cust) {
        this.cust = cust;
    }
    public String getFuncRemark() {
        return funcRemark;
    }
    public void setFuncRemark(String funcRemark) {
        this.funcRemark = funcRemark;
    }
    public Date getKickOffDate() {
        return kickOffDate;
    }
    public void setKickOffDate(Date kickOffDate) {
        this.kickOffDate = kickOffDate;
    }
    public String getProjCode() {
        return projCode;
    }
    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }
    public String getProjName() {
        return projName;
    }
    public void setProjName(String projName) {
        this.projName = projName;
    }
    public String getProjOption() {
        return projOption;
    }
    public void setProjOption(String projOption) {
        this.projOption = projOption;
    }
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
}
