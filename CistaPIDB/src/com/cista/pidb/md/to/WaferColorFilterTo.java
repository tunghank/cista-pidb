
/*
 * 2010.03.23/FCG1 @Jere Huang - create mpStatus
 */
package com.cista.pidb.md.to;

public class WaferColorFilterTo {
	
	private String waferCfMaterialNum;
	private String projectCodeWVersion;
	private String description;
	private String remark;
	private String updateDate;
	private String createdBy;
	private String modifiedBy;
	private String waferCfVariant;
	private String mpStatus; //FCG1
	
	
	
	public String getMpStatus() 
	{
		return mpStatus;
	}

	public void setMpStatus(String mpStatus) 
	{
		this.mpStatus = mpStatus;
	}

	public String getWaferCfMaterialNum() {
		return waferCfMaterialNum;
	}

	public void setWaferCfMaterialNum(String waferCfMaterialNum) {
		this.waferCfMaterialNum = waferCfMaterialNum;
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

	public String getWaferCfVariant() {
		return waferCfVariant;
	}

	public void setWaferCfVariant(String waferCfVariant) {
		this.waferCfVariant = waferCfVariant;
	}

}
